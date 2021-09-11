package com.example.nowledge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nowledge.data.User;
import com.example.nowledge.sqlite.UtilData;

public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        setTitle("用户");

        TextView showUsername = findViewById(R.id.showUsername);
        showUsername.setText(User.getUsername());

        Button starlistButton = findViewById(R.id.starlistButton);
        Button historyButton = findViewById(R.id.historyButton);
        Button logoutButton = findViewById(R.id.logoutButton);
        Button clearButton = findViewById(R.id.clearCache);
        Button KnowledgeCombingButton =findViewById(R.id.KnowledgeCombingButton);
        Button QuestionRecommendationButton =findViewById(R.id.QuestionRecommendationButton);
        Button UserAnalysisButton =findViewById(R.id.UserAnalysisButton);

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
                String old_userName = User.getUsername();
                User.setUsername("0");
                User.setSpecialState(old_userName);
                Intent intentMain = new Intent(UserActivity.this, MainActivity.class);
                intentMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intentMain.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intentMain);
            }
        });

         clearButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Log.d("click!", "clear cache");
                 UtilData uData = new UtilData(getApplicationContext());
                 uData.clearData();
                 uData.getClose();
             }
         });
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentHistory = new Intent
                        (UserActivity.this, HistoryActivity.class);
                startActivity(intentHistory);
            }
        });

        KnowledgeCombingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentCB = new Intent(UserActivity.this,KnowledgeCombingActivity.class);
                startActivity(intentCB);
            }
        });

        QuestionRecommendationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentQR =new Intent(UserActivity.this,QuestionTestActivity.class);
                startActivity(intentQR);
            }
        });

        UserAnalysisButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentUserAna = new Intent(UserActivity.this, UserAnalyzeActivity.class);
                startActivity(intentUserAna);
            }
        });

    }
}