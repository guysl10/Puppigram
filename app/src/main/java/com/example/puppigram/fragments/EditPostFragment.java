package com.example.puppigram.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.puppigram.R;
import com.example.puppigram.model.ImagePost;
import com.example.puppigram.model.MyApp;
import com.example.puppigram.model.PostsModelSQL;

import static android.app.Activity.RESULT_OK;
import static com.example.puppigram.fragments.UploadPostFragment.REQUEST_IMAGE_CAPTURE;

/**
 * A simple {@link Fragment} subclass.
 * Use the factory method to
 * create an instance of this fragment.
 */
public class EditPostFragment extends Fragment {
    public EditPostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditPostFragment.
     */
    // TODO: Rename and change types and number of parameters
//    public static EditPostFragment newInstance(String param1, String param2) {
//        EditPostFragment fragment = new EditPostFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
    ImageView upload_btn;
    EditText description;
    ImageView post_img;
    ImageView username_img;
    TextView username_text;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_post, container, false);
        upload_btn = view.findViewById(R.id.edit_post_text);
        post_img = (ImageView) view.findViewById(R.id.edit_post_img);
        description = view.findViewById(R.id.edit_description);
        username_img = (ImageView) view.findViewById(R.id.edit_username_img);
        username_text = view.findViewById(R.id.edit_username_text);
        ImageView capture_btn = view.findViewById(R.id.edit_capture_img);

        //TODO: show username image and name;
        //username_text.setText();
        // username_img.setImageDrawable();


        capture_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: add support to pickup image from gallery
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        });
        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editPost(view);
            }
        });
        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            ImageView new_img = this.getView().findViewById(R.id.edit_post_img);
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            new_img.setImageBitmap(imageBitmap);
        }
    }

    @SuppressLint("WrongConstant")
    public void editPost(View view){
        upload_btn.setEnabled(false);
        if (post_img.getDrawable() == null){
            Toast.makeText(MyApp.context,"No image selected",40).show();
            upload_btn.setEnabled(true);
            return;
        }
//        if (description.getText() == null)
//            description.setText("");
//        ImagePost new_post = new ImagePost(50,50,description.getText().toString(), "hello");
        ImagePost new_post = new ImagePost("55","50","haroy", "hello");
        PostsModelSQL.instance.editPost(new_post, null);
        upload_btn.setEnabled(true);
    }
}