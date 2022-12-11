package com.example.demo6.Client;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class Unwrapper {

    public byte firstByte;
    public byte secondByte;
    public int length;
    public byte[] payload;
    
    Unwrapper(BufferedInputStream buffer) throws IOException
    {
        this.firstByte = ByteBuffer.allocate(4).putInt(buffer.read()).get(0);  System.out.println("first"+firstByte); //gets byte at 0 of returned int (4 bytes)
        this.secondByte = ByteBuffer.allocate(4).putInt(buffer.read()).get(0);  System.out.println("second"+secondByte);
        this.length = getLength(buffer);
        payload = new byte[length];
        buffer.readNBytes(payload, 0, length);
    }

    private int getLength(BufferedInputStream buffer) throws IOException
    {
        byte[] array = new byte[4];
        for(int i=0; i<4; i++)
        {
            array[i] = ByteBuffer.allocate(4).putInt(buffer.read()).get(0);
        } 

        int test = ByteBuffer.wrap(array).getInt();
        return test;
    }

    private byte[] getPayload(BufferedInputStream buffer, int length) throws IOException
    {
        byte[] array = new byte[length];
        for(int i=0; i<length; i++)
        {
            array[i] = ByteBuffer.allocate(4).putInt(buffer.read()).get(0);
        }
        return array;
    }



}