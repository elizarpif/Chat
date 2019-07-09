package com.company;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.*;


public class ClientHandler implements Runnable{

    private final static int port = 6666;
    private static final String host = "localhost"; ///!!!

    private String name="unknownUser"+clients_count;

    private Scanner inMessage;
    private PrintWriter outMessage;

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

            while(true){
                String clientMsg="";
                clientMsg = this.inMessage.nextLine();
                System.out.println("server got this message "+clientMsg);

                if (clientMsg.equals("$exit$")){
                    this.server.removeClient(this);
                    break;
                }
                else if (clientMsg.length()>5 && clientMsg.substring(0,6).equals("$name$")){ //имя текущего пользователя
                    this.name = clientMsg.substring(6,clientMsg.length());
                    this.server.addName(this.name);
                }
                else if (clientMsg.equals("$list$")){
                    this.server.getList(this);
                }
                else if (clientMsg.length()>5 && clientMsg.substring(0,6).equals("$user ")){
                    int index = clientMsg.indexOf('$', 6);
                    String user = clientMsg.substring(6,index);
                    String msg = clientMsg.substring(index+1,clientMsg.length());
                    this.server.sendMsgtoUser(user,this, msg);
                }
                else if (clientMsg.equals("$help$")){
                    this.sendMsg("commands:\n\n\t$list$ - list of all users\n\n\t$user TONAME$ YOURMESSAGE - private message to user TONAME" +
                            "\n\n\t$name$ - change your name\n\n\t$exit$ - exit\n\n\t$help$ - help");
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

        server.removeClient(this);

    }

    public String getName(){
        return this.name;
    }
}
