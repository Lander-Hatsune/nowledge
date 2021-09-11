package com.example.nowledge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.icu.text.CaseMap;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.nowledge.data.Course;
import com.example.nowledge.data.Singleton;
import com.example.nowledge.data.Uris;
import com.example.nowledge.data.User;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.google.android.material.chip.Chip;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserAnalyzeActivity extends AppCompatActivity {

    private float quesCorrect = 0, quesFault = 0;
    private TextView questionText, courseText, corrRate, questionComment, learnComment, totalCount;
    private final String[] level = {"不及格", "及格", "良好", "优秀"};
    private final String[] nums = {"很少", "一般", "较多", "很多"};
    private int[] courseTimes;
    private int totalCourses = 0;
    private List<Integer> chipColors;

    Chip gradeChip, courseChip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_analyze);
        setTitle("用户分析");
        quesCorrect = (float) 3;
        quesFault = (float) 10;
        courseTimes = new int[9];
        totalCourses = 0;
        for (int i = 0; i < 9; ++i) {
            courseTimes[i] = i + 1;
            totalCourses += i;
        }

        questionText = findViewById(R.id.UserAnaQuestion);
        courseText = findViewById(R.id.UserAnaCourse);
        questionText.getPaint().setFakeBoldText(true);
        courseText.getPaint().setFakeBoldText(true);
        gradeChip = findViewById(R.id.gradeChip);
        courseChip = findViewById(R.id.courseChip);
        corrRate = findViewById(R.id.corrRate);
        questionComment = findViewById(R.id.gradeComment);
        learnComment = findViewById(R.id.learnComment);
        totalCount = findViewById(R.id.totalCourse);

        chipColors= new ArrayList<>();
        chipColors.add(ContextCompat.getColor(this, R.color.brown));
        chipColors.add(ContextCompat.getColor(this, R.color.orangered));
        chipColors.add(ContextCompat.getColor(this, R.color.orange));
        chipColors.add(ContextCompat.getColor(this, R.color.green));

        RequestQueue req = Singleton.getInstance(this).getRequestQueue();
        String url = Uris.getInfo() + "?username=" + User.getUsername();
        Log.d("request url", url);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("get user info", response.toString());
                try{

                    String[] courses = Course.getCourses();
                    JSONObject obj = response.getJSONObject("payload");
                    totalCourses = 0;
                    for (int i = 0; i < 9; ++i) {
                        courseTimes[i] = obj.getInt(courses[i]);
                        totalCourses += courseTimes[i];
                    }
                    quesCorrect = obj.getInt("correctQuestion");
                    int total = obj.getInt("totalQuestion");
                    quesFault = total - quesCorrect;

                    manageRadar();
                    managePie();


                } catch (JSONException e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("User analysis error", error.toString());

            }
        });
        req.add(request);
    }

    private void manageRadar() {
//        Log.d("course times", courseTimes.toString());
        RadarChart radarChart = findViewById(R.id.radarCourse);
        List<RadarEntry> radarEntries = new ArrayList<>();
        List<String> courses = Course.getCourseNames();
        for (int i = 0; i < 9; ++i) {
            Log.d("Radar " + courses.get(i), courseTimes[i] + "");
            radarEntries.add(new RadarEntry((float)courseTimes[i]));
        }
        RadarDataSet radarDataset = new RadarDataSet(radarEntries , "学科访问次数");
        radarDataset.setDrawFilled(true);
        radarDataset.setFillColor(ContextCompat.getColor(this, R.color.lightpink));
        radarDataset.setFillAlpha(51);
        radarDataset.setColor(ContextCompat.getColor(this, R.color.hotpink));
        radarDataset.setLineWidth(3);
        radarDataset.setDrawValues(false);
        radarDataset.setDrawHighlightCircleEnabled(true);
//        radarDataset.setValueTextColor(ContextCompat.getColor(this, R.color.chocolate));
//        radarDataset.setValueTextSize(13f);
        RadarData radarData = new RadarData(radarDataset);

        radarChart.getDescription().setText("");
        Legend legend = radarChart.getLegend();
        legend.setEnabled(false);


        YAxis yAxis = radarChart.getYAxis();
        yAxis.setDrawLabels(false);
        XAxis xAxis = radarChart.getXAxis();
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return courses.get((int)value % courses.size()).substring(0,1) +
                        "\n(" + courseTimes[(int)value % courses.size()] + ")";
            }
        });
        xAxis.setTextSize(13f);
        int num = 1;
        for (int j : courseTimes)
            num = num < j ? j : num;
        yAxis.setAxisMaximum((float)num);
        radarChart.setData(radarData);
        radarChart.invalidate();

        manageCourse();
    }

    private void manageCourse() {
        totalCount.setText(totalCourses + "");
        int level = getCountGrade();
        courseChip.setTextColor(chipColors.get(level));
        courseChip.setText(nums[level]);
        courseChip.getPaint().setFakeBoldText(true);
        List<String> courseNames = Course.getCourseNames();
        float average = 0.001f + totalCourses / 9.0f;
        float mmax = courseTimes[0], mmin = courseTimes[0], sigma = 0;
        int maxR = 0, minR = 8;
        for (int i = 1; i < 9; ++i) {
            if (mmax < courseTimes[i]) {
                mmax = courseTimes[i]; maxR = i;
            }
            if (mmin > courseTimes[8-i]) {
                mmin = courseTimes[8-i]; minR = 8-i;
            }
            sigma += (courseTimes[i] - average) * (courseTimes[i] - average) / 9;
        }
        sigma /= average * average;

        Log.d("sigma", sigma + "");

        String show = "到目前为止，你一共阅读了" + totalCourses + "个实体(包含重复）。你阅读次数的学科是"
                + courseNames.get(maxR) + "，" + "次数最少的学科是" + courseNames.get(minR)+ "。";
        if (totalCourses < 50)
            show += "广泛的阅读是好成绩的基础。不要偷懒！";
        else if (sigma > 0.1 | (mmax - mmin) / average > 1)
            show += "看起来你有些偏科，要注意全面发展!";
        else
            show += "目前你的学习状态不错，再接再厉！";

        learnComment.setText(show);
    }

    private void manageGrade() {
        int total = (int)(quesCorrect + quesFault);

        int corrLevel = getCorrGrade(quesCorrect / total);
        String chipText = level[corrLevel];
        gradeChip.setText(chipText);
        gradeChip.setTextColor(chipColors.get(corrLevel));
        gradeChip.getPaint().setFakeBoldText(true);
        corrRate.setText((int)(quesCorrect * 100 / total) + "%");

        String show = "到目前为止，你一共完成了" + total + "题，正确率等级为" + chipText + "。";
        if (total < 30)
            show += "书山有路勤为径，学海无涯苦作舟。要多刷题呀！";
        else if (corrLevel < 3)
            show += "细节决定成败。要在做题时注意正确率哦！";
        else
            show += "行百里者半九十。表现很棒，继续加油！";

        questionComment.setText(show);
    }

    private int getCorrGrade(float correct_rate) {
        if (correct_rate < 0.6)
            return 0;
        else if (correct_rate < 0.75)
            return 1;
        else if (correct_rate < 0.85)
            return 2;
        else return 3;
    }

    private int getCountGrade() {
        if (totalCourses < 50)
            return 0;
        else if (totalCourses < 150)
            return 1;
        else if (totalCourses < 500)
            return 2;
        else return 3;
    }

    private void managePie() {
        PieChart pieChart = findViewById(R.id.QuestionPie);
        List<PieEntry> data = new ArrayList<>();
        data.add(new PieEntry(quesCorrect, "正确"));
        data.add(new PieEntry(quesFault, "错误"));

        PieDataSet dataSet = new PieDataSet(data, "题目数量");
        List<Integer> colors = new ArrayList<>();
        colors.add(ContextCompat.getColor(this, R.color.cornflowerblue));
        colors.add(ContextCompat.getColor(this, R.color.indianred));

        dataSet.setColors(colors);
        PieData pieData = new PieData(dataSet);
        pieData.setDrawValues(true);
        pieData.setValueTextSize(16f);
        pieData.setValueTypeface(Typeface.MONOSPACE);
        pieData.setValueTextColor(ContextCompat.getColor(this, R.color.gold));


        pieChart.setData(pieData);
        pieChart.setDrawCenterText(false);
        pieChart.getDescription().setText("题目数量");
        pieChart.getDescription().setTextSize(17f);
        pieChart.setEntryLabelTextSize(14f);

        Legend legend = pieChart.getLegend();
        legend.setEnabled(false);


        pieChart.invalidate();
        manageGrade();
    }
}