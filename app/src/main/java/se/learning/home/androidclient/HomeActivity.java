package se.learning.home.androidclient;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;

import DTO.Device;
import DTO.Devices;
import se.learning.home.androidclient.controller.Controller;
import se.learning.home.androidclient.model.ConnectionToServer;

public class HomeActivity extends AppCompatActivity {
    private final Controller controller = new Controller();
    private ConnectionToServer server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        server = controller.connectToServer(new DTO.ServerData("10.0.2.2", 5821));

        while(!server.isConnected()){}

        System.out.println("--------Connected!");
        new ShowAllDevices(this).execute();
    }

    private class ShowAllDevices extends AsyncTask {
        private Context context;

        public ShowAllDevices(Context context){
            this.context = context;
        }

        @Override
        protected Devices doInBackground(Object[] params) {
            return controller.getListOfDevices();
        }

        @Override
        protected void onPostExecute(Object o) {
            Devices devices = (Devices)o;
            int position = 250;
            RelativeLayout layout = (RelativeLayout) findViewById(R.id.homeLayout);
            System.out.println(devices);
            for(Device d : devices.getDeviceList()){
                Switch s = new Switch(context);
                s.setId(d.getId());
                s.setText(d.getName());
                s.setChecked(d.getStatus());
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, position, 0, 0);
                s.setLayoutParams(params);
                layout.addView(s);
                position += 120;
            }






        }
    }
}











        /*RelativeLayout layout = new RelativeLayout(this);
        HandleServerConnection server = new HandleServerConnection("10.0.2.2", 5821, layout, this);
        server.execute();

        while(!server.getDone()){
            try {
                wait(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Devices devices = server.requestDeviceList();

        Switch[] switches = new Switch[devices.getNumberOfDevices()];
        for(int i = 0; i < devices.getNumberOfDevices(); i++){
            switches[i] = new Switch(this);
            layout.addView(switches[i]);
        }

        setContentView(layout);*/