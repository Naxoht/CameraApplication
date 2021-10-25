package com.example.cameraapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {


    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            ImageView img = (ImageView) findViewById(R.id.imageView);
            // Get the dimensions of the View
            int targetW = img.getWidth();
            int targetH = img.getHeight();

            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            File path = this.getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            try {
                BitmapFactory.decodeFile(getFile().getAbsolutePath(), bmOptions);
            } catch (IOException e) {
                e.printStackTrace();
            }

            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            int scaleFactor = Math.max(1, Math.min(photoW/targetW, photoH/targetH));

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeFile(getFile().getAbsolutePath(), bmOptions);
            } catch (IOException e) {
                e.printStackTrace();
            }
            img.setImageBitmap(bitmap);

        }

    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = getFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }

    }
    protected File getFile() throws IOException{
        File path = this.getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imatge = new File(path,"imatge34.jpg");
        return imatge;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final ImageView imageView = findViewById(R.id.imageView);
        try {
            Uri fileUri = Uri.fromFile(getFile());
            imageView.setImageURI(fileUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final ImageView imageView = findViewById(R.id.imageView);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
            //Save image in file
            try {
                FileOutputStream os = new FileOutputStream(image);
                imageBitmap.compress(Bitmap.CompressFormat.JPEG,100,os);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
     */

}