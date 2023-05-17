package com.example.smokelesseveryday.adapters;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smokelesseveryday.R;
import com.example.smokelesseveryday.repository.database.entities.Health;

public class HealthAdapter extends ListAdapter<Health, HealthAdapter.HealthHolder> {
    public HealthAdapter(@NonNull DiffUtil.ItemCallback<Health> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public HealthHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_health_item, parent, false);

        return new HealthHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HealthHolder holder, int position) {
        Health current = getItem(position);
        holder.bind(current);
    }

    public static class HealthHolder extends RecyclerView.ViewHolder {

        private final TextView titleTextView;
        private final TextView statusTextView;
        private final TextView descriptionTextView;
        private final TextView percentTextView;
        private final ProgressBar healthProgressBar;

        public HealthHolder(@NonNull View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.title_health_item);
            statusTextView = itemView.findViewById(R.id.status_health_item);
            descriptionTextView = itemView.findViewById(R.id.description_health_item);
            percentTextView = itemView.findViewById(R.id.percent_health_item);

            healthProgressBar = itemView.findViewById(R.id.health_progress);
        }

        public void bind(Health health) {
            titleTextView.setText(health.title);
            statusTextView.setText(health.status);
            descriptionTextView.setText(health.description);
            percentTextView.setText(health.percent + "%");
            healthProgressBar.setProgress(health.percent);

            if (health.percent == 100) {
                healthProgressBar.setProgressTintList(ColorStateList.valueOf(itemView.getResources().getColor(R.color.completed, null)));
            }
        }
    }

    public static class HealthDiff extends DiffUtil.ItemCallback<Health> {

        @Override
        public boolean areItemsTheSame(@NonNull Health oldItem, @NonNull Health newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Health oldItem, @NonNull Health newItem) {
            return oldItem.title.equals(newItem.title);
        }
    }
}
