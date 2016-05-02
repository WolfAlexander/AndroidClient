package se.learning.home.androidclient.factories;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.TimePicker;

import android.text.format.DateFormat;
import java.util.Calendar;

/**
 * This is pop-up view which will be used by user to chose time to schedule event
 */
public class TimePickerFactory extends DialogFragment implements TimePickerDialog.OnTimeSetListener{
    private TextView output;

    /**
     * Sets reference to output TextView where user input will be printed
     * @param output - reference to TextView for output
     */
    public void setOutputElement(TextView output){
        this.output = output;
    }

    /**
     * Gets called when this picker is creating
     * @param savedInstanceState
     * @return reference to this picker
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));

    }

    /**
     *  /**
     * This method is handling user input when user is done picking time and clicks ok
     * @param view - reference to the picker
     * @param hourOfDay - user chosen hour
     * @param minute - user chosen minute
     */
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if(output != null){
            String chosenTime = hourOfDay + ":" + minute;
            output.setText(chosenTime);
        }

    }
}
