package com.example.puppigram.adapters;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.puppigram.R;
import com.example.puppigram.activities.MainActivity;
import com.example.puppigram.model.User;
import com.example.puppigram.repos.Repo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ImageViewHolder> {
    private static final String TAG = "UserAdapter";
    private final Context context;
    private List<User> users;
    private final boolean isFragment;
    Repo repository;
    private FirebaseUser firebaseUser;

    public UserAdapter(Context context, List<User> users, boolean isFragment) {
        this.context = context;
        Log.d(TAG, "UserAdapter: " + users);
        this.users = users;
        this.isFragment = isFragment;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public UserAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_feed, parent, false);
        return new UserAdapter.ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ImageViewHolder holder, int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final User user = users.get(position);

        holder.name.setText(user.getName());
        holder.userName.setText(user.getUserName());
        //Glide.with(context).load(user.getUserImage()).into(holder.userImage);

        //if user click on other user go to his profile
        holder.itemView.setOnClickListener(view -> {
            if (isFragment) {
                SharedPreferences.Editor editor = context.getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                editor.putString("profileId", user.getId());
                editor.putString("other", "true");
                editor.apply();
                Navigation.createNavigateOnClickListener(R.id.action_postFragment_to_editPostFragment);

            } else {
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("ownerId", user.getId());
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        if (users == null) {
            return 0;
        }
        return users.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView userName;
        //public CircleImageView userImage;
        public TextView email;
        public TextView bio;

        public ImageViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.profile_full_name);
            userName = itemView.findViewById(R.id.profile_username_text);
            //userImage = itemView.findViewById(R.id.profile_image);
            email = itemView.findViewById(R.id.profile_email);
            bio = itemView.findViewById(R.id.profile_bio_text);
        }
    }
}
