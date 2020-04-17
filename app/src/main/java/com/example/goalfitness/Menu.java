package com.example.goalfitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        BottomNavigationView bottomnaviga = findViewById(R.id.bottom_menu);
        bottomnaviga.setOnNavigationItemSelectedListener(navigaListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new Map()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigaListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selected = null;

            switch (item.getItemId()){
                case R.id.home_btn:
                    selected = new Map();
                    break;

                case R.id.chat_btn:
                    selected = new chat();
                    break;

                case R.id.profile_btn:
                    selected = new profile();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,selected).commit();

            return true;

        }
    };
}
