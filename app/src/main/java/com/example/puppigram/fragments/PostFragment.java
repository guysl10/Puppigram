package com.example.puppigram.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.puppigram.R;
import com.example.puppigram.activities.MainActivity;

//Responsible to handle all simple post issues
public class PostFragment extends Fragment {
    private TextView post_description, post_username, post_count_likers;
    private ImageView post_img, post_user_img, post_liker_img;
    private ProgressBar post_progressBar;
    private MainActivity mainActivity;
    private View post;
    private int post_id;

    public PostFragment(){};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mainActivity = (MainActivity) getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(
                R.layout.feed_post_row,
                container,
                false
        );

        this.post_description = view.findViewById(R.id.post_description);
        this.post_username = view.findViewById(R.id.post_username);
        this.post_count_likers = view.findViewById(R.id.post_count_likers);

        this.post_img = view.findViewById(R.id.post_img);
        this.post_liker_img = view.findViewById(R.id.post_liker_img);
        this.post_user_img = view.findViewById(R.id.post_user_img);
        this.post_progressBar = view.findViewById(R.id.post_spinner);


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