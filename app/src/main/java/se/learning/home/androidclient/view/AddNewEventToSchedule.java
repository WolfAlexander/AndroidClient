package se.learning.home.androidclient.view;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;

import DTO.Device;
import DTO.Devices;
import DTO.ScheduledEvent;
import se.learning.home.androidclient.R;
import se.learning.home.androidclient.interfaces.DeviceListObserver;

/**
 * This activity give user possibility to create new events
 * and add them to the schedule
 * User should provide device whose status will be changed, time of the event,
 * date of the event and new status of the device (on/off)
 */
public class AddNewEventToSchedule extends CustomActivity implements DeviceListObserver{
    private DeviceSpinnerItem deviceChosen;
    private String dateChosen;
    private String timeChosen;
    private String deviceStatusChosen;
    private TextView date;
    private TextView time;
    private ArrayList<DeviceSpinnerItem> deviceSpinnerItems = new ArrayList<>();
    private final int timePickerDialogId = 0;
    private final int datePickerDialogId = 1;

    /**
     * Runs when this activity is launched
     * This method will register this activity as Device list observer, request Device list and
     * create all necessary fields and buttons for user input
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_event);

        super.getController().requestListOfDevicesFromServer(this);
        createDeviceSelectors();
        createDateSelector();
        createTimeSelector();
        handleOnOffSpinner();
        createSubmitBttnListener();
    }

    /**
     * This method is part of observable pattern - it gets called when new list of
     * devices is received from server
     * This method will receive
     * @param devices - list of devices that comes from server
     */
    @Override
    public void updateDeviceList(Devices devices) {
        deviceSpinnerItems = new ArrayList<>();

        for(Device device : devices.getDeviceList()){
            deviceSpinnerItems.add(new DeviceSpinnerItem(device.getId(), device.getName()));
        }

        createDeviceSelectors();
    }

    /**
     * Creates items for dropdown menu of devices and sets them to the dropdown menu
     */
    private void createDeviceSelectors(){
        Spinner devicesSpinner = (Spinner)findViewById(R.id.deviceListForScheduling);
        ArrayAdapter<DeviceSpinnerItem> devicesSpinnerArrayAdapter = new ArrayAdapter<DeviceSpinnerItem>(this, android.R.layout.simple_spinner_item, deviceSpinnerItems);
        devicesSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        devicesSpinner.setAdapter(devicesSpinnerArrayAdapter);

        setDeviceSpinnerListener(devicesSpinner);
    }

    /**
     * Sets a listener to device drop down chooser that will read
     * value that user have chosen
     * @param deviceSpinner
     */
    private void setDeviceSpinnerListener(Spinner deviceSpinner){
        if (deviceSpinner != null) {
            deviceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    deviceChosen = (DeviceSpinnerItem) parent.getItemAtPosition(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    deviceChosen = null;
                }
            });
        }
    }

    /**
     * Creates popup for time selection
     */
    private void createTimeSelector(){
        time = (TextView)findViewById(R.id.timePicker);
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(0);
            }
        });
    }

    /**
     * Creates popup for date selection
     */
    private void createDateSelector(){
        date = (TextView)findViewById(R.id.datePicker);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(1);
            }
        });
    }

    /**
     * Creates dialog depending on the dialog id entered as parameter
     * @param id - dialog id
     * @return reference to dialog
     */
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

    /**
     * Listens for user time value chosen from time dialog popup
     */
    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            timeChosen = hourOfDay + ":" + minute;
            time.setText(timeChosen);
        }
    };

    /**
     * Listens for user time value chosen from date dialog popup
     */
    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateChosen = year + "-" + monthOfYear + "-" + dayOfMonth;
            date.setText(dateChosen);
        }
    };

    /**
     * Handles user event on on/off drop down list by listening to value that user chose
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

    /**
     * Creates submit button listener - when button is clicked then entered
     * schedule data will be send to server
     */
    private void createSubmitBttnListener(){
        Button submitBttn = (Button)findViewById(R.id.submitNewEventBttn);
        submitBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dateTime = dateChosen + " " + timeChosen;

                ScheduledEvent event = new ScheduledEvent(deviceChosen.getDeviceId(), deviceChosen.getDeviceName(), dateTime, deviceStatusChosen);
                getController().addNewScheduledEventToServer(event);
            }
        });
    }


    /**
     * Custom device spinner(drop down list) item - it will contain device id number
     * and device name with default constructor, getters and toString() representation
     */
    private class DeviceSpinnerItem{
        private int deviceId;
        private String deviceName;

        public DeviceSpinnerItem(int deviceId, String deviceName) {
            this.deviceId = deviceId;
            this.deviceName = deviceName;
        }

        public int getDeviceId() {
            return deviceId;
        }

        public String getDeviceName() {
            return deviceName;
        }

        @Override
        public final String toString(){
            return deviceName;
        }
    }
}
