package com.example.puppigram.utils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;

import com.example.puppigram.activities.MainActivity;

import java.io.FileNotFoundException;
import java.io.InputStream;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class PhotoUtil {
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int REQUEST_IMAGE_GALLERY = 2;
    static MainActivity mainActivity;
    ImageView showPlace;
    public PhotoUtil(MainActivity mainActivity){
        PhotoUtil.mainActivity = mainActivity;
        this.showPlace = null;
    }

    public void checkAndRequestPermissionForCamera(ImageView postImg) {
        this.showPlace = mainActivity.findViewById(postImg.getId());
        getPictureFromUser();
    }

    public static void getPictureFromUser()
    {
        Log.d("TAG", "getPictureFromUser: get picture");
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        builder.setTitle("Choose picture");
        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals("Take Photo")) {
                Intent takePicture = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE
                );
                mainActivity.startActivityForResult(takePicture, REQUEST_IMAGE_CAPTURE);
            } else if (options[item].equals("Choose from Gallery")) {
                Intent pickPhoto = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                );
                mainActivity.startActivityForResult(pickPhoto, REQUEST_IMAGE_GALLERY);
            } else if (options[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public void onResult(int requestCode, int resultCode, Intent data){
        if(resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case REQUEST_IMAGE_CAPTURE:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        if(this.showPlace != null){
                            this.showPlace.setImageBitmap(selectedImage);
                        }
                    }
                    break;
                case REQUEST_IMAGE_GALLERY:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri imageUri = data.getData();
                        InputStream imageStream;
                        try {
                            imageStream = mainActivity.getContentResolver().openInputStream(imageUri);
                            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                            showPlace.setImageBitmap(selectedImage);
                            showPlace.setRotation(90);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
    }
}
