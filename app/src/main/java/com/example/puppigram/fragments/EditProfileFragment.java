package com.example.puppigram.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.puppigram.R;
import com.example.puppigram.activities.MainActivity;
import com.example.puppigram.model.User;
import com.example.puppigram.repos.UserRepo;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class EditProfileFragment extends Fragment {
    EditText username;
    EditText bio;
    EditText email;
    EditText password;
    EditText retypePassword;
    Button discardBtn;
    Button saveChanges;
    ImageView profileImg;
    User user;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_post, container, false);
        username = view.findViewById(R.id.editprofile_username_input);
        bio = view.findViewById(R.id.editprofile_bio_input);
        email = view.findViewById(R.id.editprofile_email_edittext);
        profileImg = view.findViewById(R.id.editprofile_profileimg_img);
        password = view.findViewById(R.id.editprofile_password);
        retypePassword = view.findViewById(R.id.editprofile_retype_password);
        discardBtn = view.findViewById(R.id.edit_profile_discard_btn);
        saveChanges = view.findViewById(R.id.edit_profile_save_btn);

        UserRepo.instance.getUser(
                UserRepo.instance.getAuthInstance().getCurrentUser().getUid(),
                userModel -> user = userModel
        );
        restoreDefaultData();

        profileImg.setOnClickListener(v ->
                ((MainActivity)requireActivity()).getPhotoActivity()
                        .checkAndRequestPermissionForCamera(profileImg)
        );

        saveChanges.setOnClickListener(v-> {
            //TODO: update updateProfile to support password and image profile change.
            //            UserRepo.instance.updateProfile();
        });

        discardBtn.setOnClickListener(v-> {
            restoreDefaultData();
            password.setText("");
            retypePassword.setText("");
        });


        return view;
    }

    void restoreDefaultData(){
        username.setText(user.getUserName());
        email.setText(user.getEmail());
        bio.setText(user.getBio());

        try {
            InputStream imageStream = getActivity().getApplicationContext().
                    getContentResolver().openInputStream(Uri.parse(user.getUserImage()));
            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            profileImg.setImageBitmap(selectedImage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
