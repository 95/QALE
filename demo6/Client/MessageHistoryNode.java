package com.example.demo6.Client;

import java.time.*;

public class MessageHistoryNode {
    long recId;
    long sendId;
    byte[] message;
    byte[] hash;
    Instant time;
    MessageHistoryNode prev;
    MessageHistoryNode next;
    MessageHistory partOf;

    MessageHistoryNode(NetworkMessage netMsg)
    {   
        recId = netMsg.receiverID;
        sendId = netMsg.senderID;
        message = netMsg.message;
        hash = netMsg.hash;
        time = Instant.now();
    }
    
}
