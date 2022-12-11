package com.example.demo6.Client;

import java.util.Arrays;
import java.nio.ByteBuffer;

public class NetworkMessage {


    byte[] message;
    Long receiverID;
    Long senderID;
    byte[] hash;
    byte[] assembledMessage;
    boolean failed = false;

//ALWAYS receiver first, because that's the way its rolled up, all instances of a message should include
//both two and from stamp, and always should have the to first, as is the conventional packaging overall


    NetworkMessage(Long receiverID, Long senderID, byte[] message){
        this.message = message;
        this.senderID = senderID;
        this.receiverID = receiverID;   
        this.assembleMessage();
    }

    NetworkMessage(byte[] assembledMessage){
        this.assembledMessage = assembledMessage;
        this.disassembleMessage();
    }



    public byte[] sendOut(){
        if(!failed){
            byte[] outgoingMessage = new byte[this.assembledMessage.length+6];
            outgoingMessage[0] = 0x01;
            outgoingMessage[1] = 0x01;
            byte[] length = ByteBuffer.allocate(4).putInt(assembledMessage.length).array();
            outgoingMessage[2] = length[0];
            outgoingMessage[3] = length[1];
            outgoingMessage[4] = length[2];
            outgoingMessage[5] = length[3];
            for(int i = 0; i<assembledMessage.length;i++)
                outgoingMessage[6+i] = assembledMessage[i];
            System.out.println(new String(outgoingMessage));
            return outgoingMessage;
        }
        return null;
    }

    public Object[] recOut(){
        if(!failed){
            Object[] recArr = {this.receiverID, this.senderID, this.message};
            return recArr;
        }
        return null;
    }



    private boolean checkMessageHash(){        
        byte[] hashIn = Arrays.copyOfRange(this.assembledMessage, this.assembledMessage.length-32, this.assembledMessage.length);
        byte[] hashCheck = Hashing.hashMessage(Arrays.copyOfRange(this.assembledMessage, 0, assembledMessage.length-32));
        for(int i = 0; i<32;i++){
            if(hashIn[i]!=hashCheck[i]){
                return false;
            }
        }        
        return true;
    }



    private void assembleMessage(){
        try{
            
            byte[] sendBytes = ByteUtils.longToBytes(this.senderID);
            byte[] receiverBytes = ByteUtils.longToBytes(this.receiverID);
            byte[] temp = new byte[sendBytes.length+receiverBytes.length+this.message.length];
            int index = 0;
            
            while(index<temp.length){
                if(index<8){
                    temp[index] = receiverBytes[index];
                }
                else if (index >= 8 && index<16){
                    temp[index] = sendBytes[index-8];
                }
                
                else{
                temp[index]=this.message[index-16];
                }
                index++;
            }

            this.hash = Hashing.hashMessage(temp);
            this.assembledMessage = new byte[temp.length+hash.length];
            index = 0;
            while(index<this.assembledMessage.length){
                if(index<temp.length){
                    this.assembledMessage[index] = temp[index];
                }
                else{
                    this.assembledMessage[index] = this.hash[index-temp.length];
                }
                index++;
            }
        }
        catch(Exception e){
            failed = true;
        }
    }

    private void disassembleMessage(){
        try{
            if(checkMessageHash()){
                this.receiverID = ByteUtils.bytesToLong(Arrays.copyOfRange(assembledMessage, 0, 8));
                this.senderID = ByteUtils.bytesToLong(Arrays.copyOfRange(assembledMessage, 8, 16));
                this.message = Arrays.copyOfRange(assembledMessage, 16, assembledMessage.length-32);
            }
            else{
                this.failed = true;
            }
        }
        catch(Exception e){
            failed = true;
        }
    }



}
