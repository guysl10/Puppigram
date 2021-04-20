package com.example.puppigram.model.post;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.List;

public class PostsModelFirebase {

    ImagePost imagePost = null;
    FirebaseUser firebaseUser;
    FirebaseStorage storage;
    @SuppressLint("StaticFieldLeak")
    static FirebaseFirestore db;
    public FirebaseAuth firebaseAuth;
    public static PostsModelFirebase instance;
    private static final String TAG = "PostsModelFirebase";
    private StorageTask<UploadTask.TaskSnapshot> uploadTask;

    public PostsModelFirebase() {
        storage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new
                FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        db.setFirestoreSettings(settings);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = getAuthInstance().getCurrentUser();
    }

    interface GetAllPostsListener {
        void onComplete(List<ImagePost> imagePosts);
    }

    public FirebaseAuth getAuthInstance() {
        return FirebaseAuth.getInstance();
    }

    public void saveImage(Bitmap imageBitmap) {
        Log.d(TAG, "save image");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
    }

    public void addLike(final String postId, final PostsModel.GetNewLikeListener listener) {
        Log.d(TAG, "add like");
        PostsModelFirebase.getPost(postId, post -> {
            if (post != null) {
                post.getLikes().add(firebaseUser.getUid());
                db.collection("posts").document(postId).set(post).addOnCompleteListener(task -> listener.onComplete(task.isSuccessful()));
            }
        });
    }

    public void deleteLike(final String postId, final PostsModel.DeleteLikeListener listener) {
        Log.d(TAG, "delete like");
        PostsModelFirebase.getPost(postId, post -> {
            if (post != null) {
                post.getLikes().remove(firebaseUser.getUid());
                db.collection("posts").document(postId).set(post).addOnCompleteListener(task -> listener.onComplete(task.isSuccessful()));
            }
        });
    }

    public void isLiked(final String postId, final PostsModel.GetIsLikedListener listener) {
        Log.d(TAG, "Check like");
        PostsModelFirebase.getPost(postId, post -> {
            if (post != null) {
                this.imagePost = post;
                if (this.imagePost.getLikes().contains(firebaseUser.getUid())) {
                    listener.onComplete(true);
                } else {
                    listener.onComplete(false);
                }
            }
            else{
                Log.d(TAG, "IsLiked:post is null");
            }
        });
    }

    public void addPost(final ImagePost post, final PostsModel.AddPostListener listener) {
        Log.d(TAG, "AddPost:create new post");
        db.collection("posts").document(post.getId()).set(post).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "AddPost:success");
            } else {
                Log.d(TAG, "AddPost:failure");
            }
            listener.onComplete();
        }).addOnFailureListener(exception -> listener.onComplete());
    }

    public void deletePost(final String postId, final PostsModel.DeletePostListener listener) {
        Log.d(TAG, "DeletePost:Delete post " + postId);
        db.collection("posts").document(postId).delete().addOnCompleteListener(task -> listener.onComplete(true));
    }

    public static void getPost(final String postId, final PostsModel.GetPostListener listener) {
        Log.d(TAG, "GetPostPost:get post " + postId);
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

    public void getAllPosts(final PostsModel.GetAllPostsListener listener) {
        try {
            Log.d(TAG, "getAllPosts:get all posts from db");
            db.collection("posts").addSnapshotListener((queryDocumentSnapshots, exception) -> {
                assert queryDocumentSnapshots != null;
                MutableLiveData<List<ImagePost>> data = new MutableLiveData<List<ImagePost>>() {
                };
                if (exception != null) {
                    listener.onComplete(data);
                    return;
                }
                List<ImagePost> newPosts = new LinkedList<ImagePost>();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    newPosts.add(doc.toObject(ImagePost.class));
                    Log.d(TAG, "getAllPosts: ");
                }
                data.setValue(newPosts);
                listener.onComplete(data);
            });
        } catch (NullPointerException nullPointerException) {
            listener.onComplete(null);
        }
    }

    public void editPost(final String postId, final String description, final PostsModel.EditPostListener listener) {
        getPost(postId, post -> {
            if (post != null) {
                post.setDescription(description);
                db.collection("posts").document(postId).set(post).addOnCompleteListener(task -> listener.onComplete(task.isSuccessful()));
            }
        });
    }

    public void uploadImage(Bitmap imageBmp, String name, final PostsModel.UploadImageListener listener) {
        final StorageReference imagesRef =
                storage.getReference().child("postImages").child(name);
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


    public void updatePost(ImagePost imagePost, final PostsModel.AddPostListener listener) {
        addPost(imagePost, listener);
    }
}