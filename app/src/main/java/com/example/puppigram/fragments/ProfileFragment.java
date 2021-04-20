package com.example.puppigram.fragments;


import android.annotation.SuppressLint;
import android.graphics.Color;
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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.puppigram.R;
import com.example.puppigram.model.post.ImagePost;
import com.example.puppigram.model.post.PostsModel;
import com.example.puppigram.model.user.User;
import com.example.puppigram.model.user.UsersModel;
import com.example.puppigram.viewmodel.PostsViewModel;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class ProfileFragment extends Fragment {
    private User user;
    private TextView username, bio;
    private ImageView imageProfile;
    private ProgressBar progressBarProfile;
    private TextView noPosts;
    ProfilePostRecyclerAdapter adapter;
    RecyclerView posts;
    PostsViewModel postsViewModel;
    ImageView editProfile;
    ProgressBar spinner;
    List<ImagePost> allPosts;
    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        spinner = view.findViewById(R.id.profile_spinner2);
        adapter = new ProfilePostRecyclerAdapter();
        noPosts = view.findViewById(R.id.profile_no_posts_text);
        editProfile = view.findViewById(R.id.profile_edit_img);
        progressBarProfile = view.findViewById(R.id.profile_spinner);
        imageProfile = view.findViewById(R.id.profile_img);
        username = view.findViewById(R.id.profile_username_text);
        posts = view.findViewById(R.id.profile_posts_recycler_view);
        bio = view.findViewById(R.id.profile_bio_text);
        spinner.setVisibility(View.VISIBLE);
        editProfile.setEnabled(false);
        noPosts.setEnabled(false);


        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        posts.setLayoutManager(layoutManager);
        posts.setHasFixedSize(true);
        posts.setAdapter(adapter);

        postsViewModel = new ViewModelProvider(this).get(PostsViewModel.class);
        postsViewModel.getAllUserPosts(UsersModel.instance.getAuthInstance().getCurrentUser().getUid()).observe(
                getViewLifecycleOwner(),
                imagePosts -> adapter.notifyDataSetChanged()
        );
        Log.d("UserRepo", "ProfileFragment:Start");
        UsersModel.instance.getUser(
                UsersModel.instance.getAuthInstance().getCurrentUser().getUid(),
                userModel -> {
                    user = userModel;
                    username.setText(user.getUserName());
                    bio.setText(user.getBio());
                    if (user.getUserImage() != null) {
                        Picasso.get().load(user.getUserImage()).placeholder(R.drawable.postimagereplaceable).into(imageProfile);
                    }
                    reloadData();
                }
        );

        editProfile.setEnabled(true);
        noPosts.setEnabled(true);
        spinner.setVisibility(View.INVISIBLE);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        reloadData();
    }

    @SuppressLint("WrongConstant")
    public void reloadData() {
        progressBarProfile.setVisibility(View.VISIBLE);
        noPosts.setVisibility(View.INVISIBLE);
        PostsModel.instance.refreshAllPosts(posts -> {
            //TODO: change to show only user posts!
            //TODO:view all post
            // this.viewAllPosts.setOnClickListener();
            postsViewModel.getAllUserPosts(
                    UsersModel.instance.getAuthInstance().
                            getCurrentUser().getUid()).observe(
                    getViewLifecycleOwner(),
                    allPosts -> {
                        if (allPosts == null || allPosts.isEmpty())
                            noPosts.setVisibility(View.VISIBLE);
                        else
                            this.allPosts = allPosts;
                        progressBarProfile.setVisibility(View.INVISIBLE);
                    }
            );

        });
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        /***
         * Responsible of setting all post info for post items in the recycler list.
         */
        TextView description;
        TextView likers;
        ImageView postImg;
        ImageView editBtn;
        TextView username;
        ImageView userImg;
        ImageView likeBtn;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.postDescription);
            likers = itemView.findViewById(R.id.postCountLikers);
            postImg = itemView.findViewById(R.id.postImg);
            editBtn = itemView.findViewById(R.id.post_edit_img);
            userImg = itemView.findViewById(R.id.post_user_img);
            username = itemView.findViewById(R.id.postOwner);
            likeBtn = itemView.findViewById(R.id.postLiker);
            userImg.setVisibility(View.INVISIBLE);
            username.setVisibility(View.INVISIBLE);

            // After finish configure, disable the spinner
            ProgressBar spinner = itemView.findViewById(R.id.post_spinner);
            spinner.setVisibility(View.INVISIBLE);
        }
    }
    /**
     * Responsible of recycling posts.
     */
    class ProfilePostRecyclerAdapter extends RecyclerView.Adapter<ProfileFragment.PostViewHolder> {
        @NonNull
        @Override
        public ProfileFragment.PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.feed_post_row, null);
            return new ProfileFragment.PostViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ProfileFragment.PostViewHolder holder, int position) {
            //Set the holder info for the post in the recycled post.
            ImagePost post = allPosts.get(position);
            final AtomicReference<User>[] tempUser = new AtomicReference[]{null};
            UsersModel.instance.getUser(post.getOwnerId(), userModel -> {
                tempUser[0] = new AtomicReference<>(userModel);
                holder.description.setText(post.getDescription());
                if (post.getPostImage() != null) {
                    Picasso.get().load(post.getPostImage()).placeholder(R.drawable.postimagereplaceable).into(holder.postImg);
                //TODO: apply likes
//                    holder.likers.setText(post.getLikes().size());
                holder.likers.setText("0");
                if (post.getPostImage() != null){
                    Picasso.get().load(post.getPostImage()).placeholder(
                            R.drawable.postimagereplaceable
                    ).into(holder.postImg);
                }
                PostsModel.instance.isLiked(post.getId(), isLiked -> {
                    if (isLiked) {
                        holder.likeBtn.setColorFilter(Color.GREEN);
                    }
                });
            });

            holder.editBtn.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putParcelable("post", post);
                Navigation.findNavController(holder.itemView).navigate(
                        R.id.action_profileFragment_to_editPostFragment, bundle
                );

            });

            holder.likeBtn.setOnClickListener(v -> PostsModel.instance.isLiked(post.getId(), (PostsModel.GetIsLikedListener) isLiked -> {
                if (isLiked) {
                    PostsModel.instance.deleteLike(post.getId(), success2 -> holder.likeBtn.setColorFilter(Color.BLACK));
                } else {
                    PostsModel.instance.addLike(post.getId(), success1 -> holder.likeBtn.setColorFilter(Color.GREEN));
                }
            }));
            holder.likers.setText(String.valueOf(post.getLikes().size()));

            //Check if current user own the post.
            if (UsersModel.instance.getAuthInstance().getCurrentUser().
                    getUid().equals(post.getOwnerId())) {
                holder.editBtn.setVisibility(View.VISIBLE);
                holder.editBtn.setEnabled(true);
            }
        }

        @Override
        public int getItemCount() {
            int size = 0;
            try {
                size = allPosts.size();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d("TAG", "getItemCount: posts size = " + size);
            return size;
        }
    }
}
