package com.example.nowledge.ui.robot;

import java.util.*;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nowledge.R;
import com.example.nowledge.databinding.FragmentRobotBinding;

public class RobotFragment extends Fragment {

    private RobotViewModel robotViewModel;
    private FragmentRobotBinding binding;
    private List<Message> msg_list = new ArrayList<> ();
    private RecyclerView msgRecyclerView;
    private EditText inputText;
    private Button send;
    private LinearLayoutManager layoutManager;
    private RobotMessage adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        robotViewModel =
                new ViewModelProvider(this).get(RobotViewModel.class);

        binding = FragmentRobotBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        msgRecyclerView = root.findViewById(R.id.robotDialog);
        inputText = root.findViewById(R.id.search_robot);
        send = root.findViewById(R.id.button_robotSend);
        layoutManager = new LinearLayoutManager(this.getContext());
        adapter = new RobotMessage((msg_list = getData()));

        msgRecyclerView.setLayoutManager(layoutManager);
        msgRecyclerView.setAdapter(adapter);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = inputText.getText().toString();
                if (!content.equals("")) {
                    msg_list.add(new Message(content, true));
                    adapter.notifyItemInserted((msg_list.size()-1));
                    msgRecyclerView.scrollToPosition((msg_list.size()-1));
                    inputText.setText("");

                    msg_list.add(new Message("re: " + content, false));
                    adapter.notifyItemInserted((msg_list.size()-1));
                    msgRecyclerView.scrollToPosition(msg_list.size()-1);
                }
            }
        });

        return root;
    }

    private List<Message> getData() {
        List<Message> list = new ArrayList<>();
        list.add(new Message("您好，Nowledge机器人很开心为您服务!", false));
        return list;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}