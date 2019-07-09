package com.company;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class ClientClass {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader keyboard;
    private Scanner in;

    private static int port = 6666;
    private String address = "127.0.0.1"; //ip-address of your computer "192.168.40.11"
    private String nickname ="";

    public ClientClass(){
        try{

            this.socket = new Socket(InetAddress.getByName(this.address),port);
            this.in = new Scanner(this.socket.getInputStream());
            this.out = new PrintWriter(this.socket.getOutputStream());
            this.keyboard = new BufferedReader( new InputStreamReader(System.in));



            while(this.nickname==""){   ///сделать проверку на пробелы!!!!
                System.out.println("Please enter your nickname");
                this.nickname = keyboard.readLine();
            }
            out.println("$name$"+this.nickname);
            out.flush();
            System.out.println("Loading list of users...");
            out.println("$list$");
            out.flush();
            ClientSender sender = new ClientSender();
            new Thread(sender).start();
            String line = "";

            while(!line.equals("$exit$")){
                line = keyboard.readLine();
                out.println(line);
                out.flush();
            }
            sender.setStop();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        finally {
            this.close();
        }

    }
    public void close(){
        try{
            System.out.println("command exit");
            this.socket.close();
            this.out.close();
            this.in.close();

        }catch(Exception ex){
            ex.printStackTrace();
        }

    }

    public class ClientSender implements Runnable {
        private boolean isStop = true;
        @Override
        public void run() {
            try{
                while(isStop) {

                    String str = in.nextLine();
                    System.out.println(str);
                }

            }
            catch(Exception ex){
                //ex.printStackTrace();
            }
        }
        public void setStop(){
            this.isStop = false;
        }
    }
}
