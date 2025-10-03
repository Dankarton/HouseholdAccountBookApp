package com.example.householdaccountbook.fragments;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.householdaccountbook.MyDbManager;
import com.example.householdaccountbook.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        MyDbManager.setOpenHelper(getApplicationContext());
        MyDbManager.ensureDefaultPayments();
        setContentView(R.layout.activity_main);
        setNavViewEvent();
        replaceFragment(new InputMotherFragment());
    }

    private void setNavViewEvent(){
        BottomNavigationView navView = findViewById(R.id.bottom_nav_view);
        navView.setOnItemReselectedListener(new NavigationBarView.OnItemReselectedListener(){
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                // Do nothing.
            }
        });
        navView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.navigation_edit){
                    replaceFragment(new InputMotherFragment());
                    return true;
                }
                else if (item.getItemId() == R.id.navigation_calendar) {
//                    replaceFragment(new CalendarFragment());
                    replaceFragment(new TransactionDataListFragment());
                    return true;
                }
                else if (item.getItemId() == R.id.navigation_data) {
                    replaceFragment(new ChartFragment());
//                    dateTextView.setText("Data");
                    return true;
                }
                else if (item.getItemId() == R.id.navigation_settings) {
                    replaceFragment(new SettingsFragment());
//                    dateTextView.setText("Settings");
                    return true;
                }
                else{
                    InputMotherFragment fragment = new InputMotherFragment();
                    replaceFragment(fragment);
                }
                return false;
            }
        });
    }
    private void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.commit();
    }
}