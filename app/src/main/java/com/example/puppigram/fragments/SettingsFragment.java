package com.example.puppigram.fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.puppigram.R;
import com.example.puppigram.activities.LoginActivity;
import com.example.puppigram.repos.UserRepo;
import com.example.puppigram.utils.Navigator;

import java.util.concurrent.Callable;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {
    Button logoutBtn;
    Navigator navigator;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        navigator = new Navigator((AppCompatActivity)getActivity());
        logoutBtn = view.findViewById(R.id.logout_btn);
        logoutBtn.setOnClickListener(v -> {
            UserRepo.instance.logOut((Callable<Void>) () -> {
                navigator.navigate(LoginActivity.class);
                Toast.makeText(
                        getContext(),
                        "bye bye :)",
                        Toast.LENGTH_LONG
                ).show();
                return null;
            });
        });
        return view;
    }
}