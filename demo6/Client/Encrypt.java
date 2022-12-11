package com.example.demo6.Client;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.*;
import javax.crypto.*;
import javax.crypto.spec.*;
    
//adapted from notes found on baeldung.com on AES in java

public class Encrypt {

    public static String algorithm = "AES/CBC/PKCS5Padding";
    
      
    //using this for now to assume some shared key infrastructure, perhaps we will get into 
    //ways to generate the secret on both sides and feed this with it to initialize a key...if we have time

    public static SecretKey getKeyFromPassword(String password, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
        SecretKey secret = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
        return secret;
    }

    //gets an IVParameter from some 16 byte array(we will use part of previous message's hash to feed IV for each new message)

    public static IvParameterSpec generateIv(byte[] bytes) {
            return new IvParameterSpec(bytes);
    }

    //returns encrypted bytes from a string

    public static byte[] encrypt (String input, SecretKey key, IvParameterSpec iv) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,InvalidKeyException, BadPaddingException, IllegalBlockSizeException{
        Cipher cipher = Cipher.getInstance(Encrypt.algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        byte[] output = cipher.doFinal(input.getBytes());
        return output;
    }

    //returns decrypted string from bytes

    public static String decrypt (byte[] input, SecretKey key, IvParameterSpec iv) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,InvalidKeyException, BadPaddingException, IllegalBlockSizeException{
        Cipher cipher = Cipher.getInstance(Encrypt.algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        String output = new String(cipher.doFinal(input));
        return output;
    }

}
