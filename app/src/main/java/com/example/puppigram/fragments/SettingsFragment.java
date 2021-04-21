package com.example.puppigram.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.puppigram.R;
import com.example.puppigram.activities.LoginActivity;
import com.example.puppigram.model.user.UsersModel;
import com.example.puppigram.utils.Navigator;

import java.util.concurrent.Callable;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {
    Button logoutBtn;
    Button createUserPasswordBtn;
    Navigator navigator;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        navigator = new Navigator((AppCompatActivity)getActivity());
        logoutBtn = view.findViewById(R.id.logout_btn);
        createUserPasswordBtn = view.findViewById(R.id.change_password_btn);
        logoutBtn.setOnClickListener(v -> {
            UsersModel.instance.logOut((Callable<Void>) () -> {
                navigator.navigate(LoginActivity.class);
                Toast.makeText(
                        getContext(),
                        "bye bye :)",
                        Toast.LENGTH_LONG
                ).show();
                return null;
            });
        });
        createUserPasswordBtn.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.action_settingsFragment_to_changeUserPasswordFragment);
        });
        return view;
    }
}