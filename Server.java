package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Server {

    private ArrayList<ClientHandler> clients = new ArrayList<ClientHandler>();

    private ArrayList<String> clientNames = new ArrayList<String>();

    private ArrayList<Room> rooms = new ArrayList<>();
    private ArrayList<String> roomNames = new ArrayList<>();

    private static int port = 6666;
    private Authorization auth;
    private ServerSocket server;
    private Socket socket;
    public Logger logger = Logger.getLogger("com.company");

    public Server(){

        try{
            auth = new Authorization("C:\\Users\\lil\\IdeaProjects\\Server\\paswd.txt");
            this.server = new ServerSocket(port);
            FileHandler handler = new FileHandler("log.log");
            logger.addHandler(handler);
            handler.setFormatter(new SimpleFormatter());
            logger.info("Server is ready");
            System.out.println("Server is ready");

            while(true){
                this.socket = this.server.accept();
                System.out.println("New client");
                logger.info("New client");
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
                System.out.println("Server was closed");
                logger.info("Server was closed");
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
    }

    public void addName(String s){
        clientNames.add(s);
    }
    public void Log(String s){
        logger.info(s);
    }
    public void sendMsgtoAllClients(String msg){
        for (ClientHandler o: clients)
        {
            System.out.println("sending to all clients "+msg);
            logger.info("sending to all clients "+msg);
            o.sendMsg(msg);
        }
    }
    public void sendMsgtoUser(String user, ClientHandler fromUser, String msg){
        if (clientNames.contains(user)){
            for( ClientHandler o: clients){
                if (o.getName().equals(user))
                {
                    o.sendMsg(fromUser.getName()+": "+msg);
                    System.out.println("sending "+o.getName()+" msg "+fromUser.getName()+" : "+msg);
                    logger.info("sending "+o.getName()+" msg "+fromUser.getName()+" : "+msg);

                }
            }
        }
        else
            fromUser.sendMsg("SERVER: client with name doesnt exist");
    }
    public void removeClient(ClientHandler cl){
        System.out.println("deleting "+cl.getName());
        logger.info("deleting "+cl.getName());

        clientNames.remove(cl.getName());
        clients.remove(cl);
    }

    public void getList(ClientHandler cl){
        int i=1;
        cl.sendMsg("List of all users: ");
        logger.info("List of all users: ");
        for (ClientHandler o: clients)
        {
           cl.sendMsg(i+") "+o.getName());
           logger.info(i+") "+o.getName());
           i++;
        }
    }
    public boolean tryAuth(String l, String p){
        return auth.login(l,p);
    }
    public boolean tryRegister(String login, String password) throws IOException {
        return auth.register(login, password);
    }
    public void createRoom(String s, ClientHandler cl){
        if (roomNames.contains(s)){
            logger.info("room "+s+" exists");
            cl.sendMsg("room "+s+" exists");
        }
        else{
            Room a = new Room(s);
            a.newClient(cl);
            rooms.add(a);
            logger.info("New room "+s);
            cl.sendMsg("New room "+s);
        }
    }
    public void sendRoom(String name, String msg, ClientHandler cl){
        for (Room r:rooms
             ) {
            if (r.getRoomName()==name && r.getList().contains(cl)){
                logger.info("client "+cl.getName()+" send message in room "+name);
                r.sendMsg(msg);
            }
            else{
                cl.sendMsg("you must go in this room "+name);
            }

        }
    }
    public void goRoom(String s, ClientHandler cl){
        if (roomNames.contains(s)){
            for (Room r:rooms
            ) {
                if (r.getRoomName()==s){
                    //logger.info("client "+cl.getName()+" send message in room "+name);
                    r.newClient(cl);
                }
                else{
                    //cl.sendMsg("you must go in this room "+name);
                }

            }
        }
    }
    public void listRoom(String r, ClientHandler cl){
        if (roomNames.contains(r)){
            for (Room l:rooms
                 ) {
                if (l.getRoomName()==r){
                    l.getMsgtoUser(cl);
                }
            }
        }
    }
}
