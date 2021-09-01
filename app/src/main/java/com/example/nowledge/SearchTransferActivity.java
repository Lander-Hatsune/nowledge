package com.example.nowledge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchTransferActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_transfer);

        Button searchButton = findViewById(R.id.searchButton);
        EditText searchkey = findViewById(R.id.editTextSearchKey);
        Spinner spinner = findViewById(R.id.spinner);

        List<String> courseNames = new ArrayList(Arrays.asList("语文", "数学", "英语",
                "地理", "历史", "政治",
                "物理", "化学", "生物"));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, R.layout.support_simple_spinner_dropdown_item, courseNames);
        spinner.setAdapter(adapter);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = searchkey.getText().toString();
                String course = spinner.getSelectedItem().toString();
                Log.d("selected course", course);
                Intent intentSearch = new Intent(SearchTransferActivity.this, SearchActivity.class);
                intentSearch.putExtra("key", key);
                intentSearch.putExtra("course", course);
                startActivity(intentSearch);
            }
        });
    }
}