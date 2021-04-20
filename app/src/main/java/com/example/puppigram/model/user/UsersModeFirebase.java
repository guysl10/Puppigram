package com.example.puppigram.model.user;

import android.graphics.Bitmap;
import android.util.Log;

import com.example.puppigram.model.post.PostsModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.concurrent.Callable;

public class UsersModeFirebase {

    User user = null;
    FirebaseUser firebaseUser;
    static StorageReference storageRef;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    static FirebaseFirestore db = FirebaseFirestore.getInstance();
    public FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private static final String TAG = "UsersModelFirebase";
    private static StorageTask<UploadTask.TaskSnapshot> uploadTask;

    public interface LoginUserListener {
        void onComplete(boolean success);
    }

    public void register(final User user, String password, final UsersModel.AddUserListener listener) {
        Log.d(TAG, "register:create new user");
        firebaseAuth.createUserWithEmailAndPassword(user.getEmail(), password)
                .addOnCompleteListener((OnCompleteListener<AuthResult>) task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "createUserWithEmail:success");
                        firebaseUser = firebaseAuth.getCurrentUser();
                        String userID = firebaseUser.getUid();
                        user.setId(userID);
                        db.collection("users").document(user.getId()).set(user);
                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    }
                    listener.onComplete(task.isSuccessful());
                });
    }

    public void login(String email, String password, UsersModeFirebase.LoginUserListener loginUserListener) {
        Log.d(TAG, "login current user");
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((OnCompleteListener<AuthResult>) task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithEmail:success");
                        firebaseUser = firebaseAuth.getCurrentUser();
                        Log.d(TAG, "onComplete: " + firebaseUser);
                        loginUserListener.onComplete(true);
                    } else {
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        loginUserListener.onComplete(false);
                    }
                });
    }

    public void logOut(Callable<Void> onComplete) {
        Log.d(TAG, "logout current user");
        firebaseAuth.signOut();
        firebaseAuth.addAuthStateListener(firebaseAuth -> {
            if (firebaseAuth.getCurrentUser() == null) {
                try {
                    onComplete.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void getUser(String id, UsersModel.GetUserListener listener) {
        Log.d(TAG, "getUser:get user from db");
        db.collection("users").document(id).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot snapshot = task.getResult();
                        User user = snapshot.toObject(User.class);
                        Log.d(TAG, "getUser:got user - " + user.getUserName());
                        listener.onComplete(user);
                    } else {
                        Log.d(TAG, "getUser:failed");
                    }
                });
    }

    public void addStudent(User user, final UsersModel.AddStudentListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(user.getId())
                .set(user).addOnSuccessListener(aVoid -> {
                    Log.d("TAG", "user added successfully");
                    listener.onComplete();
                }).addOnFailureListener(e -> {
                    Log.d("TAG", "fail adding user");
                    listener.onComplete();
                });
    }

    public void getAllUsers(final UsersModel.GetAllUsersListener listener) {
        Log.d(TAG, "getAllUsers:get all users from db");
        db.collection("users").addSnapshotListener((queryDocumentSnapshots, e) -> {
            ArrayList<User> data = new ArrayList<>();
            if (e != null) {
                listener.onComplete(data);
                return;
            }
            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                User user = new User(
                        doc.getString("id"),
                        doc.getString("userName"),
                        doc.getString("email"),
                        doc.getString("userImage"),
                        doc.getString("bio"));
                data.add(user);
            }
            listener.onComplete(data);
        });

    }

    public void updateProfile(final String userName, final String bio, final String pass, final PostsModel.EditProfileListener listener) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        getUser(firebaseUser.getUid(), userModel -> {
            if (userModel != null) {
                user = userModel;
                user.setUserName(userName);
                user.setBio(bio);
                firebaseAuth.getCurrentUser().updatePassword(pass);
                db.collection("users").document(firebaseUser.getUid()).set(user).addOnCompleteListener(task -> listener.onComplete(task.isSuccessful()));
            }
        });
    }

    public void uploadImage(Bitmap imageBmp, String name, UsersModel.UploadImageListener listener) {
        final StorageReference imagesRef =
                storage.getReference().child("userImages").child(name);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(exception ->
                listener.onComplete(null)).addOnSuccessListener(
                taskSnapshot ->
                        imagesRef.getDownloadUrl().addOnSuccessListener(
                                uri -> {
                                    listener.onComplete(uri.toString());
                                }
                        )
        );
    }

}
