package com.example.nowledge;import androidx.appcompat.app.AppCompatActivity;import androidx.core.content.ContextCompat;import android.graphics.Color;import android.os.Bundle;import android.util.Log;import android.view.View;import android.widget.ArrayAdapter;import android.widget.Button;import android.widget.EditText;import android.widget.ListView;import android.widget.Spinner;import android.widget.TextView;import com.android.volley.Request;import com.android.volley.RequestQueue;import com.android.volley.Response;import com.android.volley.VolleyError;import com.android.volley.toolbox.JsonObjectRequest;import com.example.nowledge.data.Course;import com.example.nowledge.data.Singleton;import com.example.nowledge.data.Uris;import com.example.nowledge.data.User;import com.example.nowledge.utils.combing_child;import com.example.nowledge.utils.combing_child_adapter;import com.example.nowledge.utils.combing_super;import com.example.nowledge.utils.combing_super_adapter;import org.json.JSONArray;import org.json.JSONException;import org.json.JSONObject;import java.util.ArrayList;import java.util.List;public class KnowledgeCombingActivity extends AppCompatActivity {    private String id = User.getID();    private Boolean starred = false;    private String name;    private String course;    private EditText searchKey;    private Spinner spinner;    private Button searchButton;    private TextView searchget;    private TextView root;    private TextView rootchar;    private TextView roottitle;    private TextView nameshow;    private TextView charshow;    private TextView relationtitle;    private TextView relationsupertitle;    private TextView relationchildtitle;    private TextView supersearchget;    private TextView childsearchget;    private String[] courses;    private String info;    private List<String> super_name_list;    private List<String> child_name_list;    private ListView listcombsuper,listcombchild;    private List<combing_super> super_ralation_list;    private List<combing_child> child_relation_list;    private final int SIZE=5;    @Override    protected void onCreate(Bundle savedInstanceState) {        super.onCreate(savedInstanceState);        setContentView(R.layout.activity_knowledge_combing);        setTitle("知识梳理");        id=User.getID();        listcombsuper=findViewById(R.id.SuperCombingList);        listcombchild=findViewById(R.id.ChildCombingList);        searchButton = findViewById(R.id.CombingSearch);        searchKey = findViewById(R.id.CombingText);        root=findViewById(R.id.CombingRoot);        rootchar=findViewById(R.id.CombingRootCharacter);        searchget=findViewById(R.id.CombingSearchGet);        roottitle=findViewById(R.id.CombingRootTitle);        nameshow=findViewById(R.id.CombingRootNameShow);        charshow=findViewById(R.id.CombingRootCharShow);        relationtitle=findViewById(R.id.CombingRelationTitle);        relationsupertitle=findViewById(R.id.CombingRelationSuperTitle);        relationchildtitle=findViewById(R.id.CombingRelationChildTitle);        supersearchget=findViewById(R.id.CombingSuperSearchGet);        childsearchget=findViewById(R.id.CombingChildSearchGet);        spinner = findViewById(R.id.CombingSpinner);        List<String> courseNames = Course.getCourseNames();        courses = Course.getCourses();        ArrayAdapter<String> adapter =new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,courseNames);        spinner.setAdapter(adapter);        searchButton.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View view) {                //clear past memory                searchget.setText("");                root.setText("");                rootchar.setText("");                roottitle.setText("");                roottitle.setBackgroundColor(Color.argb(0,0,0,0));                nameshow.setText("");                nameshow.setBackgroundColor(Color.argb(0,0,0,0));                charshow.setText("");                charshow.setBackgroundColor(Color.argb(0,0,0,0));                relationtitle.setText("");                relationtitle.setBackgroundColor(Color.argb(0,0,0,0));                relationsupertitle.setText("");                relationsupertitle.setBackgroundColor(Color.argb(0,0,0,0));                relationchildtitle.setText("");                relationchildtitle.setBackgroundColor(Color.argb(0,0,0,0));                supersearchget.setText("");                childsearchget.setText("");                listcombsuper.setAdapter(new combing_super_adapter(KnowledgeCombingActivity.this,R.layout.combing_super_item,new ArrayList<>()));                listcombchild.setAdapter(new combing_child_adapter(KnowledgeCombingActivity.this,R.layout.combing_child_item,new ArrayList<>()));                name = searchKey.getText().toString();                Log.d("Combing page get entity name",name);                if(!name.equals("")){                    String courseName = spinner.getSelectedItem().toString();                    int pos = courseNames.indexOf(courseName);                    course = courses[pos];                    Log.d("Combing page select course",course);                }                String url = Uris.getDetail() + "?";                url += "name=" + name;                url += "&course=" + course;                url += "&id=" + id;                Log.d("Combing detailurl",url);                RequestQueue reqQue = Singleton.getInstance((getApplicationContext())).getRequestQueue();                JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null,                        new Response.Listener<JSONObject>() {                            @Override                            public void onResponse(JSONObject response) {                                super_name_list=new ArrayList<>();                                child_name_list=new ArrayList<>();                                super_ralation_list=new ArrayList<>();                                child_relation_list=new ArrayList<>();                                roottitle.setText("实体对象");                                roottitle.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.purple_200));                                relationtitle.setText("相关对象");                                relationtitle.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.dodgerblue));                                relationsupertitle.setText("父类对象");                                relationsupertitle.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.lightskyblue));                                relationchildtitle.setText("子类对象");                                relationchildtitle.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.lightskyblue));                                try {                                    JSONObject dataobj = response.getJSONObject("data");                                    Log.d("Combing dataobj", dataobj.toString());                                    int rightpropertycount =0 ;                                    info ="";                                    JSONArray properties = (JSONArray) dataobj.get("property");                                    for(int i=0;i<properties.length();i++){                                        if(i>10){                                            break;                                        }                                        JSONObject obj =properties.getJSONObject(i);                                        String pred = obj.getString("predicate");                                        String predicate = obj.getString("predicateLabel");                                        String object = obj.getString("object");                                        if (pred.endsWith("common#image")) {                                            continue;                                        }                                        else if (object.contains("http")) continue;                                        info+=predicate+":  "+object+"\n\n";                                        rightpropertycount++;                                    }                                    JSONArray contents = (JSONArray) dataobj.get("content");                                    boolean Jump=false;                                    int supercount=0;                                    int childcount=0;                                    for(int i=0;i<contents.length();i++){                                        JSONObject obj = contents.getJSONObject(i);                                        if(obj.has("subject_label")){                                            Jump=false;                                            String super_name = obj.getString("subject_label");                                            if(super_name.equals(name)) continue;                                            for(int j =0 ;j<super_name_list.size();++j){                                                if(super_name.equals(super_name_list.get(j)))                                                    Jump=true;                                            }                                            for(int j=0;j<child_name_list.size();++j){                                                if(super_name.equals(child_name_list.get(j)))                                                    Jump=true;                                            }                                            if(Jump) continue;                                            super_name_list.add(super_name);                                            supercount++;                                            get_super_info(0,super_name,reqQue);                                            break;                                        }                                    }                                    for(int i=0;i<contents.length();i++){                                        JSONObject obj = contents.getJSONObject(i);                                        if(obj.has("object_label")){                                            Jump=false;                                            String child_name = obj.getString("object_label");                                            if(child_name.equals(name)) continue;                                            for(int j =0 ;j<super_name_list.size();++j){                                                if(child_name.equals(super_name_list.get(j)))                                                    Jump=true;                                            }                                            for(int j=0;j<child_name_list.size();++j){                                                if(child_name.equals(child_name_list.get(j)))                                                    Jump=true;                                            }                                            if(Jump) continue;                                            child_name_list.add(child_name);                                            childcount++;                                            get_child_info(0,child_name,reqQue);                                            break;                                        }                                        else {                                            Log.e("don't have child relation","");                                        }                                    }                                    if(rightpropertycount==0&&supercount==0&&childcount==0){                                        searchget.setText("未搜索到相关实体");                                    } else{                                        if(supercount==0&&childcount==0){                                            supersearchget.setText("未搜索到关联父类");                                            childsearchget.setText("未搜索到关联子类");                                        }else if(supercount==0){                                            supersearchget.setText("未搜索到关联父类");                                        }else if(childcount==0){                                            childsearchget.setText("未搜索到关联子类");                                        }                                        nameshow.setText("实体名称:");                                        nameshow.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.bisque));                                        charshow.setText("实体属性:");                                        charshow.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.bisque));                                        root.setText(name);                                        rootchar.setText(info);                                    }                                } catch (JSONException e) {                                    e.printStackTrace();                                }                            }                        }, new Response.ErrorListener() {                    @Override                    public void onErrorResponse(VolleyError error) {                    }                });                reqQue.add(req);            }        });    }    public void get_super_info( int count,String super_name,RequestQueue reqQue){        if(count==SIZE)            return;        String url = Uris.getDetail()+"?";        url += "name=" + super_name;        url += "&course=" + course;        url += "&id=" + id;        Log.d("super "+count+" detailurl:", url);        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null,                new Response.Listener<JSONObject>() {                    @Override                    public void onResponse(JSONObject response) {                        try {                            String Count="第"+(count+1)+"级父代";                            JSONObject dataobj = response.getJSONObject("data");                            Log.d("super " + count + " dataobj", dataobj.toString());                            info = "";                            JSONArray properties = (JSONArray) dataobj.get("property");                            for (int i = 0; i < properties.length(); i++) {                                if (i > 10) {                                    break;                                }                                JSONObject obj = properties.getJSONObject(i);                                String pred = obj.getString("predicate");                                String predicate = obj.getString("predicateLabel");                                String object = obj.getString("object");                                if (pred.endsWith("common#image")) {                                    continue;                                }                                else if (object.contains("http")) continue;                                info += predicate + ":  " + object + "\n\n";                            }                            super_ralation_list.add(new combing_super(Count,super_name,info));                            JSONArray contents = (JSONArray) dataobj.get("content");                            boolean Jump = false;                            int Supercount=0;                            for (int i = 0; i < contents.length(); i++) {                                JSONObject obj = contents.getJSONObject(i);                                if (obj.has("subject_label")) {                                    Jump=false;                                    String super_name = obj.getString("subject_label");                                    if(super_name.equals(name)) continue;                                    for(int j =0 ;j<super_name_list.size();++j){                                        if(super_name.equals(super_name_list.get(j)))                                            Jump=true;                                    }                                    for(int j=0;j<child_name_list.size();++j){                                        if(super_name.equals(child_name_list.get(j)))                                            Jump=true;                                    }                                    if(Jump) continue;                                    Supercount++;                                    get_super_info(count + 1, super_name, reqQue);                                    break;                                }                            }                            if(Supercount==0){                                Log.e("don't have super " + (count + 1)+" relation", "");                                combing_super_adapter adapterCS = new combing_super_adapter(KnowledgeCombingActivity.this,R.layout.combing_super_item,super_ralation_list);                                listcombsuper.setAdapter(adapterCS);                            }                        } catch (JSONException e) {                            e.printStackTrace();                        }                    }                }, new Response.ErrorListener() {            @Override            public void onErrorResponse(VolleyError error) {            }        });        reqQue.add(req);    }    public void get_child_info( int count,String child_name,RequestQueue reqQue){        if(count==SIZE)            return;        String url = Uris.getDetail()+"?";        url += "name=" + child_name;        url += "&course=" + course;        url += "&id=" + id;        Log.d("child "+count+" detailurl:", url);        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null,                new Response.Listener<JSONObject>() {                    @Override                    public void onResponse(JSONObject response) {                        try {                            String Count = "第"+(count+1)+"级子类";                            JSONObject dataobj = response.getJSONObject("data");                            Log.d("child " + count + " dataobj", dataobj.toString());                            info = "";                            JSONArray properties = (JSONArray) dataobj.get("property");                            for (int i = 0; i < properties.length(); i++) {                                if (i > 10) {                                    break;                                }                                JSONObject obj = properties.getJSONObject(i);                                String pred = obj.getString("predicate");                                String predicate = obj.getString("predicateLabel");                                String object = obj.getString("object");                                if (pred.endsWith("common#image")) {                                    continue;                                }                                else if (object.contains("http")) continue;                                info += predicate + ":  " + object + "\n\n";                            }                            child_relation_list.add(new combing_child(Count,child_name,info));                            JSONArray contents = (JSONArray) dataobj.get("content");                            boolean Jump=false;                            int Childcount=0;                            for (int i = 0; i < contents.length(); i++) {                                JSONObject obj = contents.getJSONObject(i);                                if (obj.has("object_label")) {                                    Jump=false;                                    String object_name = obj.getString("object_label");                                    if(object_name.equals(name)) continue;                                    for(int j =0 ;j<super_name_list.size();++j){                                        if(object_name.equals(super_name_list.get(j)))                                            Jump=true;                                    }                                    for(int j=0;j<child_name_list.size();++j){                                        if(object_name.equals(child_name_list.get(j)))                                            Jump=true;                                    }                                    if(Jump) continue;                                    Childcount++;                                    get_child_info(count + 1, child_name, reqQue);                                    break;                                }                            }                            if(Childcount==0){                                Log.e("don't have child "+(count+1)+" relation","");                                combing_child_adapter adapterCC = new combing_child_adapter(KnowledgeCombingActivity.this,R.layout.combing_child_item,child_relation_list);                                listcombchild.setAdapter(adapterCC);                            }                        } catch (JSONException e) {                            e.printStackTrace();                        }                    }                }, new Response.ErrorListener() {            @Override            public void onErrorResponse(VolleyError error) {            }        });        reqQue.add(req);    }    protected void updateId() {        new Thread(new Runnable() {            @Override            public void run() {                RequestQueue reqQue = Singleton.getInstance                        (getApplicationContext()).getRequestQueue();                JSONObject obj = null;                try {                    obj = new JSONObject();                    obj.put("username", "0");                    obj.put("password", "0");                } catch (JSONException e) {                    Log.e("UpdateId error:", e.toString());                }                Log.d("UpdateId obj", obj.toString());                JsonObjectRequest req = new JsonObjectRequest                        (Request.Method.POST, Uris.getLogin(),                                obj, new Response.Listener<JSONObject>() {                            @Override                            public void onResponse(JSONObject response) {                                Log.i("Login request success.", "");                                String msg = "Unknown Error";                                String code = "";                                try {                                    msg = response.getString("msg");                                    code = response.getString("id");                                } catch (JSONException e) {                                    Log.e("Login request msg/id error", e.toString());                                }                                if (!(code.equals("-1") || code.equals("-2"))) {                                    Log.d("logged in, id", code);                                    id = code;                                    User.setID(id);                                }                            }                        }, new Response.ErrorListener() {                            @Override                            public void onErrorResponse(VolleyError error) {                                Log.e("Login error:", error.toString());                            }                        });                Log.d("Request:", req.toString());                reqQue.add(req);            }        }).start();    }}