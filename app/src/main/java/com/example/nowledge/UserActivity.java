package com.example.nowledge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.nowledge.data.User;

public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        setTitle("用户");

        TextView showUsername = findViewById(R.id.showUsername);
        showUsername.setText(User.getUsername());

        Button starlistButton = findViewById(R.id.starlistButton);
        Button logoutButton = findViewById(R.id.logoutButton);

        starlistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentStarlist = new Intent
                        (UserActivity.this, StarlistActivity.class);
                startActivity(intentStarlist);
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User.setLoggedin(false);
                User.setUsername("0");
                onBackPressed();
            }
        });
    }
}