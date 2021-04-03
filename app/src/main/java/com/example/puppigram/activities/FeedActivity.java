package com.example.puppigram.activities;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.puppigram.R;
import com.example.puppigram.model.ImagePost;

import java.util.LinkedList;
import java.util.List;

//Responsible to handle all feed issues.
public class FeedActivity extends AppCompatActivity {
    RecyclerView posts;
    List<ImagePost> imagePosts;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_feed);

        posts = findViewById(R.id.feed_posts_recycler_list);
        posts.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        posts.setLayoutManager(layoutManager);

        imagePosts = new LinkedList<ImagePost>();

        //TODO: to remove after connecting to db and apply getting all posts.
        for(int i=0;i<100;i++) {
            ImagePost post = new ImagePost("12","13","bla", "bla2");
            imagePosts.add(post);
        }

        PostRecyclerAdapter adapter = new PostRecyclerAdapter();
        posts.setAdapter(adapter);

        // After finish configure, disable the spinner
        ProgressBar spinner = findViewById(R.id.feed_spinner);
        spinner.setVisibility(View.INVISIBLE);
    }

    static class PostViewHolder extends RecyclerView.ViewHolder{
        /***
         * Responsible of setting all post info for post items in the recycler list.
         */
        TextView username;
        TextView description;
        TextView likers;
        ImageView post_img;
        ImageView user_img;
        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.post_description);
            likers = itemView.findViewById(R.id.post_count_likers);
            username = itemView.findViewById(R.id.post_username);
            user_img = itemView.findViewById(R.id.post_user_img);
            post_img = itemView.findViewById(R.id.post_img);

            // After finish configure, disable the spinner
            ProgressBar spinner = itemView.findViewById(R.id.post_spinner);
            spinner.setVisibility(View.INVISIBLE);
        }
    }

    class PostRecyclerAdapter extends RecyclerView.Adapter<PostViewHolder>{
        @NonNull
        @Override
        public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            /***
             * Responsible of recycling posts.
             */
            View view = getLayoutInflater().inflate(R.layout.feed_post_row, null);
            PostViewHolder holder = new PostViewHolder(view);

            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
            /**
             * Set the holder info for the post in the recycled post.
             */
            ImagePost post = imagePosts.get(position);
            holder.description.setText(post.getDescription());
            //TODO: add all posts items to set for the recyclerview feed.

        }

        @Override
        public int getItemCount() {
            return imagePosts.size();
        }
    }
}