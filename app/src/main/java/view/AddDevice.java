package view;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import DTO.Device;
import se.learning.home.androidclient.R;
import se.learning.home.androidclient.controller.Controller;

/**
 * This activity is used by user to add new devices
 * to the server
 */
public class AddDevice extends CustomActivity {
    private final Controller controller = super.getController();
    private String protocolChosen;
    private String modelChosen;
    private String deviceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);

        createBackBttn();
        submitDeviceBttn();
        handleProtocolSpinner();
        handleModelSpinner();
    }

    /**
     * Creates and handles back button and its action
     */
    private void createBackBttn() {
        final Button backBttn = (Button) findViewById(R.id.backBttn);
        if (backBttn != null) {
            backBttn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    /**
     * Handles protocol spinner by writing chosen item to a field
     */
    private void handleProtocolSpinner(){
        Spinner protocolSpinner = (Spinner) findViewById(R.id.protocolSpinner);
        if (protocolSpinner != null) {
            protocolSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    protocolChosen = (String) parent.getItemAtPosition(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    protocolChosen = null;
                }
            });
        }
    }

    /**
     * Handles protocol spinner by writing chosen item to a field
     */
    private void handleModelSpinner(){
        Spinner modelSpinner = (Spinner) findViewById(R.id.modelSpinner);
        if (modelSpinner != null) {
            modelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    modelChosen = (String) parent.getItemAtPosition(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    modelChosen = null;
                }
            });
        }
    }

    /**
     * Creates and handles submit device button and its action
     */
    private void submitDeviceBttn() {
        final Button submitNewDeviceBttn = (Button) findViewById(R.id.submitNewDeviceButton);
        if (submitNewDeviceBttn != null) {
            submitNewDeviceBttn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (protocolChosen == null || modelChosen == null) {
                        System.out.println("Choose stuff!!!!!!!!");
                    } else {
                        EditText deviceNameTextView = (EditText) findViewById(R.id.editText);
                        deviceName = deviceNameTextView.getText().toString();
                        new SubmitNewDevice().execute();
                    }
                }
            });
        }
    }


    /**
     * UI Thread sends new device data to server
     */
    private class SubmitNewDevice extends AsyncTask{
        /**
         *
         * @param params
         * @return in this case null
         */
        @Override
        protected Object doInBackground(Object[] params) {
            System.out.println("Submint");
            controller.addingDeviceToServer(new Device(deviceName, protocolChosen, modelChosen));
            return null;
        }

        /**
         * This method is called when doInBackground is done
         */
        @Override
        protected void onPreExecute() {
            System.out.println("Done");
            finish();
        }
    }
}
