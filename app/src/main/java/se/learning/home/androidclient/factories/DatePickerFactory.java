package se.learning.home.androidclient.factories;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Calendar;

/**
 * This is pop-up view which will be used by user to chose a date to schedule event
 */
public class DatePickerFactory extends DialogFragment implements DatePickerDialog.OnDateSetListener{
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
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    /**
     * This method is handling user input when user is done picking date and clicks ok
     * @param view - reference to the picker
     * @param year - user chosen year
     * @param monthOfYear - user chosen month
     * @param dayOfMonth - user chosen day
     */
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        if(output != null){
            String chosenDate = year + "-" + (++monthOfYear) + "-" + dayOfMonth;
            output.setText(chosenDate);
        }

    }
}
