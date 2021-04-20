package com.example.puppigram.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.io.FileNotFoundException;
import java.io.InputStream;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class PhotoUtil {
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int REQUEST_IMAGE_GALLERY = 2;
    @SuppressLint("StaticFieldLeak")
    static Activity myActivity;
    ImageView showPlace;
    public PhotoUtil(Activity myActivity){
        PhotoUtil.myActivity = myActivity;
        this.showPlace = null;
    }

    public void checkAndRequestPermissionForCamera(ImageView postImg) {
        this.showPlace = myActivity.findViewById(postImg.getId());
        getPictureFromUser();
    }

    public static void getPictureFromUser()
    {
        Log.d("TAG", "getPictureFromUser: get picture");
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(myActivity);
        builder.setTitle("Choose picture");
        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals("Take Photo")) {
                Intent takePicture = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE
                );
                myActivity.startActivityForResult(takePicture, REQUEST_IMAGE_CAPTURE);
            } else if (options[item].equals("Choose from Gallery")) {
                Intent pickPhoto = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                );
                myActivity.startActivityForResult(pickPhoto, REQUEST_IMAGE_GALLERY);
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
                            imageStream = myActivity.getContentResolver().openInputStream(imageUri);
                            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                            showPlace.setImageBitmap(selectedImage);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
    }
    public static void UriToImageView(Uri srcImg, ImageView dstImg, String errorMsg, Context context){
        try {
            InputStream inputStream =
                    context.getContentResolver().openInputStream(srcImg);
            dstImg.setImageDrawable(
                    Drawable.createFromStream(
                            inputStream,
                            srcImg.toString()
                    )
            );
        } catch (FileNotFoundException e) {
            Toast.makeText(
                    context,
                    errorMsg,
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    public static Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte = Base64.decode(encodedString,Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        }
        catch(Exception e){
            e.getMessage();
            return null;
        }
    }
}
