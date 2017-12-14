package com.andreas.chat.server.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Connection extends Thread{
    private final Socket socket;
    private final BufferedReader reader;
    private final PrintWriter writer;
    private final Server server;

    public Connection(Server server, Socket socket) throws IOException {
        this.server = server;
        this.socket = socket;
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new PrintWriter(socket.getOutputStream(), true);
        start();
    }

    public synchronized void send(String message) {
        writer.println(message);
    }

    @Override
    public void run() {
        boolean connected = true;
        int numberOfLines = 0;
        while (connected){
            try {
                System.out.println("Trying to read line " + numberOfLines++);
                String message = reader.readLine();
                if (message == null)
                    connected = false;
                server.handleMessage(message);
            } catch (IOException e) {
                server.remove(this);
                connected = false;
            }
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Connection closed");
    }
}
