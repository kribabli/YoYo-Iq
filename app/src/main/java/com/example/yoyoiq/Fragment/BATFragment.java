package com.example.yoyoiq.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yoyoiq.Adapter.SquadsBAdapter;
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

public class BATFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ArrayList<SquadsA> list = new ArrayList<>();
    RecyclerView recyclerView;
    String matchA, matchB;

    private String mParam1;
    private String mParam2;

    public BATFragment() {
        // Required empty public constructor
    }

    public static BATFragment newInstance(String param1, String param2) {
        BATFragment fragment = new BATFragment();
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

    SquadsBAdapter squadsBAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_b_a_t, container, false);
        recyclerView = root.findViewById(R.id.recyclerView);
        return root;
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

                    //----------------------for TeamA----------------------------
                    JSONObject jsonObjectTeamA = null;
                    try {
                        jsonObjectTeamA = new JSONObject(jsonArray);
                        for (int i = 0; i < jsonObjectTeamA.length(); i++) {
                            String team_id = jsonObjectTeamA.getString("team_id");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    JSONArray jsonArrayA = null;
                    try {
                        jsonArrayA = new JSONArray(SquadsA);
                        for (int i = 0; i < jsonArrayA.length(); i++) {
                            JSONObject jsonObject = jsonArrayA.getJSONObject(i);
                            String player_id = jsonObject.getString("player_id");
                            String role = jsonObject.getString("role");
                            String substitute = jsonObject.getString("substitute");
                            String role_str = jsonObject.getString("role_str");
                            String playing11 = jsonObject.getString("playing11");
                            String name = jsonObject.getString("name");

                            if (role.equals("bat")) {
                                com.example.yoyoiq.Model.SquadsA squadsA = new SquadsA(player_id, role, substitute, role_str, playing11, name, matchA);
                                list.add(squadsA);

                                squadsBAdapter = new SquadsBAdapter(getContext(), list);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                recyclerView.setAdapter(squadsBAdapter);
                                squadsBAdapter.notifyDataSetChanged();
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
                            String team_id = jsonObjectTeamB.getString("team_id");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    JSONArray jsonArrayB = null;
                    try {
                        jsonArrayB = new JSONArray(SquadsB);
                        for (int i = 0; i < jsonArrayB.length(); i++) {
                            JSONObject jsonObject = jsonArrayB.getJSONObject(i);
                            String player_id = jsonObject.getString("player_id");
                            String role = jsonObject.getString("role");
                            String substitute = jsonObject.getString("substitute");
                            String role_str = jsonObject.getString("role_str");
                            String playing11 = jsonObject.getString("playing11");
                            String name = jsonObject.getString("name");

                            if (role.equals("bat")) {
                                SquadsA squadsA = new SquadsA(player_id, role, substitute, role_str, playing11, name, matchB);
                                list.add(squadsA);

                                squadsBAdapter = new SquadsBAdapter(getContext(), list);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                recyclerView.setAdapter(squadsBAdapter);
                                squadsBAdapter.notifyDataSetChanged();
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