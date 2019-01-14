package com.fii.picture.recipe.alignment.searchableencryption.service;

/**
 * Created by Ariana on 1/14/2019.
 */

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;


public class StreamCipher  {
    private static StreamCipher ourInstance = new StreamCipher();
    protected static Cipher cipher = null;
    protected static SecretKeySpec keySpec ;
    public static StreamCipher getInstance() {
        return ourInstance;
    }

    private StreamCipher(){

    }
    public static boolean init(SecretKeySpec spec){
        try {

            keySpec = spec;
            cipher = Cipher.getInstance("AES/CTR/NoPadding", "BC");

            return true;
        }
        catch (Exception e){
            // Do Some thing later .
        }
        return  false;
    }


    public static byte[] getRandomStreamOfBytes(long recordId, byte[] seedBytes) throws Exception {
        try {
            // xor(recordId, seedByte)
            byte[] nonce = getNonce(recordId, seedBytes);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(nonce));
        }
        catch (Exception e){
            // do something later ;
        }

        // Plain text is 0 always we don't really care
        return cipher.doFinal(ByteBuffer.allocate(16).putInt(0).array());
    }
    private static byte[] getNonce(long recordId, byte[] seedBytes){
        byte[] nonce = new byte[16];

        try {

            byte[] recordIdBytes = ByteBuffer.allocate(16).putLong(recordId).array();
            nonce = SSEUtil.xorTwoByteArrays(seedBytes, recordIdBytes);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return nonce;
    }
    public  byte[] encrypt(byte[] plainBytes) throws Exception {

        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        return cipher.doFinal(plainBytes);
    }


    public  byte[] decrypt(byte[] cipherText) throws Exception {

        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        return cipher.doFinal(cipherText);
    }

    public byte[] encrypt(byte[] plainBytes, byte[] ivBytes) throws Exception {

        cipher.init(Cipher.ENCRYPT_MODE, keySpec,new IvParameterSpec(ivBytes) );

        return cipher.doFinal(plainBytes);
    }

    public byte[] decrypt(byte[] cipherBytes, byte[] ivBytes) throws Exception {

        cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(ivBytes));

        return cipher.doFinal(cipherBytes);
    }

}
