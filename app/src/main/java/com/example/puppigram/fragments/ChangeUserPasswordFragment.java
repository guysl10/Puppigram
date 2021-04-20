package com.example.puppigram.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.puppigram.R;
import com.example.puppigram.model.user.UsersModel;

public class ChangeUserPasswordFragment extends Fragment {

    private TextView currentPassword;
    private TextView pass;
    private TextView rePass;
    private Button updateBtn;

    public ChangeUserPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_user_password, container, false);
        currentPassword = view.findViewById(R.id.change_current_password);
        pass = view.findViewById(R.id.change_new_password);
        rePass = view.findViewById(R.id.change_repassword);
        updateBtn = view.findViewById(R.id.change_save_new_password_btn);
        updateBtn.setOnClickListener(v -> {
            if (pass.getText().toString().equals(rePass.getText().toString())) {
                UsersModel.instance.changeUserPassword(pass.getText().toString());
                Navigation.findNavController(view).navigate(R.id.settingsFragment);
                Toast.makeText(getContext(), "Password changed successfully", Toast.LENGTH_LONG).show();
            } else {
                currentPassword.setText("");
                pass.setText("");
                rePass.setText("");
                Toast.makeText(getContext(), "Password not equal!", Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }
}