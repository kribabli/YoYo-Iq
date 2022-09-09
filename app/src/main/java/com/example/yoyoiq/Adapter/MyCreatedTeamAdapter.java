package com.example.yoyoiq.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yoyoiq.CreateTeamActivity;
import com.example.yoyoiq.InSideContestActivityFragments.AllSelectedPlayerFromServer;
import com.example.yoyoiq.InSideContestActivityFragments.myAllTeamRequest;
import com.example.yoyoiq.OnlyTeamPreView.MyTeamPreview;
import com.example.yoyoiq.R;
import com.example.yoyoiq.common.HelperData;
import com.example.yoyoiq.common.SessionManager;

import java.util.ArrayList;

public class MyCreatedTeamAdapter extends RecyclerView.Adapter<MyCreatedTeamAdapter.MyViewHolder> {
    Context context;
    ArrayList<myAllTeamRequest> list;
    ArrayList<AllSelectedPlayerFromServer> listdata;
    SessionManager sessionManager;

    public MyCreatedTeamAdapter(Context context, ArrayList<myAllTeamRequest> list) {
        this.context = context;
        this.list = list;
        sessionManager = new SessionManager(context.getApplicationContext());
    }

    @NonNull
    @Override
    public MyCreatedTeamAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_created_team_list, parent, false);
        return new MyCreatedTeamAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyCreatedTeamAdapter.MyViewHolder holder, int position) {
        myAllTeamRequest allTeamRequest = list.get(position);
        holder.setIsRecyclable(false);
        if (list.size() > 0) {
            holder.CName.setText(allTeamRequest.getCaptain());
            holder.VCname.setText(allTeamRequest.getVicecaptain());
            holder.wkTv.setText(String.valueOf(allTeamRequest.getWkeeper()));
            holder.batTv.setText(String.valueOf(allTeamRequest.getBatsman()));
            holder.arTv.setText(String.valueOf(allTeamRequest.getAllrounder()));
            holder.bowlTv.setText(String.valueOf(allTeamRequest.getBoller()));
            holder.teamACount.setText(String.valueOf(allTeamRequest.getTeamAcount()));
            holder.teamBCount.setText(String.valueOf(allTeamRequest.getTeamBcount()));
            holder.userNameAndTid.setText(sessionManager.getUserData().getUserName() + "(T" + (position + 1) + ")");
            holder.teamA.setText(allTeamRequest.getTeamAName());
            holder.teamB.setText(allTeamRequest.getTeamBName());

            holder.linearLayout1.setOnClickListener(view -> {
                Intent intent = new Intent(context, MyTeamPreview.class);
                intent.putExtra("position", position);
                intent.putExtra("listdata", list.get(position).getSquads().toString());
                intent.putExtra("selectedPosition", position);
                intent.putExtra("TeamName", sessionManager.getUserData().getUserName() + "(T" + (position + 1) + ")");
                HelperData.team1NameShort = list.get(position).getTeamAName();
                HelperData.team2NameShort = list.get(position).getTeamBName();
                context.startActivity(intent);
            });

            holder.editImg.setOnClickListener(view -> {
                Intent intent = new Intent(context, CreateTeamActivity.class);
                intent.putExtra("match_id", list.get(position).getMatch_id());
                intent.putExtra("matchA", list.get(position).getTeamAName());
                intent.putExtra("matchB", list.get(position).getTeamBName());
                intent.putExtra("logo_url_a", list.get(position).getLogo_url_a());
                intent.putExtra("logo_url_b", list.get(position).getLogo_url_b());
                intent.putExtra("date_start", list.get(position).getDate_start());
                intent.putExtra("date_end", list.get(position).getDate_end());
                intent.putExtra("listdata", list.get(position).getSquads().toString());
                intent.putExtra("CreatedTeamId", list.get(position).getTeamId());
                context.startActivity(intent);
            });

            holder.duplicate.setOnClickListener(view -> {
                Intent intent = new Intent(context, CreateTeamActivity.class);
                intent.putExtra("match_id", list.get(position).getMatch_id());
                intent.putExtra("matchA", list.get(position).getTeamAName());
                intent.putExtra("matchB", list.get(position).getTeamBName());
                intent.putExtra("logo_url_a", list.get(position).getLogo_url_a());
                intent.putExtra("logo_url_b", list.get(position).getLogo_url_b());
                intent.putExtra("date_start", list.get(position).getDate_start());
                intent.putExtra("date_end", list.get(position).getDate_end());
                intent.putExtra("listdata", list.get(position).getSquads().toString());
                intent.putExtra("CreatedTeamId", list.get(position).getTeamId());
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView teamA, teamB, wkTv, batTv, arTv, bowlTv, CName, VCname, teamACount, teamBCount, userNameAndTid, editImg, duplicate;
        CardView cardView1;
        LinearLayout linearLayout1;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            teamA = itemView.findViewById(R.id.teamA);
            teamB = itemView.findViewById(R.id.teamB);
            wkTv = itemView.findViewById(R.id.wkTv);
            batTv = itemView.findViewById(R.id.batTv);
            arTv = itemView.findViewById(R.id.arTv);
            bowlTv = itemView.findViewById(R.id.bowlTv);
            CName = itemView.findViewById(R.id.CName);
            VCname = itemView.findViewById(R.id.VCname);
            teamACount = itemView.findViewById(R.id.teamACount);
            teamBCount = itemView.findViewById(R.id.teamBCount);
            userNameAndTid = itemView.findViewById(R.id.userNameAndTid);
            cardView1 = itemView.findViewById(R.id.cardView1);
            editImg = itemView.findViewById(R.id.editImg);
            duplicate = itemView.findViewById(R.id.duplicate);
            linearLayout1 = itemView.findViewById(R.id.linearLayout1);
        }
    }
}
