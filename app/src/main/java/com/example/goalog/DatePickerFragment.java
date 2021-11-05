package com.example.goalog;

// For date picker, i search android developer documents:https://developer.android.com/guide/topics/ui/controls/pickers#DatePicker
//it will open the build-in date picker and update medicineDate String

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TextView;
import androidx.fragment.app.DialogFragment;
import java.util.Calendar;

/**
 * DatePickerFragment
 * For date picker, we search android developer documents:
 *  https://developer.android.com/guide/topics/ui/controls/pickers#DatePicker
 * it will open the build-in date picker and update medicineDate String
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    /**
     * onCreateDialog
     * @param savedInstanceState
     *  saveInstanceState parameter
     * @return
     *  The DatePickerDialog Fragment
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    /**
     * onDateSet： convert the selected date into "yyyy--MM--dd" format
     * @param view
     *  Current View
     * @param year
     *  The year in the Date
     * @param month
     *  The month in the Date
     * @param dayOfMonth
     *  The day in the Date
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String startDateSelected = String.valueOf(year) + "-" + String.valueOf(month+1) + "-" + String.valueOf(dayOfMonth);
        AddHabitActivity.dateDisplay.setText(startDateSelected);
    }

}