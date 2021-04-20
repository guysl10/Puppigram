package com.example.puppigram.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.puppigram.R;
import com.example.puppigram.activities.MainActivity;
import com.example.puppigram.model.post.ImagePost;
import com.example.puppigram.model.post.PostsModel;
import com.example.puppigram.model.user.User;
import com.example.puppigram.model.user.UsersModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;
import static com.example.puppigram.utils.PhotoUtil.REQUEST_IMAGE_CAPTURE;


/**
 * A simple {@link Fragment} subclass.
 * Use the factory method to
 * create an instance of this fragment.
 */
public class EditPostFragment extends Fragment {
    TextView editBtn;
    EditText description;
    ImageView postImg;
    ImageView userImage;
    TextView username;
    ImageView removeContentImg;
    TextView deletePostBtn;
    FirebaseUser currentUser;
    ProgressBar spinner;
    ImagePost editablePost;

    public EditPostFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_post, container, false);
        spinner = view.findViewById(R.id.editpost_spinner);
        editBtn = view.findViewById(R.id.edit_post_text);
        postImg = (ImageView) view.findViewById(R.id.edit_post_img);
        description = view.findViewById(R.id.edit_description);
        userImage = (ImageView) view.findViewById(R.id.edit_username_img);
        username = view.findViewById(R.id.edit_username_text);
        removeContentImg = view.findViewById(R.id.edit_remove_img);
        deletePostBtn = view.findViewById(R.id.edit_delete_post_text);
        ImageView captureBtn = view.findViewById(R.id.edit_capture_img);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        spinner.setVisibility(View.VISIBLE);
        editBtn.setEnabled(false);
        description.setEnabled(false);
        deletePostBtn.setEnabled(false);
        removeContentImg.setEnabled(false);

        editablePost = getArguments().getParcelable("post");
        UsersModel.instance.getUser(editablePost.getOwnerId(), userModel -> {
            description.setText(editablePost.getDescription());
            username.setText(userModel.getUserName());
            Picasso.get().load(userModel.getUserImage()).placeholder(
                    R.drawable.editpostuserimagereplaceable
            ).into(userImage);
            Picasso.get().load(editablePost.getPostImage()).placeholder(
                    R.drawable.editpostimagereplaceable
            ).into(postImg);
        });

        captureBtn.setOnClickListener(v ->
                ((MainActivity) requireActivity()).getPhotoActivity().
                        checkAndRequestPermissionForCamera(postImg)
        );

        removeContentImg.setOnClickListener(v -> removeContent());
        deletePostBtn.setOnClickListener(v-> deletePost());
        editBtn.setOnClickListener(v -> editPost(view));

        spinner.setVisibility(View.INVISIBLE);
        editBtn.setEnabled(true);
        description.setEnabled(true);
        deletePostBtn.setEnabled(true);
        removeContentImg.setEnabled(true);
        return view;
    }


    private void removeContent() {
        description.setText("");
        postImg.setImageResource(0);
    }

    @SuppressLint("ShowToast")
    private void deletePost(){
        PostsModel.instance.deletePost(editablePost, success -> {
            removeContent();
            Toast.makeText(getContext(),"Post deleted successfully!", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(getView()).navigate(R.id.action_editPostFragment_to_feedFragment);
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            ImageView new_img = this.getView().findViewById(R.id.edit_post_img);
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            new_img.setImageBitmap(imageBitmap);
        }
    }

    @SuppressLint("WrongConstant")
    public void editPost(View view) {
        spinner.setVisibility(View.VISIBLE);
        editBtn.setEnabled(false);
        description.setEnabled(false);
        deletePostBtn.setEnabled(false);
        removeContentImg.setEnabled(false);

        if (postImg.getDrawable() == null) {
            Log.d("TAG", "upload_post: No image selected");
            Toast.makeText(view.getContext(), "No image selected", Toast.LENGTH_LONG).show();
            editBtn.setEnabled(true);
            return;
        }
        if (description.getText() == null)
            description.setText("");

//        File tempFile = new File(this.postImg.toString());
//        Uri photoUri = Uri.fromFile(tempFile);
//        String userUid = this.currentUser.getUid();
//        String photoUid = UUID.randomUUID().toString();

//        ImagePost new_post = new ImagePost(photoUid, userUid, description.getText().toString(), photoUri);
//        PostsModel.instance.updatePost(new_post, () -> {
//            Log.d("TAG", "editPost: Post was uploaded");
//            Toast.makeText(view.getContext(), "Post was updated successfully", Toast.LENGTH_LONG).show();
//            spinner.setVisibility(View.INVISIBLE);
//            uploadBtn.setEnabled(true);
//            description.setEnabled(true);
//            deletePostBtn.setEnabled(true);
//            uploadBtn.setEnabled(true);
//            description.setEnabled(true);
//            deletePostBtn.setEnabled(true);
//            removeContentImg.setEnabled(true);
//        });
    }
}