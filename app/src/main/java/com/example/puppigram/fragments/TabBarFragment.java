package com.example.puppigram.fragments;

import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.puppigram.R;

public class TabBarFragment extends Fragment implements View.OnClickListener {
    Fragment[] tabs = new Fragment[4];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab_bar, container,false);
        ImageView feed = view.findViewById(R.id.tabbar_feed_img);
        ImageView upload_post = view.findViewById(R.id.tabbar_upload_img);
        ImageView search = view.findViewById(R.id.tabbar_search_img);
        ImageView profile = view.findViewById(R.id.tabbar_profile_img);

        feed.setTag(0);
        upload_post.setTag(1);
        search.setTag(2);
        profile.setTag(3);

        feed.setOnClickListener(this);
        upload_post.setOnClickListener(this);
        search.setOnClickListener(this);
        profile.setOnClickListener(this);


        tabs[0] = new FeedFragment();
        tabs[1] = new UploadPostFragment();
        tabs[2] = new SearchFragment();
        tabs[3] = new ProfileFragment();

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.tabbar_container, tabs[0]);
        transaction.commit();

        return view;
    }

    @Override
    public void onClick(View v) {
        int selected = (int)v.getTag();
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.tabbar_container, tabs[selected]);
        transaction.commit();
    }
}