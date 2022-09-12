package com.example.yoyoiq;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.yoyoiq.PrivacyPolicy.AboutUsActivity;
import com.example.yoyoiq.WalletPackage.AddCash;
import com.example.yoyoiq.common.HelperData;
import com.example.yoyoiq.common.SessionManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    TextView textView, notification, profileView;
    SessionManager sessionManager;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sessionManager = new SessionManager(getApplicationContext());
        fragmentManager = getSupportFragmentManager();

        textView = findViewById(R.id.walletTV);
        notification = findViewById(R.id.notification);
        HomeFragment homeFragment = new HomeFragment();
        loadFragment(homeFragment, fragmentManager);

        textView.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddCash.class);
            startActivity(intent);
        });

        notification.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
            startActivity(intent);
        });

        profileView = findViewById(R.id.profileView);
        new Thread(this::mBottomNavigationBar).start();

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.NavigationView);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.start, R.string.close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        setAction();
        navigationView.setNavigationItemSelectedListener(this);
        googleSignIn();
    }

    private void googleSignIn() {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (googleSignInAccount != null) {
            String userName = googleSignInAccount.getDisplayName();
            String userEmail = googleSignInAccount.getEmail();
            Uri photoUrl = googleSignInAccount.getPhotoUrl();
            String id = googleSignInAccount.getId();
            Log.d("TAG", "onCreate: " + id + "  " + userName + "  " + userEmail + "  " + photoUrl);
        }
    }

    private void setAction() {
        profileView.setOnClickListener(v -> {
            if (!drawerLayout.isDrawerOpen(GravityCompat.START))
                drawerLayout.openDrawer(GravityCompat.START);
            else drawerLayout.closeDrawer(GravityCompat.END);
        });
    }

    private void loadFragment(Fragment f1, FragmentManager fm) {
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, f1);
        ft.commit();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    private void mBottomNavigationBar() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                boolean bool = false;
                if (bottomNavigationView.getSelectedItemId() != item.getItemId()) {
                    switch (item.getItemId()) {
                        case R.id.home:
                            bool = true;
                            HomeFragment homeFragment = new HomeFragment();
                            loadFragment(homeFragment, fragmentManager);
                            break;
                        case R.id.my_matches:
                            bool = true;
                            MyMatchesFragment myMatchesFragment = new MyMatchesFragment();
                            loadFragment(myMatchesFragment, fragmentManager);
                            break;
                        case R.id.winners:
                            bool = true;
                            WinnersFragment winnersFragment = new WinnersFragment();
                            loadFragment(winnersFragment, fragmentManager);
                            break;
                    }
                }
                return bool;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            finish();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile:
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.wallet_item:
                Intent intent3 = new Intent(MainActivity.this, AddCash.class);
                startActivity(intent3);
                break;
            case R.id.search:
                Intent intent2 = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent2);
                break;
            case R.id.howToPlay:
                Intent intent6 = new Intent(MainActivity.this, HowToPlay.class);
                startActivity(intent6);
                break;
            case R.id.infoSettings:
                Intent intent1 = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent1);
                break;
            case R.id.aboutUs:
                Intent intent5 = new Intent(MainActivity.this, AboutUsActivity.class);
                startActivity(intent5);
                break;
            case R.id.termsAndConditions:
                Intent intent4 = new Intent(MainActivity.this, AboutUsActivity.class);
                startActivity(intent4);
                break;
            case R.id.referAndEarn:
                Intent intent7 = new Intent(MainActivity.this, InviteFriends.class);
                startActivity(intent7);
                break;
            case R.id.logOut:
                userLogout();
                break;
        }
        return true;
    }

    private void userLogout() {
        gsc.signOut();
        sessionManager.logout();
        HelperData.UserId = "";
        HelperData.UserName = "";
        HelperData.Usermobile = "";
        HelperData.UserEmail = "";
        Intent intent = new Intent(MainActivity.this, FrontActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        Toast.makeText(this, "Logout Successfully", Toast.LENGTH_SHORT).show();
    }
}