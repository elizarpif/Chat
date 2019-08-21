package com.company;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class Authorization {
    private HashMap<String, String> accounts = new HashMap<String, String>();
    private String dbPath = null;

    public Authorization(String path) {
        try {
            this.dbPath = path;

            FileInputStream inputStream = new FileInputStream(path);
            Scanner scanner = new Scanner(inputStream);

            while (scanner.hasNext()) {
                String login, password;
                login = scanner.next();
                password = scanner.next();
                accounts.put(login, password);
            }
            inputStream.close();
        }catch(Exception ex){

        }

    }

    public boolean register(String username, String password) throws IOException {
        if (username.length() < 1 || password.length() < 1)
            return false;
        else {
            //Do you already exists?
            if (accounts.containsKey(username))
                return false;

            FileWriter outputStream = new FileWriter(dbPath, true);
            outputStream.append(username + " " + password + "\n");
            outputStream.close();
            accounts.put(username, password);

            return true;
        }
    }


    public boolean login(String username, String password){
        if (accounts.containsKey(username)) {
            if (password.equals(accounts.get(username))) {
                return true;
            }
        }
        return false;
    }

}
