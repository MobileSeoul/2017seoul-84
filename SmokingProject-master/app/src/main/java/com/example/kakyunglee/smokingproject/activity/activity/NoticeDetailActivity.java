package com.example.kakyunglee.smokingproject.activity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.kakyunglee.smokingproject.R;
import com.example.kakyunglee.smokingproject.activity.dto.NoticeDTO;

/**
 * Created by KakyungLee on 2017-10-23.
 */

public class NoticeDetailActivity extends AppCompatActivity{


    NoticeDTO notice;
    TextView title;
    TextView created_at;
    TextView contnet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_detail);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_bar);
        TextView barTitle = (TextView)findViewById(R.id.title_bar);
        barTitle.setText("공지사항");

        Intent intent = getIntent();
        notice = (NoticeDTO) intent.getExtras().getSerializable("notice_detail");



        title = (TextView)findViewById(R.id.notice_title);
        created_at = (TextView)findViewById(R.id.notice_created_at);
        contnet = (TextView)findViewById(R.id.notice_content);

        title.setText(notice.getTitle());
        created_at.setText(notice.getCreated_at());
        contnet.setText(notice.getContents());

    }

}
