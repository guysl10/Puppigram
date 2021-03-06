package com.example.puppigram.fragments;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.puppigram.R;
import com.example.puppigram.activities.MainActivity;
import com.example.puppigram.model.post.ImagePost;
import com.example.puppigram.model.post.PostsModel;
import com.example.puppigram.model.user.User;
import com.example.puppigram.model.user.UsersModel;
import com.squareup.picasso.Picasso;

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
    ImageView userImage;
    ImageView removeContentImg;
    User currentUser;
    ProgressBar spinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upload_post, container, false);
        spinner = view.findViewById(R.id.uploadpost_spinner);
        spinner.setVisibility(View.VISIBLE);
        captureBtn = view.findViewById(R.id.upload_capture_img);
        postImg = (ImageView) view.findViewById(R.id.upload_post_img);
        description = view.findViewById(R.id.upload_post_description);
        username = view.findViewById(R.id.upload_username_text);
        userImage = (ImageView) view.findViewById(R.id.upload_username_img);
        removeContentImg = (ImageView) view.findViewById(R.id.upload_remove_img);

        UsersModel.instance.getUser(
                UsersModel.instance.getAuthInstance().getCurrentUser().getUid(),
                userModel -> {
                    currentUser = userModel;
                    username.setText(currentUser.getUserName());
                    Picasso.get().load(userModel.getUserImage()).placeholder(R.drawable.postimagereplaceable).into(userImage);
                });

        TextView postBtn = view.findViewById(R.id.upload_post_text);
        postBtn.setOnClickListener(v -> upload_post(view));

        captureBtn.setOnClickListener(v ->
                        ((MainActivity) requireActivity()).getPhotoActivity().
                        checkAndRequestPermissionForCamera(postImg)
        );
        removeContentImg.setOnClickListener(v -> removeContent());
        spinner.setVisibility(View.INVISIBLE);
        return view;
    }

    private void removeContent() {
        description.setText("");
        postImg.setImageResource(0);
    }

    @SuppressLint("WrongConstant")
    public void upload_post(View view) {
        spinner.setVisibility(View.VISIBLE);
        captureBtn.setEnabled(false);
        removeContentImg.setEnabled(false);
        description.setEnabled(false);

        if (postImg.getDrawable() == null) {
            Log.d("TAG", "upload_post: No image selected");
            Toast.makeText(view.getContext(), "No image selected", Toast.LENGTH_LONG).show();
            spinner.setVisibility(View.INVISIBLE);
            captureBtn.setEnabled(true);
            return;
        }
        if (description.getText() == null)
            description.setText("");

        String userUid = this.currentUser.getId();
        String photoUid = UUID.randomUUID().toString();

        Bitmap postBitmap = ((BitmapDrawable)this.postImg.getDrawable()).getBitmap();

        PostsModel.instance.uploadImage(postBitmap, photoUid, url -> {
            if (url == null){
                displayFailedError();
            }else{
                ImagePost new_post = new ImagePost(
                        photoUid,
                        userUid,
                        description.getText().toString(),
                        url,
                        System.currentTimeMillis()
                        );
                PostsModel.instance.addPost(new_post, () -> {
                    Log.d("TAG", "upload_post: Post was uploaded");
                    Toast.makeText(view.getContext(), "Post was uploaded", Toast.LENGTH_LONG).show();
                    removeContent();
                    captureBtn.setEnabled(true);
                    removeContentImg.setEnabled(true);
                    description.setEnabled(true);
                    spinner.setVisibility(View.INVISIBLE);
                });
            }
        });
    }

    private void displayFailedError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Operation Failed");
        builder.setMessage("Saving image failed, please try again later...");
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }
}