package com.example.demo6.Client;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

public class Server {

    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket)
    {
        this.serverSocket = serverSocket;
    }


    public void startServer()
    {
        try {

            while(!serverSocket.isClosed())
            {
                Socket socket = serverSocket.accept();
                System.out.println("A new client has connected.");
                ClientHandler clientHandler = new ClientHandler(socket);

                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closedserverSocket(){
        try {
            if(serverSocket != null)
            {
                serverSocket.close();   
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void main(String[] args) { //main test
        try{
            ServerSocket serverSocket = new ServerSocket(1112);
            Server server = new Server(serverSocket);
            server.startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}