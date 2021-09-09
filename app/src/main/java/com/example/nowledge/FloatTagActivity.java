package com.example.nowledge;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.nowledge.data.Course;
import com.zhl.channeltagview.bean.ChannelItem;
import com.zhl.channeltagview.bean.GroupItem;
import com.zhl.channeltagview.listener.UserActionListener;
import com.zhl.channeltagview.view.ChannelTagView;

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
        ArrayList<GroupItem> GroupItemList = new ArrayList<>(Arrays.asList(groupedUnAddedItem));


        Log.d("float tag all prepared", "");
        ctv.initChannels(addedItem, GroupItemList,
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
            public void onMoved(int i, int i1, ArrayList<ChannelItem> arrayList) {

            }

            @Override
            public void onSwiped(int i, View view, ArrayList<ChannelItem> arrayList, ArrayList<ChannelItem> arrayList1) {
                List<ChannelItem> added = ctv.getAddedChannels();

                List<String> addedNames = new ArrayList<>();
                for (ChannelItem item: added) {
                    addedNames.add(item.title);
                }
                Log.d("float tag", addedNames.toString());
                Course.setSelectedCourses(addedNames);
            }
        });
    }
}