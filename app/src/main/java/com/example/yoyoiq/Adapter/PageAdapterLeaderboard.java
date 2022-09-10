package com.example.yoyoiq.Adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.yoyoiq.InSideAddCashLeaderboard.LiveLeaderboardFragment;
import com.example.yoyoiq.InSideAddCashLeaderboard.WinningFragment;

public class PageAdapterLeaderboard extends FragmentPagerAdapter {
    int tabCount;
    String price_contribution = "";
    String match_id = "";
    String contest_id = "";
    String matchA = "";
    String matchB = "";

    public PageAdapterLeaderboard(@NonNull FragmentManager fm, int behavior, String price_contribution, String match_id, String contest_id, String matchA, String matchB) {
        super(fm, behavior);
        tabCount = behavior;
        this.price_contribution = price_contribution;
        this.match_id = match_id;
        this.contest_id = contest_id;
        this.matchA = matchA;
        this.matchB = matchB;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Bundle bundle = new Bundle();
                bundle.putString("price_contribution", price_contribution);
                WinningFragment winningFragment = new WinningFragment();
                winningFragment.setArguments(bundle);
                return winningFragment;
            case 1:
                Bundle bundle1 = new Bundle();
                bundle1.putString("match_id", match_id);
                bundle1.putString("contestId", contest_id);
                bundle1.putString("matchA", matchA);
                bundle1.putString("matchB", matchB);
                LiveLeaderboardFragment liveLeaderboardFragment = new LiveLeaderboardFragment();
                liveLeaderboardFragment.setArguments(bundle1);
                return liveLeaderboardFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}