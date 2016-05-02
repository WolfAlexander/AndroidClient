package se.learning.home.androidclient.view;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.ArrayList;
import DTO.Device;
import DTO.Devices;
import DTO.ScheduledEvent;
import se.learning.home.androidclient.R;
import se.learning.home.androidclient.factories.UIFactory;
import se.learning.home.androidclient.interfaces.DeviceListObserver;

/**
 * This activity give user possibility to create new events
 * and add them to the schedule
 * User should provide device whose status will be changed, time of the event,
 * date of the event and new status of the device (on/off)
 */
public class AddNewEventToSchedule extends CustomActivity implements DeviceListObserver{
    private DeviceSpinnerItem deviceChosen;
    private TextView startDatePicker;
    private TextView startTimePicker;
    private CheckBox endDataAvailableCheckbox;
    private TextView endDatePicker;
    private TextView endTimePicker;
    private String deviceStatusChosen;
    private ArrayList<DeviceSpinnerItem> deviceSpinnerItems = new ArrayList<>();

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

        createStartDateInput();
        createStartTimeInput();
        handleEndDataCheckbox();
        handleOnOffSelector();

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

        createDeviceSelector();
    }

    /**
     * Creates items for dropdown menu of devices and sets them to the dropdown menu
     */
    private void createDeviceSelector(){
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
     * Creates and shows date picker for user
     */
    private void createStartDateInput(){
        startDatePicker = (TextView)findViewById(R.id.startDatePicker);
        if (startDatePicker != null) {
            startDatePicker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UIFactory.getInstance().createDatePicker(startDatePicker).show(getSupportFragmentManager(), "datePicker");
                }
            });
        }
    }

    /**
     * Creates and shows time picker for user
     */
    private void createStartTimeInput(){
        startTimePicker = (TextView)findViewById(R.id.startTimePicker);
        if(startTimePicker != null){
            startTimePicker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UIFactory.getInstance().createTimePicker(startTimePicker).show(getSupportFragmentManager(), "timePicker");
                }
            });
        }
    }

    /**
     * This method will handle checkbox events in this activity - if checkbox is checked then view for entering
     * end date and time will be shown to the user, if not - end date and time view will be gone
     */
    private void handleEndDataCheckbox(){
        endDataAvailableCheckbox = (CheckBox)findViewById(R.id.endDataCheckbox);
        if (endDataAvailableCheckbox != null) {
            endDataAvailableCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                LinearLayout endDateBlock = (LinearLayout) findViewById(R.id.endDateBlock);
                LinearLayout endTimeBlock = (LinearLayout) findViewById(R.id.endTimeBlock);

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        endDateBlock.setVisibility(View.VISIBLE);
                        endTimeBlock.setVisibility(View.VISIBLE);
                        createEndTimeInput();
                        createEndDateInput();
                    } else {
                        endDateBlock.setVisibility(View.GONE);
                        endTimeBlock.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    /**
     * Creates and shows time picker for user
     */
    private void createEndTimeInput(){
        endDatePicker = (TextView)findViewById(R.id.endDatePicker);
        endDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIFactory.getInstance().createDatePicker(endDatePicker).show(getSupportFragmentManager(), "datePicker");
            }
        });
    }

    /**
     * Creates and shows date picker for user
     */
    private void createEndDateInput(){
        endTimePicker = (TextView)findViewById(R.id.endTimePicker);
        endTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIFactory.getInstance().createTimePicker(endTimePicker).show(getSupportFragmentManager(), "timePicker");
            }
        });
    }


    /**
     * Handles user event on on/off drop down list by listening to value that user chose
     */
    private void handleOnOffSelector() {
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
        if (submitBttn != null) {
            submitBttn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String endTimeDate = null;

                    if(endTimePicker != null && endDatePicker != null && endDataAvailableCheckbox.isChecked())
                        endTimeDate =  endTimePicker.getText().toString() + " " + endDatePicker.getText().toString();

                    String startTimeDate = startDatePicker.getText() + " " + startTimePicker.getText();

                    ScheduledEvent event = new ScheduledEvent(deviceChosen.getDeviceId(), deviceChosen.getDeviceName(), startTimeDate, endTimeDate, deviceStatusChosen);
                    getController().addNewScheduledEventToServer(event);
                }
            });
        }
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
