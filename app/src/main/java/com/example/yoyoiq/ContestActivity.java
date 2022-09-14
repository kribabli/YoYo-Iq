package com.example.yoyoiq;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.yoyoiq.Adapter.PageAdapter;
import com.example.yoyoiq.WalletPackage.AddCash;
import com.example.yoyoiq.common.HelperData;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class ContestActivity extends AppCompatActivity {
    TextView walletTV, backPress, matchATv, matchBTv;
    String matchA = "", matchB = "", match_id, logo_url_a, logo_url_b, date_start, date_end;
    LinearLayout createTeamLayout, createTeamLay;
    ViewPager viewPager;
    TabLayout tabLayout;
    TabItem tabItem1, tabItem2, tabItem3;
    PageAdapter pageAdapter;
    TextView matchList, createTeam, createTeam1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contest);
        initMethod();
        setAction();

        pageAdapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), match_id, matchA, matchB, logo_url_a, logo_url_b, date_start, date_end);
        viewPager.setAdapter(pageAdapter);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0) {
                    createTeamLay.setVisibility(View.GONE);
                    createTeamLayout.setVisibility(View.VISIBLE);
                    pageAdapter.notifyDataSetChanged();
                } else if (tab.getPosition() == 1) {
                    createTeamLay.setVisibility(View.GONE);
                    createTeamLayout.setVisibility(View.GONE);
                    pageAdapter.notifyDataSetChanged();
                } else if (tab.getPosition() == 2) {
                    createTeamLay.setVisibility(View.VISIBLE);
                    createTeamLayout.setVisibility(View.GONE);
                    pageAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    private void initMethod() {
        walletTV = findViewById(R.id.walletTV);
        backPress = findViewById(R.id.backPress);
        matchATv = findViewById(R.id.matchATv);
        matchBTv = findViewById(R.id.matchBTv);
        createTeamLayout = findViewById(R.id.createTeamLayout);
        createTeamLay = findViewById(R.id.createTeamLay);
        createTeamLayout.setVisibility(View.VISIBLE);

        matchA = getIntent().getStringExtra("shortNameA");
        matchB = getIntent().getStringExtra("shortNameB");
        match_id = getIntent().getStringExtra("match_id");
        logo_url_a = getIntent().getStringExtra("logo_url_a");
        logo_url_b = getIntent().getStringExtra("logo_url_b");
        date_start = getIntent().getStringExtra("date_start");
        date_end = getIntent().getStringExtra("date_end");
        matchATv.setText(matchA);
        matchBTv.setText(matchB);
        HelperData.logoUrlTeamA = logo_url_a;
        HelperData.logoUrlTeamB = logo_url_b;
        HelperData.MatchStartTime = date_start;
        HelperData.MatchEndTime = date_end;

        tabLayout = findViewById(R.id.tabLayout);
        tabItem1 = findViewById(R.id.contests);
        tabItem2 = findViewById(R.id.myContests);
        tabItem3 = findViewById(R.id.myTeams);
        viewPager = findViewById(R.id.viewPager);
        matchList = findViewById(R.id.matchList);
        createTeam = findViewById(R.id.createTeam);
        createTeam1 = findViewById(R.id.createTeam1);
    }

    private void setAction() {

        walletTV.setOnClickListener(view -> {
            Intent intent = new Intent(ContestActivity.this, AddCash.class);
            startActivity(intent);
        });

        backPress.setOnClickListener(view -> onBackPressed());

        createTeam.setOnClickListener(view -> {
            Intent intent = new Intent(this, CreateTeamActivity.class);
            intent.putExtra("match_id", match_id);
            intent.putExtra("matchA", matchA);
            intent.putExtra("matchB", matchB);
            HelperData.teamEdt=false;
            intent.putExtra("logo_url_a", logo_url_a);
            intent.putExtra("logo_url_b", logo_url_b);
            intent.putExtra("date_start", date_start);
            intent.putExtra("date_end", date_end);
            startActivity(intent);
        });

        createTeam1.setOnClickListener(view -> {
            Intent intent = new Intent(this, CreateTeamActivity.class);
            intent.putExtra("match_id", match_id);
            intent.putExtra("matchA", matchA);
            intent.putExtra("matchB", matchB);
            HelperData.teamEdt=false;
            intent.putExtra("logo_url_a", logo_url_a);
            intent.putExtra("logo_url_b", logo_url_b);
            intent.putExtra("date_start", date_start);
            intent.putExtra("date_end", date_end);
            startActivity(intent);
        });
    }
}