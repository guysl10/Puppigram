package com.example.puppigram.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.puppigram.R;
import com.example.puppigram.model.ImagePost;
import com.example.puppigram.model.PostsModelFirebase;
import com.example.puppigram.model.PostsModelSQL;
import com.example.puppigram.utils.PhotoUtils;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UploadPostFragment#} factory method to
 * create an instance of this fragment.
 */
public class UploadPostFragment extends Fragment {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageView uploadPostBtn;
    ImageView postImg;
    EditText description;
    TextView username;
    ImageView usernameImg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upload_post, container, false);
        uploadPostBtn = view.findViewById(R.id.upload_post_img);
        postImg = (ImageView) view.findViewById(R.id.upload_post_img);
        description = view.findViewById(R.id.postDescription);
        username = view.findViewById(R.id.upload_username_text);
        usernameImg = (ImageView) view.findViewById(R.id.upload_post_img);

        //TODO: show username image and name;
        //username_text.setText();
        // username_img.setImageDrawable();

        TextView postBtn = view.findViewById(R.id.upload_post_text);
        postBtn.setOnClickListener(v -> upload_post(view));
        ImageView captureBtn = view.findViewById(R.id.upload_capture_img);
        captureBtn.setOnClickListener(v -> PhotoUtils.getPictureFromUser());
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        PhotoUtils.onResult(requestCode, resultCode, data, postImg);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            ImageView newImg = this.getView().findViewById(R.id.upload_post_img);
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            newImg.setImageBitmap(imageBitmap);
        }
    }

    @SuppressLint("WrongConstant")
    public void upload_post(View view) {
//TODO: fix handling images..

        uploadPostBtn.setEnabled(false);
        if (postImg.getDrawable() == null) {
            Log.d("TAG", "upload_post: No image selected");
            Toast.makeText(view.getContext(), "No image selected", 40).show();
            uploadPostBtn.setEnabled(true);
            return;
        }
        if (description.getText() == null)
            description.setText("");
        ImagePost new_post = new ImagePost("44", "50", "haroy", null);
        PostsModelSQL.instance.addPost(new_post, null);
        PostsModelFirebase.instance.addPost(new_post, () -> {
            Log.d("TAG", "upload_post: Post was uploaded");
            Toast.makeText(view.getContext(), "Post was uploaded", 40).show();
            uploadPostBtn.setEnabled(true);
        });
    }
}