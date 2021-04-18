package com.example.puppigram.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.puppigram.R;
import com.example.puppigram.model.ImagePost;
import com.example.puppigram.model.PostsModel;
import com.example.puppigram.repos.PostRepo;
import com.example.puppigram.repos.Repo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ImageViewHolder> {
    private static final String TAG = "PostAdapter";
    private Context context;
    private List<ImagePost> posts;

    public PostAdapter(Context context, List<ImagePost> posts) {
        this.context = context;
        this.posts = posts;
    }

    public void setPosts(List<ImagePost> posts) {
        this.posts = posts;
    }

    @NonNull
    @Override
    public PostAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.feed_post_row, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PostAdapter.ImageViewHolder holder, final int position) {// where we will pass our data to our ViewHolder

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final ImagePost post = posts.get(position);

        Glide.with(context).load(post.getPostImage())
                .apply(new RequestOptions().placeholder(R.drawable.camera))
                .into(holder.postImage);

        if (post.getDescription().equals("")) {//if not have description don't show
            holder.description.setVisibility(View.GONE);
        } else {
            holder.description.setVisibility(View.VISIBLE);
            holder.description.setText(post.getDescription());
        }

        Repo.instance.isLiked(post.getId(), holder.like, success -> Log.d(TAG, "onComplete: like button is change if liked"));
        getNumLikes(holder.likes, post.getId());

        //if click on like
        holder.like.setOnClickListener(view -> {
            if (holder.like.getTag().equals("like")) {
                Repo.instance.addLike(post.getId(), success -> {
                    holder.like.setImageResource(R.drawable.is_liked);
                    holder.like.setTag("liked");
                    getNumLikes(holder.likes, post.getId());
                });
            } else {
                Repo.instance.deleteLike(post.getId(), success -> {
                    holder.like.setImageResource(R.drawable.is_liked);
                    holder.like.setTag("like");
                    getNumLikes(holder.likes, post.getId());
                });
            }
        });

        //if click on image profile go to owner profile
        holder.usernameImage.setOnClickListener(view -> {//if click on image profile go to publisher profile
            SharedPreferences.Editor editor = context.getSharedPreferences("PREFS", MODE_PRIVATE).edit();
            editor.putString("profileId", post.getOwnerId());
            editor.putString("other", "true");
            editor.apply();

            Navigation.findNavController(view)
                    .navigate(R.id.action_postFragment_to_editPostFragment);
        });

        //if click on user name go to owner profile
        holder.username.setOnClickListener(view -> {
            SharedPreferences.Editor editor = context.getSharedPreferences("PREFS", MODE_PRIVATE).edit();
            editor.putString("profileId", post.getOwnerId());
            editor.putString("other", "true");
            editor.apply();

            Navigation.findNavController(view)
                    .navigate(R.id.action_postFragment_to_editPostFragment);
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {

        public ImageView usernameImage, postImage, like;
        public TextView username, likes, owner, description;

        public ImageViewHolder(View itemView) {
            super(itemView);

            usernameImage = itemView.findViewById(R.id.userPhoto);
            username = itemView.findViewById(R.id.userName);
            postImage = itemView.findViewById(R.id.postImg);
            like = itemView.findViewById(R.id.postLiker);
            likes = itemView.findViewById(R.id.postCountLikers);
            owner = itemView.findViewById(R.id.postOwner);
            description = itemView.findViewById(R.id.postDescription);
        }
    }


    //for showing who many do likes
    @SuppressLint("SetTextI18n")
    private void getNumLikes(final TextView likes, String postId) {
        PostRepo.getPost(postId, postModel -> {
            if (postModel != null) {
                likes.setText(postModel.getLikes().size() + " likes");
            }
        });
    }

    //for more if the current user want to edit the post
    private void editPost(final String postId) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Edit Post");

        final EditText editText = new EditText(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        editText.setLayoutParams(lp);
        alertDialog.setView(editText);

        getDescription(postId, editText);
        String changes = editText.getText().toString();
        alertDialog.setPositiveButton("Edit",
                (dialogInterface, i) -> PostRepo.editPost(postId, changes, success -> {
                    if (success) {
                        Toast.makeText(context, "done!", Toast.LENGTH_SHORT).show();
                    }
                }));
        alertDialog.setNegativeButton("Cancel",
                (dialogInterface, i) -> dialogInterface.cancel());
        alertDialog.show();
    }

    //fer edit post get the text that already there
    private void getDescription(String postId, final EditText editText) {
        PostRepo.getPost(postId, (PostsModel.GetPostListener) post -> {
            if (post != null) {
                editText.setText(post.getDescription());
            }
        });
    }
}
