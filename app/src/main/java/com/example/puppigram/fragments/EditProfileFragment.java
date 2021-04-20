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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.puppigram.R;
import com.example.puppigram.activities.MainActivity;
import com.example.puppigram.model.user.User;
import com.example.puppigram.model.user.UsersModel;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class EditProfileFragment extends Fragment {
    EditText username;
    EditText bio;
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
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        username = view.findViewById(R.id.editprofile_username_input);
        bio = view.findViewById(R.id.editprofile_bio_input);
        profileImg = view.findViewById(R.id.editprofile_profileimg_img);
        password = view.findViewById(R.id.editprofile_password);
        retypePassword = view.findViewById(R.id.editprofile_retype_password);
        discardBtn = view.findViewById(R.id.edit_profile_discard_btn);
        saveChanges = view.findViewById(R.id.edit_profile_save_btn);

        UsersModel.instance.getUser(
                UsersModel.instance.getAuthInstance().getCurrentUser().getUid(),
                userModel -> {
                    user = userModel;
                    restoreDefaultData();
                }
        );

        profileImg.setOnClickListener(v ->
                ((MainActivity)requireActivity()).getPhotoActivity()
                        .checkAndRequestPermissionForCamera(profileImg)
        );

        saveChanges.setOnClickListener(v-> {
            //TODO: update updateProfile to support password and image profile change.
            //            UserRepo.instance.updateProfile();
            if(!password.getText().toString().equals(retypePassword.getText().toString()))
            {
                showMessage("passwords not equal");
                return;
            }
            UsersModel.instance.updateProfile(
                    username.getText().toString(),
                    bio.getText().toString(),
                    password.getText().toString(),
                    y->{
                        showMessage("update profile successfully");
                        restoreDefaultData();
                        Navigation.findNavController(view).navigate(
                                R.id.action_editProfileFragment_to_profileFragment
                        );
                }
            );
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

    private void showMessage(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
    }
}
