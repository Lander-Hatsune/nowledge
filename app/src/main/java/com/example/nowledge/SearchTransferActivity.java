package com.example.nowledge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.nowledge.data.Course;
import com.example.nowledge.data.User;

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

        List<String> courseNames = Course.getCourseNames();
        String[] courses = Course.getCourses();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, R.layout.support_simple_spinner_dropdown_item, courseNames);
        spinner.setAdapter(adapter);

        ListView hisListView = findViewById(R.id.historyListView);
        List<String> historyStr = new ArrayList<>();
        if (User.getHistory() != null) {
            for (Pair<String, String> his : User.getHistory()) {
                historyStr.add(his.second);
            }
            ArrayAdapter<String> hisAdaptor = new ArrayAdapter<>
                    (this, R.layout.entity_short_item, historyStr);
            hisListView.setAdapter(hisAdaptor);
            hisListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String course = User.getHistory().get(i).first;
                    String name = User.getHistory().get(i).second;
                    Log.d("selected course", course);
                    Intent intentSearch = new Intent(SearchTransferActivity.this, SearchActivity.class);
                    intentSearch.putExtra("key", name);
                    intentSearch.putExtra("course", course);
                    startActivity(intentSearch);
                }
            });
        }

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = searchkey.getText().toString();
                if (!key.equals("")) {
                    String courseName = spinner.getSelectedItem().toString();
                    int pos = courseNames.indexOf(courseName);
                    String course = courses[pos];
                    User.addHistory(course, key);
                    Log.d("selected course", course);
                    Intent intentSearch = new Intent(SearchTransferActivity.this, SearchActivity.class);
                    intentSearch.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intentSearch.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intentSearch.putExtra("key", key);
                    intentSearch.putExtra("course", course);
                    searchkey.setText("");
                    startActivity(intentSearch);
                }
            }
        });
    }
}