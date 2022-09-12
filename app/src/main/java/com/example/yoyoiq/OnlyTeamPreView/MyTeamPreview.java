package com.example.yoyoiq.OnlyTeamPreView;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yoyoiq.InSideContestActivityFragments.AllSelectedPlayerFromServer;
import com.example.yoyoiq.R;
import com.example.yoyoiq.common.HelperData;
import com.google.android.flexbox.FlexboxLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class MyTeamPreview extends AppCompatActivity {
    TextView close, teamA, teamB, userNameAndTid;
    int teamAcount = 0, teamBCount = 0;
    FlexboxLayout LL_GroundWK;
    FlexboxLayout LL_GroundBAT;
    FlexboxLayout LL_GroundAR;
    FlexboxLayout LL_GroundBOWL;
    int position;
    private List<AllSelectedPlayerFromServer> listData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_team_preview);
        initMethod();
        setAction();
        getAllRequestedData();
    }

    private void getAllRequestedData() {
        teamA.setText("" + HelperData.team1NameShort);
        teamB.setText("" + HelperData.team2NameShort);
        if (listData.size() > 0) {
            for (int i = 0; i < listData.size(); i++) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View tv = inflater.inflate(R.layout.single_grounder, null);
                TextView nameTxt = tv.findViewById(R.id.nameTxt);
                TextView playerPts = tv.findViewById(R.id.pointsTxt);
                TextView badgeTxtGoru = tv.findViewById(R.id.badgeTxtGoru);
                TextView VCnameTv = tv.findViewById(R.id.VCnameTv);
                if (listData.get(i).isCap()) {
                    badgeTxtGoru.setText("C");
                    badgeTxtGoru.setVisibility(View.VISIBLE);
                }
                if (listData.get(i).isVcap()) {
                    VCnameTv.setText("VC");
                    VCnameTv.setVisibility(View.VISIBLE);
                }
                String one = "" + listData.get(i).getTitle();
                String two = "" + listData.get(i).getPoints();
                nameTxt.setText("" + one);
                playerPts.setText("" + two);
                if (listData.get(i).getCountry().equals(HelperData.team1NameShort)) {
                    nameTxt.setBackgroundColor(Color.BLACK);
                    nameTxt.setTextColor(Color.WHITE);
                }
                if (listData.get(i).getCountry().equalsIgnoreCase(HelperData.team2NameShort)) {
                    nameTxt.setBackgroundColor(Color.WHITE);
                    nameTxt.setTextColor(Color.BLACK);
                }
                if (listData.get(i).getPlaying_role().equalsIgnoreCase("wk")) {
                    assert LL_GroundWK != null;
                    LL_GroundWK.setBackgroundColor(Color.TRANSPARENT);
                    if (HelperData.team1NameShort.equalsIgnoreCase(listData.get(i).getCountry())) {
                        LL_GroundWK.addView(tv);
                        teamAcount++;
                    } else {
                        LL_GroundWK.addView(tv);
                        teamBCount++;
                    }
                } else if (listData.get(i).getPlaying_role().equalsIgnoreCase("bat")) {
                    LL_GroundBAT.setBackgroundColor(Color.TRANSPARENT);
                    if (HelperData.team1NameShort.equalsIgnoreCase(listData.get(i).getCountry())) {
                        LL_GroundBAT.addView(tv);
                        teamAcount++;
                    } else {
                        LL_GroundBAT.addView(tv);
                        teamBCount++;
                    }
                } else if (listData.get(i).getPlaying_role().equalsIgnoreCase("ar")) {
                    LL_GroundAR.setBackgroundColor(Color.TRANSPARENT);
                    if (HelperData.team1NameShort.equalsIgnoreCase(listData.get(i).getCountry())) {
                        LL_GroundAR.addView(tv);
                        teamAcount++;
                    } else {
                        LL_GroundAR.addView(tv);
                        teamBCount++;
                    }
                } else if (listData.get(i).getPlaying_role().equalsIgnoreCase("bowl")) {
                    LL_GroundBOWL.setBackgroundColor(Color.TRANSPARENT);
                    if (HelperData.team1NameShort.equalsIgnoreCase(listData.get(i).getCountry())) {
                        LL_GroundBOWL.addView(tv);
                        teamAcount++;
                    } else {
                        LL_GroundBOWL.addView(tv);
                        teamBCount++;
                    }
                }
            }
        }
    }

    private void initMethod() {
        close = findViewById(R.id.close);
        userNameAndTid = findViewById(R.id.userNameAndTid);
        teamA = findViewById(R.id.teamA);
        teamB = findViewById(R.id.teamB);
        LL_GroundWK = findViewById(R.id.LL_GroundWK);
        LL_GroundBAT = findViewById(R.id.LL_GroundBAT);
        LL_GroundAR = findViewById(R.id.LL_GroundAR);
        LL_GroundBOWL = findViewById(R.id.LL_GroundBOWL);
        if (getIntent().hasExtra("position")) {
            listData = new Gson().fromJson(getIntent().getStringExtra("listdata"), new TypeToken<ArrayList<AllSelectedPlayerFromServer>>() {
            }.getType());
        }
        userNameAndTid.setText("" + getIntent().getStringExtra("TeamName"));
        position = getIntent().getIntExtra("selectedPosition", 0);
    }

    private void setAction() {
        close.setOnClickListener(v -> finish());
    }
}