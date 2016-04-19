package se.learning.home.androidclient;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.Switch;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Executors;

import javax.net.SocketFactory;

import DTO.ClientServerTransferObject;
import DTO.Device;
import DTO.Devices;
import DTO.GetDataRequest;

public class HandleServerConnection extends AsyncTask<String, Void, String> {
    private Socket connection;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private String serverIP;
    private int portNr;
    private RelativeLayout layout;
    private Context context;
    private boolean done = false;

    public HandleServerConnection(String serverIP, int portNr, RelativeLayout layout, Context context) {
        this.serverIP = serverIP;
        this.portNr = portNr;
        this.layout = layout;
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {
        try{
            connectToServer();
            createIOStreams();
            this.done = true;
        }catch (Exception ex){
            System.err.println(ex.getMessage());
        }

        return "";
    }

    public boolean getDone(){
        return done;
    }

    /**
     * Connecting to server using sockets
     * @throws Exception
     */
    private void connectToServer() throws Exception{
        SocketFactory socketFactory = SocketFactory.getDefault();
        connection = socketFactory.createSocket(serverIP, portNr);
        System.out.println("----------Socket created!------------");
    }

    /**
     * Create input and output streams
     * @throws Exception
     */
    private void createIOStreams() throws Exception{
        outputStream = new ObjectOutputStream(connection.getOutputStream());
        outputStream.flush();

        inputStream = new ObjectInputStream(connection.getInputStream());
        System.out.println("----------IO Streams created!------------");
    }

    /**
     * Creates request for getting list of connected devices
     */
    public Devices requestDeviceList(){
        ClientServerTransferObject request = new GetDataRequest("devices");
        if(outputStream == null)
            System.out.println("--------------------Null");
        sendMessage(request);
        return (Devices)getUserResponse();
    }

    /**
     * Sends messages to server
     * @param request - request of type ClientServerTransferObject
     */
    private void sendMessage(ClientServerTransferObject request){
        try{
            outputStream.writeObject(request);
            outputStream.flush();
            System.out.println("--------- Message sended");
        }catch (Exception ex){
            ex.printStackTrace();
            System.err.println("----------ERROR SENDING !!!!!!!!!! ------------ :((((");
        }
    }

    /**
     * Gets and returns user response
     * @return ClientServerTransferObject
     */
    private ClientServerTransferObject getUserResponse(){
        try {
            ClientServerTransferObject response = (ClientServerTransferObject)inputStream.readObject();
            return response;
        }catch (ClassNotFoundException cnfEx){
            System.out.println("------- Don't understand what server tells me! -------------");
        }catch (Exception ex){
            ex.printStackTrace();
            System.out.println("-----------Aaaaaaaa... Something really wrong!!!!");
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result){}
}
