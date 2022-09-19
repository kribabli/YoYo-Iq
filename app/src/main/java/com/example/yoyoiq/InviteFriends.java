package com.example.yoyoiq;

import androidx.appcompat.app.AppCompatActivity;
import androidx.multidex.BuildConfig;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.yoyoiq.common.HelperData;

public class InviteFriends extends AppCompatActivity {
    TextView backPress, INVITE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friends);
        initMethod();
        setAction();
    }

    private void initMethod() {
        backPress = findViewById(R.id.backPress);
        INVITE = findViewById(R.id.INVITE);
    }

    private void setAction() {
        backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        INVITE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                referAndEarn();
            }
        });
    }

    //ReferAndEarn Share-----------------------------------------
    private void referAndEarn() {
        INVITE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "YoYoIQ");
                    String shareMessage = "\nLet me recommend you this application\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "";
                    shareMessage = shareMessage + "\n1.Use my invite code " + HelperData.referral_code + "\n2.Get discounts\n";
                    shareMessage = shareMessage + "\nLet the games begin!";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}