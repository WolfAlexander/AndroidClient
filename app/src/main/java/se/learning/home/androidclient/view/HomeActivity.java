package se.learning.home.androidclient.view;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;

import java.util.ArrayList;

import DTO.Device;
import DTO.Devices;
import se.learning.home.androidclient.R;
import se.learning.home.androidclient.controller.Controller;

/**
 * Start activity of this app
 * Connects to server and creates list of switches to control
 * devices that are connected to server
 */
public class HomeActivity extends CustomActivity{
    private final Controller controller = super.getController();
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

        new ShowAllDevices(this).execute();

    }

    /**
     * Creates a button that will close application and sets a listener for user click
     */
    private void createExitButton(){
        final Button exitButton = (Button) findViewById(R.id.closeAppBttn);
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
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextScreen = new Intent(getApplicationContext(), AddDeviceActivity.class);
                startActivity(nextScreen);
            }
        });
    }
    private void createScheduleButton(){
        final Button scheduleBttn = (Button) findViewById(R.id.scheduleBttn);
        scheduleBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextScreen = new Intent(getApplicationContext(), ScheduleActivity.class);
                startActivity(nextScreen);

            }
        });
    }

    /**
     * UI Thread that collects device list from server and
     * makes device controllers appear to the screen
     */
    private class ShowAllDevices extends AsyncTask {
        private Context context;

        /**
         * Constructor that initializes context variable
         * @param context - context of UI Activity that uses this UI Thread
         */
        public ShowAllDevices(Context context){
            this.context = context;
        }

        /**
         * This method requests model to retrieve device list from server
         * @param params - must be there since we override this method
         * @return DTO.Devices - list of devices from server
         */
        @Override
        protected Devices doInBackground(Object[] params) {
            return controller.getListOfDevices();
        }

        /**
         * This method is being called after  doInBackground returned
         * This method creates switch-buttons from device list and makes them appear on the screen
         * @param o - return value from doInBackground method
         */
        @Override
        protected void onPostExecute(Object o) {
            Devices devices = (Devices)o;
            LinearLayout layout = (LinearLayout) findViewById(R.id.deviceListView);
            ArrayList<Device> deviceList = devices.getDeviceList();

            if(!deviceList.isEmpty()){
                for(Device d : devices.getDeviceList()){
                    Switch s = createSwitch(d);
                    layout.addView(s);
                }
            }else{
                showAlertMessage("No devices found!");
            }

        }

        /**
         * Creates custom switches
         * @param deviceInformation - information about a device that this switch will control
         * @return instance of created Switch
         */
        private Switch createSwitch(Device deviceInformation){
            Switch s = new Switch(context);
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
        private void setNonLayoutParams(Switch s, Device deviceInformation){
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