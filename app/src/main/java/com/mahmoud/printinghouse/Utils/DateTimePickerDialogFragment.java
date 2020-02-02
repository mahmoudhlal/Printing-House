package com.mahmoud.printinghouse.Utils;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.mahmoud.printinghouse.R;
import com.mahmoud.printinghouse.databinding.FragmentDateTimePickerDialogBinding;

import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

/**
 * <p>A fragment that shows a list of items as a modal bottom sheet.</p>
 * <p>You can show this modal bottom sheet from your activity like this:</p>
 * <pre>
 *     TripDateDialogFragment.newInstance(30).show(getSupportFragmentManager(), "dialog");
 * </pre>
 * <p>You activity (or fragment) needs to implement {@link Listener}.</p>
 */
public class DateTimePickerDialogFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    private FragmentDateTimePickerDialogBinding dateTimePickerDialogBinding;
    private Listener mListener;

    public DateTimePickerDialogFragment() {
        // Required empty public constructor
    }

    public static DateTimePickerDialogFragment newInstance() {
        return new DateTimePickerDialogFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        dateTimePickerDialogBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_date_time_picker_dialog, container, false);
        dateTimePickerDialogBinding.timePicker.setIs24HourView(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            dateTimePickerDialogBinding.timePicker.setVisibility(View.VISIBLE);
            dateTimePickerDialogBinding.datePicker.setVisibility(View.VISIBLE);
            dateTimePickerDialogBinding.date.setVisibility(View.GONE);
            dateTimePickerDialogBinding.time.setVisibility(View.GONE);
        }else {
            dateTimePickerDialogBinding.timePicker.setVisibility(View.GONE);
            dateTimePickerDialogBinding.datePicker.setVisibility(View.GONE);
            dateTimePickerDialogBinding.date.setVisibility(View.VISIBLE);
            dateTimePickerDialogBinding.time.setVisibility(View.VISIBLE);
        }
        dateTimePickerDialogBinding.time.setOnClickListener(this);
        dateTimePickerDialogBinding.date.setOnClickListener(this);
        dateTimePickerDialogBinding.btnConfirm.setOnClickListener(this);
        dateTimePickerDialogBinding.datePicker.setMinDate(Calendar.getInstance().getTimeInMillis());
        return dateTimePickerDialogBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            Calendar mCurrentTime = Calendar.getInstance();
            int year = mCurrentTime.get(Calendar.YEAR);
            int month = mCurrentTime.get(Calendar.MONTH);
            int day = mCurrentTime.get(Calendar.DAY_OF_MONTH);
            int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
            String hourStr = hour < 10 ? "0" + hour : String.valueOf(hour);

            int minute = mCurrentTime.get(Calendar.MINUTE);
            String minuteStr = minute < 10 ? "0" + minute : String.valueOf(minute);

            dateTimePickerDialogBinding.time.setText(String.format(Locale.US,"%s:%s", hourStr, minuteStr));
            dateTimePickerDialogBinding.date.setText(String.format(Locale.US,"%s/%s/%s", day, month + 1, String.valueOf(year)));
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        final Fragment parent = getParentFragment();
        if (parent != null)
            mListener = (Listener) parent;
        else
            mListener = (Listener) context;
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.date:
                Calendar mCurrentDate = Calendar.getInstance();
                int year = mCurrentDate.get(Calendar.YEAR);
                int month = mCurrentDate.get(Calendar.MONTH);
                int day = mCurrentDate.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(Objects.requireNonNull(getActivity()), (datePicker, year1, month1, dayOfMonth) ->
                        dateTimePickerDialogBinding.date.setText(String.format(Locale.US,"%s/%s/%s", dayOfMonth, month1 + 1, String.valueOf(year1))), year, month, day);
                datePickerDialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
                datePickerDialog.show();
                break;
            case R.id.time:
                Calendar mCurrentTime = Calendar.getInstance();
                int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
                String hourStr = hour < 10 ? "0" + hour : String.valueOf(hour);
                int minute = mCurrentTime.get(Calendar.MINUTE);
                String minuteStr = minute < 10 ? "0" + minute : String.valueOf(minute);
                dateTimePickerDialogBinding.time.setText(String.format("%s:%s", hourStr, minuteStr));
                new TimePickerDialog(Objects.requireNonNull(getActivity()), (timePicker, hourOfDay, minute1) -> {
                    String hourStr1 = hourOfDay < 10 ? "0" + hourOfDay : String.valueOf(hourOfDay);
                    String minuteStr1 = minute1 < 10 ? "0" + minute1 : String.valueOf(minute1);
                    dateTimePickerDialogBinding.time.setText(String.format(Locale.US,"%s:%s", hourStr1, minuteStr1));
                }, hour, minute, true).show();
                break;
            case R.id.btnConfirm:
                if(mListener != null){
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        String timePickerHour = dateTimePickerDialogBinding.timePicker.getHour() < 10 ? "0" + dateTimePickerDialogBinding.timePicker.getHour() : String.valueOf(dateTimePickerDialogBinding.timePicker.getHour());
                        String timePickerMin = dateTimePickerDialogBinding.timePicker.getMinute() < 10 ? "0" + dateTimePickerDialogBinding.timePicker.getMinute() : String.valueOf(dateTimePickerDialogBinding.timePicker.getMinute());
                        String datePickerYear = String.valueOf(dateTimePickerDialogBinding.datePicker.getYear());
                        String datePickerMonth = dateTimePickerDialogBinding.datePicker.getMonth() + 1< 10 ? "0" + (dateTimePickerDialogBinding.datePicker.getMonth() + 1): String.valueOf(dateTimePickerDialogBinding.datePicker.getMonth()+ 1);
                        String datePickerDayOfMonth = dateTimePickerDialogBinding.datePicker.getDayOfMonth() < 10 ? "0" + dateTimePickerDialogBinding.datePicker.getDayOfMonth() : String.valueOf(dateTimePickerDialogBinding.datePicker.getDayOfMonth());
                        String timeStr = String.format(Locale.US,"%s:%s", timePickerHour, timePickerMin);
                        String dateStr = String.format(Locale.US,"%s/%s/%s", datePickerYear, datePickerMonth, datePickerDayOfMonth);
                        mListener.onDateSelectedClicked(dateStr + " " + timeStr);
                        dismiss();
                    }
                    else if(!dateTimePickerDialogBinding.date.getText().toString().isEmpty() && !dateTimePickerDialogBinding.time.getText().toString().isEmpty()) {
                        mListener.onDateSelectedClicked(dateTimePickerDialogBinding.date.getText().toString() + " " + dateTimePickerDialogBinding.time.getText().toString());
                        dismiss();
                    }
                }
                break;
        }
    }

    public interface Listener {
        void onDateSelectedClicked(String dateTime);
    }
}
