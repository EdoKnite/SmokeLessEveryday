package com.example.smokelesseveryday.fragments;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import java.util.HashMap;
import java.util.ListIterator;

public class ProgressFragment extends Fragment {

    private TextView nonSmokerTextView, recoveredLifeExpectancyTextView, moneySavedTextView,
            unsmokedCigarettesTextView, moneySpentTextView, lifeLostTextView,
            smokedCigarettesTextView, dayTextView, selectedDaysTextView, progressPercentTextView;
    public static DateTimeFormatter dateTimeFormatter;
    private Profile profile;
    private ProgressBar progressBar;

    private final HashMap<String, Integer> selectedDays
            = new HashMap<String, Integer>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        selectedDays.put("2 days", 2);
        selectedDays.put("3 days", 3);
        selectedDays.put("4 days", 4);
        selectedDays.put("5 days", 5);
        selectedDays.put("6 days", 6);
        selectedDays.put("1 week", 7);
        selectedDays.put("10 days", 10);
        selectedDays.put("2 weeks", 14);
        selectedDays.put("3 weeks", 21);
        selectedDays.put("1 month", 30);
        selectedDays.put("2 month", 60);
        selectedDays.put("3 month", 90);
        selectedDays.put("1 year", 365);
        selectedDays.put("5 years", 1825);

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

        setData();
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
            int nbDays = selectedDays.get(item.getTitle());
            setProgressValues(nbDays);
            return false;
        });
    }

    public void setData() {
        AppViewModel appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        this.profile = appViewModel.getProfile();

        if (profile == null) {

            Intent intent = new Intent(getContext(), ProfileActivity.class);
            startActivity(intent);
            getActivity().finish();
        } else {
            calculateValues();

//            final Handler handler = new Handler();
//            final int delay = 1000;
//            handler.postDelayed(new Runnable() {
//                public void run() {
//                    calculateValues(daysStringResource);
//                    handler.postDelayed(this, delay);
//                }
//            }, delay);
        }

        dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDate dateOfBeginning = LocalDate.now();
        dateOfBeginning = dateOfBeginning.minusYears(profile.yearsOfSmoking);
        LocalDate quittingDate = LocalDate.parse(profile.quittingDate, dateTimeFormatter);

        Period smokingDuration = Period.between(dateOfBeginning, quittingDate);
        lifeLostTextView.setText(smokingDuration.getYears() + "Y " + smokingDuration.getMonths() + "M "
                + smokingDuration.getDays() + "D");

        long cigaretteSmoked = ChronoUnit.DAYS.between(dateOfBeginning, quittingDate) * profile.cigarettesPerDay;
        smokedCigarettesTextView.setText(String.valueOf(cigaretteSmoked));
        float moneySpent = (float) cigaretteSmoked * profile.pricePerPack / profile.cigarettesInPack;
        moneySpentTextView.setText((int) moneySpent + " " + profile.currency);

        int nbDays = selectedDays.get(selectedDaysTextView.getText().toString());;
        setProgressValues(nbDays);

    }


    private void setProgressValues(float nbDays) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDate quittingDate = LocalDate.parse(profile.quittingDate, dateTimeFormatter);
        Duration duration = Duration.between(quittingDate, currentDateTime);

        float progressPercent = duration.toDays() * 100 / nbDays;

        String progressPercentStr;
        if (progressPercent > 100) {

            progressPercent = 100;
        }

        progressPercentStr = String.format("%.1f", progressPercent) + " %";

        progressPercentTextView.setText(progressPercentStr);
        onProgressPercentChanged((int) progressPercent);
    }

    private void onProgressPercentChanged(int percentage) {
        Animator animation = ObjectAnimator.ofInt(progressBar, "progress", percentage);
        animation.setDuration(750);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();
    }

    private void calculateValues() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDate quittingDate = LocalDate.parse(profile.quittingDate, dateTimeFormatter);

        Duration duration = Duration.between(quittingDate, currentDateTime);
        String nonSmokerString = duration.toDays() + "d " + duration.toHours() + "h " +
                duration.toMinutes() + "m";
        nonSmokerTextView.setText(nonSmokerString);

        String recoveredLifeExpectancy = duration.dividedBy(4).toDays() + "d " + duration.dividedBy(4).toHours() + "h " +
                duration.dividedBy(4).toMinutes() + "m";
        recoveredLifeExpectancyTextView.setText(recoveredLifeExpectancy);

        long cigaretteNotSmoked = duration.toDays() * profile.cigarettesPerDay;
        unsmokedCigarettesTextView.setText(String.valueOf(cigaretteNotSmoked));

        float moneySaved = cigaretteNotSmoked * profile.pricePerPack / profile.cigarettesInPack;
        String moneySavedString = String.format("%.2f", moneySaved) + " " + profile.currency;
        moneySavedTextView.setText(moneySavedString);

        String days = (int) duration.toDays() + 1 + " " + getResources().getString(R.string.days);
        dayTextView.setText(days);
    }
}
