package se.learning.home.androidclient.view;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import DTO.Device;
import se.learning.home.androidclient.R;

public class AddNewEventToSchedule extends CustomActivity {
    private Device deviceChosen;
    private String dateChosen;
    private String timeChosen;
    private String deviceStatusChosen;
    private TextView date;
    private TextView time;
    private final int timePickerDialogId = 0;
    private final int datePickerDialogId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_event);

        //createDevicesSelector();
        createDateSelector();
        createTimeSelector();
        handleOnOffSpinner();
        createSubmitBttn();
    }

    private void createTimeSelector(){
        time = (TextView)findViewById(R.id.timePicker);
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(0);
            }
        });
    }

    private void createDateSelector(){
        date = (TextView)findViewById(R.id.datePicker);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(1);
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id){
        if(id == timePickerDialogId){
            return new TimePickerDialog(this, timePickerListener, 0, 0, true);
        }else if(id == datePickerDialogId){
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
            int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(this, datePickerListener, currentYear, currentMonth, currentDay);
        }

        return null;
    }

    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            timeChosen = hourOfDay + ":" + minute;
            time.setText(timeChosen);
        }
    };

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateChosen = year + "-" + monthOfYear + "-" + dayOfMonth;
            date.setText(dateChosen);
        }
    };

    /**
     * Handles on/off spinner by writing chosen item to field
     */
    private void handleOnOffSpinner() {
        Spinner onOffSpinner = (Spinner) findViewById(R.id.onOffSpinner);
        if (onOffSpinner != null) {
            onOffSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    deviceStatusChosen = (String) parent.getItemAtPosition(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    deviceStatusChosen = null;
                }
            });
        }
    }

    private void createSubmitBttn(){
        Button submitBttn = (Button)findViewById(R.id.submitNewEventBttn);
        submitBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Device: " + deviceChosen + "\nDate chosen: " + dateChosen + "\nTime chosen: " + timeChosen + "\nStatus chosen: " + deviceStatusChosen);
            }
        });
    }
}
