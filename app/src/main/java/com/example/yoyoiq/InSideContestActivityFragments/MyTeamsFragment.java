package com.example.yoyoiq.InSideContestActivityFragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.yoyoiq.Adapter.MyCreatedTeamAdapter;
import com.example.yoyoiq.CreatedTeamPOJO.CreatedTeamResponse;
import com.example.yoyoiq.R;
import com.example.yoyoiq.Retrofit.ApiClient;
import com.example.yoyoiq.common.HelperData;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyTeamsFragment extends Fragment {
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    ArrayList<myAllTeamRequest> list = new ArrayList();
    ArrayList<AllSelectedPlayerFromServer> listData = new ArrayList<AllSelectedPlayerFromServer>();
    String match_id;
    String matchA, matchB, logo_url_a, logo_url_b, date_start, date_end;

    public MyTeamsFragment() {
        // Required empty public constructor
    }

    public static MyTeamsFragment newInstance(String param1, String param2) {
        MyTeamsFragment fragment = new MyTeamsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    MyCreatedTeamAdapter myCreatedTeamAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my_teams, container, false);
        recyclerView = root.findViewById(R.id.myAllTeamList);
        swipeRefreshLayout = root.findViewById(R.id.swiper);
        swipeRefreshLayout.setOnRefreshListener(() -> getMyAllCreatedTeam());
        return root;
    }

    private void getMyAllCreatedTeam() {
        list.clear();
        matchA = getArguments().getString("matchA");
        matchB = getArguments().getString("matchB");
        match_id = getArguments().getString("match_id");
        logo_url_a = getArguments().getString("logo_url_a");
        logo_url_b = getArguments().getString("logo_url_b");
        date_start = getArguments().getString("date_start");
        date_end = getArguments().getString("date_end");

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

                                        myAllTeamRequest myAllTeamRequest = new myAllTeamRequest(TeamName, CreatedTeamId, match_id, user_id, captain, vicecaptain, teamAName, teamBName, batsman, boller, allrounder, wkeeper, teamAcount, teamBcount, false, squads, logo_url_a, logo_url_b, date_start, date_end);
                                        list.add(myAllTeamRequest);
                                        myCreatedTeamAdapter = new MyCreatedTeamAdapter(getContext(), list);
                                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                        recyclerView.setAdapter(myCreatedTeamAdapter);
                                        myCreatedTeamAdapter.notifyDataSetChanged();
                                        swipeRefreshLayout.setRefreshing(false);
                                    } catch (Exception e) {
                                        swipeRefreshLayout.setRefreshing(false);
                                        e.printStackTrace();
                                    }
                                }
                            } catch (Exception e) {
                                swipeRefreshLayout.setRefreshing(false);
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        swipeRefreshLayout.setRefreshing(false);
                        e.printStackTrace();
                    }
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<CreatedTeamResponse> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getMyAllCreatedTeam();
        }
    }
}