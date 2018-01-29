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
 * Created by KakyungLee on 2017-10-24.
 */

public class LawSubListActivity extends AppCompatActivity {

    private final String[] listMenu = {"정부지방청사","학교","의료기관","어린이 관련시설","청소년 이용시설",
            "도서관","학원","교통 관련 시설","대형건물","공연장",
            "대규모 점포","관광숙박업소","체육시설","사회복지시설","목욕장",
            "게임방/ PC방","만화방","음식점","고속도로 휴게시설","공동주택",
            "산림인접지역","자연공원","숭례문","버스 및 택시","기차",
            "공항","항공기","문화재","그 외"};
    private  String title;
    private int where;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.law_list);
        Intent intent = getIntent();
        where = intent.getExtras().getInt("where");
        title = intent.getExtras().getString("title");

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_bar);
        TextView barTitle = (TextView)findViewById(R.id.title_bar);
        barTitle.setText(title);

        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,listMenu);
        ListView listView = (ListView)findViewById(R.id.list_law);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(LawSubListActivity.this, LawDetailActivity.class);
                intent.putExtra("where", where);
                intent.putExtra("position", position);
                intent.putExtra("title",listMenu[position]);
                startActivity(intent);
            }

        });

    }
}
