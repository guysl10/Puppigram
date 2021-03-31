package com.example.puppigram.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.puppigram.R;
import com.example.puppigram.model.MyApp;
import com.example.puppigram.model.PostsModel;
import com.example.puppigram.model.PostsModelSQL;
import com.example.puppigram.model.ImagePost;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UploadPostFragment#} factory method to
 * create an instance of this fragment.
 */
public class UploadPostFragment extends Fragment {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageView upload_post_btn;
    ImageView post_img;
    EditText description;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_upload_post, container, false);
        upload_post_btn = view.findViewById(R.id.upload_post_img);
        post_img = (ImageView) view.findViewById(R.id.upload_post_img);
        description = view.findViewById(R.id.post_description);

        TextView post_btn = view.findViewById(R.id.upload_post_text);
        post_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload_post(view);
            }
        });
        ImageView capture_btn = view.findViewById(R.id.upload_capture_img);
        capture_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: add support to pickup image from gallery
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            ImageView new_img = this.getView().findViewById(R.id.upload_post_img);
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            new_img.setImageBitmap(imageBitmap);
        }
    }


    public void upload_post(View view){
//TODO: fix handling images..

        upload_post_btn.setEnabled(false);
        if (post_img.getDrawable() == null){
            Toast.makeText(MyApp.context,"No image selected",40).show();
            upload_post_btn.setEnabled(true);
            return;
        }
//        if (description.getText() == null)
//            description.setText("");
//        ImagePost new_post = new ImagePost(50,50,description.getText().toString(), "hello");
        ImagePost new_post = new ImagePost(55,50,"haroy", "hello");
        PostsModelSQL.instance.addPost(new_post, null);
        upload_post_btn.setEnabled(true);
    }
}