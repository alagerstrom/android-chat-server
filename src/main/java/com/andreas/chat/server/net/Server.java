package com.andreas.chat.server.net;

import com.andreas.chat.common.Constants;
import com.andreas.chat.common.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread {

    private final ServerSocket serverSocket;

    private final List<Connection> connections = new ArrayList<>();

    public static void main(String[] args) {
        try {
            new Server();
            System.out.println("Server started, listening on port " + Constants.PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Server() throws IOException {
        serverSocket = new ServerSocket(Constants.PORT);
        start();
    }

    @Override
    public void run() {
        int numberOfClients = 0;
        while (true) {
            try {
                System.out.println("Waiting for client " + numberOfClients++);
                Socket clientSocket = serverSocket.accept();
                Connection connection = new Connection(this, clientSocket);
                synchronized (connections) {
                    connections.add(connection);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void broadcast(String message) {
        System.out.println(message);
        for (Connection connection : connections)
            connection.send(message);
    }

    public synchronized void remove(Connection connection) {
        connections.remove(connection);
    }

    public synchronized void handleMessage(String stringRepresentation) {
        if (stringRepresentation == null)
            return;
        broadcast(stringRepresentation);
        Message message = new Message(stringRepresentation);
        System.out.println("Message from " + message.getUsername() + ": " + message.getText());
    }
}
