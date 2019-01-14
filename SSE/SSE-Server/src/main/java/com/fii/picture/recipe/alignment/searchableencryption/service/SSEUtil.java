package com.fii.picture.recipe.alignment.searchableencryption.service;

/**
 * Created by Ariana on 1/14/2019.
 */
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileReader;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;


public class SSEUtil {
    public static void translateToDecryptedJSON() throws Exception
    {
        Object obj = new JSONParser().parse(new FileReader("D:\\Facultate\\master1\\masterIP\\PictureRecipeAlignment\\SSE\\javaSSE\\src\\main\\java\\jsse\\truncated-det-ingrs-100.json"));
        // parsing file "JSONExample.json"
        JSONArray jsonArr = (JSONArray) obj;
        JSONArray finalJsonArr = new JSONArray();

        int recordId = 1;
        // getting firstName and lastName
        for( Object jo : jsonArr){

            JSONObject jsonObject  = (JSONObject) jo;
            JSONObject finalJsonObject  = new JSONObject();

            JSONArray validArray = (JSONArray) jsonObject.get("valid");
            JSONArray ingredientsArray = (JSONArray) jsonObject.get("ingredients");

            List<JSONObject> validIngredients = new ArrayList<>();

            int index = 0;
            for (Object valid: validArray){
                Boolean isValid = (Boolean) valid;
                if(isValid){
                    JSONObject validIngredient = (JSONObject) ingredientsArray.get(index);
                    validIngredients.add(validIngredient);
                }

                index += 1;
            }

            finalJsonObject.put("id", recordId);
            finalJsonObject.put("ingredients", validIngredients);
            finalJsonArr.add(finalJsonObject);

            recordId += 1;
        }

        // writing JSON to file:"JSONExample.json" in cwd
        PrintWriter pw = new PrintWriter("D:\\Facultate\\master1\\masterIP\\PictureRecipeAlignment\\SSE\\javaSSE\\src\\main\\java\\jsse\\decrypted-truncated-det-ingrs-100.json");
        pw.write(finalJsonArr.toJSONString());

        pw.flush();
        pw.close();
    }

    public static byte[] xorTwoByteArrays(byte[] byteArray1, byte[] byteArray2) {
        int maxLength = byteArray1.length > byteArray2.length ? byteArray1.length : byteArray2.length;
        int minLength = byteArray1.length > byteArray2.length ? byteArray2.length : byteArray1.length;

        byte[] xorBytes = new byte[maxLength];
        for (int i = 0; i < minLength ; i++){
            xorBytes[i] = (byte) (byteArray1[i] ^ byteArray2[i]);
        }
        if (maxLength == byteArray1.length)
            System.arraycopy(byteArray1,minLength,xorBytes,minLength,maxLength-minLength);
        if (maxLength == byteArray2.length)
            System.arraycopy(byteArray2,minLength,xorBytes,minLength,maxLength-minLength);

        return xorBytes;
    }

    public static byte[] getRandomBytes(int count) {

        // Generate the Salt
        SecureRandom random = new SecureRandom();
        byte[] saltyBytes = new byte[count];
        random.nextBytes(saltyBytes);

        return saltyBytes;
    }
    public static SecretKeySpec getSecretKeySpec(String password, byte[] saltyBytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        int pswdIterations = 65536;
        int keySize = 128;

        // Derive the key
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        PBEKeySpec spec =  new PBEKeySpec( password.toCharArray(),saltyBytes,
                pswdIterations, keySize );


        SecretKey secretKey = factory.generateSecret(spec);

        return new SecretKeySpec(secretKey.getEncoded(), "AES");
    }

}
