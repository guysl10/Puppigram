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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.puppigram.R;
import com.example.puppigram.model.post.PostsModel;
import com.example.puppigram.model.post.ImagePost;
import com.example.puppigram.model.user.User;
import com.example.puppigram.model.user.UsersModel;
import com.example.puppigram.utils.PhotoUtil;
import com.example.puppigram.viewmodel.PostsViewModel;

import java.util.List;
import java.util.Objects;

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
        imageProfile = view.findViewById(R.id.profile_image);
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
        postsViewModel.getImagePosts().observe(
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
                    PhotoUtil.UriToImageView(
                            Uri.parse(user.getUserImage()),
                            imageProfile,
                            "Image was not updated",
                            getActivity().getApplicationContext()
                    );
                    reloadData();
                }
        );

        editProfile.setOnClickListener(v->
            Navigation.findNavController(view).navigate(
                            R.id.action_profileFragment_to_editProfileFragment
            )
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
            List<ImagePost> allPosts = postsViewModel.getImagePosts().getValue();
            if (allPosts == null || allPosts.isEmpty())
                noPosts.setVisibility(View.VISIBLE);
            else
                adapter.notifyDataSetChanged();
            progressBarProfile.setVisibility(View.INVISIBLE);
        });
    }

    static class ProfilePostViewHolder extends RecyclerView.ViewHolder {
        /***
         * Responsible of setting all post info for post items in the recycler list.
         */
        TextView username;
        TextView description;
        TextView likers;
        ImageView post_img;
        ImageView user_img;

        public ProfilePostViewHolder(@NonNull View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.postDescription);
            likers = itemView.findViewById(R.id.postCountLikers);
            username = itemView.findViewById(R.id.postOwner);
            user_img = itemView.findViewById(R.id.post_user_img);
            post_img = itemView.findViewById(R.id.postImg);

            // After finish configure, disable the spinner
            ProgressBar spinner = itemView.findViewById(R.id.post_spinner);
            spinner.setVisibility(View.INVISIBLE);
        }
    }
    /**
     * Responsible of recycling posts.
     */
    class ProfilePostRecyclerAdapter extends RecyclerView.Adapter<ProfileFragment.ProfilePostViewHolder> {
        @NonNull
        @Override
        public ProfileFragment.ProfilePostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.feed_post_row, null);
            return new ProfileFragment.ProfilePostViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ProfileFragment.ProfilePostViewHolder holder, int position) {
            //Set the holder info for the post in the recycled post.
            ImagePost post = Objects.requireNonNull(postsViewModel.getImagePosts().getValue()).get(position);
            holder.description.setText(post.getDescription());
            //TODO: add all posts items to set for the recyclerview feed.
        }

        @Override
        public int getItemCount() {
            int size = 0;
            try {
                size = Objects.requireNonNull(postsViewModel.getImagePosts().getValue()).size();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d("TAG", "getItemCount: posts size = " + size);
            return size;
        }
    }
}
