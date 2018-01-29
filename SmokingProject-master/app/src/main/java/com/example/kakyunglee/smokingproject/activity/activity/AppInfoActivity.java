package com.example.kakyunglee.smokingproject.activity.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.kakyunglee.smokingproject.R;

/**
 * Created by KakyungLee on 2017-10-22.
 */

public class AppInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_information);

        // 액션바 사용자 정의로 만들기
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_bar);
        TextView title = (TextView)findViewById(R.id.title_bar);
        title.setText("앱정보");


    }
}