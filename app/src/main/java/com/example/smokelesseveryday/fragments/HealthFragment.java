package com.example.smokelesseveryday.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smokelesseveryday.R;
import com.example.smokelesseveryday.adapters.HealthAdapter;
import com.example.smokelesseveryday.viewmodel.AppViewModel;


public class HealthFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_health, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ProgressBar progressBar = view.findViewById(R.id.progress_bar_health);
        LinearLayout noElements = view.findViewById(R.id.no_element_linear_layout);

        progressBar.setVisibility(View.GONE);
        noElements.setVisibility(View.GONE);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_health);
        AppViewModel appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);

        HealthAdapter adapter = new HealthAdapter(new HealthAdapter.HealthDiff());

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        appViewModel.getAchievements().observe(getViewLifecycleOwner(), adapter::submitList);
    }
}
