package com.example.puppigram.utils;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.puppigram.activities.MainActivity;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class PhotoActivity {
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int REQUEST_IMAGE_GALLERY = 2;
    static MainActivity mainActivity;
    ImageView showPlace;
    public PhotoActivity (MainActivity mainActivity){
        this.mainActivity = mainActivity;
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
                        Uri selectedImage =  data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = mainActivity.getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();
                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                if(this.showPlace != null){
                                    this.showPlace.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                }
                                cursor.close();
                            }
                        }
                    }
                    break;
            }
        }
    }
}
