package com.crime.cout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.crime.cout.Fragments.CrimeFragment;
import com.crime.cout.Fragments.ProfileFragment;
import com.crime.cout.Fragments.CrimeListFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.bottom_nav);
        navView.setOnNavigationItemSelectedListener(this);
        LoadFragment(new CrimeFragment());


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==R.id.bottom_crime){
            LoadFragment(new CrimeFragment());
        }else if(item.getItemId()==R.id.bottom_profile) {
            LoadFragment(new ProfileFragment());
        }else if(item.getItemId()==R.id.bottom_setting) {
            LoadFragment(new CrimeListFragment());
        }
        return true;
    }
    private void LoadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_continer, fragment)
                .commit();
    }
}