package com.example.yoyoiq.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yoyoiq.Adapter.SquadsAAdapter;
import com.example.yoyoiq.Model.SquadsA;
import com.example.yoyoiq.PlayerPOJO.ResponsePlayer;
import com.example.yoyoiq.R;
import com.example.yoyoiq.Retrofit.ApiClient;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WKFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ArrayList<SquadsA> list = new ArrayList<>();
    RecyclerView recyclerView;
    String team_idA, team_idB, matchA, matchB;
    ArrayList listPlayerIdA = new ArrayList();
    ArrayList listPlayerIdB = new ArrayList();
    String fantasy_player_rating, pid, playing_role;
    ArrayList list1 = new ArrayList();

    private String mParam1;
    private String mParam2;

    public WKFragment() {
        // Required empty public constructor
    }

    public static WKFragment newInstance(String param1, String param2) {
        WKFragment fragment = new WKFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getAllPlayer();

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    SquadsAAdapter squadsAAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_w_k, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        return view;
    }

    private void getAllPlayer() {
        matchA = getArguments().getString("matchA");
        matchB = getArguments().getString("matchB");

        Call<ResponsePlayer> call = ApiClient
                .getInstance()
                .getApi()
                .getMatchPlaying11(getArguments().getString("match_id"));

        call.enqueue(new Callback<ResponsePlayer>() {
            @Override
            public void onResponse(Call<ResponsePlayer> call, Response<ResponsePlayer> response) {
                ResponsePlayer responsePlayer = response.body();
                if (response.isSuccessful()) {
                    String jsonArray = new Gson().toJson(responsePlayer.getResponsePlay().getTeama());
                    String SquadsA = new Gson().toJson(responsePlayer.getResponsePlay().getTeama().getSquads());

                    String jsonArray1 = new Gson().toJson(responsePlayer.getResponsePlay().getTeamb());
                    String SquadsB = new Gson().toJson(responsePlayer.getResponsePlay().getTeamb().getSquads());

                    String jsonArray2 = new Gson().toJson(responsePlayer.getResponsePlay().getTeams());
                    String jsonArray3 = new Gson().toJson(responsePlayer.getResponsePlay().getPlayers());

                    Log.d("TAG", "onResponse1: " + jsonArray);
                    Log.d("TAG", "onResponse2: " + jsonArray1);
                    Log.d("TAG", "onResponse3: " + jsonArray2);
                    Log.d("TAG", "onResponse4: " + jsonArray3);


                    String role;
                    String substitute;
                    String role_str;
                    String playing11;
                    String name;
                    //----------------------for TeamA----------------------------
                    JSONObject jsonObjectTeamA = null;
                    try {
                        jsonObjectTeamA = new JSONObject(jsonArray);
                        for (int i = 0; i < jsonObjectTeamA.length(); i++) {
                            team_idA = jsonObjectTeamA.getString("team_id");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    JSONArray jsonArrayA = null;
                    try {
                        jsonArrayA = new JSONArray(SquadsA);
                        for (int i = 0; i < jsonArrayA.length(); i++) {
                            JSONObject jsonObject = jsonArrayA.getJSONObject(i);
//                            String player_id = jsonObject.getString("player_id");
                            role = jsonObject.getString("role");
                            substitute = jsonObject.getString("substitute");
                            role_str = jsonObject.getString("role_str");
                            playing11 = jsonObject.getString("playing11");
                            name = jsonObject.getString("name");

                            if (role.equals("wk")) {
                                String player_id = jsonObject.getString("player_id");
                                listPlayerIdA.add(player_id);
                                SquadsA squadsA = new SquadsA(player_id, role, substitute, role_str, playing11, name, matchA, fantasy_player_rating);
                                list.add(squadsA);

                                squadsAAdapter = new SquadsAAdapter(getContext(), list);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                recyclerView.setAdapter(squadsAAdapter);
                                squadsAAdapter.notifyDataSetChanged();
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    //----------------------for TeamB----------------------------
                    JSONObject jsonObjectTeamB = null;
                    try {
                        jsonObjectTeamB = new JSONObject(jsonArray1);
                        for (int i = 0; i < jsonObjectTeamB.length(); i++) {
                            team_idB = jsonObjectTeamB.getString("team_id");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    JSONArray jsonArrayB = null;
                    try {
                        jsonArrayB = new JSONArray(SquadsB);
                        for (int i = 0; i < jsonArrayB.length(); i++) {
                            JSONObject jsonObject = jsonArrayB.getJSONObject(i);
//                            String player_id = jsonObject.getString("player_id");
                            role = jsonObject.getString("role");
                            substitute = jsonObject.getString("substitute");
                            role_str = jsonObject.getString("role_str");
                            playing11 = jsonObject.getString("playing11");
                            name = jsonObject.getString("name");
                            if (role.equals("wk")) {
                                String player_id = jsonObject.getString("player_id");
                                listPlayerIdB.add(player_id);

                                SquadsA squadsA = new SquadsA(player_id, role, substitute, role_str, playing11, name, matchB, fantasy_player_rating);
                                list.add(squadsA);

                                squadsAAdapter = new SquadsAAdapter(getContext(), list);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                recyclerView.setAdapter(squadsAAdapter);
                                squadsAAdapter.notifyDataSetChanged();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    //----------------------for TeamS----------------------------
                    JSONArray jsonArrayS = null;
                    try {
                        jsonArrayS = new JSONArray(jsonArray2);
                        for (int i = 0; i < jsonArrayS.length(); i++) {
                            JSONObject jsonObject = jsonArrayS.getJSONObject(i);
                            String tid = jsonObject.getString("tid");
                            if (tid.equals(team_idA) || tid.equals(team_idB)) {
                                String country = jsonObject.getString("country");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    JSONObject jsonObjectPlayers = null;
                    //----------------------for Players----------------------------
                    JSONArray jsonArrayPlayers = null;
                    try {
                        jsonArrayPlayers = new JSONArray(jsonArray3);
                        for (int i = 0; i < jsonArrayPlayers.length(); i++) {
                            jsonObjectPlayers = jsonArrayPlayers.getJSONObject(i);
                            pid = jsonObjectPlayers.getString("pid");
                            playing_role = jsonObjectPlayers.getString("playing_role");
                            if ((listPlayerIdA.contains(pid) || listPlayerIdB.contains(pid))) {
                                fantasy_player_rating = jsonObjectPlayers.getString("fantasy_player_rating");

                                Log.d("TAG", "onResponsedds: " + fantasy_player_rating);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                }
            }

            @Override
            public void onFailure(Call<ResponsePlayer> call, Throwable t) {
            }
        });

    }
}