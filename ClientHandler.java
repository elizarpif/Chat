package com.company;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.*;


public class ClientHandler implements Runnable{

    private final static int port = 6666;

    private String name="unknownUser"+clients_count;

    private Scanner inMessage;
    private PrintWriter outMessage;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    private boolean auth = true;
    private Server server;
    private Socket clientSocket;
    private static int clients_count = 0;

    public ClientHandler(Socket s, Server server){
        try{
            this.clientSocket = s;
            this.server = server;
            this.clients_count++;

            this.inMessage = new Scanner(s.getInputStream());
            this.outMessage = new PrintWriter(s.getOutputStream());
        }
        catch(Exception ex){
            ex.printStackTrace();
        }


    }
    @Override
    public void run() {
        System.out.println("start running");
        try{
            this.Auth();
            sendMsg("lets go");
            while(true){
                String clientMsg="";
                clientMsg = this.inMessage.nextLine();
                //System.out.println("server got this message "+clientMsg);

                if (clientMsg.equals("$exit$")){
                    server.Log("client "+this.name+" leave chat");
                    this.server.removeClient(this);
                    break;
                }
                else if (clientMsg.length()>5 && clientMsg.substring(0,6).equals("$room ")){
                    int index = clientMsg.indexOf('$', 6);
                    String room = clientMsg.substring(6,index);
                    String msg = clientMsg.substring(index+1,clientMsg.length());
                    this.server.sendRoom(room, msg, this);
                    //server.Log("client "+this.name+" leave chat");
                }
                else if (clientMsg.length()>13 && clientMsg.substring(0,13).equals("$room-create ")){
                    int index = clientMsg.indexOf('$', 13);
                    String room = clientMsg.substring(13,index);
                    this.server.createRoom(room, this);
                    //this.server.sendMsgtoUser(user,this, msg);
                    //server.Log("client "+this.name+" leave chat");
                }
                else if (clientMsg.length()>9 && clientMsg.substring(0,13).equals("$room-go ")){
                    int index = clientMsg.indexOf('$', 9);
                    String room = clientMsg.substring(9,index);
                    this.server.goRoom(room, this);
                    //this.server.sendMsgtoUser(user,this, msg);
                    //server.Log("client "+this.name+" leave chat");
                }
                else if (clientMsg.equals("$list$")){
                    this.server.getList(this);
                }
                else if (clientMsg.length()>5 && clientMsg.substring(0,6).equals("$user ")){
                    int index = clientMsg.indexOf('$', 6);
                    String user = clientMsg.substring(6,index);
                    String msg = clientMsg.substring(index+1,clientMsg.length());
                    this.server.sendMsgtoUser(user,this, msg);
                    //server.Log("client "+this.name+" leave chat");
                }
                else if (clientMsg.equals("$help$")){
                    this.sendMsg("commands:\n\n\t$list$ - list of all users\n\n\t$user TONAME$ YOURMESSAGE - private message to user TONAME" +
                            "\n\n\t$exit$ - exit\n\n\t$help$ - help\n\n\t$room ROOM$ YOURMESSAGE - send message to room if you in room" +
                            "\n\n\t$room-create ROOM$ - create room\n\n\t$room-go ROOM$ - go to room");
                    server.Log("client "+this.name+" using help");
                }
                //clientMsg.length()>5 && clientMsg.substring(0,6).equals("$chat$")
                else {
                    this.server.sendMsgtoAllClients(this.name+": "+clientMsg);
                }


            }
            //System.out.println("что-то пошло не так");
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        finally {
            this.close();
        }
    }
    public void sendMsg(String msg){
        try{
            System.out.println("sendMessage "+ msg);
            this.outMessage.println(msg);
            this.outMessage.flush();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public void close(){
        clients_count--;
        server.sendMsgtoAllClients("client "+this.name+" quit, Client's count: "+this.clients_count);

        //server.removeClient(this);

    }

    public String getName(){
        return this.name;
    }

    public void Auth(){
        try{
            sendMsg("For authorization use:\n$login$[username]$[password].\nFor registration write:\n$register$[username]$[password]");
            while (auth) {
                String msg = this.inMessage.nextLine();

                if(msg.length()<=2){
                    //sendMsg("msg.len<2");
                    continue;
                }
                msg = msg.substring(1);
                String[] substr = msg.split("\\$");
                String type="", login="", password="";
                if(substr.length==3){
                    type = substr[0];
                    login = substr[1];
                    password = substr[2];
                }


                boolean result = false;
                if (type.equals("login")) {
                    result = server.tryAuth(login,password);
                    if (result) {
                        this.name = login;
                        auth = false;
                        sendMsg(this.name+" in chat now.");
                        server.Log(this.name+" in chat now");
                    }
                    else {
                        sendMsg("Uncorrect login or password. Please, try again.");
                    }

                }
                else if (type.equals("register")) {
                    result = server.tryRegister(login, password);
                    if (result) {
                        server.Log("register "+login);
                        sendMsg("Success! For authorization write login$username$password");
                    }
                    else {
                        sendMsg("Account exists");
                    }
                }

            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
