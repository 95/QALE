package com.example.demo6.Client;

import com.example.demo6.Server.NetworkMessage;
import com.example.demo6.Server.Unwrapper;

import java.util.ArrayList;
import java.net.Socket;
import java.io.*;

public class ClientHandler implements Runnable {
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedInputStream bufferedInput;
    private BufferedOutputStream bufferedOutput; 
    private Long clientID;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedOutput = new BufferedOutputStream(socket.getOutputStream()); //new OutputStreamWriter(X); 
            this.bufferedInput = new BufferedInputStream(socket.getInputStream());
            this.clientID = getUsername(); //updated
            clientHandlers.add(this);
            //REMOVED: sendRecievedMessage("SERVER: " + clientID + "has entered the chat!")
        } catch (IOException e) {
            e.printStackTrace();
            closeEverything(socket, bufferedInput, bufferedOutput);
        }
    }

    public Long getUsername() throws IOException //updated
    {
        Long clientID = new NetworkMessage(new Unwrapper(bufferedInput).payload).senderID; //at ClientHandler.getUsername(ClientHandler.java:28)
        return clientID;
    }

    @Override
    public void run(){ 

        while (socket.isConnected())
        {
            try {

                NetworkMessage messageFromClient = new NetworkMessage(new Unwrapper(bufferedInput).payload); 

                if(messageFromClient.receiverID == (long)0){continue;} //messages for server discarded.

                sendRecievedMessage(messageFromClient);
            } catch(IOException e) {
                closeEverything(socket, bufferedInput, bufferedOutput);
                e.printStackTrace();
            }
        }
    }

    public void sendRecievedMessage(NetworkMessage messageToSend){


        for(ClientHandler clientHandler : clientHandlers)
        {
            try {
                if (clientHandler.clientID == messageToSend.receiverID)
                {
                    clientHandler.bufferedOutput.write(messageToSend.sendOut()); //send message method
                    clientHandler.bufferedOutput.flush();
                    return;
                }
            } catch (IOException e) {
                closeEverything(socket, bufferedInput, bufferedOutput);
                e.printStackTrace();
            }   
        } 
    }

    public void removeClientHandler()
    {
        clientHandlers.remove(this);
        //sendRecievedMessage("User Disconnected.");
    }

    public void closeEverything(Socket socket, BufferedInputStream bufferedInput, BufferedOutputStream bufferedOutput)
    {
        removeClientHandler();
        try {
            if(bufferedInput != null)
            {
                bufferedInput.close();
            }
            if(bufferedOutput != null)
            {
                bufferedInput.close();
            }
            if(socket != null)
            {
                bufferedInput.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}