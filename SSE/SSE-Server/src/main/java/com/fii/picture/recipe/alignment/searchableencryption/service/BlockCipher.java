package com.fii.picture.recipe.alignment.searchableencryption.service;

/**
 * Created by Ariana on 1/14/2019.
 */
public interface BlockCipher {

    public byte[] encrypt(byte[] plainBytes) throws Exception;
    public byte[] decrypt(byte[] cipherText) throws Exception;
    public byte[] encrypt(byte[] plainBytes, byte[] ivBytes) throws Exception;


    public byte[] decrypt(byte[] cipherText, byte[] ivBytes) throws Exception;

    public byte[] getIvBytes(long id);
}
