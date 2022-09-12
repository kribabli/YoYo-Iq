package com.example.yoyoiq;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class FrontActivity extends AppCompatActivity {
    TextView register, enterCode, login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front);
        initMethod();
        setAction();
    }

    private void initMethod() {
        register = findViewById(R.id.register);
        enterCode = findViewById(R.id.enterCode);
        login = findViewById(R.id.login);
    }

    private void setAction() {
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FrontActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FrontActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        enterCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FrontActivity.this, MyCoupons.class);
                startActivity(intent);
            }
        });
    }
}