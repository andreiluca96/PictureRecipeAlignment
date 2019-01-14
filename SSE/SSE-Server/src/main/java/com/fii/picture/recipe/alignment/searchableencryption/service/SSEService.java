package com.fii.picture.recipe.alignment.searchableencryption.service;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;

@Service
public class SSEService implements CrudService{
    private String password;
    private double loadFactor = 1;

    @Override
    public List getAll(String id)throws Exception {
        password = "test"; // NOT FOR PRODUCTION
        Security.addProvider(new BouncyCastleProvider());

        List<Integer> recordIds = new ArrayList<Integer>();
        try {
            final SecretKeySpec secretKeySpec = SSEUtil.getSecretKeySpec(password,
                    SSEUtil.getRandomBytes(20));
            SWP swp = new SWP(secretKeySpec, "AES",loadFactor, 128);

            JsonUtil.swp =swp;
            JsonUtil.translateToDecryptedJSON();
            JsonUtil.translateToEncryptedJSON();
            byte[] searchPlainText = (id).getBytes();
            recordIds = JsonUtil.searchEncryptedJSON(searchPlainText);

        } catch (Exception e){
            e.printStackTrace();
        }

        return recordIds;
    }

    public void testJsonUtil() throws Exception {
        System.out.println("Test AES Searchable ");

        double loadFactor = 1; // No false positives but additional storage

    }
}
