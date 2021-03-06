package com.example.nowledge;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.nowledge.data.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.nowledge.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("!@", "oncreate");
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_link, R.id.navigation_robot)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionLogin:
                if (!User.isLoggedin()) {
                    Intent intentLogin = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intentLogin);
                } else {
                    Log.d("Main activity", "goto user page");
                    Intent intentUser = new Intent(MainActivity.this, UserActivity.class);
                    startActivity(intentUser);
                }
                return true;
            case R.id.actionSearch:
                Intent intentSearch = new Intent(MainActivity.this, SearchTransferActivity.class);
                startActivity(intentSearch);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("!@", "onstart!");

        String specialState = User.getSpecialState();
        Log.d("specialState", specialState);
        SharedPreferences sp = getSharedPreferences("data", Context.MODE_PRIVATE);
        String username = sp.getString("username", "0");
        if (specialState.equals("")) {
            if (!username.equals("0")) {
                Boolean state = sp.getBoolean(username, false);
                User.setUsername(username);
                User.setLoggedin(state);
                Log.d("Home set User " + username, state.toString());
            }
        } else {

            SharedPreferences.Editor editor = sp.edit();
            Boolean putState = specialState.equals("1") ? true : false;
            if (!putState){
                editor.putBoolean(specialState, false);
                Log.d("Homefragment LOGOUT" , specialState);
            }

            else {
                username = User.getUsername();
                editor.putString("username", username);
                editor.putBoolean(username, true);
                Log.d("Homefragment LOGin" , username);
            }

            User.setSpecialState("");
            editor.apply();

        }
    }



}