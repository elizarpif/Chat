package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    private ArrayList<ClientHandler> clients = new ArrayList<ClientHandler>();
    private ArrayList<String> clientNames = new ArrayList<String>();

    private static int port = 6666;

    private ServerSocket server;
    private Socket socket;

    public Server(){

        try{
            this.server = new ServerSocket(port);
            System.out.println("Server is ready");

            while(true){
                this.socket = this.server.accept();
                System.out.println("Client came");
                ClientHandler client = new ClientHandler(socket, this);
                this.clients.add(client);
                new Thread(client).start();

            }

        }
        catch(Exception ex){
            System.err.println(ex);
            ex.printStackTrace();
        }
        finally {
            try{
                server.close();
                socket.close();
                System.out.println("Server is closed");
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
    }

    public void addName(String s){
        clientNames.add(s);
    }
    public void sendMsgtoAllClients(String msg){
        for (ClientHandler o: clients)
        {
            System.out.println("sending all clients "+msg);
            o.sendMsg(msg);
        }
    }
    public void sendMsgtoUser(String user, ClientHandler fromUser, String msg){
        if (clientNames.contains(user)){
            for( ClientHandler o: clients){
                if (o.getName().equals(user))
                {
                    o.sendMsg(fromUser.getName()+": "+msg);
                    System.out.println("отправляю "+o.getName()+" msg "+fromUser.getName()+" : "+msg);

                }
            }
        }
        else
            fromUser.sendMsg("SERVER: client with name doesnt exist");
    }
    public void removeClient(ClientHandler cl){
        System.out.println("deleting "+cl.getName());

        clientNames.remove(cl.getName());
        clients.remove(cl);
    }

    public void getList(ClientHandler cl){
        int i=1;
        cl.sendMsg("List of all users: ");
        for (ClientHandler o: clients)
        {
           cl.sendMsg(i+") "+o.getName());
           i++;
        }
    }
}
