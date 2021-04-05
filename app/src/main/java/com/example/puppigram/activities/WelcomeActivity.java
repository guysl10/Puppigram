package com.example.puppigram.activities;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.puppigram.R;
import com.example.puppigram.utils.Navigator;


public class WelcomeActivity extends AppCompatActivity {
    Navigator navigator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navigator = new Navigator(this);
        setContentView(R.layout.activity_welcome);
        class MyAsyncTask extends AsyncTask {
            @Override
            protected Object doInBackground(Object[] objects) {
                try {
                    //TODO: to check for connectivity
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                navigator.navigate(LoginActivity.class);
            }
        }
        MyAsyncTask task = new MyAsyncTask();
        task.execute();
    }
}

