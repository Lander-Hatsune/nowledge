package com.example.nowledge;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.hardware.lights.LightState;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.nowledge.data.Course;
import com.example.nowledge.data.Singleton;
import com.example.nowledge.data.Uris;
import com.example.nowledge.data.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class KnowledgeCombingActivity extends AppCompatActivity {

    private String id = User.getID();
    private Boolean starred = false;
    private String name;
    private String course;
    private EditText searchKey;
    private Spinner spinner;
    private Button searchButton;
    private ListView comListview;
    private String[] courses;
    private String info;
    private String fathername;
    private String childname;
    private List<String> info_list;
    private final int SIZE=5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowledge_combing);

        id=User.getID();
        searchButton = findViewById(R.id.CombingSearch);
        searchKey = findViewById(R.id.CombingText);

        spinner = findViewById(R.id.CombingSpinner);

        List<String> courseNames = Course.getCourseNames();
        courses = Course.getCourses();

        ArrayAdapter<String> adapter =new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,courseNames);
        spinner.setAdapter(adapter);

        comListview = findViewById(R.id.CombingList);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = searchKey.getText().toString();
                Log.d("Combing page get entity name",name);
                if(!name.equals("")){
                    String courseName = spinner.getSelectedItem().toString();
                    int pos = courseNames.indexOf(courseName);
                    course = courses[pos];
                    Log.d("Combing page select course",course);
                }
                String url = Uris.getDetail() + "?";
                url += "name=" + name;
                url += "&course=" + course;
                url += "&id=" + id;

                Log.d("Combing detailurl",url);

                RequestQueue reqQue = Singleton.getInstance((getApplicationContext())).getRequestQueue();

                JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                info_list=new ArrayList<>();
                                try {
                                    JSONObject dataobj = response.getJSONObject("data");
                                    Log.d("Combing dataobj", dataobj.toString());
                                    info="name\n";
                                    JSONArray properties = (JSONArray) dataobj.get("property");
                                    for(int i=0;i<properties.length();i++){
                                        if(i>SIZE){
                                            break;
                                        }
                                        JSONObject obj =properties.getJSONObject(i);
                                        String predicate = obj.getString("predicateLabel");
                                        String object = obj.getString("object");
                                        info+=predicate+":"+object+"\n";
                                        info_list.add(info);
                                    }

//                                    JSONArray contents = (JSONArray) dataobj.get("content");
//                                    for(int i=0,j=0;i<contents.length();i++){
//                                        if(j>SIZE){
//                                            break;
//                                        }
//                                        JSONObject
//                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
            }
        });

    }
}