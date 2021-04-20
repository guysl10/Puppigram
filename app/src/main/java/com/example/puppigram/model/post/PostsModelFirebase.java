package com.example.puppigram.model.post;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.example.puppigram.R;
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
import java.util.ArrayList;
import java.util.List;

public class PostsModelFirebase {

    Uri imageUri = null;
    ImagePost imagePost = null;
    FirebaseUser firebaseUser;
    static FirebaseFirestore db;
    StorageReference storageRef;
    public FirebaseAuth firebaseAuth;
    public static PostsModelFirebase instance;
    private static final String TAG = "PostsModelFirebase";
    private StorageTask<UploadTask.TaskSnapshot> uploadTask;

    public PostsModelFirebase() {
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new
                FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        db.setFirestoreSettings(settings);
        firebaseAuth = FirebaseAuth.getInstance();
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
        firebaseUser = getAuthInstance().getCurrentUser();
        PostsModelFirebase.getPost(postId, post -> {
            if (post != null) {
                post.getLikes().add(firebaseUser.getUid());
                db.collection("posts").document(postId).set(post).addOnCompleteListener(task -> listener.onComplete(task.isSuccessful()));
            }
        });
    }

    public void deleteLike(final String postId, final PostsModel.DeleteLikeListener listener) {
        Log.d(TAG, "delete like");
        firebaseUser = getAuthInstance().getCurrentUser();
        PostsModelFirebase.getPost(postId, post -> {
            if (post != null) {
                post.getLikes().remove(firebaseUser.getUid());
                db.collection("posts").document(postId).set(post).addOnCompleteListener(task -> listener.onComplete(task.isSuccessful()));
            }
        });
    }

    public void isLiked(final String postId, final ImageView imageView, final PostsModel.GetIsLikedListener listener) {
        firebaseUser = getAuthInstance().getCurrentUser();
        PostsModelFirebase.getPost(postId, imagePost -> {
            if (imagePost != null) {
                this.imagePost = imagePost;
                if (this.imagePost.getLikes().contains(firebaseUser.getUid())) {
                    imageView.setImageResource(R.drawable.is_liked);
                    imageView.setTag("liked");
                } else {
                    imageView.setImageResource(R.drawable.is_liked);
                    imageView.setTag("like");
                }
                listener.onComplete(true);
            }
            listener.onComplete(false);
        });
    }

    public void addPost(final ImagePost post, final PostsModel.AddPostListener listener) {
        Log.d(TAG, "AddPost:create new post");
        db.collection("posts").document(post.getId()).set(post).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "AddPost:success");
                uploadPost(post);
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
        try{Log.d(TAG, "getAllPosts:get all posts from db");
            db.collection("posts").addSnapshotListener((queryDocumentSnapshots, exception) -> {
                assert queryDocumentSnapshots != null;
                ArrayList<ImagePost> data = new ArrayList<>();
                if (exception != null) {
                    listener.onComplete(data);
                    return;
                }
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
//                    Gson gson = new Gson();
//                    JsonElement jsonElement = gson.toJsonTree(doc.getData());
//                    ImagePost post = gson.fromJson(jsonElement, ImagePost.class);
//                    data.add(post);
                    Log.d(TAG, "getAllPosts: ");
                }
                listener.onComplete(data);
            });
        }
        catch (NullPointerException nullPointerException){
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
            }).addOnFailureListener(exception -> {
                Log.d(TAG, "onComplete: task not succeed and not complete");
            });

        } else {
            Log.d(TAG, "uploadImage: The user did not choose to upload a photo ");
        }
    }

    public void updatePost(ImagePost imagePost, final PostsModel.AddPostListener listener) {
        addPost(imagePost, listener);
    }

    public void uploadImage(Bitmap imageBmp, String name, final PostsModel.UploadImageListener listener) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference imagesRef = storage.getReference().child("images").child(name);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        imageBmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] data = stream.toByteArray();
        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(exception -> listener.onComplete(null)).addOnSuccessListener(taskSnapshot -> imagesRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Uri downloadUrl = uri;
            listener.onComplete(downloadUrl.toString());
        }));
    }
}