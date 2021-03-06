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
import java.util.List;

public class SearchTransferActivity extends AppCompatActivity {

    private List<String> historyStr;
    private Button searchButton;
    private EditText searchkey;
    private ListView hisListView;
    private String[] courses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_transfer);

        searchButton = findViewById(R.id.searchButton);
        searchkey = findViewById(R.id.editTextSearchKey);

        Spinner spinner = findViewById(R.id.spinner);

        List<String> courseNames = Course.getCourseNames();
        courses = Course.getCourses();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, R.layout.support_simple_spinner_dropdown_item, courseNames);
        spinner.setAdapter(adapter);

        hisListView = findViewById(R.id.historyListView);


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = searchkey.getText().toString();
                if (!key.equals("")) {
                    String courseName = spinner.getSelectedItem().toString();
                    int pos = courseNames.indexOf(courseName);
                    String course = courses[pos];
                    User.addSearchHistory(course, key);
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

        updateHistory();
    }

    private void updateHistory() {
        historyStr = new ArrayList<>();
        if (User.getSearchHistory() != null) {
            for (Pair<String, String> his : User.getSearchHistory()) {
                historyStr.add(his.second);
            }
            ArrayAdapter<String> hisAdaptor = new ArrayAdapter<>
                    (this, R.layout.entity_short_item, historyStr);
            hisListView.setAdapter(hisAdaptor);
            hisListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String course = User.getSearchHistory().get(i).first;
                    String name = User.getSearchHistory().get(i).second;
                    Log.d("selected course", course);
                    Intent intentSearch = new Intent(SearchTransferActivity.this, SearchActivity.class);
                    intentSearch.putExtra("key", name);
                    intentSearch.putExtra("course", course);
                    startActivity(intentSearch);
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateHistory();
    }

}