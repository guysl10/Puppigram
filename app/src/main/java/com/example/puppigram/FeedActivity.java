package com.example.puppigram;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

//Responsible to handle all feed issues.
public class FeedActivity extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_feed);
//
////        RecyclerView posts = findViewById(R.id.postslist_recyclerv);
        ListView posts = findViewById(R.id.feed_posts_lists);
        PostAdapter postAdapter = new PostAdapter();
        posts.setAdapter(postAdapter);

//        posts.setHasFixedSize(true);
//        layoutManager = new LinearLayoutManager(this);
//        posts.setLayoutManager(layoutManager);
    }


    class PostAdapter extends BaseAdapter{
//        List<ImagePost> imagePosts = new LinkedList<ImagePost>();
        List<String> imagePosts = new LinkedList<String>();
        PostAdapter(){
            //TODO: get all posts from db.
            for(int i=0;i<10;++i){
                imagePosts.add("element #"+i);//add post
            }
        }

        @Override
        public int getCount() {
            return imagePosts.size();
        }

        @Override
        public Object getItem(int position) {
            return imagePosts.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int post_id, View view, ViewGroup viewGroup) {
            View v = getLayoutInflater().inflate(R.layout.feed_post_row, null);

            TextView description = v.findViewById(R.id.post_description);
            description.setText(imagePosts.get(post_id));//.getDescription());

//            TextView likers = v.findViewById(R.id.post_count_likers);
//            likers.setText(imagePosts.get(post_id).getNumLikers());
//
//            TextView username = v.findViewById(R.id.post_username);
//            //TODO: chanage for getting the username instead of owner_id.
//            username.setText(imagePosts.get(post_id).getOwner_id());
//
//            ImageView user_img = v.findViewById(R.id.post_user_img);
//            //TODO: chanage for getting the user_img instead of string, has to be drawable.
////            user_img.setImageDrawable();
//
//            ImageView post_img = v.findViewById(R.id.post_img);
//            //TODO: chanage for getting the post_img instead of string.
////            post_img.setImageDrawable(imagePosts.get(post_id).getImage());


            return v;
        }
    }

}