package com.example.kakyunglee.smokingproject.activity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.kakyunglee.smokingproject.R;

/**
 * Created by KakyungLee on 2017-10-08.
 */

public class LawListActivity extends AppCompatActivity{

    private final String[] listMenu = {"흡연법 연혁/과태료 정보","금연 금지 구역"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.law_list);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_bar);
        TextView barTitle = (TextView)findViewById(R.id.title_bar);
        barTitle.setText("법률정보");

        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,listMenu);
        ListView listView = (ListView)findViewById(R.id.list_law);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;

                switch (position) {
                    case 0:
                        intent = new Intent(LawListActivity.this, LawDetailActivity.class);
                        intent.putExtra("where", position);
                        intent.putExtra("position", position);
                        intent.putExtra("title",listMenu[position]);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(LawListActivity.this, LawSubListActivity.class);
                        intent.putExtra("where", position);
                        intent.putExtra("title",listMenu[position]);
                        startActivity(intent);
                        break;
                }
            }

        });
   }
}
