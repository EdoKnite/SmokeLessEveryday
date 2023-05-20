package com.example.smokelesseveryday.fragments;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.smokelesseveryday.ProfileActivity;
import com.example.smokelesseveryday.R;
import com.example.smokelesseveryday.repository.database.entities.Profile;
import com.example.smokelesseveryday.viewmodel.AppViewModel;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.ListIterator;
import java.util.Locale;

public class ProgressFragment extends Fragment {

    private TextView nonSmokerTextView, recoveredLifeExpectancyTextView, moneySavedTextView,
            unsmokedCigarettesTextView, moneySpentTextView, lifeLostTextView,
            smokedCigarettesTextView, dayTextView, selectedDaysTextView, progressPercentTextView;
    private DateTimeFormatter dateTimeFormatter;
    private Profile profile;
    private ProgressBar progressBar;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        String[] daysArray = getResources().getStringArray(R.array.days);
        sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(daysArray[0], 2);
        editor.putInt(daysArray[1], 3);
        editor.putInt(daysArray[2], 4);
        editor.putInt(daysArray[3], 5);
        editor.putInt(daysArray[4], 6);
        editor.putInt(daysArray[5], 7);
        editor.putInt(daysArray[6], 10);
        editor.putInt(daysArray[7], 14);
        editor.putInt(daysArray[8], 21);
        editor.putInt(daysArray[9], 30);
        editor.putInt(daysArray[10], 60);
        editor.putInt(daysArray[11], 90);
        editor.putInt(daysArray[12], 365);
        editor.putInt(daysArray[13], 1825);
        editor.apply();

        return inflater.inflate(R.layout.fragment_progress, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dayTextView = view.findViewById(R.id.day_text_view);
        progressBar = view.findViewById(R.id.progress_bar);
        progressPercentTextView = view.findViewById(R.id.progress_percent_text_view);
        selectedDaysTextView = view.findViewById(R.id.selected_days_text_view);
        ImageButton imageButton = view.findViewById(R.id.select_days_btn);
        imageButton.setOnClickListener(this::showPopup);

        nonSmokerTextView = view.findViewById(R.id.non_smoker_time_text_view);
        moneySavedTextView = view.findViewById(R.id.money_saved_text_view);
        recoveredLifeExpectancyTextView = view.findViewById(R.id.recovered_life_expectancy_text_view);
        unsmokedCigarettesTextView = view.findViewById(R.id.unsmoked_cigarette_text_view);

        smokedCigarettesTextView = view.findViewById(R.id.smoked_cigarettes_text_view);
        moneySpentTextView = view.findViewById(R.id.money_spent_text_view);
        lifeLostTextView = view.findViewById(R.id.life_lost_text_view);

        dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        setData();
    }

    public void setData() {

        AppViewModel appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        appViewModel.getProfiles().observe(getViewLifecycleOwner(), profiles -> {

            if (profiles.isEmpty()) {
                Intent intent = new Intent(getContext(), ProfileActivity.class);
                startActivity(intent);
            }

            profile = profiles.get(0);
            calculateValues();

//TODO
//            final Handler handler = new Handler();
//            final int delay = 1000;
//            handler.postDelayed(new Runnable() {
//                public void run() {
//                    calculateValues();
//                    handler.postDelayed(this, delay);
//                }
//            }, delay);


            LocalDate dateOfBeginningSmoking = LocalDate.now();
            dateOfBeginningSmoking = dateOfBeginningSmoking.minusYears(profile.yearsOfSmoking);
            LocalDate quittingDate = LocalDate.parse(profile.quittingDate, dateTimeFormatter);

            Period smokingDuration = Period.between(dateOfBeginningSmoking, quittingDate);
            lifeLostTextView.setText(String.format(getString(R.string.life_lost_contents),
                    smokingDuration.getYears(), smokingDuration.getMonths(), smokingDuration.getDays()));

            long cigaretteSmoked = ChronoUnit.DAYS.between(dateOfBeginningSmoking, quittingDate) * profile.cigarettesPerDay;
            smokedCigarettesTextView.setText(String.valueOf(cigaretteSmoked));
            float moneySpent = (float) cigaretteSmoked * profile.pricePerPack / profile.cigarettesInPack;

            String moneySpentText = moneySpent + " " + profile.currency;
            moneySpentTextView.setText(moneySpentText);

            String day = getResources().getStringArray(R.array.days)[1];

            selectedDaysTextView.setText(day);
            int numberOfDays = sharedPreferences.getInt(day, 0);
            setProgressValues(numberOfDays);

        });

    }

    private void calculateValues() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime quittingDate = LocalDateTime.parse(profile.quittingDate, dateTimeFormatter);
        Duration duration = Duration.between(quittingDate, currentDateTime);

        if (quittingDate.isBefore(currentDateTime)) {

            String nonSmokerString = duration.toDays() + "d " + duration.toHours() % 24 + "h " +
                    duration.toMinutes() % 60 + "m";
            nonSmokerTextView.setText(nonSmokerString);

            String recoveredLifeExpectancy = duration.dividedBy(4).toDays() + "d " + duration.dividedBy(4).toHours() % 24 + "h " +
                    duration.dividedBy(4).toMinutes() % 60 + "m";
            recoveredLifeExpectancyTextView.setText(recoveredLifeExpectancy);

            long cigaretteNotSmoked = duration.toDays() * profile.cigarettesPerDay;
            unsmokedCigarettesTextView.setText(String.valueOf(cigaretteNotSmoked));

            float moneySaved = cigaretteNotSmoked * profile.pricePerPack / profile.cigarettesInPack;
            String moneySavedString = String.format(Locale.getDefault(), "%.2f", moneySaved) + profile.currency;
            moneySavedTextView.setText(moneySavedString);

            setProgressValues(3);

        } else {
            String moneySavedString = "0" + profile.currency;
            moneySavedTextView.setText(moneySavedString);
            setProgressValues(3);

        }

        int delta = (int) duration.toDays() + 1;
        String days;

        if (delta == 1)
            days = delta + " " + getResources().getString(R.string.day);
        else
            days = delta + " " + getResources().getString(R.string.days);

        dayTextView.setText(days);

    }

    public void showPopup(View v) {

        PopupMenu menu = new PopupMenu(getContext(), v);
        menu.setGravity(Gravity.END);

        ListIterator<String> it = Arrays.asList(getResources().getStringArray(R.array.days)).listIterator();
        while (it.hasNext()) {
            menu.getMenu().add(it.nextIndex(), it.nextIndex(), it.nextIndex(), it.next());
        }

        menu.show();
        menu.setOnMenuItemClickListener(item -> {

            selectedDaysTextView.setText(item.getTitle());

            int numberOfDays = sharedPreferences.getInt(item.getTitle().toString(), 0);
            setProgressValues(numberOfDays);

            return false;
        });
    }

    private void setProgressValues(int numberOfDays) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime quittingDate = LocalDateTime.parse(profile.quittingDate, dateTimeFormatter);

        if (quittingDate.isBefore(currentDateTime)) {
            Duration duration = Duration.between(quittingDate, currentDateTime);

            long days = duration.toDays();
            float progressPercent;

            if (days >= numberOfDays)
                progressPercent = 100;

            else
                progressPercent = days * 100.0f / numberOfDays;


            String progressPercentStr;
            progressPercentStr = String.format(Locale.getDefault(), "%.1f", progressPercent) + "%";

            progressPercentTextView.setText(progressPercentStr);
            onProgressPercentChanged((int) progressPercent);

        } else {
            String progressPercentStr;
            progressPercentStr = String.format(Locale.getDefault(), "%.1f", 0.0) + "%";

            progressPercentTextView.setText(progressPercentStr);
            onProgressPercentChanged(0);
        }

    }

    private void onProgressPercentChanged(int percentage) {
        Animator animation = ObjectAnimator.ofInt(progressBar, "progress", percentage);
        animation.setDuration(750);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();
    }
}
