package com.company;

import java.util.ArrayList;

public class Room {
    private ArrayList<ClientHandler> rclients = new ArrayList<ClientHandler>();
    private String RoomName = "";
    Room(String s){
        //clients.add(cl);
        RoomName=s;
    }
    public void newClient(ClientHandler cl){
        rclients.add(cl);
    }
    public String getRoomName(){
        return RoomName;
    }
    public ArrayList<ClientHandler> getList(){
        return rclients;
    }
    public void sendMsg(String msg){
        for (ClientHandler r:rclients
             ) {
            r.sendMsg(msg);
        }
    }
    public void getMsgtoUser(ClientHandler cl){
        for (ClientHandler r:rclients
        ) {
            cl.sendMsg(r.getName());
        }
    }
}
