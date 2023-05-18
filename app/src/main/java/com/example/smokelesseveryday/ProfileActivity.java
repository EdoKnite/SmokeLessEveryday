package com.example.smokelesseveryday;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.smokelesseveryday.repository.database.entities.Profile;
import com.example.smokelesseveryday.viewmodel.AppViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


public class ProfileActivity extends AppCompatActivity {
    private AppViewModel appViewModel;
    private DateTimeFormatter dateTimeFormatter;

    private ImageButton cigarettesPerDayIncrementBtn, cigarettesPerDayDecrementBtn, cigarettesPerPackIncrementBtn, cigarettesPerPackDecrementBtn, yearOfSmokingIncrementBtn, yearOfSmokingDecrementBtn;
    private EditText cigarettesPerDayEditText, cigarettesPerPackEditText, yearOfSmokingEditText, pricePerPackEditText, dateStoppingEditText;
    private Spinner currencySpinner;
    private TextInputLayout cigarettesPerDayWrapper, cigarettesPerPackWrapper, yearOfSmokingWrapper, pricePerPackWrapper, dateStoppingWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        appViewModel = new ViewModelProvider(this).get(AppViewModel.class);

        configureToolbar();

        dateStoppingEditText = findViewById(R.id.date_stopping_edit_text);

        cigarettesPerDayEditText = findViewById(R.id.cigarette_per_day_edit_text);
        cigarettesPerPackEditText = findViewById(R.id.nb_cigarettes_per_pack_edit_text);
        yearOfSmokingEditText = findViewById(R.id.years_of_smoking_edit_text);
        pricePerPackEditText = findViewById(R.id.price_per_pack_edit_text);
        cigarettesPerDayWrapper = findViewById(R.id.cigarette_per_day_edit_text_wrapper);
        cigarettesPerPackWrapper = findViewById(R.id.nb_cigarettes_per_pack_edit_text_wrapper);
        yearOfSmokingWrapper = findViewById(R.id.years_of_smoking_edit_text_wrapper);
        pricePerPackWrapper = findViewById(R.id.price_per_pack_edit_text_wrapper);
        dateStoppingWrapper = findViewById(R.id.date_stopping_edit_text_wrapper);
        cigarettesPerDayIncrementBtn = findViewById(R.id.cigarette_smoked_increment_btn);
        cigarettesPerDayDecrementBtn = findViewById(R.id.cigarette_smoked_decrement_btn);
        cigarettesPerPackIncrementBtn = findViewById(R.id.nb_cigarettes_per_pack_increment_btn);
        cigarettesPerPackDecrementBtn = findViewById(R.id.nb_cigarettes_per_pack_decrement_btn);
        yearOfSmokingIncrementBtn = findViewById(R.id.years_of_smoking_increment_btn);
        yearOfSmokingDecrementBtn = findViewById(R.id.years_of_smoking_decrement_btn);
        currencySpinner = findViewById(R.id.currency_spinner);


        setListenersToIncrementAndDecrementBtn();
        setErrorListenersToRequiredFields();
        AppCompatImageButton backBtn = findViewById(R.id.back_btn_tool_bar);
        backBtn.setOnClickListener(v -> finish());
        FloatingActionButton saveButton = findViewById(R.id.btn_save_profile);
        saveButton.setOnClickListener(v -> saveProfile());

        String[] currencies = getResources().getStringArray(R.array.currency);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(ProfileActivity.this, android.R.layout.simple_dropdown_item_1line, currencies);
        currencySpinner.setAdapter(spinnerAdapter);

        this.getProfile();
        setEventsToFieldStoppingDate();
    }

    private void configureToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle(R.string.profile);
        }

    }


    private void getProfile() {
        Profile profile = appViewModel.getProfile();
        if (profile != null) {
            cigarettesPerPackEditText.setText(String.valueOf(profile.cigarettesInPack));
            yearOfSmokingEditText.setText(String.valueOf(profile.yearsOfSmoking));
            pricePerPackEditText.setText(String.valueOf(profile.pricePerPack));
            currencySpinner.setSelection(1);
            cigarettesPerDayEditText.setText(String.valueOf(profile.cigarettesPerDay));
            dateStoppingEditText.setText(profile.quittingDate);
        }
    }

    private void saveProfile() {

        String cigarettesPerDay = cigarettesPerDayEditText.getText().toString();
        String cigarettesPerPack = cigarettesPerPackEditText.getText().toString();
        String yearOfSmoking = yearOfSmokingEditText.getText().toString();
        String pricePerPack = pricePerPackEditText.getText().toString();
        String currency = currencySpinner.getSelectedItem().toString();
        String stoppingDate = dateStoppingEditText.getText().toString();

        if (TextUtils.isEmpty(stoppingDate) || TextUtils.isEmpty(cigarettesPerDay) || TextUtils.isEmpty(cigarettesPerPack) || TextUtils.isEmpty(yearOfSmoking) || TextUtils.isEmpty(pricePerPack) || TextUtils.isEmpty(currency)) {
            setErrorMessageToRequiredFields(cigarettesPerDayEditText, cigarettesPerDayWrapper, !TextUtils.isEmpty(cigarettesPerDayEditText.getText()));
            setErrorMessageToRequiredFields(cigarettesPerPackEditText, cigarettesPerPackWrapper, !TextUtils.isEmpty(cigarettesPerPackEditText.getText()));
            setErrorMessageToRequiredFields(yearOfSmokingEditText, yearOfSmokingWrapper, !TextUtils.isEmpty(yearOfSmokingEditText.getText()));
            setErrorMessageToRequiredFields(pricePerPackEditText, pricePerPackWrapper, !TextUtils.isEmpty(pricePerPackEditText.getText()));
            setErrorMessageToRequiredFields(dateStoppingEditText, dateStoppingWrapper, !TextUtils.isEmpty(dateStoppingEditText.getText()));
            setErrorMessageToSpinner(currencySpinner, !TextUtils.isEmpty(currencySpinner.getSelectedItem().toString()));
            Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.msg_required_fields_empty), Toast.LENGTH_LONG);
            toast.show();
        } else {
            Profile profile = appViewModel.getProfile();
            if (profile == null) {
                profile = new Profile(0, stoppingDate, Integer.parseInt(cigarettesPerDay), Integer.parseInt(cigarettesPerPack), Integer.parseInt(yearOfSmoking), Integer.parseInt(pricePerPack), currency);
                appViewModel.insertProfile(profile);
            } else {
                profile = new Profile(profile.uid, stoppingDate, Integer.parseInt(cigarettesPerDay), Integer.parseInt(cigarettesPerPack), Integer.parseInt(yearOfSmoking), Integer.parseInt(pricePerPack), currency);
                appViewModel.updateProfile(profile);
            }
            finish();
        }
    }

    private void changeValueOfEditText(EditText editText, TextInputLayout textInputLayout, boolean isIncrement) {
        editText.requestFocus();
        int newValue = 0;
        if (!editText.getText().toString().equals("")) {
            newValue = Integer.parseInt(editText.getText().toString());
            if (isIncrement) {
                newValue += 1;
            } else if (newValue > 0) {
                newValue -= 1;
            }
        }
        editText.setText(String.valueOf(newValue));
        setErrorMessageToRequiredFields(editText, textInputLayout, true);
    }

    private void setListenersToIncrementAndDecrementBtn() {
        cigarettesPerDayIncrementBtn.setOnClickListener(v -> changeValueOfEditText(cigarettesPerDayEditText, cigarettesPerDayWrapper, true));
        cigarettesPerDayDecrementBtn.setOnClickListener(v -> changeValueOfEditText(cigarettesPerDayEditText, cigarettesPerDayWrapper, false));
        cigarettesPerPackIncrementBtn.setOnClickListener(v -> changeValueOfEditText(cigarettesPerPackEditText, cigarettesPerPackWrapper, true));
        cigarettesPerPackDecrementBtn.setOnClickListener(v -> changeValueOfEditText(cigarettesPerPackEditText, cigarettesPerPackWrapper, false));
        yearOfSmokingIncrementBtn.setOnClickListener(v -> changeValueOfEditText(yearOfSmokingEditText, yearOfSmokingWrapper, true));
        yearOfSmokingDecrementBtn.setOnClickListener(v -> changeValueOfEditText(yearOfSmokingEditText, yearOfSmokingWrapper, false));
    }

    private void setErrorListenersToRequiredFields() {
        cigarettesPerDayEditText.setOnFocusChangeListener((view, b) -> setErrorMessageToRequiredFields(cigarettesPerDayEditText, cigarettesPerDayWrapper, b));
        cigarettesPerPackEditText.setOnFocusChangeListener((view, b) -> setErrorMessageToRequiredFields(cigarettesPerPackEditText, cigarettesPerPackWrapper, b));
        yearOfSmokingEditText.setOnFocusChangeListener((view, b) -> setErrorMessageToRequiredFields(yearOfSmokingEditText, yearOfSmokingWrapper, b));
        pricePerPackEditText.setOnFocusChangeListener((view, b) -> setErrorMessageToRequiredFields(pricePerPackEditText, pricePerPackWrapper, b));
        dateStoppingEditText.setOnFocusChangeListener((view, b) -> setErrorMessageToRequiredFields(dateStoppingEditText, dateStoppingWrapper, b));
        currencySpinner.setOnFocusChangeListener((view, b) -> setErrorMessageToSpinner(currencySpinner, b));
    }

    private void setErrorMessageToRequiredFields(EditText editText, TextInputLayout textInputLayout, boolean b) {
        if (!b && TextUtils.isEmpty(editText.getText())) {
            textInputLayout.setErrorEnabled(true);
            textInputLayout.setError(getResources().getString(R.string.field_required));
        } else {
            textInputLayout.setErrorEnabled(false);
        }
    }

    private void setErrorMessageToSpinner(Spinner spinner, boolean b) {
        if (!b && TextUtils.isEmpty(spinner.getSelectedItem().toString())) {
            TextView errorText = (TextView) spinner.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            errorText.setText(getResources().getString(R.string.field_required));
        }
    }

    TimePickerDialog.OnTimeSetListener onTimeStoppingSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            LocalDate localDate = LocalDate.parse(dateStoppingEditText.getText().toString(), dateTimeFormatter);
            LocalTime localTime = LocalTime.of(hourOfDay, minute);
            LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);

            dateStoppingEditText.setText(localDateTime.format(dateTimeFormatter));
        }
    };

    DatePickerDialog.OnDateSetListener onStoppingDateChangeListener = (view, year, monthOfYear, dayOfMonth) -> {

        String date = LocalDateTime.of(year, monthOfYear, dayOfMonth, 0,0).format(dateTimeFormatter);
        dateStoppingEditText.setText(date);

        showTime(onTimeStoppingSetListener);
    };

    private void showCalender(DatePickerDialog.
            OnDateSetListener onDateSetListener) {
        DatePickerDialog picker = new DatePickerDialog(ProfileActivity.this, R.style.DialogDateTheme, onDateSetListener, 2023, 5, 18);
        picker.show();
    }

    private void setEventsToFieldStoppingDate() {
        dateStoppingEditText.setInputType(InputType.TYPE_NULL);
        dateStoppingEditText.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                showCalender(onStoppingDateChangeListener);
            }
        });
        dateStoppingEditText.setOnClickListener(view -> showCalender(onStoppingDateChangeListener));
    }

    private void showTime(TimePickerDialog.OnTimeSetListener listener) {

        TimePickerDialog timePicker = new TimePickerDialog(ProfileActivity.this, listener, 0, 0, true);

        timePicker.show();
    }


}
