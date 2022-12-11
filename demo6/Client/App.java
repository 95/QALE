import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.*;
import javax.crypto.*;
import javax.crypto.spec.*;
/*
public class App {
    public static void main(String[] args) throws Exception {
        try{
        SecretKey key = Encrypt.getKeyFromPassword("testcase","passed");
        IvParameterSpec iv = Encrypt.generateIv("abcdefghijklmnop".getBytes());
        
        String test ="Hello encryption!";

        System.out.println(test);
        
        
        byte[] byteMessage = Encrypt.encrypt(test, key, iv);
        long senderId = 75;
        long recId=83;
        NetworkMessage message = new NetworkMessage(senderId, recId, byteMessage);
        System.out.println(new String(message.assembledMessage));
        System.out.println(message.senderID);
        System.out.println(message.receiverID);

        NetworkMessage message0 = new NetworkMessage(message.assembledMessage);
        System.out.println(new String(message0.message));
        System.out.println(message0.senderID);
        System.out.println(message0.receiverID);

        String testOut = Encrypt.decrypt(message0.message, key, iv);
        System.out.println(testOut);
        }
        catch(Exception e){}
    }
}


 */