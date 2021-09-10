package com.example.nowledge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.example.nowledge.data.Course;
import com.zhl.channeltagview.bean.ChannelItem;
import com.zhl.channeltagview.bean.GroupItem;
import com.zhl.channeltagview.listener.OnChannelItemClicklistener;
import com.zhl.channeltagview.listener.UserActionListener;
import com.zhl.channeltagview.view.ChannelTagView;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bingoogolapple.badgeview.BGABadgeTextView;

public class FloatTagActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_float_tag);

        ChannelTagView ctv = findViewById(R.id.channel_tag_view);

        TextView text_top = ctv.findViewById(R.id.categray_added_title);
        TextView text_bottom = ctv.findViewById(R.id.categray_unadded_title);
        text_top.setText("我的学科");
        text_bottom.setText("未选择学科");
        text_top.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        text_bottom.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        text_bottom.getPaint().setFakeBoldText(true);
        text_top.getPaint().setFakeBoldText(true);
        Drawable color = ContextCompat.getDrawable(this, R.color.purple_500);
        text_top.setBackground(color);
        text_bottom.setBackground(color);

        RecyclerView v_top = findViewById(R.id.added_channel_recyclerview),
                v_bottom = findViewById(R.id.unAdded_channel_recyclerview);
        v_top.setMinimumHeight(300);
        v_bottom.setMinimumHeight(300);
        v_top.setVerticalScrollBarEnabled(false);
        v_bottom.setVerticalScrollBarEnabled(false);


        ArrayList<ChannelItem> addedItem = new ArrayList<>();
        ArrayList<ChannelItem> unAddedItem = new ArrayList<>();

        List<String> addedName = Course.getSelectedCourseNames();
        List<String> unAddedName = Course.getUnSelectedCourseNames();

        Log.d("added name", addedName.toString());
        Log.d("unadded name", unAddedName.toString());

        for (String name: addedName) {
            ChannelItem item = new ChannelItem();
            item.id = Course.getCourseIDByName(name);
            item.title = name;
            addedItem.add(item);
        }

        for (String name: unAddedName) {
            ChannelItem item = new ChannelItem();
            item.id = Course.getCourseIDByName(name);
            item.title = name;
            unAddedItem.add(item);
        }

        GroupItem groupedUnAddedItem = new GroupItem();
        groupedUnAddedItem.setChannelItems(unAddedItem);
        ArrayList<GroupItem> unAddedChannel = new ArrayList<>();
        unAddedChannel.add(groupedUnAddedItem);


        Log.d("float tag all prepared", "");
        ctv.initChannels(addedItem, unAddedChannel,
                false, new ChannelTagView.RedDotRemainderListener() {
                    @Override
                    public boolean showAddedChannelBadge(BGABadgeTextView bgaBadgeTextView, int i) {
                        return false;
                    }

                    @Override
                    public boolean showUnAddedChannelBadge(BGABadgeTextView bgaBadgeTextView, int i) {
                        return false;
                    }

                    @Override
                    public void handleAddedChannelReddot(BGABadgeTextView bgaBadgeTextView, int i) {

                    }

                    @Override
                    public void handleUnAddedChannelReddot(BGABadgeTextView bgaBadgeTextView, int i) {

                    }

                    @Override
                    public void OnDragDismiss(BGABadgeTextView bgaBadgeTextView, int i) {

                    }
                });
        ctv.setUserActionListener(new UserActionListener() {
            @Override
            public void onMoved(int i, int i1, ArrayList<ChannelItem> arrayList) { ;

            }

            @Override
            public void onSwiped(int i, View view, ArrayList<ChannelItem> arrayList, ArrayList<ChannelItem> arrayList1) {
                List<ChannelItem> added = ctv.getAddedChannels();

                List<String> addedNames = new ArrayList<>();
                for (ChannelItem item: added) {
                    addedNames.add(item.title);
                }
                Log.d("float tag", addedNames.toString());
                ArrayList<String> res = new ArrayList<>(), res1 = new ArrayList<>();
                for (int ii = 0; ii < arrayList.size(); ++ii) {
                    res.add(arrayList.get(ii).title);
                }
                for (int j = 0; j < arrayList1.size(); ++j) {
                    res1.add(arrayList1.get(j).title);
                }
                Log.d("swipe array", res.toString());
                Log.d("swipe array1", res1.toString());
                Course.setSelectedCourses(addedNames);
            }
        });


        ctv.setOnChannelItemClicklistener(new OnChannelItemClicklistener() {
            @Override
            public void onAddedChannelItemClick(View view, int i) {
                Log.d("click added",String.valueOf(i));
            }

            @Override
            public void onUnAddedChannelItemClick(View view, int i) {
                Log.d("click tag",String.valueOf(i));
            }

            @Override
            public void onItemDrawableClickListener(View view, int i) {
                Log.d("click drawable",String.valueOf(i));
            }
        });


    }
}