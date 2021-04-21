package com.example.puppigram.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.puppigram.R;
import com.example.puppigram.activities.MainActivity;
import com.example.puppigram.model.user.User;
import com.example.puppigram.model.user.UsersModel;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class EditProfileFragment extends Fragment {
    EditText username;
    EditText bio;
    Button discardBtn;
    Button saveChanges;
    ImageView imageProfile;
    User user;
    ProgressBar spinner;

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
        spinner = view.findViewById(R.id.editprofile_spinner);
        username = view.findViewById(R.id.editprofile_username_input);
        bio = view.findViewById(R.id.editprofile_bio_input);
        imageProfile = view.findViewById(R.id.editprofile_profileimg_img);
        discardBtn = view.findViewById(R.id.edit_profile_discard_btn);
        saveChanges = view.findViewById(R.id.edit_profile_save_btn);
        spinner.setVisibility(View.VISIBLE);
        UsersModel.instance.getUser(
                UsersModel.instance.getAuthInstance().getCurrentUser().getUid(),
                userModel -> {
                    user = userModel;
                    restoreDefaultData();
                }
        );

        imageProfile.setOnClickListener(v ->
                ((MainActivity) requireActivity()).getPhotoActivity()
                        .checkAndRequestPermissionForCamera(imageProfile)
        );

        saveChanges.setOnClickListener(v -> {
            User user = new User(UsersModel.instance.getAuthInstance().getCurrentUser().getUid(), username.getText().toString(), UsersModel.instance.getAuthInstance().getCurrentUser().getEmail(), bio.getText().toString(), imageProfile.toString());
            UsersModel.instance.updateUser(user, () -> {
                BitmapDrawable drawable = (BitmapDrawable) imageProfile.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                UsersModel.instance.uploadImage(bitmap, user.getId(), url -> {
                    if (url == null) {
                        showMessage("update profile image failed");
                    } else {
                        user.setUserImage(url);
                        UsersModel.instance.addUser(user, () -> {
                            showMessage("update profile successfully");
                            restoreDefaultData();
                            Navigation.findNavController(view).navigate(
                                    R.id.action_editProfileFragment_to_profileFragment
                            );
                        });
                    }
                });
            });
        });

        discardBtn.setOnClickListener(v -> {
            restoreDefaultData();
        });
        spinner.setVisibility(View.INVISIBLE);
        return view;
    }

    void restoreDefaultData() {
        username.setText(user.getUserName());
        bio.setText(user.getBio());
        if (user.getUserImage() != null) {
            Picasso.get().load(user.getUserImage()).placeholder(R.drawable.postimagereplaceable).into(imageProfile);
        }
        try {
            InputStream imageStream = getActivity().getApplicationContext().
                    getContentResolver().openInputStream(Uri.parse(user.getUserImage()));
            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            imageProfile.setImageBitmap(selectedImage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void showMessage(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
    }
}
