package com.example.yoyoiq;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.yoyoiq.Adapter.PageAdapterWinnings;
import com.example.yoyoiq.CreatedTeamPOJO.CreatedTeamResponse;
import com.example.yoyoiq.Retrofit.ApiClient;
import com.example.yoyoiq.WalletPackage.AddCash;
import com.example.yoyoiq.WalletPackage.ViewBalanceResponse;
import com.example.yoyoiq.common.DatabaseConnectivity;
import com.example.yoyoiq.common.HelperData;
import com.example.yoyoiq.common.SessionManager;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import org.json.JSONArray;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCashActivity extends AppCompatActivity {
    ViewPager viewPager;
    TabLayout tabLayout;
    LinearLayout linearLayout, joinLinearLayout;
    SessionManager sessionManager;
    PageAdapterWinnings pageAdapterWinnings;
    String total_prize, entryFee, totalSports, leftSports, winningPer, upTo, matchA, matchB, match_id, first_price, price_contribution, contestId;
    TextView backPress, teamATv, teamBTv, walletTV;
    TextView total_prize1, entryFee1, totalSports1, leftSports1, winningPer1, upTo1, first_price1;
    int balance = 0;
    DatabaseConnectivity cmn = DatabaseConnectivity.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cash);
        initMethod();
        setAction();
        linearLayout.setVisibility(View.VISIBLE);
        HelperData.selectSingleTeamCounter.setValue(0);

        pageAdapterWinnings = new PageAdapterWinnings(getSupportFragmentManager(), tabLayout.getTabCount(), total_prize, entryFee, totalSports, leftSports, winningPer, upTo, match_id, first_price, price_contribution, contestId, matchA, matchB);
        viewPager.setAdapter(pageAdapterWinnings);
        sessionManager = new SessionManager(getApplicationContext());

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0) {
                    linearLayout.setVisibility(View.VISIBLE);
                    pageAdapterWinnings.notifyDataSetChanged();
                } else if (tab.getPosition() == 1) {
                    linearLayout.setVisibility(View.GONE);
                    pageAdapterWinnings.notifyDataSetChanged();
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
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        linearLayout = findViewById(R.id.linerLayout1);
        joinLinearLayout = findViewById(R.id.joinLinearLayout);

        backPress = findViewById(R.id.backPress);
        teamATv = findViewById(R.id.matchATv);
        teamBTv = findViewById(R.id.matchBTv);
        walletTV = findViewById(R.id.walletTV);
        total_prize1 = findViewById(R.id.total_prize);
        first_price1 = findViewById(R.id.first_price);
        entryFee1 = findViewById(R.id.joinFee);
        totalSports1 = findViewById(R.id.totalSports);
        leftSports1 = findViewById(R.id.leftSports);
        winningPer1 = findViewById(R.id.winningPer);
        upTo1 = findViewById(R.id.upTo);

        total_prize = getIntent().getStringExtra("total_prize");
        entryFee = getIntent().getStringExtra("entryFee");
        totalSports = getIntent().getStringExtra("totalSports");
        leftSports = getIntent().getStringExtra("leftSports");
        winningPer = getIntent().getStringExtra("winningPer");
        upTo = getIntent().getStringExtra("upTo");
        matchA = getIntent().getStringExtra("matchA");
        matchB = getIntent().getStringExtra("matchB");
        match_id = getIntent().getStringExtra("match_id");
        first_price = getIntent().getStringExtra("first_price");
        contestId = getIntent().getStringExtra("Contest_id");
        price_contribution = getIntent().getStringExtra("price_contribution");

        teamATv.setText(matchA);
        teamBTv.setText(matchB);
        first_price1.setText(first_price);
        total_prize1.setText(total_prize);
        entryFee1.setText(entryFee);
        totalSports1.setText(totalSports);
        leftSports1.setText(leftSports);
        winningPer1.setText(winningPer);
        upTo1.setText(upTo);
    }

    private void setAction() {
        backPress.setOnClickListener(v -> onBackPressed());

        walletTV.setOnClickListener(v -> {
            Intent intent = new Intent(AddCashActivity.this, AddCash.class);
            startActivity(intent);
            finish();
        });

        joinLinearLayout.setOnClickListener(v -> {
            loadTeamData();

        });
    }

    private void loadTeamData() {
        Call<CreatedTeamResponse> call = ApiClient
                .getInstance()
                .getApi()
                .getUserTeamCreated(HelperData.UserId, match_id);
        cmn.setProgressDialog("", "Loading Data...", AddCashActivity.this, AddCashActivity.this);
        call.enqueue(new Callback<CreatedTeamResponse>() {
            @Override
            public void onResponse(Call<CreatedTeamResponse> call, Response<CreatedTeamResponse> response) {
                CreatedTeamResponse createdTeamResponse = response.body();
                if (response.isSuccessful()) {
                    cmn.closeDialog(AddCashActivity.this);
                    String totalData = new Gson().toJson(createdTeamResponse.getResponse());
                    String data = new Gson().toJson(createdTeamResponse.getResponse());
                    if (totalData.length() == 2) {
                        Intent intent = new Intent(AddCashActivity.this, CreateTeamActivity.class);
                        intent.putExtra("match_id", match_id);
                        intent.putExtra("matchA", matchA);
                        intent.putExtra("matchB", matchB);
                        HelperData.teamEdt=false;
                        intent.putExtra("logo_url_a", HelperData.logoUrlTeamA);
                        intent.putExtra("logo_url_b", HelperData.logoUrlTeamB);
                        intent.putExtra("date_start", HelperData.MatchStartTime);
                        intent.putExtra("date_end", HelperData.MatchEndTime);
                        startActivity(intent);
                        finish();
                        JSONArray short_squads = null;
                        JSONArray jsonArray = null;
                    } else {
                        Intent intent = new Intent(AddCashActivity.this, SelectTeams.class);
                        intent.putExtra("Match_id", match_id);
                        intent.putExtra("Contest_id", contestId);
                        intent.putExtra("EntryFee", entryFee);
                        intent.putExtra("upto", upTo);
                        startActivity(intent);
                        SelectTeams.ContestTeamId = null;
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<CreatedTeamResponse> call, Throwable t) {

            }
        });
    }

    private void LoadBalanceData() {
        Call<ViewBalanceResponse> call = ApiClient.getInstance().getApi().getBalanceDetails(sessionManager.getUserData().getUser_id());
        call.enqueue(new Callback<ViewBalanceResponse>() {
            @Override
            public void onResponse(Call<ViewBalanceResponse> call, Response<ViewBalanceResponse> response) {
                ViewBalanceResponse viewBalanceResponse = response.body();
                if (response.isSuccessful()) {
//                        balance= Integer.parseInt(viewBalanceResponse.getBalance());
                    if (balance >= Integer.parseInt(entryFee)) {
                    }
                }
            }

            @Override
            public void onFailure(Call<ViewBalanceResponse> call, Throwable t) {
            }
        });
    }
}