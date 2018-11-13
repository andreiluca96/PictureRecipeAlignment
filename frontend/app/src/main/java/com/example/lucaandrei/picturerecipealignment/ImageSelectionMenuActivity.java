package com.example.lucaandrei.picturerecipealignment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.lucaandrei.picturerecipealignment.camera.CameraActivity;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageSelectionMenuActivity extends AppCompatActivity {

    public static final int PICK_IMAGE = 1;
    static final int REQUEST_IMAGE_CAPTURE = 2;

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
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            try {
                InputStream inputStream = getApplicationContext().getContentResolver().openInputStream(data.getData());
                processPicture(IOUtils.toByteArray(inputStream));
                //TODO: request with the selected picture
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            processPicture(stream.toByteArray());
            imageBitmap.recycle();

            ImageView mImageView = new ImageView(getApplicationContext());
            mImageView.setImageBitmap(imageBitmap);
            //TODO: get full size picture and request
        }
    }

    private void processPicture(byte[] picture) {
        //TODO: store the picture somewhere
    }

    public void startCameraActivity() {
        startActivity(new Intent(this,CameraActivity.class));
    }
}
