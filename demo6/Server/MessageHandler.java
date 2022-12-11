package com.example.demo6.Server;

public class MessageHandler implements Runnable {
    
    private Unwrapper unwrap;
    private ClientHandler clientHandler;

    MessageHandler(Unwrapper unwrap, ClientHandler clientHandler){
        System.out.println("Lets handle it");
        this.clientHandler = clientHandler;
        this.unwrap = unwrap;
    }

    @Override
    public void run() {

        System.out.print("in the running");
        switch (unwrap.firstByte) {


            case 0:
                switch(unwrap.secondByte){
                    case (byte)0:
                        break;
                    case(byte)1:
                        break;
                }
            break;

            case 0x01:
                switch(unwrap.secondByte){
                    case (byte)0:
                        break;
                    case 0x01:
                        System.out.println("managed");
                        clientHandler.forward(unwrap.payload);
                        break;
                }
            break;
        }
    }
    
}
