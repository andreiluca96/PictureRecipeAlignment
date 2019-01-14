package com.fii.picture.recipe.alignment.searchableencryption.service;

/**
 * Created by Ariana on 1/14/2019.
 */

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.InvalidParameterException;


public class AES implements BlockCipher {

    protected Cipher cipher;
    protected SecretKeySpec keySpec ;
    byte[]  ivBytes = null;

    public AES(String mode, SecretKeySpec spec) throws InvalidParameterException {
        try {
            keySpec = spec ;
            cipher = Cipher.getInstance(mode, "BC");
        } catch (Exception e){
            throw  new InvalidParameterException("Invalid Parameters" + e.getMessage());
        }

    }
    public AES(String mode, SecretKeySpec spec, byte[] ivBytes) throws InvalidParameterException{
        try {
            keySpec = spec ;
            this.ivBytes = ivBytes;
            cipher = Cipher.getInstance(mode, "BC");
        } catch (Exception e){
            throw  new InvalidParameterException("Invalid Parameters" + e.getMessage());
        }
    }
    public AES(String mode, byte[] key) throws InvalidParameterException {
        byte[] fullKey = new byte[16];

        if(key == null )
            throw new InvalidParameterException("Key is empty cannot proceed" );
        if(key.length > 16)
            throw new InvalidParameterException("Key Size > 16 bytes" + key.length);

        System.arraycopy(key,0,fullKey,0,key.length);
        if(key.length < 16){
            for (int i = key.length ; i < 16 ; i ++)
                fullKey[i] = 0 ;
        }

        try {
            keySpec = new SecretKeySpec(fullKey, "AES");
            cipher = Cipher.getInstance(mode, "BC");
        }   catch (Exception e){
            throw  new InvalidParameterException("Invalid Parameters" + e.getMessage());
        }

    }

    public  byte[] encrypt(byte[] plainBytes) throws Exception {

        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        return cipher.doFinal(plainBytes);
    }


    public  byte[] decrypt(byte[] cipherText) throws Exception {

        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        return cipher.doFinal(cipherText);
    }

    public byte[] getIvBytes(long id){
        byte[] idBytes = ByteBuffer.allocate(16).putLong(id).array();
        return SSEUtil.xorTwoByteArrays(ivBytes, idBytes);
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
