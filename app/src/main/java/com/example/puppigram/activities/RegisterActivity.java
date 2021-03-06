package com.example.puppigram.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.puppigram.R;
import com.example.puppigram.model.user.User;
import com.example.puppigram.model.user.UsersModel;
import com.example.puppigram.utils.Navigator;
import com.example.puppigram.utils.PhotoUtil;

import java.util.regex.Pattern;

import static com.example.puppigram.utils.PhotoUtil.REQUEST_IMAGE_CAPTURE;
import static com.example.puppigram.utils.PhotoUtil.REQUEST_IMAGE_GALLERY;

public class RegisterActivity extends AppCompatActivity {

    private EditText userEmail, userName, userPassword, userRePassword, userBio;
    private ProgressBar loadingProgress;
    private Button registerButton;
    private ImageView userPhoto;
    private PhotoUtil photoActivity;
    private Navigator navigator;

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        navigator = new Navigator(this);
        userPhoto = findViewById(R.id.register_puppi_img);
        userName = findViewById(R.id.register_username_input);
        userEmail = findViewById(R.id.register_email_input);
        userBio = findViewById(R.id.register_bio_input);
        userPassword = findViewById(R.id.register_pass_input);
        userRePassword = findViewById(R.id.register_retypepass_input);
        TextView cancel = findViewById(R.id.register_cancel_text);
        registerButton = findViewById(R.id.register_btn);
        loadingProgress = findViewById(R.id.register_spinner);
        TextView empty = findViewById(R.id.register_empty_text);
        loadingProgress.setVisibility(View.INVISIBLE);
        photoActivity = new PhotoUtil(this);
        userPhoto.setOnClickListener(v ->
                getPhotoActivity().checkAndRequestPermissionForCamera(userPhoto)
        );


        registerButton.setOnClickListener(v -> createProfile());

        empty.setOnClickListener(v -> emptyFields());

        cancel.setOnClickListener(v -> navigator.navigate(LoginActivity.class));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void emptyFields() {
        this.userPhoto.setImageDrawable(
                getResources().getDrawable(
                        getResources().getIdentifier(
                                "circle_cropped",
                                "drawable",
                                getPackageName()
                        )
                )
        );
        this.userBio.setText("");
        this.userEmail.setText("");
        this.userName.setText("");
        this.userPassword.setText("");
        this.userRePassword.setText("");
    }

    public PhotoUtil getPhotoActivity() {
        return photoActivity;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQUEST_IMAGE_CAPTURE || requestCode == REQUEST_IMAGE_GALLERY) && resultCode == RESULT_OK) {
            photoActivity.onResult(requestCode, resultCode, data);
        }
    }
    private void createProfile() {
        loadingProgress.setVisibility(View.VISIBLE);
        //registerButton.setEnabled(false);
        userName.setEnabled(false);
        userEmail.setEnabled(false);
        userBio.setEnabled(false);
        userPassword.setEnabled(false);
        userRePassword.setEnabled(false);
        userPhoto.setEnabled(false);

        final String username = userName.getText().toString();
        final String email = userEmail.getText().toString();
        final String bio = userBio.getText().toString();
        final String pass = userPassword.getText().toString();
        final String repass = userRePassword.getText().toString();
        final String userImage =userPhoto.toString();

        if (!VALID_EMAIL_ADDRESS_REGEX.matcher(email).find() || pass.length() < 6 || !pass.equals(repass) || email.isEmpty() || pass.isEmpty() || username.isEmpty() || bio.isEmpty()) {
            showMessage("Please Verify all fields");
            userName.setEnabled(true);
            userEmail.setEnabled(true);
            userBio.setEnabled(true);
            userPassword.setEnabled(true);
            userRePassword.setEnabled(true);
            userPhoto.setEnabled(true);
            registerButton.setVisibility(View.VISIBLE);

        } else {
            User user = new User(username, email, bio, userImage);
            UsersModel.instance.register(user, pass, success -> {
                showMessage("Register complete");
                BitmapDrawable drawable = (BitmapDrawable) userPhoto.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                UsersModel.instance.uploadImage(bitmap, user.getId(), url -> {
                    if (url == null) {
                        displayFailedError();
                    } else {
                        user.setUserImage(url);
                        UsersModel.instance.addUser(user, () -> UsersModel.instance.login(user.getEmail(), userPassword.toString(), v -> {
                            if (success) {
                                navigator.navigate(MainActivity.class);
                            } else
                                showMessage("SignIn failed");
                        }));
                        ;
                    }
                });
            });
        }
        loadingProgress.setVisibility(View.INVISIBLE);
    }

    private void showMessage(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }

    private void displayFailedError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Operation Failed");
        builder.setMessage("Saving image failed, please try again later...");
        builder.setNeutralButton("OK", (dialogInterface, i) -> dialogInterface.dismiss());
        builder.show();
    }
}