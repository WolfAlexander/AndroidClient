package se.learning.home.androidclient.view;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import java.util.ArrayList;

import DTO.Device;
import DTO.Devices;
import se.learning.home.androidclient.R;
import se.learning.home.androidclient.controller.Controller;
import se.learning.home.androidclient.interfaces.DeviceListObserver;

/**
 * Start activity of this app
 * Connects to server and creates list of switches to control
 * devices that are connected to server
 */
public class HomeActivity extends CustomActivity implements DeviceListObserver{
    private final Controller controller = super.getController();
    private final DeviceListObserver deviceListObserver = this;
    //private final String serverIP = "130.237.238.42";
    private final String serverIP = "10.0.2.2";

    /**
     * Called when app is starting
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        createExitButton();
        createAddButton();
        createScheduleButton();

        System.out.println("------Connecting...-------");

        controller.connectToServer(new DTO.ServerData(serverIP, 5821));
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
     * Creates a button that will close application and sets a listener for user click
     */
    private void createExitButton(){
        final Button exitButton = (Button) findViewById(R.id.closeAppBttn);
        assert exitButton != null;
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });
    }

    private void createAddButton(){
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

    private void createScheduleButton(){
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

    @Override
    public void updateDeviceList(Devices devices) {
        HomeActivity.this.runOnUiThread(new ShowDevices(devices));
    }

    private class ShowDevices implements Runnable{
        private Devices devices;

        public ShowDevices(Devices devices){
            this.devices = devices;
        }

        @Override
        public void run() {
            System.out.println("-----Notified");
            LinearLayout layout = (LinearLayout) findViewById(R.id.deviceListView);
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
        Switch s = new Switch(this);
        setNonLayoutParams(s, deviceInformation);
        setLayoutParams(s);
        setEventListener(s);

        return s;
    }

    /**
     * Sets functional parameters to the Switch - id, name, checked/unchecked
     * @param s - Switch instance
     * @param deviceInformation - DTO.Device information about the device
     */
    private void setNonLayoutParams(Switch s, Device deviceInformation) {
        s.setId(deviceInformation.getId());
        s.setText(deviceInformation.getName());
        s.setChecked(deviceInformation.getStatus());
    }

    /**
     * Sets layout parameter to the Switch
     * @param s - Switch instance
     */
    private void setLayoutParams(Switch s){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        s.setLayoutParams(params);
        s.setPadding(0, 30, 0, 30);

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
    private class SwitchDevice extends AsyncTask {
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