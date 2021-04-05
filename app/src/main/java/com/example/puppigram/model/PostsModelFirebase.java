package com.example.puppigram.model;

import android.net.Uri;
import android.util.Log;

import com.example.puppigram.repos.Repo;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class PostsModelFirebase {

    static FirebaseFirestore db;
    Uri imageUri = null;
    StorageReference storageRef;
    private static final String TAG = "FirebaseModel";
    private StorageTask<UploadTask.TaskSnapshot> uploadTask;

    public void addPost(final ImagePost post, final Repo.AddPostListener listener) {
        Log.d(TAG, "create new post");
        db.collection("posts").document(post.getId()).set(post).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                uploadPost(post);
            }
            listener.onComplete(task.isSuccessful());
        });
    }

    public void deletePost(final String postId, final Repo.DeletePostListener listener) {
        Log.d(TAG, "Delete post " + postId);
        db.collection("posts").document(postId).delete().addOnCompleteListener(task -> listener.onComplete(task.isSuccessful()));
    }

    public static void getPost(final String postId, final Repo.GetPostListener listener) {
        db.collection("posts").document(postId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot snapshot = task.getResult();
                        ImagePost postModel = snapshot.toObject(ImagePost.class);
                        listener.onComplete(postModel);
                        return;
                    }
                    listener.onComplete(null);
                });
    }

    public void getAllPost(final Repo.GetAllPostsListener listener) {
        db.collection("posts").addSnapshotListener((queryDocumentSnapshots, e) -> {
            ArrayList<ImagePost> data = new ArrayList<>();
            if (e != null) {
                listener.onComplete(data);
                return;
            }
            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                ImagePost post = doc.toObject(ImagePost.class);
                data.add(post);
            }
            listener.onComplete(data);
        });
    }

    public void editPost(final String postId, final String description, final Repo.EditPostListener listener) {
        getPost(postId, post -> {
            if (post != null) {
                post.setDescription(description);
                db.collection("posts").document(postId).set(post).addOnCompleteListener(task -> listener.onComplete(task.isSuccessful()));
            }
        });
    }

    public void uploadPost(ImagePost post) {
        Log.d(TAG, "Upload new post");
        storageRef = FirebaseStorage.getInstance().getReference("posts");
        if (post.getPostImage() != null) {
            final StorageReference fileReference = storageRef.child(System.currentTimeMillis()
                    + "." + post.getPostImage().getLastPathSegment());

            uploadTask = fileReference.putFile(post.getPostImage());
            uploadTask.continueWithTask(task -> {
                Log.d(TAG, "then: task of upload the file(image) to the storage");
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return fileReference.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                Log.d(TAG, "onComplete: task complete");
                if (task.isSuccessful()) {
                    imageUri = task.getResult();
                    post.setPostImage(imageUri);
                    db.collection("posts").document(post.getId()).set(post).addOnCompleteListener(task1 -> Log.d(TAG, "onComplete: final !!"));
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
