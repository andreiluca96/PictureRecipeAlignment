package com.fii.picture.recipe.alignment.searchableencryption.service;

/**
 * Created by Ariana on 1/14/2019.
 */
public interface SearchableCipher {

    byte[] encrypt(byte[] plainBytes) throws Exception;

    byte[] decrypt(byte[] cipherBytes)throws Exception;

    byte[] getTrapDoor(byte[] plainBytes) throws Exception;

    boolean isMatch(byte[] trapDoorBytes, byte[] cipherBytes)throws Exception;
}

