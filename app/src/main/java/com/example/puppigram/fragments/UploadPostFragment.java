package com.example.puppigram.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.concurrent.Callable;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.puppigram.R;
import com.example.puppigram.model.ImagePost;
import com.example.puppigram.model.PostsModelSQL;
import com.example.puppigram.utils.PhotoUtils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UploadPostFragment#} factory method to
 * create an instance of this fragment.
 */
public class UploadPostFragment extends Fragment {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageView uploadImgBtn;
    ImageView postImg;
    EditText description;
    TextView usernameText;
    ImageView usernameImg;
    ImageView removeImgBtn;
    PhotoUtils photoUtils;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        photoUtils = new PhotoUtils(this.getActivity());
    }

    @SuppressLint({"WrongViewCast", "WrongConstant"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_upload_post, container, false);
        uploadImgBtn = view.findViewById(R.id.upload_capture_img);
        postImg = (ImageView) view.findViewById(R.id.upload_post_img);
        description = (EditText) view.findViewById(R.id.upload_post_description);
        usernameText = view.findViewById(R.id.upload_username_text);
        usernameImg = (ImageView) view.findViewById(R.id.upload_username_img);
        removeImgBtn = (ImageView) view.findViewById(R.id.upload_remove_img);
        //TODO: show username image and name;
        //username_text.setText();
        // username_img.setImageDrawable();

        TextView post_btn = view.findViewById(R.id.upload_post_text);
        post_btn.setOnClickListener(v -> uploadPost(v));
        uploadImgBtn.setOnClickListener(v -> photoUtils.getPictureFromUser());

        removeImgBtn.setOnClickListener(v -> {
            cleanView();
            Toast.makeText(getContext(),"post details removed",40).show();
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        photoUtils.onResult(requestCode,resultCode, data, postImg);
    }


    @SuppressLint("WrongConstant")
    public void uploadPost(View view){
        //TODO: fix handling images..
        uploadImgBtn.setEnabled(false);
        if (postImg.getDrawable() == null){
            Log.d("TAG", "upload_post: No image selected");
            Toast.makeText(view.getContext(),"No image selected",40).show();
            uploadImgBtn.setEnabled(true);
            return;
        }
        if (description.getText() == null)
            description.setText("");
        ImagePost newPost = new ImagePost("44","50","haroy", "hello");
        PostsModelSQL.instance.addPost(newPost, result -> {
            Log.d("TAG", "uploadPost: Post was uploaded");
            Toast.makeText(view.getContext(),"Post was uploaded",40).show();
            cleanView();
            uploadImgBtn.setEnabled(true);
        });
    }
    void cleanView() {
        if (this.description.getText() != null)
            this.description.setText("");
        if (this.postImg.getDrawable() == null)
            this.postImg.setImageDrawable(null);
    }
}