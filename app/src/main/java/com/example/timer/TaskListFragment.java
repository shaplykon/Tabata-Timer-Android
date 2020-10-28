package com.example.timer;
import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Objects;

public class TaskListFragment extends Fragment {

    ArrayList<String> tasks = new ArrayList<>();
    ArrayAdapter<String> adapter;

    ListView listView;
    private ActiveTimerViewModel activeViewModel;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        activeViewModel = new ViewModelProvider(requireActivity()).get(ActiveTimerViewModel.class);
        int counter = 0;

        Resources resources = getResources();

        tasks.add(++counter + ". " + resources.getString(R.string.preparing) + ": " + Objects.requireNonNull(activeViewModel.preparingTime.getValue()).toString());
        for(int i = 0; i < activeViewModel.setsAmount.getValue(); i++){
            tasks.add(++counter + ". " + resources.getString(R.string.work) + ": " + Objects.requireNonNull(activeViewModel.workingTime.getValue()).toString());
            tasks.add(++counter + ". " + resources.getString(R.string.rest) + ": " + Objects.requireNonNull(activeViewModel.restTime.getValue()).toString());
        }
        tasks.add(++counter + ". " + resources.getString(R.string.cooldown) + ": " + Objects.requireNonNull(activeViewModel.cooldownTime.getValue()).toString());

        tasks.add(resources.getString(R.string.finish));
        adapter = new  ArrayAdapter<>(getContext(), R.layout.task_list_item, tasks);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_task_list, container, false);
        listView = view.findViewById(R.id.list_view);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (activeViewModel.isRunning) {
                    activeViewModel.changePhase(position);
                    listView.smoothScrollToPosition(position);
                }
            }
        });

        return view;
    }
}