package com.example.nowledge.sqlite;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONException;
import com.example.nowledge.UserActivity;

import java.util.ArrayList;
import java.util.List;


public class UtilData {
    private MyDBHelper du;
    private SQLiteDatabase db;
    private Context context;

    public UtilData(Context context){
        du = new MyDBHelper(context);
        db = du.getWritableDatabase();
        this.context = context;
    }

    private final String tableName = du.TABLE_NAME;


    /**
     * 添加数据
     * */
    public void addData(String name, String course, String properties,
                        String subject_content, String object_content){
        String[] args = {name,course};
        Cursor cursor = db.rawQuery("select properties,subject_content,object_content from " + tableName
                +"  where name=? and course=?", args);

        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("course", course);
        contentValues.put("properties", properties);
        contentValues.put("subject_content", subject_content);
        contentValues.put("object_content", object_content);
        Log.d("sql add content", contentValues.toString());
        if (cursor.moveToNext()) {
            Toast.makeText(context, "更新数据到数据库", Toast.LENGTH_SHORT).show();
            db.update(tableName, contentValues, "name=? and course=?", args);
        } else {
            db.insert(tableName,null,contentValues);
            Toast.makeText(context, "缓存数据到数据库", Toast.LENGTH_SHORT).show();
            Log.d("sqlite3", "addData to sql");
        }

        contentValues.clear();


    }


    /**
     * 删除数据
     * */
    public void clearData() {
        db.execSQL("delete from " + tableName);
        Toast.makeText(context, "清空缓存成功", Toast.LENGTH_SHORT).show();
    }



    /**
     * 查询数据
     * */
    public String[] inquireData(String name, String course){
        String[] res= {"", "", "", ""};
        String[] args = {name, course};
        Cursor cursor = db.rawQuery("select properties,subject_content,object_content from " + tableName
                +"  where name=? and course=?", args);
        if (cursor.moveToNext()) {
            Log.d("sqlite3", "get data successfully");
            for (int i = 0; i < 3; ++i) {
                res[i] = cursor.getString(i);
            }
            res[3] = "get";
            Log.d("sqlite3 properties", res[0]);
            Log.d("sqlite3 sub", res[1]);
            Log.d("sqlite3 obj", res[2]);
        } else {
            Log.d("sqlite3", "don't get data");
        }

        return res;
    }


    /**
     * 关闭数据库连接
     * */
    public void getClose(){
        if(db != null){
            db.close();
        }
    }
}