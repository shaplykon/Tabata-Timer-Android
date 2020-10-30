package com.example.timer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


public class CounterFragment extends Fragment {
    TextView counterText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActiveTimerViewModel viewModel  = new ViewModelProvider(requireActivity()).get(ActiveTimerViewModel.class);

        viewModel.counterValue.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {

                counterText.setText(String.valueOf(integer));
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_counter, container, false);
        counterText = view.findViewById(R.id.stepCounter);
        return view;
    }
}