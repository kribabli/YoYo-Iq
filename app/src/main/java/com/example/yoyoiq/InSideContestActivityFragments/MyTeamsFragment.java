package com.example.yoyoiq.InSideContestActivityFragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
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

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMyAllCreatedTeam();
            }
        });
        return root;
    }

    private void getMyAllCreatedTeam() {
        Call<CreatedTeamResponse> call = ApiClient
                .getInstance()
                .getApi()
                .getUserTeamCreated("71", HelperData.matchId);

        call.enqueue(new Callback<CreatedTeamResponse>() {
            @Override
            public void onResponse(Call<CreatedTeamResponse> call, Response<CreatedTeamResponse> response) {
                CreatedTeamResponse createdTeamResponse = response.body();
                if (response.isSuccessful()) {
                    String totalData = new Gson().toJson(createdTeamResponse.getResponse());

                    Log.d("TAG", "onResponse: " + totalData);
                    //----------------------for CreatedTeam(Per User)----------------------------
                    JSONArray short_squads = null;
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = new JSONArray(totalData);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            try {
                                short_squads = jsonObject.getJSONArray("short_squads");
                                Log.d("TAG", "onResponse1: " + short_squads);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        Log.d("TAG", "onResponse2: " + short_squads);
                        for (int j = 0; j < short_squads.length(); j++) {
                            try {
                                JSONObject jsonObjectSquads = short_squads.getJSONObject(0);
                                Log.d("TAG", "onResponse3: " + jsonObjectSquads);
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

//                                CreatedTeamPOJOClass createdTeamPOJOClass = new CreatedTeamPOJOClass(added, country, fantasy_player_rating, isCap, isVcap, matchId, pid, playing_role, points, title);
//                                list.add(createdTeamPOJOClass);

//                                myCreatedTeamAdapter = new MyCreatedTeamAdapter(getContext(), list);
//                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//                                recyclerView.setAdapter(myCreatedTeamAdapter);
//                                myCreatedTeamAdapter.notifyDataSetChanged();
//                                swipeRefreshLayout.setRefreshing(false);

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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getMyAllCreatedTeam();
        }
    }
}