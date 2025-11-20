package com.example.householdaccountbook.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.householdaccountbook.db.MyDbManager;
import com.example.householdaccountbook.R;
import com.example.householdaccountbook.fragments.chart.BaseChartFragment;
import com.example.householdaccountbook.fragments.main.ChartMotherFragment;
import com.example.householdaccountbook.fragments.main.InputMotherFragment;
import com.example.householdaccountbook.fragments.main.SettingsMenuFragment;
import com.example.householdaccountbook.fragments.main.TransactionDataListFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    private ImageView toolbarIcon;
    private TextView toolbarText;
    private BottomNavigationView navView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        this.toolbarIcon = findViewById(R.id.toolbar_icon);
        this.toolbarText = findViewById(R.id.toolbar_text);
        this.navView = findViewById(R.id.bottom_nav_view);

        setNavViewEvent();


        replaceFragment(new InputMotherFragment());
        this.navView.setSelectedItemId(R.id.navigation_edit);
    }

    private void setNavViewEvent(){

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
                    toolbarIcon.setBackground(item.getIcon());
                    toolbarText.setText(item.getTitle());
                    return true;
                }
                else if (item.getItemId() == R.id.navigation_data) {
//                    replaceFragment(new CalendarFragment());
                    replaceFragment(new TransactionDataListFragment());
                    toolbarIcon.setBackground(item.getIcon());
                    toolbarText.setText(item.getTitle());
                    return true;
                }
                else if (item.getItemId() == R.id.navigation_chart) {
                    replaceFragment(new ChartMotherFragment());
//                    dateTextView.setText("Data");
                    toolbarIcon.setBackground(item.getIcon());
                    toolbarText.setText(item.getTitle());
                    return true;
                }
                else if (item.getItemId() == R.id.navigation_settings) {
                    replaceFragment(new SettingsMenuFragment());
//                    dateTextView.setText("Settings");
                    toolbarIcon.setBackground(item.getIcon());
                    toolbarText.setText(item.getTitle());
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
    public void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.commit();
    }
}