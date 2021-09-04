package com.example.nowledge.ui.robot;

import java.util.*;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nowledge.R;

class Message {
    private String content;
    private boolean ifUser;

    public Message(String $content, boolean if_user) {
        this.content = $content;
        this.ifUser = if_user;
    }
    public String getContent() { return content; }
    public boolean ifUser() { return ifUser; }
}

public class RobotMessage extends RecyclerView.Adapter<RobotMessage.ViewHolder> {
    private List<Message> list;
    public RobotMessage(List<Message> list) {
        this.list = list;
    }
    static class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout robotLayout, humanLayout;
        TextView robot_msg, human_msg;

        public ViewHolder(View view) {
            super(view);

            robotLayout = view.findViewById(R.id.robot_layout);
            robot_msg = view.findViewById(R.id.robot_msg);
            humanLayout = view.findViewById(R.id.human_layout);
            human_msg = view.findViewById(R.id.human_msg);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.robot_message_item, parent, false);
        view.setFocusable(false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message msg = list.get(position);
        if (!msg.ifUser()) {
            holder.robotLayout.setVisibility(View.VISIBLE);
            holder.robot_msg.setText(msg.getContent());
            holder.humanLayout.setVisibility(View.GONE);
        } else {
            holder.humanLayout.setVisibility(View.VISIBLE);
            holder.human_msg.setText(msg.getContent());
            holder.robotLayout.setVisibility(View.GONE);
        }
    }
    public int getItemCount() { return list.size(); }
}
