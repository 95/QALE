package com.example.demo6.Client;

import java.security.*;
//takes care of message integrity verification

public class Hashing {

    public static byte[] hashMessage(byte[] message){
        MessageDigest digest;
        byte[] hash = new byte[32];
        try {
            digest = MessageDigest.getInstance("SHA-256");
            hash = digest.digest(message);
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
            return hash;
        }
/*depreciated appendHash function, moved this into message assembly in network message */
 /*     public static byte[] appendHash(byte[] message){
        byte[] hash = hashMessage(message);
        byte[] temp = new byte[message.length+hash.length];
        int index = 0;
        while(index<temp.length){
            if(index<message.length){
                temp[index] = message[index];
            }
            else{
                temp[index] = hash[index-message.length];
            }
            index++;
        }
        return temp;
    }
*/
    public static boolean checkHash(byte[] message, byte[] hash){
        
        byte[] hashCheck = hashMessage(message);

        for(int i = 0; i<hash.length;i++){
            if(hash[i]!=hashCheck[i]){
                return false;
            }
        }        
        return true;
    }
}
