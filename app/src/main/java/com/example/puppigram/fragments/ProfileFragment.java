package com.example.puppigram.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.puppigram.R;
import com.example.puppigram.model.Profile;
import com.example.puppigram.repos.ProfileRepo;

public class ProfileFragment extends Fragment {

    private String profileId;
    private Profile profile;
    private TextView fullName, email;
    private ImageView imageProfile;
    private ProgressBar progressBarProfile;
    private ProfileRepo profileRepo;
    private Button viewAllPosts;

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
        View view = inflater.inflate(R.layout.profile, container, false);
        assert getArguments() != null;
//        this.profileId = ProfileFragmentArgs.fromBundle(getArguments()).getProfileId();

        this.viewAllPosts = view.findViewById(R.id.go_to_all_user_posts);
        this.progressBarProfile = view.findViewById(R.id.profile_progressBar);
        this.imageProfile = view.findViewById(R.id.profile_image);
        this.fullName = view.findViewById(R.id.profile_full_name);
        this.email = view.findViewById(R.id.profile_email);

//TODO:view all post
//        this.viewAllPosts
//                .setOnClickListener(
//                );

        //TODO:Get profile from db

        return view;
    }
}
