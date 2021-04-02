
package com.example.puppigram.fragments;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.puppigram.R;
import com.example.puppigram.model.MyApp;
import com.example.puppigram.model.PostsModelSQL;
import com.example.puppigram.model.ImagePost;
import com.example.puppigram.viewmodel.PostsViewModel;

import java.util.LinkedList;
import java.util.List;

//Responsible to handle all feed issues.
public class FeedFragment extends Fragment {

    private List<ImagePost> imagePosts = new LinkedList<ImagePost>();

    PostsViewModel posts_viewmodel;
    RecyclerView posts;
    ProgressBar spinner;
    TextView no_posts;
    PostRecyclerAdapter adapter;
    int i;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        adapter = new PostRecyclerAdapter();
        spinner = view.findViewById(R.id.feed_spinner);
        no_posts = view.findViewById(R.id.feed_no_posts_text);
        posts = view.findViewById(R.id.feed_posts_recycler_list);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        posts.setLayoutManager(layoutManager);
        posts.setHasFixedSize(true);
        posts.setAdapter(adapter);

        posts_viewmodel = new ViewModelProvider(this).get(PostsViewModel.class);
        i = 0;
        reloadData();

        // After finish configure, disable the spinner
        spinner.setVisibility(View.INVISIBLE);
        view.invalidate();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        reloadData();
    }

    @SuppressLint("WrongConstant")
    public void reloadData(){
        spinner.setVisibility(View.VISIBLE);
        no_posts.setVisibility(View.INVISIBLE);
        PostsModelSQL.instance.getAllPosts(posts -> {
            if (posts_viewmodel.getImagePosts().size() == 0)
                no_posts.setVisibility(View.VISIBLE);
            else
                adapter.notifyDataSetChanged();

            spinner.setVisibility(View.INVISIBLE);
        });
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
            ImagePost post = posts_viewmodel.getImagePosts().get(position);//getValue().get(position);
            holder.description.setText(post.getDescription());
            holder.likers.setText(post.getLikers().toString());
            holder.username.setText(post.getOwner_id().toString());
//            holder.user_img.setImageDrawable(post.getImage());
//            holder.post_img.setImageDrawable(post.getImage());
            //TODO: add all posts items to set for the recyclerview feed.
        }

        @Override
        public int getItemCount() {
            int size = 0;
            try {
                size = posts_viewmodel.getImagePosts().size();//.getValue().size();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d("TAG", "getItemCount: posts size = "+ size);
            return size;

        }

    }
}