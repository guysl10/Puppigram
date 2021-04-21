package com.example.puppigram.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.puppigram.R;
import com.example.puppigram.activities.MainActivity;
import com.example.puppigram.utils.Navigator;

//Responsible to handle all simple post issues
public class PostFragment extends Fragment {
    private TextView postDescription, postUsername, postCountLikers;
    private ImageView postImg, postUserImg, postLikerImg;
    private ProgressBar postProgressBar;
    private MainActivity mainActivity;
    private View post;
    private int post_id;

    public PostFragment() {
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mainActivity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.feed_post_row,
                container,
                false
        );

        this.postDescription = view.findViewById(R.id.postDescription);
        this.postUsername = view.findViewById(R.id.postOwner);
        this.postCountLikers = view.findViewById(R.id.postCountLikers);

        this.postImg = view.findViewById(R.id.postImg);
        this.postLikerImg = view.findViewById(R.id.postLiker);
        this.postUserImg = view.findViewById(R.id.post_user_img);
        this.postProgressBar = view.findViewById(R.id.post_spinner);

        //TODO: check if the user own the post.
        ImageView edit_post = view.findViewById(R.id.upload_post_img);

        edit_post.setOnClickListener(Navigation.createNavigateOnClickListener(
                R.id.action_postFragment_to_editPostFragment
        ));
        return view;
    }

    private void updateView() {
//        populatePostView(
//                mainActivity,
//                mainActivity.getApplicationContext(),
//                post,
//                post_liker_img,
//                post_description,
//                post_user_img,
//                post_username,
//                post_img,
//                post_count_likers,
//                post_progressBar
//        );
    }
}