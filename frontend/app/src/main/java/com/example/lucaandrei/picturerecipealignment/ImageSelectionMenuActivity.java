package com.example.lucaandrei.picturerecipealignment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;

import com.example.lucaandrei.picturerecipealignment.camera.CameraActivity;
import com.example.lucaandrei.picturerecipealignment.result.ResultActivity;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageSelectionMenuActivity extends AppCompatActivity {

    public static final int PICK_IMAGE = 1;
    static final int REQUEST_IMAGE_CAPTURE = 2;

    private static final String IMG_URL = "http://10.0.2.2:5000/image";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_menu_selection);

        Button btnGallery = (Button) findViewById(R.id.button_gallery);

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent selectPictureIntent = new Intent();
                selectPictureIntent.setType("image/*");
                selectPictureIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(selectPictureIntent, "Select Picture"), PICK_IMAGE);
            }
        });

        Button btnCamera = (Button) findViewById(R.id.button_camera);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String encoded;
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                selectedImage.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();

                encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

                JSONObject requestBody = new JSONObject();
                requestBody.put("img", encoded);

                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), requestBody.toString());

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .post(body)
                        .url(IMG_URL)
                        .build();

                Response response = client.newCall(request).execute();
                JSONObject responseBody = new JSONObject(response.body().string());

                JSONArray ingredientList = responseBody.getJSONArray("ingredients");
                String[] ingredients = new String[ingredientList.length()];

                for (int i = 0; i < ingredientList.length(); i++) {
                    ingredients[i] = ingredientList.getString(i);
                }


                Intent myIntent = new Intent(this, ResultActivity.class);
                myIntent.putExtra("image", byteArray);
                myIntent.putExtra("ingredients", ingredients);
                this.startActivity(myIntent);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            try {
                final Bitmap selectedImage = (Bitmap) data.getExtras().get("data");

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                selectedImage.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();

                encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

                String ingredientsJson =
                        "{" +
                                "\"img\": \"" + encoded + "\"" +
                                "}";

                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), ingredientsJson);

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .post(body)
                        .url(IMG_URL)
                        .build();

                Response response = client.newCall(request).execute();
                JSONObject responseBody = new JSONObject(response.body().string());

                JSONArray ingredientList = responseBody.getJSONArray("ingredients");
                String[] ingredients = new String[ingredientList.length()];

                for (int i = 0; i < ingredientList.length(); i++) {
                    ingredients[i] = ingredientList.getString(i);
                }


                Intent myIntent = new Intent(this, ResultActivity.class);
                myIntent.putExtra("image", byteArray);
                myIntent.putExtra("ingredients", ingredients);
                this.startActivity(myIntent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void startCameraActivity() {
        startActivity(new Intent(this, CameraActivity.class));
    }
}
