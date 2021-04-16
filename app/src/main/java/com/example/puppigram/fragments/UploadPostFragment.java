package com.example.puppigram.fragments;

import android.annotation.SuppressLint;
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
import com.example.puppigram.activities.MainActivity;
import com.example.puppigram.model.ImagePost;
import com.example.puppigram.model.PostsModelFirebase;
import com.example.puppigram.model.PostsModelSQL;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UploadPostFragment#} factory method to
 * create an instance of this fragment.
 */
public class UploadPostFragment extends Fragment {
    ImageView uploadPostBtn;
    ImageView postImg;
    EditText description;
    TextView username;
    ImageView usernameImg;
    ImageView removeContentImg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upload_post, container, false);
        uploadPostBtn = view.findViewById(R.id.upload_capture_img);
        postImg = (ImageView) view.findViewById(R.id.upload_post_img);
        description = view.findViewById(R.id.upload_post_description);
        username = view.findViewById(R.id.upload_username_text);
        usernameImg = (ImageView) view.findViewById(R.id.upload_username_img);
        removeContentImg = (ImageView) view.findViewById(R.id.upload_remove_img);

        //TODO: show username image and name;
        //username_text.setText();
        // username_img.setImageDrawable();

        TextView postBtn = view.findViewById(R.id.upload_post_text);
        postBtn.setOnClickListener(v -> upload_post(view));
        uploadPostBtn.setOnClickListener(v ->
                        ((MainActivity) requireActivity()).getPhotoActivity().
                        checkAndRequestPermissionForCamera(postImg)
        );
        removeContentImg.setOnClickListener(v -> removeContent());
        return view;
    }

    private void removeContent() {
        description.setText("");
        postImg.setImageResource(0);
    }

    @SuppressLint("WrongConstant")
    public void upload_post(View view) {
//TODO: fix handling images..

        uploadPostBtn.setEnabled(false);
        if (postImg.getDrawable() == null) {
            Log.d("TAG", "upload_post: No image selected");
            Toast.makeText(view.getContext(), "No image selected", Toast.LENGTH_LONG).show();
            uploadPostBtn.setEnabled(true);
            return;
        }
        if (description.getText() == null)
            description.setText("");
        //TODO: pas the post image..
        ImagePost new_post = new ImagePost("44", "50", "haroy", null);
        PostsModelSQL.instance.addPost(new_post, null);
        PostsModelFirebase.instance.addPost(new_post, () -> {
            Log.d("TAG", "upload_post: Post was uploaded");
            Toast.makeText(view.getContext(), "Post was uploaded", Toast.LENGTH_LONG).show();
            uploadPostBtn.setEnabled(true);
        });
    }
}