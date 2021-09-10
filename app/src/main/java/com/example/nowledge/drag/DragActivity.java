package com.example.nowledge.drag;


import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.example.nowledge.R;
import com.example.nowledge.data.Course;

import java.util.ArrayList;
import java.util.List;


/**
 * 频道 增删改查 排序
 * Created by YoKeyword on 15/12/29.
 */
public class DragActivity extends AppCompatActivity {

    private RecyclerView mRecy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag);
        mRecy = (RecyclerView) findViewById(R.id.recy);
        init();
    }

    private void init() {
        final List<ChannelEntity> items = new ArrayList<>();
        List<String> selected$courseNames = Course.getSelectedCourseNames();
        for (String i : selected$courseNames) {
            ChannelEntity entity = new ChannelEntity();
            entity.setName(i);
            items.add(entity);
        }
        final List<ChannelEntity> otherItems = new ArrayList<>();
        List<String> unSelected$courseNames = Course.getUnSelectedCourseNames();
        for (String i : unSelected$courseNames) {
            ChannelEntity entity = new ChannelEntity();
            entity.setName(i);
            otherItems.add(entity);
        }

        GridLayoutManager manager = new GridLayoutManager(this, 4);
        mRecy.setLayoutManager(manager);

        ItemDragHelperCallback callback = new ItemDragHelperCallback();
        final ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecy);

        final ChannelAdapter adapter = new ChannelAdapter(this, helper, items, otherItems);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int viewType = adapter.getItemViewType(position);
                return viewType == ChannelAdapter.TYPE_MY || viewType == ChannelAdapter.TYPE_OTHER ? 1 : 4;
            }
        });
        mRecy.setAdapter(adapter);

        adapter.setOnMyChannelItemClickListener(new ChannelAdapter.OnMyChannelItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Toast.makeText(DragActivity.this, items.get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });

        Button rtn_button = findViewById(R.id.dragButtonBack);
        rtn_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
