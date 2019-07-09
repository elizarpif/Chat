package com.company;


import java.io.*;
import java.net.Socket;
import java.util.*;


public class Client {

    public static void main(String[] args) {
        // write your code here
        System.out.println("hello client");
        new ClientClass();
        //InetAddress ip = InetAddress.getByName(address);
        System.out.println("exit");
    }
}

//public class ClientClass {
//
//    private Socket socket;
//    private PrintWriter out;
//    private BufferedReader keyboard;
//    private Scanner in;
//
//    private static int port = 6666;
//    private String address = "localhost";
//    private String nickname ="";
//
//    public ClientClass(){
//        try{
//
//
//            this.socket = new Socket(this.address,port);
//            this.in = new Scanner(this.socket.getInputStream());
//            this.out = new PrintWriter(this.socket.getOutputStream());
//            this.keyboard = new BufferedReader( new InputStreamReader(System.in));
//
//            ClientSender sender = new ClientSender();
//            new Thread(sender).start();
//
//
//            while(this.nickname==""){   ///сделать проверку на пробелы!!!!
//                System.out.println("Please enter your nickname");
//                this.nickname = keyboard.readLine();
//            }
//            out.println("$name$"+this.nickname);
//            out.flush();
//            System.out.println("Loading list of users...");
//            out.println("$list$");
//            out.flush();
//
//
//            System.out.println("Please enter your message");
//            String line = "";
//
//            while(!line.equals("$exit$")){
//                line = keyboard.readLine();
//                out.println(line);
//                out.flush();
//            }
//            sender.setStop();
//        }
//        catch(Exception ex){
//            ex.printStackTrace();
//        }
//        finally {
//            this.close();
//        }
//
//    }
//    public void close(){
//        try{
//            this.in.close();
//            this.out.close();
//            this.socket.close();
//            System.out.println("command exit");
//        }catch(Exception ex){
//            ex.printStackTrace();
//        }
//
//    }
//
//    public class ClientSender implements Runnable {
//        private boolean isStop = true;
//        @Override
//        public void run() {
//            try{
//                while(isStop) {
//                    String str = in.nextLine();
//                    System.out.println(str);
//                }
//            }
//            catch(Exception ex){
//                ex.printStackTrace();
//            }
//        }
//        public void setStop(){
//            this.isStop = false;
//        }
//    }
//}
