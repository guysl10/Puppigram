package com.example.puppigram.fragments;

import android.annotation.SuppressLint;
import android.net.Uri;
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

import androidx.fragment.app.Fragment;

import com.example.puppigram.R;
import com.example.puppigram.activities.MainActivity;
import com.example.puppigram.model.ImagePost;
import com.example.puppigram.model.PostsModel;
import com.example.puppigram.model.PostsModelFirebase;
import com.example.puppigram.model.PostsModelSQL;
import com.example.puppigram.repos.Repo;
import com.example.puppigram.repos.UserRepo;
import com.example.puppigram.utils.PhotoUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UploadPostFragment#} factory method to
 * create an instance of this fragment.
 */
public class UploadPostFragment extends Fragment {
    ImageView captureBtn;
    ImageView postImg;
    EditText description;
    TextView username;
    ImageView usernameImg;
    ImageView removeContentImg;
    FirebaseUser currentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upload_post, container, false);
        captureBtn = view.findViewById(R.id.upload_capture_img);
        postImg = (ImageView) view.findViewById(R.id.upload_post_img);
        description = view.findViewById(R.id.upload_post_description);
        username = view.findViewById(R.id.upload_username_text);
        usernameImg = (ImageView) view.findViewById(R.id.upload_username_img);
        removeContentImg = (ImageView) view.findViewById(R.id.upload_remove_img);
        currentUser= UserRepo.instance.getAuthInstance().getCurrentUser();

        if (currentUser != null) {
            username.setText(currentUser.getDisplayName());
            try {
                postImg.setImageBitmap(MediaStore.Images.Media.getBitmap(
                        getActivity().getApplicationContext().getContentResolver(),
                        currentUser.getPhotoUrl()
                ));
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(
                        view.getContext(),
                        "Image post not found",
                        Toast.LENGTH_SHORT
                ).show();
            }
        }

        TextView postBtn = view.findViewById(R.id.upload_post_text);
        postBtn.setOnClickListener(v -> upload_post(view));

        captureBtn.setOnClickListener(v ->
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
        captureBtn.setEnabled(false);
        if (postImg.getDrawable() == null) {
            Log.d("TAG", "upload_post: No image selected");
            Toast.makeText(view.getContext(), "No image selected", Toast.LENGTH_LONG).show();
            captureBtn.setEnabled(true);
            return;
        }
        if (description.getText() == null)
            description.setText("");

        File tempFile = new File(this.postImg.toString());
        Uri photoUri = Uri.fromFile(tempFile);

        String userUid = this.currentUser.getUid();
        String photoUid = UUID.randomUUID().toString();


        ImagePost new_post = new ImagePost(userUid, userUid, description.getText().toString(), photoUri);
        PostsModel.instance.addPost(new_post, () -> {
            Log.d("TAG", "upload_post: Post was uploaded");
            Toast.makeText(view.getContext(), "Post was uploaded", Toast.LENGTH_LONG).show();
            captureBtn.setEnabled(true);
        });
    }
}