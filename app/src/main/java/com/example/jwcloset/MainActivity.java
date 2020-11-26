package com.example.jwcloset;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.jwcloset.Fragment.ChatFragment;
import com.example.jwcloset.Fragment.MarketFragment;
import com.example.jwcloset.Fragment.ProfileFragment;
import com.example.jwcloset.Fragment.TradeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    FragmentManager fm = getSupportFragmentManager();
    MarketFragment marketFragment = new MarketFragment();
    ProfileFragment profileFragment = new ProfileFragment();
    TradeFragment tradeFragment = new TradeFragment();
    ChatFragment chatFragment = new ChatFragment();

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.frameLayout, tradeFragment).commitAllowingStateLoss();

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());

    }

    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener{

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction transaction = fm.beginTransaction();
            switch (item.getItemId()){
                case R.id.tradeTab:
                    transaction.replace(R.id.frameLayout, tradeFragment).commitAllowingStateLoss();
                    break;
                case R.id.marketTab:
                    transaction.replace(R.id.frameLayout, marketFragment).commitAllowingStateLoss();
                    break;
                case R.id.chatTab:
                    transaction.replace(R.id.frameLayout, chatFragment).commitAllowingStateLoss();
                    break;
                case R.id.profileTab:
                    transaction.replace(R.id.frameLayout, profileFragment).commitAllowingStateLoss();
                    break;
            }
            return true;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
    }
}