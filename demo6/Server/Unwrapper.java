package com.example.demo6.Server;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class Unwrapper {
    
    //lets get these bytes.
    
    public byte firstByte;
    public byte secondByte;
    public int length;
    public byte[] payload;
    
    public Unwrapper(BufferedInputStream buffer) throws IOException
    {
        //gets system codes from first two bytes, can be used to create new message types
        //plan to build out to facilitate login cryptographic trust and message acks

        this.firstByte = (byte)buffer.read();//ByteBuffer.allocate(4).put((byte)buffer.read()).get(0);  System.out.println("first"+firstByte); //gets byte at 0 of returned int (4 bytes)
        this.secondByte = (byte)buffer.read(); //I am going to suggest we perahaps simplify to this? no need to create a buffer here I don't think, we can read bytes directly from the stream by casting the ints of read(){which reads only one byte} back to byte
        

        this.length = getLength(buffer); //System.out.println("lenth"+length);

        //doing this seemed to get the payload correctly, I was getting an array of 0/nulls before
        payload = new byte[length];
        buffer.readNBytes(payload, 0, length);
    }

    private int getLength(BufferedInputStream buffer) throws IOException
    {
        byte[] array = new byte[4];
        for(int i=0; i<4; i++)
        {
            array[i] = (byte)buffer.read(); //I am pretty sure this is the business, its been running
        } 

        int test = ByteBuffer.wrap(array).getInt();
        //System.out.println(test);
        return test;
    }
//depreciated this method for getPayload by using readNBytes
/*/    private byte[] getPayload(BufferedInputStream buffer, int length) throws IOException
    {
        byte[] array = new byte[length];
        for(int i=0; i<length; i++)
        {
            array[i] = ByteBuffer.allocate(4).putInt(buffer.read()).get(0);
        }
        return array;
    }
*/


}