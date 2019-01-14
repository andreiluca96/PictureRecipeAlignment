package com.fii.picture.recipe.alignment.searchableencryption.service;

/**
 * Created by Ariana on 1/14/2019.
 */
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.PrintWriter;
import java.util.*;

public class JsonUtil
{
    public static SWP swp;

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

    public static void translateToEncryptedJSON() throws Exception
    {
        Object obj = new JSONParser().parse(new FileReader("D:\\Facultate\\master1\\masterIP\\PictureRecipeAlignment\\SSE\\javaSSE\\src\\main\\java\\jsse\\decrypted-truncated-det-ingrs-100.json"));
        // parsing file "JSONExample.json"
        JSONArray jsonArr = (JSONArray) obj;
        JSONArray finalJsonArr = new JSONArray();

        int recordId = 1;
        for( Object jo : jsonArr){

            JSONObject jsonObject  = (JSONObject) jo;
            JSONObject finalJsonObject  = new JSONObject();

            JSONArray ingredientsArray = (JSONArray) jsonObject.get("ingredients");

            JSONArray validIngredients = new JSONArray();

            int index = 0;
            for (Object object: ingredientsArray){
                JSONObject ingredient = (JSONObject) object;
                String stringIngredient = (String) ingredient.get("text");
                if (stringIngredient.length()<=15){
                    byte[] plainBytes = stringIngredient.toLowerCase().replaceAll("[^A-Za-z]","").getBytes();
                    byte[] cipherText = swp.encrypt(plainBytes, recordId);
                    swp.decrypt(cipherText, recordId);

                    String encodedCipherText = Base64.getEncoder().encodeToString(cipherText);
                    Map m = new LinkedHashMap(1);
                    m.put("text", encodedCipherText);
                    validIngredients.add(m);
                }
                index += 1;
            }

            finalJsonObject.put("id", recordId);
            finalJsonObject.put("ingredients", validIngredients);
            finalJsonArr.add(finalJsonObject);

            recordId += 1;
        }

        // writing JSON to file:"JSONExample.json" in cwd
        PrintWriter pw = new PrintWriter("D:\\Facultate\\master1\\masterIP\\PictureRecipeAlignment\\SSE\\javaSSE\\src\\main\\java\\jsse\\encrypted-truncated-det-ingrs-100.json");
        pw.write(finalJsonArr.toJSONString());

        pw.flush();
        pw.close();
    }

    public static List<Integer> searchEncryptedJSON(byte[] searchPlainText) throws Exception
    {
        Object obj = new JSONParser().parse(new FileReader("D:\\Facultate\\master1\\masterIP\\PictureRecipeAlignment\\SSE\\javaSSE\\src\\main\\java\\jsse\\encrypted-truncated-det-ingrs-100.json"));
        // parsing file "JSONExample.json"
        JSONArray jsonArr = (JSONArray) obj;
        List<Integer> recordIds = new ArrayList<>();
        int recordId = 1;
        for( Object jo : jsonArr){

            JSONObject jsonObject  = (JSONObject) jo;

            JSONArray ingredientsArray = (JSONArray) jsonObject.get("ingredients");

            for (Object object: ingredientsArray ){
                JSONObject ingredient = (JSONObject) object;
                String stringIngredient = (String) ingredient.get("text");
                byte[] byteIngrefient = Base64.getDecoder().decode(stringIngredient);
                byte[] plainText = swp.decrypt(byteIngrefient, recordId);

                if(Arrays.equals(searchPlainText, plainText)){
                    recordIds.add(recordId);
                }
            }
            recordId += 1;
        }

        return recordIds;
    }
}