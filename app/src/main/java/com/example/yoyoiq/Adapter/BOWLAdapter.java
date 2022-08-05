package com.example.yoyoiq.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yoyoiq.CreateTeamActivity;
import com.example.yoyoiq.Model.AllSelectedPlayer;
import com.example.yoyoiq.Model.SquadsA;
import com.example.yoyoiq.R;
import com.example.yoyoiq.common.HelperData;

import java.util.ArrayList;
import java.util.Collections;

public class BOWLAdapter extends RecyclerView.Adapter<BOWLAdapter.MyViewHolder> {
    Context context;
    ArrayList<SquadsA> list;
    boolean isEnable = false;
    private int lastSelectedPosition = -1;

    public BOWLAdapter(Context context, ArrayList<SquadsA> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public BOWLAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.selected_players, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BOWLAdapter.MyViewHolder holder, int position) {
        SquadsA listData = list.get(position);
        String fullPlayerName = listData.getName();
        holder.playerName.setText(listData.getShort_namePlayers());
        holder.playerCredit.setText(listData.getFantasy_player_rating());
        holder.country.setText(listData.getAbbr());

        holder.cardViewSelected.setOnClickListener(view -> {
            if(CreateTeamActivity.addedPlayerIds.contains("_" + list.get(position).getPidPlayers() + "_")){
                //removed minus sign
                String credit = listData.getFantasy_player_rating();
                double playerCredit= Double.parseDouble(credit);
                int pid= Integer.parseInt(listData.getPidPlayers());
                Log.d("Amit","Value 1111 "+pid);
                holder.im_AddPlayer.setImageResource(R.drawable.plus_icon);
                CreateTeamActivity.addedPlayerIds = CreateTeamActivity.addedPlayerIds.replace("_" + list.get(position).getPidPlayers() + "_\n", "");
                HelperData.bowl.setValue(HelperData.bowl.getValue() - 1);
                HelperData.creditCounter.setValue(Double.valueOf(HelperData.creditCounter.getValue() + playerCredit ));
                HelperData.playerCounter.setValue(HelperData.playerCounter.getValue() - 1);
                if(HelperData.team1NameShort==listData.getAbbr()){
                    HelperData.conty1.setValue(HelperData.conty1.getValue()-1);}
                else if(HelperData.team2NameShort==listData.getAbbr()){
                    HelperData.conty2.setValue(HelperData.conty2.getValue()-1);}
                holder.cardViewSelected.setBackgroundColor(Color.WHITE);
                HelperData.allSelectedPlayer.getValue().remove(listData);
                int index = HelperData.myTeamList.size() - 1;
                HelperData.myTeamList.remove(index);

            }
            else {
                if (HelperData.playerCounter.getValue() < HelperData.limit) {
                    if (HelperData.creditCounter.getValue() >= Double.valueOf(listData.getFantasy_player_rating())) {
                        CreateTeamActivity.addedPlayerIds = CreateTeamActivity.addedPlayerIds + "_" + listData.getPidPlayers() + "_\n";
                        if (HelperData.team1NameShort == listData.getAbbr()) {
                            if (HelperData.conty1.getValue() < 7) {
                                HelperData.conty1.setValue(HelperData.conty1.getValue() + 1);
                            } else {
                                Toast.makeText(context, "Please select player from another Country", Toast.LENGTH_SHORT).show();
                            }
                        } else if (HelperData.team2NameShort == listData.getAbbr()) {
                            if (HelperData.conty2.getValue() <7) {
                                HelperData.conty2.setValue(HelperData.conty2.getValue() + 1);
                            } else {
                                Toast.makeText(context, "Please select player from another Country", Toast.LENGTH_SHORT).show();
                            }
                        }
                        if (HelperData.bowl.getValue()<5) {
                            Log.d("Amit","Value 11 "+HelperData.wk.getValue());
                            HelperData.bowl.setValue(HelperData.wk.getValue()+1);
                            HelperData.creditCounter.setValue(HelperData.creditCounter.getValue() - Double.valueOf(listData.getFantasy_player_rating()));
                            HelperData.playerCounter.setValue(HelperData.playerCounter.getValue()+1);
                            holder.im_AddPlayer.setImageResource(R.drawable.minus_icon);
                            holder.cardViewSelected.setBackgroundColor(Color.LTGRAY);
                            AllSelectedPlayer allSelectedPlayer= new AllSelectedPlayer(Integer.valueOf(listData.getPidPlayers()), listData.getShort_namePlayers(), listData.getAbbr(), "BOWL", Double.valueOf(listData.getFantasy_player_rating()), false, false, false, "");
                            HelperData.allSelectedPlayer.setValue(Collections.singletonList(allSelectedPlayer));
                            HelperData.myTeamList.add(allSelectedPlayer);
                        }
                        else{
                            Toast.makeText(context, "Please add player from another tab", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(context, "Not enough credits left", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(context, HelperData.limit+"Player Added", Toast.LENGTH_SHORT).show();
                }
            }



        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView playerName, country, playerCredit;
        ImageView playerImg,alreadyAddedPlayer, im_AddPlayer;
        CardView cardViewSelected;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            playerName = itemView.findViewById(R.id.playerName);
            country = itemView.findViewById(R.id.country);
            playerCredit = itemView.findViewById(R.id.tv_PlayerCredit);
            playerImg = itemView.findViewById(R.id.playerImg);
            im_AddPlayer = itemView.findViewById(R.id.im_AddPlayer);
            cardViewSelected = itemView.findViewById(R.id.cardViewSelected);
            alreadyAddedPlayer = itemView.findViewById(R.id.alreadyAddedPlayer);
        }
    }
}
