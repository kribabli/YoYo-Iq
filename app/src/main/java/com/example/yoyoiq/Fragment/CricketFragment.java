package com.example.yoyoiq.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.example.yoyoiq.Adapter.AllMatchAdapter;
import com.example.yoyoiq.Adapter.BannerAdapter;
import com.example.yoyoiq.BannerPOJO.Banner;
import com.example.yoyoiq.Model.The_Slide_Items_Model_Class;
import com.example.yoyoiq.Model.TotalHomeData;
import com.example.yoyoiq.R;
import com.example.yoyoiq.Retrofit.ApiClient;
import com.example.yoyoiq.UpcommingReq.UpcommingResponse;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CricketFragment extends Fragment {
    ViewPager view_bannerItem;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<TotalHomeData> list = new ArrayList<>();
    ArrayList<The_Slide_Items_Model_Class> listItems = new ArrayList<>();
    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 1000;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000;

    public CricketFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getAllMatches();
        setAutoSliderBanner();
        if (getArguments() != null) {
        }
    }

    public static CricketFragment getInstance() {
        return new CricketFragment();
    }

    BannerAdapter bannerAdapter;
    AllMatchAdapter allMatchAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_cricket, container, false);
        view_bannerItem = root.findViewById(R.id.view_bannerItem);
        swipeRefreshLayout = root.findViewById(R.id.swiper);
        recyclerView = root.findViewById(R.id.recyclerViewMatchList);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllMatches();
            }
        });
        return root;
    }

    private void setAutoSliderBanner() {
        Call<Banner> call = ApiClient.getInstance().getApi().getBanner();
        call.enqueue(new Callback<Banner>() {
            @Override
            public void onResponse(Call<Banner> call, Response<Banner> response) {
                Banner banner = response.body();
                if (response.isSuccessful()) {
                    String bannerData = new Gson().toJson(banner.getResponse());
                    try {
                        JSONArray jsonArray = new JSONArray(bannerData);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String id = jsonObject.getString("id");
                            String image = jsonObject.getString("image");
                            The_Slide_Items_Model_Class the_slide_items_model_class = new The_Slide_Items_Model_Class(image);
                            listItems.add(the_slide_items_model_class);
                            bannerAdapter = new BannerAdapter(getContext(), listItems);
                            view_bannerItem.setAdapter(bannerAdapter);
                            bannerAdapter.notifyDataSetChanged();
                        }
                        final Handler handler = new Handler();
                        final Runnable Update = () -> {
                            if (currentPage == jsonArray.length()) {
                                currentPage = 0;
                            }
                            view_bannerItem.setCurrentItem(currentPage++, true);
                        };
                        timer = new Timer(); // This will create a new Thread
                        timer.schedule(new TimerTask() { // task to be scheduled
                            @Override
                            public void run() {
                                handler.post(Update);
                            }
                        }, DELAY_MS, PERIOD_MS);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<Banner> call, Throwable t) {

            }
        });
    }

    private void getAllMatches() {
        list.clear();
        Call<UpcommingResponse> call = ApiClient
                .getInstance()
                .getApi()
                .getMatch();

        call.enqueue(new Callback<UpcommingResponse>() {
            @Override
            public void onResponse(Call<UpcommingResponse> call, Response<UpcommingResponse> response) {
                UpcommingResponse status = response.body();
                if (response.isSuccessful()) {
                    String jsonArray = new Gson().toJson(status.getResponse().getItems());
                    JSONArray jsonArray1 = null;
                    try {
                        jsonArray1 = new JSONArray(jsonArray);
                        for (int i = 0; i < jsonArray1.length(); i++) {
                            JSONObject jsonObject = jsonArray1.getJSONObject(i);
                            String title = jsonObject.getString("title");
                            String match_id = jsonObject.getString("match_id");
                            String date_start = jsonObject.getString("date_start_ist");
                            String date_end = jsonObject.getString("date_end_ist");

                            JSONArray teama1 = jsonObject.getJSONArray("teama");
                            JSONArray teamb1 = jsonObject.getJSONArray("teamb");

                            JSONObject jsonObject11 = teama1.getJSONObject(0);
                            JSONObject jsonObject22 = teamb1.getJSONObject(0);

                            String logo_url_a = jsonObject11.getString("logo_url");
                            String name_a = jsonObject11.getString("name");
                            String short_name_a = jsonObject11.getString("short_name");
                            int teamIda = Integer.parseInt(jsonObject11.getString("team_id"));

                            String logo_url_b = jsonObject22.getString("logo_url");
                            String name_b = jsonObject22.getString("name");
                            String short_name_b = jsonObject22.getString("short_name");
                            int teamIdb = Integer.parseInt(jsonObject22.getString("team_id"));

                            TotalHomeData totalHomeData = new TotalHomeData(title, match_id, logo_url_a, name_a, short_name_a, logo_url_b, name_b, short_name_b, date_start, date_end);
                            list.add(totalHomeData);

                            allMatchAdapter = new AllMatchAdapter(getContext(), list);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            recyclerView.setAdapter(allMatchAdapter);
                            allMatchAdapter.notifyDataSetChanged();
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    } catch (JSONException e) {
                        swipeRefreshLayout.setRefreshing(false);
                        e.printStackTrace();
                    }

                } else {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
            @Override
            public void onFailure(Call<UpcommingResponse> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}