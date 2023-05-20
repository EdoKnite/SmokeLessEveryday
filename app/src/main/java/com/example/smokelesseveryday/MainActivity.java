package com.example.smokelesseveryday;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;



import com.example.smokelesseveryday.fragments.HealthFragment;
import com.example.smokelesseveryday.fragments.ProgressFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

    private NavController navController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        BottomNavigationView bottom_navigation = findViewById(R.id.bottom_navigation);

        bottom_navigation.setSelectedItemId(R.id.progress_nav_item);
        bottom_navigation.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.health_nav_item){
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new HealthFragment())
                        .commit();

                return true;
            }

            else if (item.getItemId() == R.id.progress_nav_item) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new ProgressFragment())
                        .commit();

                return true;

            }

            return false;
        });

        configureToolbar();

    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);

        navController = Navigation.findNavController(this, R.id.fragment_container);

    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_profile) {
            Intent intent = new Intent(this, ProfileActivity.class);
            this.startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void configureToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle(R.string.app_name);
        }

        AppCompatImageButton backBtn = findViewById(R.id.back_btn_tool_bar);
        backBtn.setVisibility(View.GONE);
    }
}