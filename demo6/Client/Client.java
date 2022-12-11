package com.example.demo6.Client;

import com.example.demo6.HelloController;

import java.util.ArrayList;
import java.util.Scanner;
import java.net.Socket;
import java.io.*;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class Client {

    private Socket socket;
    private BufferedInputStream bufferedInput;
    private BufferedOutputStream bufferedOutput;
    private KeyMap keyMap;
    private byte[] someBytes = {(byte)0,(byte)1,(byte)2,(byte)3,(byte)4,(byte)5,(byte)6,(byte)7,(byte)8,(byte)9,(byte)0,(byte)1,(byte)2,(byte)3,(byte)4,(byte)5};
    private SecretKey hardcodePeerKey;

    public Long getMyGivenID() {
        return myGivenID;
    }

    public void setMyGivenID(Long myGivenID) {
        this.myGivenID = myGivenID;
    }

    private Long myGivenID;


    public Client(Socket socket, long ID)
    {
        try {
            this.socket = socket;
            this.bufferedOutput = new BufferedOutputStream(socket.getOutputStream()); //new OutputStreamWriter(X); new OutputStream()
            this.bufferedInput = new BufferedInputStream(socket.getInputStream()); //new InputStream()
            this.myGivenID = ID;
        
        
            //added key storage in hashmap
            //currently just assumes another peer, uses a key generator for now to do it the same every time
            //later, each client could have a RSA key and send a public key to the server,
            //then server could create random symmetric keys  when requested by both clients so keys are random, private, and not saved aside by clients
            keyMap = new KeyMap();
            hardcodePeerKey = Encrypt.getKeyFromPassword("Roger", "doger");
            if(myGivenID == 1){keyMap.put(hardcodePeerKey, (long)2);}
            if(myGivenID==2){keyMap.put(hardcodePeerKey, (long)1 );}
        
        } catch (Exception e) {
            closeEverything(socket, bufferedInput, bufferedOutput);
        }
    }

    public void session(){
        Long recieverID;
        try {
            byte[] bufferForHash = Encrypt.encrypt("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",hardcodePeerKey,Encrypt.generateIv(someBytes));
            Long serverID = (long)0;
            NetworkMessage message =  new NetworkMessage(serverID, myGivenID, bufferForHash); 
            bufferedOutput.write(message.sendOut()); 
            bufferedOutput.flush();

            Scanner scan = new Scanner(System.in);
            System.out.println("Recipient ID?: ");
            recieverID = scan.nextLong();
            while (socket.isConnected())
            {
                String messageToSend = scan.nextLine();
                send(recieverID, messageToSend);
            }
        } catch (Exception e) {
            closeEverything(socket, bufferedInput, bufferedOutput);
        }
    }

    public void send(Long toID, String message){       
            byte[] cipherBytes;
            try {
            cipherBytes = Encrypt.encrypt(message, keyMap.get(toID),Encrypt.generateIv(someBytes));
            NetworkMessage newMsg =  new NetworkMessage(toID, myGivenID, cipherBytes);
            bufferedOutput.write(newMsg.sendOut());
            bufferedOutput.flush();
            System.out.println("sent message to " + toID + " with message : " + message);
            } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            } 
    }

    public void listenForMessage() {
        new Thread(new Runnable(){
            @Override
            public void run() {
                String recievedDecoded; //recievedMessage
                

                while (socket.isConnected()){
                    try {
                        byte[] payload = new Unwrapper(bufferedInput).payload; //RETURNS INT -> NEEDS TO BE CONVERTED TO BYTE
                        NetworkMessage recievedMessage = new NetworkMessage(payload);
                        recievedDecoded = Encrypt.decrypt(recievedMessage.message, keyMap.get(recievedMessage.senderID),Encrypt.generateIv(someBytes));
                        System.out.println(recievedDecoded);

                        HelloController hello = new HelloController();
                        hello.addInformation(recievedDecoded + "\n");

                    } catch (Exception e) {
                        closeEverything(socket, bufferedInput, bufferedOutput);
                    }
                }
            }
        }).start();
    }

    public void closeEverything(Socket socket, BufferedInputStream bufferedInput, BufferedOutputStream bufferedOutput) {
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

    private void recordChat()
    {
        MessageHistory history = new MessageHistory();
    }

    public static void main(String[] args){
        Scanner scan = new Scanner(System.in);

        System.out.println("Enter your ID: ");
        Long clientID = scan.nextLong();
        System.out.println("Initializing please wait");

        try{
            Socket socket = new Socket("localhost", 1112); 
            Client client = new Client(socket, clientID);
        
            client.listenForMessage();
            client.session();
        } catch(IOException e){
            e.printStackTrace();
        }
    
        System.out.println("Done. Chat room secured.");
        //System.exit(0); ERROR: JDWP Unable to get JNI 1.2 environment, jvm->GetEnv() return code = -2
    }
}


