package com.example.yoyoiq;

import static com.example.yoyoiq.common.HelperData.limit;
import static com.example.yoyoiq.common.HelperData.newTeamMaking;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.yoyoiq.Adapter.PageAdapterPlayer;
import com.example.yoyoiq.InSideContestActivityFragments.AllSelectedPlayerFromServer;
import com.example.yoyoiq.Model.AllSelectedPlayer;
import com.example.yoyoiq.OnlyTeamPreView.OnlyTeamPreview;
import com.example.yoyoiq.common.HelperData;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class CreateTeamActivity extends AppCompatActivity {
    TextView backPress;
    Button continueBtn, teamPreview;
    String match_id, matchA, matchB, logo_url_a, logo_url_b, date_start, date_end;
    ViewPager viewPager;
    TabLayout tabLayout;
    TabItem tabItem1, tabItem2, tabItem3, tabItem4;
    PageAdapterPlayer pageAdapterPlayer;
    LinearLayout linearLayout1, linearLayout2, linearLayout3, linearLayout4;
    CircleImageView imageViewA, imageViewB;
    RecyclerView ball_recyclerView;
    TextView textViewA, textViewB, tv_TotalSelectedPlayer, tv_TotalCredit, tv_TeamOneSize, tv_TeamTwoSize;
    TextView text1, text2, text3, text4, text5, text6, text7, text8, text9, text10, text11;
    private String EVENT_DATE_TIME = "2022-07-23 18:08:00";
    private String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private TextView tv_days, tv_done;
    private Handler handler = new Handler();
    private Runnable runnable;
    public static String addedPlayerIds;
    public static String CreatedTeamId;
    private List<AllSelectedPlayerFromServer> listData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team_actvity);
        initMethod();
        setAction();
        countDownStart();
        linearLayout1.setVisibility(View.VISIBLE);

        //for  Team Edit Section working
        if (getIntent().hasExtra("CreatedTeamId")) {
            CreateTeamActivity.CreatedTeamId = getIntent().getStringExtra("CreatedTeamId");
            listData = new Gson().fromJson(getIntent().getStringExtra("listdata"), new TypeToken<ArrayList<AllSelectedPlayerFromServer>>() {
            }.getType());
        }

        if (listData.size() > 0) {
            HelperData.myTeamList.clear();
            for (int i = 0; i < listData.size(); i++) {
                int id = listData.get(i).getPid();
                String match_id = listData.get(i).getMatchId();
                String title = listData.get(i).getTitle();
                String country = listData.get(i).getCountry();
                String playing_role = listData.get(i).getPlaying_role();
                double fantasy_rating = listData.get(i).getFantasy_player_rating();
                boolean cap = listData.get(i).isCap();
                boolean Vcap = listData.get(i).isVcap();
                boolean added = listData.get(i).isAdded();
                String point = listData.get(i).getPoints();
                AllSelectedPlayer allSelectedPlayer = new AllSelectedPlayer(id, match_id, title, country, playing_role, fantasy_rating, added, cap, Vcap, point);
                HelperData.myTeamList.add(allSelectedPlayer);
                CreateTeamActivity.addedPlayerIds = CreateTeamActivity.addedPlayerIds + "_" + listData.get(i).getPid() + "_\n";
            }
        }

        // till here-------------
        pageAdapterPlayer = new PageAdapterPlayer(getSupportFragmentManager(), tabLayout.getTabCount(), match_id, matchA, matchB, logo_url_a, logo_url_b, listData);
        viewPager.setAdapter(pageAdapterPlayer);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0) {
                    linearLayout1.setVisibility(View.VISIBLE);
                    linearLayout2.setVisibility(View.GONE);
                    linearLayout3.setVisibility(View.GONE);
                    linearLayout4.setVisibility(View.GONE);
                    pageAdapterPlayer.notifyDataSetChanged();
                } else if (tab.getPosition() == 1) {
                    linearLayout1.setVisibility(View.GONE);
                    linearLayout2.setVisibility(View.VISIBLE);
                    linearLayout3.setVisibility(View.GONE);
                    linearLayout4.setVisibility(View.GONE);
                    pageAdapterPlayer.notifyDataSetChanged();
                } else if (tab.getPosition() == 2) {
                    linearLayout1.setVisibility(View.GONE);
                    linearLayout2.setVisibility(View.GONE);
                    linearLayout3.setVisibility(View.VISIBLE);
                    linearLayout4.setVisibility(View.GONE);
                    pageAdapterPlayer.notifyDataSetChanged();
                } else if (tab.getPosition() == 3) {
                    linearLayout1.setVisibility(View.GONE);
                    linearLayout2.setVisibility(View.GONE);
                    linearLayout3.setVisibility(View.GONE);
                    linearLayout4.setVisibility(View.VISIBLE);
                    pageAdapterPlayer.notifyDataSetChanged();
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

        if (!HelperData.teamEdt) {
            newTeamMaking();
        }

        playerCounter();
        creditCounter();
        playerSectionCounter();

        HelperData.wk.observe(this, e -> {
            if (e == null) {
                tabLayout.getTabAt(0).setText("WK(0)");
                return;
            }
            tabLayout.getTabAt(0).setText("WK(" + e + ")");
        });

        HelperData.bat.observe(this, e -> {
            if (e == null) {
                tabLayout.getTabAt(1).setText("BAT(0)");
                return;
            }
            tabLayout.getTabAt(1).setText("BAT(" + e + ")");
        });

        HelperData.ar.observe(this, e -> {
            if (e == null) {
                tabLayout.getTabAt(2).setText("AR(0)");
                return;
            }
            tabLayout.getTabAt(2).setText("AR(" + e + ")");
        });

        HelperData.bowl.observe(this, e -> {
            if (e == null) {
                tabLayout.getTabAt(3).setText("BOWL(0)");
                return;
            }
            tabLayout.getTabAt(3).setText("BOWL(" + e + ")");
        });
    }

    private void initMethod() {
        linearLayout1 = findViewById(R.id.linerLayout1);
        linearLayout2 = findViewById(R.id.linerLayout2);
        linearLayout3 = findViewById(R.id.linerLayout3);
        linearLayout4 = findViewById(R.id.linerLayout4);
        ball_recyclerView = findViewById(R.id.ball_recyclerView);
        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        text3 = findViewById(R.id.text3);
        text4 = findViewById(R.id.text4);
        text5 = findViewById(R.id.text5);
        text6 = findViewById(R.id.text6);
        text7 = findViewById(R.id.text7);
        text8 = findViewById(R.id.text8);
        text9 = findViewById(R.id.text9);
        text10 = findViewById(R.id.text10);
        text11 = findViewById(R.id.text11);

        tv_days = findViewById(R.id.tv_days);
        tv_done = findViewById(R.id.done);

        backPress = findViewById(R.id.backPress);
        teamPreview = findViewById(R.id.teamPreview);
        continueBtn = findViewById(R.id.Continue);

        match_id = getIntent().getStringExtra("match_id");
        matchA = getIntent().getStringExtra("matchA");
        matchB = getIntent().getStringExtra("matchB");
        logo_url_a = getIntent().getStringExtra("logo_url_a");
        logo_url_b = getIntent().getStringExtra("logo_url_b");
        date_start = getIntent().getStringExtra("date_start");
        date_end = getIntent().getStringExtra("date_end");

        tabLayout = findViewById(R.id.tabLayout);
        tabItem1 = findViewById(R.id.WK);
        tabItem2 = findViewById(R.id.BAT);
        tabItem3 = findViewById(R.id.AR);
        tabItem4 = findViewById(R.id.BOWL);
        viewPager = findViewById(R.id.viewPager);

        imageViewA = findViewById(R.id.im_Team1);
        imageViewB = findViewById(R.id.im_Team2);
        textViewA = findViewById(R.id.tvHead_TeamOneName);
        textViewB = findViewById(R.id.tvHead_TeamTwoName);

        tv_TotalSelectedPlayer = findViewById(R.id.tv_TotalSelectedPlayer);
        tv_TotalCredit = findViewById(R.id.tv_TotalCredit);
        tv_TeamOneSize = findViewById(R.id.tv_TeamOneSize);
        tv_TeamTwoSize = findViewById(R.id.tv_TeamTwoSize);
    }

    public static void removedPlayerFromAddedList(int pid) {
        for (int i = 0; i < HelperData.myTeamList.size(); i++) {
            if (HelperData.myTeamList.get(i).getPid() == pid) {
                HelperData.myTeamList.remove(i);
            }
        }
    }

    private void setAction() {
        backPress.setOnClickListener(view ->
                onBackPressed()
        );

        textViewA.setText(matchA);
        textViewB.setText(matchB);
        HelperData.team1NameShort = matchA;
        HelperData.team2NameShort = matchB;
        HelperData.logoUrlTeamA = logo_url_a;
        HelperData.logoUrlTeamB = logo_url_b;

        Glide.with(this)
                .load(logo_url_a)
                .into(imageViewA);

        Glide.with(this)
                .load(logo_url_b)
                .into(imageViewB);

        continueBtn.setOnClickListener(v -> {
            handleAfterContinueButton();
        });

        teamPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateTeamActivity.this, OnlyTeamPreview.class);
                startActivity(intent);
            }
        });
    }

    private void handleAfterContinueButton() {
        if (HelperData.playerCounter.getValue() == limit) {
            if (HelperData.wk.getValue() >= 1) {
                if (HelperData.bat.getValue() >= 3) {
                    if (HelperData.ar.getValue() >= 1) {
                        if (HelperData.bowl.getValue() >= 3) {
                            Intent intent = new Intent(CreateTeamActivity.this, TeamPreviewActivity.class);
                            intent.putExtra("date_start", date_start);
                            if (HelperData.teamEdt == true) {
                                intent.putExtra("updateTeam", true);
                            }
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(this, "Add atleast 3 player in BOWL section", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(this, "Add atleast 1 player in AlRounder section", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, "Add atleast 3 player in Batting section", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Add atleast 1 player in WicketKeeper section", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "You have selected " + HelperData.playerCounter.getValue() + " it required " + limit + " players", Toast.LENGTH_LONG).show();
        }
    }

    private void playerSectionCounter() {
        HelperData.wk.observe(this, integer -> {
            if (integer >= 0) {
                tabLayout.getTabAt(0).setText("" + integer);
            }
        });
        HelperData.bat.observe(this, integer -> {
            if (integer >= 0) {
                tabLayout.getTabAt(1).setText("" + integer);
            }
        });
        HelperData.ar.observe(this, integer -> {
            if (integer >= 0) {
                tabLayout.getTabAt(2).setText("" + integer);
            }
        });
        HelperData.bowl.observe(this, integer -> {
            if (integer >= 0) {
                tabLayout.getTabAt(3).setText("" + integer);
            }
        });
    }

    private void playerCounter() {
        HelperData.playerCounter.observe(this, integer -> {
            int limit = 0;
            if (HelperData.type_selected.equalsIgnoreCase("Cricket")) {
                limit = 11;
            } else {
                limit = 5;
            }
            tv_TotalSelectedPlayer.setText("" + integer + "/" + limit);
            LinearProgressIndicator maxPlayerSelected = new LinearProgressIndicator(CreateTeamActivity.this);
            try {
                maxPlayerSelected.setProgressCompat(integer, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

            ball_recyclerView.setLayoutManager(layoutManager);

            if (integer != null) {
                LinearLayout layout = (LinearLayout) findViewById(R.id.playerPosition);
                for (int i = 0; i < layout.getChildCount(); i++) {
                    TextView b = (TextView) layout.getChildAt(i);
                    b.setBackground(getResources().getDrawable(R.drawable.circle_shape));
                }

                for (int i = 0; i < integer; i++) {
                    TextView b = (TextView) layout.getChildAt(i);
                    b.setBackground(getResources().getDrawable(R.drawable.green_circleshape));
                }

                TextView b = (TextView) layout.getChildAt(integer);
                try {
                    b.setText(String.valueOf(integer + 1));
                } catch (NullPointerException e) {

                }
            }
        });
    }

    private void creditCounter() {
        tv_TeamOneSize = findViewById(R.id.tv_TeamOneSize);
        tv_TeamTwoSize = findViewById(R.id.tv_TeamTwoSize);

        HelperData.creditCounter.observe(this, integer -> tv_TotalCredit.setText("" + integer + "/100"));

        HelperData.conty1.observe(this, integer -> tv_TeamOneSize.setText("" + integer));

        HelperData.conty2.observe(this, integer -> tv_TeamTwoSize.setText("" + integer));
    }

    private void countDownStart() {
        runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    handler.postDelayed(this, 1000);
                    SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
                    Date event_date = dateFormat.parse(date_start);
                    Date current_date = new Date();
                    if (!current_date.after(event_date)) {
                        long diff = event_date.getTime() - current_date.getTime();
                        long Days = diff / (24 * 60 * 60 * 1000);
                        long Hours = diff / (60 * 60 * 1000) % 24;
                        long Minutes = diff / (60 * 1000) % 60;
                        long Seconds = diff / 1000 % 60;
                        tv_days.setText(String.format("%02d", Days) + " " + String.format("%02d", Hours) + "h " + String.format("%02d", Minutes) + "m " + String.format("%02d", Seconds) + "s " + " left");
                    } else {
                        tv_days.setVisibility(View.GONE);
                        tv_done.setVisibility(View.VISIBLE);
                        tv_done.setText("Done!");
                        handler.removeCallbacks(runnable);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 0);
    }

    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        HelperData.myTeamList.clear();
    }
}