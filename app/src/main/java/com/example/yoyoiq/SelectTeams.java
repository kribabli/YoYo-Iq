package com.example.yoyoiq;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yoyoiq.Adapter.SelectTeamsAdapter;
import com.example.yoyoiq.CreatedTeamPOJO.CreatedTeamResponse;
import com.example.yoyoiq.InSideContestActivityFragments.myAllTeamRequest;
import com.example.yoyoiq.JoinContest.JoinContestsResponse;
import com.example.yoyoiq.Retrofit.ApiClient;
import com.example.yoyoiq.WalletPackage.AddCash;
import com.example.yoyoiq.WalletPackage.ContestJoinResponse;
import com.example.yoyoiq.WalletPackage.ViewBalanceResponse;
import com.example.yoyoiq.common.DatabaseConnectivity;
import com.example.yoyoiq.common.HelperData;
import com.example.yoyoiq.common.SessionManager;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectTeams extends AppCompatActivity {
    TextView backPress, team, selectAll;
    Button joinBtn;
    RecyclerView recyclerView;
    ArrayList<myAllTeamRequest> list = new ArrayList();
    SelectTeamsAdapter selectTeamsAdapter;
    public static Integer count = 0;
    String contest_id;
    String Entryfee;
    int netEntryFee = 0;
    String Upto;
    AlertDialog deleteDialog;
    public static String ContestTeamId = null;
    ProgressDialog progressDialog;
    SessionManager sessionManager;
    DatabaseConnectivity common = DatabaseConnectivity.getInstance();
    public static boolean allSelectedTeam = false;
    public static String selectedSingleTeam;

    public static void selected_single_Team(String teamId) {
        for (int i = 0; i < HelperData.myCountyPlayer.size(); i++) {
            if (HelperData.myCountyPlayer.get(i).isSlected() == true) {
                SelectTeams.ContestTeamId = teamId;
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_teams);
        initMethod();
        setAction();
        getMyAllCreatedTeam();
        selectedTeamCounter();
        sessionManager = new SessionManager(getApplicationContext());
        progressDialog = new ProgressDialog(SelectTeams.this);
        progressDialog.setTitle("Please Wait Joining Contest..");
    }

    private void LoadBalanceData() {
        common.setProgressDialog("", "Loading..", SelectTeams.this, SelectTeams.this);
        Call<ViewBalanceResponse> call = ApiClient.getInstance().getApi().getBalanceDetails(sessionManager.getUserData().getUser_id());
        call.enqueue(new Callback<ViewBalanceResponse>() {
            @Override
            public void onResponse(Call<ViewBalanceResponse> call, Response<ViewBalanceResponse> response) {
                ViewBalanceResponse viewBalanceResponse = response.body();
                if (response.isSuccessful()) {
                    String balanceData = new Gson().toJson(viewBalanceResponse.getBalance());
                    JSONArray jsonArray = null;
                    String balance = null;
                    String bouns_cash = null;
                    int netBalnace = 0;
                    try {
                        jsonArray = new JSONArray(balanceData);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            balance = jsonObject.getString("balance");
                            netBalnace = Integer.parseInt(balance);
                            bouns_cash = jsonObject.getString("add_bonus");
                        }
                        if (netBalnace >= netEntryFee) {
                            CheckBalanceData(balance, bouns_cash);
                        } else {
                            Intent intent = new Intent(SelectTeams.this, AddCash.class);
                            startActivity(intent);
                            finish();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    common.closeDialog(SelectTeams.this);
                }
            }

            @Override
            public void onFailure(Call<ViewBalanceResponse> call, Throwable t) {
            }
        });
    }

    private void CheckBalanceData(String balance, String bouns_cash) {
        final View deleteDialogView = LayoutInflater.from(SelectTeams.this).inflate(R.layout.customised_dialog_layout, null);
        TextView textView = deleteDialogView.findViewById(R.id.entryFeeAmount);
        TextView textView1 = deleteDialogView.findViewById(R.id.cash_bonus);
        TextView textView2 = deleteDialogView.findViewById(R.id.toPayfee);
        Button button = deleteDialogView.findViewById(R.id.joinContest);
        LinearLayout layout = deleteDialogView.findViewById(R.id.layout);
        deleteDialog = new AlertDialog.Builder(SelectTeams.this).create();
        textView.setText("" + Entryfee);
        textView1.setText("" + bouns_cash);
        textView2.setText("" + Entryfee);
        button.setOnClickListener(view -> {
           JoinContestData();
        });
        deleteDialog.setView(deleteDialogView);
        deleteDialog.show();
    }

    private void requestTOBalanceDeduction() {
        Call<ContestJoinResponse> call = ApiClient.getInstance().getApi().requestToJoinFee(sessionManager.getUserData().getUser_id(), contest_id, Entryfee);
        call.enqueue(new Callback<ContestJoinResponse>() {
            @Override
            public void onResponse(Call<ContestJoinResponse> call, Response<ContestJoinResponse> response) {
                ContestJoinResponse contestJoinResponse = response.body();
                if (response.isSuccessful()) {
                    String Data = new Gson().toJson(contestJoinResponse);
                    if (contestJoinResponse.getResponse().equalsIgnoreCase("successfully amount debited")) {
                    }
                }
            }
            @Override
            public void onFailure(Call<ContestJoinResponse> call, Throwable t) {
            }
        });
    }

    private void JoinContestData() {
        progressDialog.setTitle("Please Wait Joining Contest..");
        progressDialog.show();
        Log.d("Amit","Value "+contest_id);
        Log.d("Amit","Value22"+HelperData.matchId);
        Call<JoinContestsResponse> call = ApiClient.getInstance().getApi().getJoinContestResponse(sessionManager.getUserData().getUser_id(), HelperData.matchId, contest_id, SelectTeams.ContestTeamId);
        call.enqueue(new Callback<JoinContestsResponse>() {
            @Override
            public void onResponse(Call<JoinContestsResponse> call, Response<JoinContestsResponse> response) {
               JoinContestsResponse joinContestsResponse= response.body();
                if (response.isSuccessful()) {
                    Log.d("Amit","Check data"+response.body().getResponse());
                    String Data = new Gson().toJson(response.body());
                    try {
                        JSONObject jsonObject = new JSONObject(Data);
                        Log.d("Amit","Value "+jsonObject);

                        if (joinContestsResponse.getStatus().equalsIgnoreCase("true")) {
                            progressDialog.dismiss();
                            Toast.makeText(SelectTeams.this, "Contest Join Successfully..", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SelectTeams.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                            requestTOBalanceDeduction();
                        }
                        if(joinContestsResponse.getStatus().equalsIgnoreCase("false")){
                            progressDialog.dismiss();
                            if(joinContestsResponse.getResponse().equalsIgnoreCase("team_id already exist")){
                                Toast.makeText(SelectTeams.this, "This Team Already Joined..", Toast.LENGTH_SHORT).show();
                                deleteDialog.dismiss();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<JoinContestsResponse> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private void selectedTeamCounter() {
        HelperData.selectSingleTeamCounter.observe(this, integer -> team.setText("" + integer));
    }

    private void initMethod() {
        backPress = findViewById(R.id.backPress);
        recyclerView = findViewById(R.id.recyclerView);
        joinBtn = findViewById(R.id.JoinTeam);
        team = findViewById(R.id.team);
        selectAll = findViewById(R.id.selectAll);
        contest_id = getIntent().getStringExtra("Contest_id");
        Entryfee = getIntent().getStringExtra("EntryFee");
        netEntryFee = Integer.parseInt(Entryfee);
        Upto = getIntent().getStringExtra("upto");
    }

    private void setAction() {
        backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SelectTeams.ContestTeamId != null) {
                    LoadBalanceData();
                } else {
                    Toast.makeText(SelectTeams.this, "Please Select Atleast 1 Team..", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getMyAllCreatedTeam() {
        list.clear();
        Call<CreatedTeamResponse> call = ApiClient
                .getInstance()
                .getApi()
                .getUserTeamCreated(HelperData.UserId, HelperData.matchId);

        call.enqueue(new Callback<CreatedTeamResponse>() {
            @Override
            public void onResponse(Call<CreatedTeamResponse> call, Response<CreatedTeamResponse> response) {
                CreatedTeamResponse createdTeamResponse = response.body();
                if (response.isSuccessful()) {
                    String totalData = new Gson().toJson(createdTeamResponse.getResponse());

                    //----------------------for CreatedTeam(Per User)----------------------------
                    JSONArray short_squads = null;
                    JSONArray squads = null;
                    JSONArray jsonArray = null;
                    String CreatedTeamId;
                    try {
                        jsonArray = new JSONArray(totalData);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            try {
                                CreatedTeamId = jsonObject.getString("id");
                                squads = jsonObject.getJSONArray("squads");
                                short_squads = jsonObject.getJSONArray("short_squads");
                                for (int j = 0; j < short_squads.length(); j++) {
                                    try {
                                        JSONObject jsonObjectSquads = short_squads.getJSONObject(0);
                                        String TeamName = jsonObjectSquads.getString("TeamName");
                                        int allrounder = Integer.parseInt(jsonObjectSquads.getString("allrounder"));
                                        int batsman = Integer.parseInt(jsonObjectSquads.getString("batsman"));
                                        int boller = Integer.parseInt(jsonObjectSquads.getString("boller"));
                                        String captain = jsonObjectSquads.getString("captain");
                                        String match_id = jsonObjectSquads.getString("match_id");
                                        int teamAcount = Integer.parseInt(jsonObjectSquads.getString("teamAcount"));
                                        int teamBcount = Integer.parseInt(jsonObjectSquads.getString("teamBcount"));
                                        String user_id = jsonObjectSquads.getString("user_id");
                                        String vicecaptain = jsonObjectSquads.getString("vicecaptain");
                                        int wkeeper = Integer.parseInt(jsonObjectSquads.getString("wkeeper"));
                                        String teamAName = jsonObjectSquads.getString("teamAName");
                                        String teamBName = jsonObjectSquads.getString("teamBName");
                                        myAllTeamRequest myAllTeamRequest = new myAllTeamRequest(TeamName, CreatedTeamId, match_id, user_id, captain, vicecaptain, teamAName, teamBName, batsman, boller, allrounder, wkeeper, teamAcount, teamBcount, false, squads, "", "", "", "");
                                        list.add(myAllTeamRequest);
                                        selectTeamsAdapter = new SelectTeamsAdapter(SelectTeams.this, list);
                                        recyclerView.setLayoutManager(new LinearLayoutManager(SelectTeams.this));
                                        recyclerView.setAdapter(selectTeamsAdapter);
                                        selectTeamsAdapter.notifyDataSetChanged();

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                selectAll.setText("Select All (" + list.size() + ")");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<CreatedTeamResponse> call, Throwable t) {
            }
        });
    }
}