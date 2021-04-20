package com.example.puppigram.model.user;

import android.net.Uri;
import android.util.Log;

import com.example.puppigram.repos.Repo;
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

import java.util.ArrayList;

public class UsersModeFirebase {

    User user = null;
    FirebaseUser firebaseUser;
    static StorageReference storageRef;
    static FirebaseFirestore db = FirebaseFirestore.getInstance();
    public FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private static final String TAG = "UsersModelFirebase";
    private static StorageTask<UploadTask.TaskSnapshot> uploadTask;

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
                        UsersModeFirebase.uploadImage(user);
                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    }
                    listener.onComplete(task.isSuccessful());
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

    public void updateProfile(final String userName, final String bio, final String pass, final Repo.EditProfileListener listener) {
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

    public static void uploadImage(User user) {
        Log.d(TAG, "uploadImage: Upload a profile picture");
        storageRef = FirebaseStorage.getInstance().getReference("userImage");
        if (user.getUserImage() != null) {
            final StorageReference fileReference = storageRef.child(System.currentTimeMillis()
                    + "." + Uri.parse(user.getUserImage()).getLastPathSegment());
            uploadTask = fileReference.putFile(Uri.parse(user.getUserImage()));

            uploadTask.continueWithTask(task -> {
                Log.d(TAG, "then: task of upload the file(image) to the storage");
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return fileReference.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                Log.d(TAG, "onComplete: task complete");
                if (task.isSuccessful()) {
                    Log.d(TAG, "onComplete: task succeed");
                    Uri downloadUri = task.getResult();
                    user.setUserImage(downloadUri.toString());
                    db.collection("users").document(user.getId()).set(user);
                } else {
                    Log.d(TAG, "onComplete: task not succeed");
                }
            }).addOnFailureListener(e -> {
                Log.d(TAG, "onComplete: task not succeed and not complete");
            });

        } else {
            Log.d(TAG, "uploadImage: The user did not choose to upload a photo ");
        }
    }
}
