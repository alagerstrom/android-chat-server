package com.andreas.chat.client.view;

import com.andreas.chat.client.net.Connection;

import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

public class Client implements Connection.Delegate{
    private final Scanner in = new Scanner(System.in);
    private String username = "Anonymous";
    private static final String QUIT_COMMAND = "quit";
    private Connection connection = null;

    public static void main(String[] args) {
        new Client();
    }

    public Client() {

        System.out.println("Welcome to chat client");

        while (connection == null){
            System.out.println("Enter host:");
            String host = in.nextLine();
            System.out.println("Enter port number:");
            String portString = in.nextLine();
            int port;
            try {
                port = Integer.parseInt(portString);
                connection = new Connection(host, port);
            }catch (Exception e){
                System.out.println("Failed to connect, try again.");
            }
        }

        connection.setDelegate(this);

        System.out.println("Choose username: ");

        this.username = in.nextLine();
        boolean quit = false;

        while (!quit) {
            String input = in.nextLine();
            if (input.equals(QUIT_COMMAND)) {
                quit = true;
            } else {
                sendMessage(input);
            }
        }

    }

    private void sendMessage(String message) {
        CompletableFuture.runAsync(()->{
            if (connection != null)
                connection.send(username, message);
            else
                System.err.println("Connection was null");
        });
    }

    @Override
    public void incomingMessage(String username, String message) {
        synchronized (this){
            System.out.println("Message from " + username + ": " + message);
        }
    }
}
