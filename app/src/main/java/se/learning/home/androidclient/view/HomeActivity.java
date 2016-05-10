package se.learning.home.androidclient.view;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import java.util.ArrayList;

import DTO.Device;
import DTO.Devices;
import se.learning.home.androidclient.R;
import se.learning.home.androidclient.controller.Controller;
import se.learning.home.androidclient.factories.UIFactory;
import se.learning.home.androidclient.interfaces.DeviceListObserver;

/**
 * Start activity of this app
 * This activity displays device switches for all devices retrieved from server
 */
public class HomeActivity extends CustomActivity implements DeviceListObserver{
    private final Controller controller = super.getController();
    private final DeviceListObserver deviceListObserver = this;

    /**
     * Called when app is starting
     * It creates buttons and requests server
     * to subscribe this view as an observer for device list
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        createExitButtonListener();
        createAddButtonListener();
        createScheduleButtonListener();

        System.out.println("------Connecting...-------");
        controller.establishConnection();
        while(!controller.isConnectedToServer()){}

        showListOfDevices();


        /*synchronized (this){
            try {
                int connectionTries = 0;
                System.out.println("Waiting...");
                this.wait(1000);
                System.out.println("Waiting done!");

                while (connectionTries != 5){
                    System.out.println("Hellooooo! Its Ulrika ;)! Ulrika says: " + connectionTries);

                    if(connectionTries == 4){
                        super.showAlertMessage("Could not connect to server");
                        System.exit(0);
                    }else if (!controller.isConnectedToServer()) {
                        connectionTries++;
                        this.wait(3000);
                    }else{
                        System.out.println("--------Connected!---------");
                        break;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/
    }

    /**
     * Calls server to subscribe this view as observer
     * for device list and request first update immediately
     */
    private void showListOfDevices(){
        new AsyncTask(){
            @Override
            protected Object doInBackground(Object[] params) {
                System.out.println("---------Requesting");
                controller.requestListOfDevicesFromServer(deviceListObserver);
                return null;
            }
        }.execute();
    }

    /**
     * Creates a button click listener for close app button
     */
    private void createExitButtonListener(){
        final Button exitButton = (Button) findViewById(R.id.closeAppBttn);
        if (exitButton != null) {
            exitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    System.exit(0);
                }
            });
        }
    }

    /**
     * Create a button click listener for add device button
     */
    private void createAddButtonListener(){
        final Button addButton = (Button) findViewById(R.id.addDeviceBttn);
        if (addButton != null) {
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent nextScreen = new Intent(getApplicationContext(), AddDeviceActivity.class);
                    startActivity(nextScreen);
                }
            });
        }
    }

    /**
     * Creates a button click listener for schedule button
     */
    private void createScheduleButtonListener(){
        final Button scheduleBttn = (Button) findViewById(R.id.scheduleBttn);
        if (scheduleBttn != null) {
            scheduleBttn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent nextScreen = new Intent(getApplicationContext(), ScheduleActivity.class);
                    startActivity(nextScreen);

                }
            });
        }
    }

    /**
     * A DeviceListObserver notifier - this method gets called by model
     * when new device list are received from server
     * @param devices - devices received from server of type DTO.Devices
     */
    @Override
    public void updateDeviceList(Devices devices) {
        HomeActivity.this.runOnUiThread(new ShowDevices(devices));
    }

    /**
     * This class will be run on an UI thread and it will add switches to the screen
     * for each device received from server
     */
    private class ShowDevices implements Runnable{
        private Devices devices;

        /**
         * Constructor initializes devices variable
         * @param devices - DTO.Devices that comes from server
         */
        public ShowDevices(Devices devices){
            this.devices = devices;
        }

        /**
         * The executor of this thread - creates necessary GUI elements and
         * creates switches
         */
        @Override
        public void run() {
            LinearLayout layout = (LinearLayout) findViewById(R.id.deviceListView);
            if(layout.getChildCount() > 0)
                layout.removeAllViews();
            ArrayList<Device> deviceList = devices.getDeviceList();

            if(!deviceList.isEmpty()){
                for(Device d : deviceList){
                    Switch s = createSwitch(d);
                    layout.addView(s);
                }
            }else{
                showAlertMessage("No devices found!");
            }
        }
    }

    /**
     * Creates custom switches
     * @param deviceInformation - information about a device that this switch will control
     * @return instance of created Switch
     */
    private Switch createSwitch(Device deviceInformation){
        Switch s = (Switch)UIFactory.getInstance().createSwitch(deviceInformation, this);
        setEventListener(s);

        return s;
    }

    /**
     * Sets a listener on the Switch that will wait for user action on the Switch
     * @param s - Switch instance
     */
    private void setEventListener(Switch s){
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                new SwitchDevice(buttonView.getId()).execute();
            }
        });
    }

    /**
     * This UI Thread that contacts server to change status(on/off) of a device
     */
    public class SwitchDevice extends AsyncTask {
        private int deviceId;

        /**
         * Constructor - initializes deviceId value
         * @param deviceId - id of the device that user wants to change status of
         */
        public SwitchDevice(int deviceId){
            this.deviceId = deviceId;
        }

        /**
         * Sends change device status request to server with identification of the device
         * @param params - has to be there because we override this method
         * @return - null in this case because we will not receive any response from server
         * for this request
         */
        @Override
        protected Object doInBackground(Object[] params) {
            controller.switchDeviceOnServer(deviceId);
            System.out.println("Switching " + deviceId);
            return null;
        }
    }
}