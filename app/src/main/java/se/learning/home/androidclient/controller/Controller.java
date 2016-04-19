package se.learning.home.androidclient.controller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import DTO.Devices;
import DTO.ServerData;
import se.learning.home.androidclient.model.ConnectionToServer;

/**
 * Created by Alexander on 2016-04-19.
 */
public class Controller {
    private ConnectionToServer server;

    public ConnectionToServer connectToServer(ServerData serverData){
        server = ConnectionToServer.getInstance();
        server.setServerData(serverData);
        new Thread(server).start();

        return server;
    }

    public boolean isConnectedToServer(){
        try{
            return server.isConnected();
        }catch (NullPointerException ex){
            return false;
        }
    }

    public Devices getListOfDevices(){
        return server.requestDeviceList();
    }
}
