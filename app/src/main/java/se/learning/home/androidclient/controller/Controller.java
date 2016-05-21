package se.learning.home.androidclient.controller;

import DTO.Device;
import DTO.ScheduledEvent;
import se.learning.home.androidclient.interfaces.DeviceListObserver;
import se.learning.home.androidclient.interfaces.ScheduleObserver;
import se.learning.home.androidclient.model.ConnectionToServer;

/**
 * Controller takes requests from View to appropriate object
 * and return values from this object to View
 * In this case we don't follow complete MVC pattern - se.learning.home.androidclient.view will have some logic
 */
public final class Controller {
    private final ConnectionToServer server = ConnectionToServer.getInstance();
    private static Controller  controller = new Controller();

    /**
     * This method tells ConnectionToServer object to establish connection
     */
    private Controller (){}

    /**
     * @return the only instance of this class
     */
    public static Controller getInstance(){
        return controller;
    }

    /**
     * This method tells ConnectionToServer object to establish connection
     */
    public void establishConnection(){
        new Thread(server).start();
    }

    /**
     * This method returns status of connection to server
     * @return boolean value true for connection established
     * of false if not or not status could be retrieved
     */
    public boolean isConnectedToServer(){
        try{
            return server.isConnected();
        }catch (NullPointerException ex){
            return false;
        }
    }

    /**
     * @return list of the devices from server
     */
    public void requestListOfDevicesFromServer(DeviceListObserver observer){
        server.addDeviceListObserver(observer);
        server.requestDeviceList();
    }

    /**
     * Request ConnectionToServer object to request server to change status of
     * a device that is connected to server
     * @param deviceID - device identifier
     */
    public void switchDeviceOnServer(int deviceID){
        server.switchDevice(deviceID);
    }

    /**
     * Requests server to add new device
     * @param device - DTO.Device contains new device data
     */
    public void addingDeviceToServer(Device device){
        server.addNewDevice(device);
    }

    /**
     * Tells ConnectionToServer object to Subscribes new Schedule Observer and requests server
     * for schedule list
     * @param observer - reference to ScheduleObserver
     */
    public void requestScheduleFromServer (ScheduleObserver observer){
        server.addScheduleObserver(observer);
        server.requestSchedule();
    }

    /**
     * Requests ConnectionToServer to add new event to server
     * @param scheduledEvent - event data
     */
    public void addNewScheduledEventToServer(ScheduledEvent scheduledEvent){
        server.addNewScheduledEvent(scheduledEvent);
    }
}