package com.example.goalfitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.goalfitness.Fragments.HomeView;
import com.example.goalfitness.Fragments.ProfileFragment;
import com.example.goalfitness.Fragments.ProfileFragment2;
import com.example.goalfitness.Fragments.UsersView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class StartPT extends AppCompatActivity {

    BottomNavigationView bottomNavigationView2;
    Fragment fragment = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start2);

        bottomNavigationView2 = findViewById(R.id.bottom_navigation);

        bottomNavigationView2.setOnNavigationItemSelectedListener(navigationItemSelectedListener);





        Bundle intent = getIntent().getExtras();
        if (intent != null){
            String publisher = intent.getString("publisherid");

            SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
            editor.putString("profileid", publisher);
            editor.apply();

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new ProfileFragment()).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeView()).commit();
        }




    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()){
                        case R.id.nav_profile:
                            SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                            editor.putString("profileid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                            editor.apply();
                            fragment = new ProfileFragment2();
                            break;

                    }
                    if (fragment != null) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                fragment).commit();
                    }

                    return true;
                }
            };



    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_pt, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.chat_btn:
                startActivity(new Intent(this, Chat.class));
                return true;

            case R.id.uploadpost:
                startActivity(new Intent(this, uploadpost.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }





}
