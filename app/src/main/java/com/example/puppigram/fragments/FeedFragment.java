
package com.example.puppigram.fragments;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.puppigram.R;
import com.example.puppigram.model.post.ImagePost;
import com.example.puppigram.model.post.PostsModel;
import com.example.puppigram.model.user.User;
import com.example.puppigram.model.user.UsersModel;
import com.example.puppigram.utils.PhotoUtil;
import com.example.puppigram.viewmodel.PostsViewModel;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

//Responsible to handle all feed issues.
public class FeedFragment extends Fragment {
    PostsViewModel postsViewModel;
    RecyclerView posts;
    ProgressBar spinner;
    TextView noPosts;
    PostRecyclerAdapter adapter;

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
        noPosts = view.findViewById(R.id.feed_no_posts_text);
        posts = view.findViewById(R.id.feed_posts_recycler_list);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        posts.setLayoutManager(layoutManager);
        posts.setHasFixedSize(true);
        posts.setAdapter(adapter);

        postsViewModel= new ViewModelProvider(this).get(PostsViewModel.class);
        postsViewModel.getImagePosts().observe(
                getViewLifecycleOwner(),
                imagePosts -> adapter.notifyDataSetChanged()
        );
        reloadData();

        // After finish configure, disable the spinner
        ProgressBar spinner = view.findViewById(R.id.feed_spinner);
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
    public void reloadData() {
        spinner.setVisibility(View.VISIBLE);
        noPosts.setVisibility(View.INVISIBLE);
        PostsModel.instance.refreshAllPosts(posts -> {
            List<ImagePost> allPosts = postsViewModel.getImagePosts().getValue();
            if (allPosts == null || allPosts.isEmpty())
                noPosts.setVisibility(View.VISIBLE);
            else
                adapter.notifyDataSetChanged();
            spinner.setVisibility(View.INVISIBLE);
        });
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        /***
         * Responsible of setting all post info for post items in the recycler list.
         */
        TextView username;
        TextView description;
        TextView likers;
        ImageView postImg;
        ImageView userImg;
        ImageView editBtn;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.postDescription);
            likers = itemView.findViewById(R.id.postCountLikers);
            username = itemView.findViewById(R.id.postOwner);
            userImg = itemView.findViewById(R.id.post_user_img);
            postImg = itemView.findViewById(R.id.postImg);
            editBtn = itemView.findViewById(R.id.post_edit_img);

            // After finish configure, disable the spinner
            ProgressBar spinner = itemView.findViewById(R.id.post_spinner);
            spinner.setVisibility(View.INVISIBLE);
        }
    }
    /**
     * Responsible of recycling posts.
     */
    class PostRecyclerAdapter extends RecyclerView.Adapter<PostViewHolder> {
        @NonNull
        @Override
        public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.feed_post_row, null);
            return new PostViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
            //Set the holder info for the post in the recycled post.
            ImagePost post = Objects.requireNonNull(postsViewModel.getImagePosts().getValue()).get(position);
            AtomicReference<User> temp_user = null;
            UsersModel.instance.getUser(post.getOwnerId(), temp_user::set);

            holder.description.setText(post.getDescription());
            holder.username.setText(temp_user.get().getUserName());
            holder.likers.setText(post.getLikes().size());

            if (post.getPostImage() != null){
                Picasso.get().load(post.getPostImage()).placeholder(R.drawable.postimagereplaceable).into(holder.postImg);
            }

//            PhotoUtil.UriToImageView(
//                    Uri.parse(temp_user.get().getUserImage()),
//                    holder.userImg,
//                    "user image in post " + post.getId()+ "not found",
//                    getActivity().getApplicationContext()
//            );


            //Check if current user own the post.
            if(UsersModel.instance.getAuthInstance().getCurrentUser().
                    getUid().equals(post.getOwnerId())){
                holder.editBtn.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public int getItemCount() {
            int size = 0;
            try {
                size = Objects.requireNonNull(
                        postsViewModel.getImagePosts().getValue()).size();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d("TAG", "getItemCount: posts size = " + size);
            return size;
        }
    }
}