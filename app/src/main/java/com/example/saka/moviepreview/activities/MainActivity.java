package com.example.saka.moviepreview.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.saka.moviepreview.R;
import com.example.saka.moviepreview.fragments.MovieFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        NavigationView.OnNavigationItemSelectedListener {


    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Drawer and Navigation view
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_drawer);
        navigationView.setNavigationItemSelectedListener(this);

        // Action bar toggle
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // Navigation View Header
        View navigationHeaderView = navigationView.getHeaderView(0);
        navigationHeaderView.findViewById(R.id.nav_header_img).setOnClickListener(this);
        navigationHeaderView.findViewById(R.id.nav_header_txt).setOnClickListener(this);

        movieClick();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.navigation_movie:
                movieClick();
                break;
            case R.id.navigation_edit_profile:
                startActivity(new Intent(getApplicationContext(), EditProfileActivity.class));
                break;
        }
        drawerLayout.closeDrawers();
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_notification) {
            Log.d("app", item.getTitle().toString() + " click");
        }
        return super.onOptionsItemSelected(item);
    }


    private void movieClick() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.layout_main, new MovieFragment()).commit();
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.nav_header_img) {
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        }
    }


}
