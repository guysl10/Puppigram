package com.example.puppigram.adapters;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.puppigram.R;
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
        View view = LayoutInflater.from(context).inflate(R.layout.feed_user_row, parent, false);
        return new UserAdapter.ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ImageViewHolder holder, int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final User user = users.get(position);

        holder.name.setText(user.getName());
        holder.userName.setText(user.getUserName());
        Glide.with(context).load(user.getUserImage()).into(holder.userImage);

        if (user.getId().equals(firebaseUser.getUid())) {//can't fallow after yourself
            holder.btn_follow.setVisibility(View.GONE);
        }

        //if user click on other user go to his profile
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFragment) {
                    SharedPreferences.Editor editor = context.getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                    editor.putString("profileid", user.getId());
                    editor.putString("other", "true");
                    editor.apply();
                    Navigation.createNavigateOnClickListener(R.id.action_global_profileFragment);

                } else {
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("publisherid", user.getId());
                    context.startActivity(intent);
                }
            }
        });


        //TODO: do followers function
        final UserModel[] userModel1 = new UserModel[2];
        //if the user want to follow after other user change the button and update in database
        holder.btn_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.btn_follow.getText().toString().equals("follow")) {
                    repository.instance.getUser(user.getId(), new Repository.GetUserListener() {
                        @Override
                        public void onComplete(UserModel userModel) {
                            if (userModel != null) {
                                userModel1[0] = userModel;
                                repository.instance.getUser(firebaseUser.getUid(), new Repository.GetUserListener() {
                                    @Override
                                    public void onComplete(UserModel userModel) {
                                        if (userModel != null) {
                                            userModel1[1] = userModel;
                                            Log.d(TAG, "onClick: " + userModel1[0]);
                                            repository.instance.addFollow(userModel1[0], userModel1[1], new Repository.GetNewFollowListener() {
                                                @Override
                                                public void onComplete(boolean success) {
                                                    repository.instance.addFollowNotification(userModel1[0].getId(), new Repository.GetNotifiListener() {
                                                        @Override
                                                        public void onComplete(boolean success) {
                                                            if (!success) {
                                                                Log.d(TAG, "onComplete: failed to add follow");
                                                            }
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        }
                    });
                } else {//If the user does not want to follow you return the button to the first option
                    // and remove it from the followers in Data Base
                    repository.instance.getUser(user.getId(), new Repository.GetUserListener() {
                        @Override
                        public void onComplete(UserModel userModel) {
                            if (userModel != null) {
                                userModel1[0] = userModel;
                                repository.instance.getUser(firebaseUser.getUid(), new Repository.GetUserListener() {
                                    @Override
                                    public void onComplete(UserModel userModel) {
                                        if (userModel != null) {
                                            userModel1[1] = userModel;
                                            Log.d(TAG, "onClick: " + userModel1[0]);
                                            repository.instance.deleteFollow(userModel1[0], userModel1[1], new Repository.DeleteFollowListener() {
                                                @Override
                                                public void onComplete(boolean success) {
                                                    repository.instance.removeFollowNotification(userModel1[0].getId(), new Repository.GetNotifiListener() {
                                                        @Override
                                                        public void onComplete(boolean success) {
                                                            if (!success) {
                                                                Log.d(TAG, "onComplete: failed to remove notify follow");
                                                            }
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        }
                    });

                }
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
