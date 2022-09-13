package com.example.yoyoiq.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.yoyoiq.InSideScoreActivity.ScoresActivity;
import com.example.yoyoiq.Model.TotalHomeData;
import com.example.yoyoiq.R;
import com.example.yoyoiq.common.HelperData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class LiveMatchListAdapter extends RecyclerView.Adapter<LiveMatchListAdapter.MyViewHolder> {
    Context context;
    ArrayList<TotalHomeData> list;
    private String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private Handler handler = new Handler();
    private Runnable runnable;

    public LiveMatchListAdapter(Context context, ArrayList<TotalHomeData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public LiveMatchListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.joincontest_matches_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LiveMatchListAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        TotalHomeData listData = list.get(position);
        holder.setIsRecyclable(false);

        if (list.size() > 0) {
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ScoresActivity.class);
                    intent.putExtra("shortNameA", listData.getShort_name_a());
                    intent.putExtra("shortNameB", listData.getShort_name_b());
                    intent.putExtra("match_id", listData.getMatch_id());
                    intent.putExtra("logo_url_a", listData.getLogo_url_a());
                    intent.putExtra("logo_url_b", listData.getLogo_url_b());
                    intent.putExtra("date_start", listData.getDate_start());
                    intent.putExtra("date_end", listData.getDate_end());
                    HelperData.matchId = listData.getMatch_id();
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                }
            });
            holder.textViewTitle.setText(list.get(position).getTitle());

            holder.matchATv.setText(listData.getName_a());
            holder.shortNameA.setText(listData.getShort_name_a());
            Glide.with(context)
                    .load(listData.getLogo_url_a())
                    .into(holder.matchAImage);

            holder.matchBTv.setText(listData.getName_b());
            holder.shortNameB.setText(listData.getShort_name_b());
            Glide.with(context)
                    .load(listData.getLogo_url_b())
                    .into(holder.matchBImage);

            runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        handler.postDelayed(this, 1000);
                        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
                        Date event_date = dateFormat.parse(listData.getDate_start());
                        Date current_date = new Date();
                        if (!current_date.after(event_date)) {
                            long diff = event_date.getTime() - current_date.getTime();
                            long Days = diff / (24 * 60 * 60 * 1000);
                            long Hours = diff / (60 * 60 * 1000) % 24;
                            long Minutes = diff / (60 * 1000) % 60;
                            long Seconds = diff / 1000 % 60;
                            holder.leftTime.setText(String.format("%02d", Days) + " " + String.format("%02d", Hours) + "h " + String.format("%02d", Minutes) + "m " + String.format("%02d", Seconds) + "s ");
                        } else {
                            holder.leftTime.setVisibility(View.GONE);
                            holder.done.setVisibility(View.VISIBLE);
                            holder.done.setText("Live");
                            handler.removeCallbacks(runnable);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            handler.postDelayed(runnable, 0);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, matchATv, matchBTv, shortNameA, shortNameB, leftTime, done, lineUp;
        ImageView matchAImage, matchBImage;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.matchTitle);
            lineUp = itemView.findViewById(R.id.lineUp);
            matchATv = itemView.findViewById(R.id.matcha);
            matchBTv = itemView.findViewById(R.id.matchb);
            shortNameA = itemView.findViewById(R.id.shortNameA);
            shortNameB = itemView.findViewById(R.id.shortNameB);
            leftTime = itemView.findViewById(R.id.leftTime);
            done = itemView.findViewById(R.id.done);
            matchAImage = itemView.findViewById(R.id.matchaImage);
            matchBImage = itemView.findViewById(R.id.matchbImage);
            cardView = itemView.findViewById(R.id.cardViewMatchList);
        }
    }
}