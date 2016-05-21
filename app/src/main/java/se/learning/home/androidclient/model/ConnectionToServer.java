package se.learning.home.androidclient.model;


import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.SocketFactory;

import DTO.ClientServerTransferObject;
import DTO.ControlDevice;
import DTO.Device;
import DTO.Devices;
import DTO.GetDataRequest;
import DTO.Schedule;
import DTO.ScheduledEvent;
import se.learning.home.androidclient.interfaces.DeviceListObserver;
import se.learning.home.androidclient.interfaces.ScheduleObserver;

/**
 * Singleton class that handles connection and conversation with server
 */
public final class ConnectionToServer implements Runnable{
    private static ConnectionToServer serverInstance = new ConnectionToServer();
    private final String serverIP = "130.237.238.42";
    private final int portNr = 5821;
    private Socket connection;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private ArrayList<DeviceListObserver> deviceListObservers = new ArrayList<>();
    private ArrayList<ScheduleObserver> scheduleObservers = new ArrayList<>();
    private static final Logger log = Logger.getLogger("ConnectionToServerLogger11");

    private ConnectionToServer(){}

    /**
     * @return the only instance of this class
     */
    public static ConnectionToServer getInstance(){
        return serverInstance;
    }

    /**
     * Starts connection to server
     */
    @Override
    public void run() {
        try{
            connectToServer();
            createIOStreams();
            receiveServerMessages();
            log.log(Level.FINE, "Connection to server established!");
        }catch (Exception ex){
            log.log(Level.SEVERE, ex.toString(), ex);
        }
    }

    /**
     * Connecting to server using sockets
     * @throws Exception
     */
    private void connectToServer() throws Exception{
        try{
            SocketFactory socketFactory = SocketFactory.getDefault();
            connection = socketFactory.createSocket(this.serverIP, this.portNr);
        }catch (Exception ex){
            throw new Exception(ex.getMessage());
        }
    }

    /**
     * Create input and output streams
     * @throws Exception
     */
    private void createIOStreams() throws Exception{
        outputStream = new ObjectOutputStream(connection.getOutputStream());
        outputStream.flush();

        inputStream = new ObjectInputStream(connection.getInputStream());
    }

    /**
     * Return boolean value true if connection and IO streams are established or false if not
     * @return boolean value of server status
     */
    public boolean isConnected(){
        return connection != null && outputStream != null && inputStream != null && connection.isConnected();
    }

    /**
     * This method adds new device list observer to list of devices observers
     * @param observer - class that waits for device list
     */
    public void addDeviceListObserver(DeviceListObserver observer){
        if(observer != null)
            deviceListObservers.add(observer);
    }

    /**
     * This method adds new schedule observer to list of schedule observers
     * @param observer - class that waits for schedule
     */
    public void addScheduleObserver(ScheduleObserver observer){
        if(observer != null)
            scheduleObservers.add(observer);
    }

    /**
     * This method gets called when new device list is available
     * from server
     * @param devices - devices from server
     */
    private void notifyAllDeviceListObservers(Devices devices){
        for (DeviceListObserver DLO : deviceListObservers) {
            DLO.updateDeviceList(devices);
        }
    }

    /**
     * This method gets called when new schedule is available
     * from server
     * @param schedule - schedule from server
     */
    private void notifyAllScheduleObservers(Schedule schedule){
        for(ScheduleObserver SO : scheduleObservers){
            SO.updateSchedule(schedule);
        }
    }

    /**
     * Tells server to switch status of the device with given deviceID
     * @param deviceID - id of the device
     */
    public void switchDevice(int deviceID){
        ClientServerTransferObject request = new ControlDevice(deviceID);
        request.setTransferType(ClientServerTransferObject.TransferType.CHANGE_DEVICE_STATUS);
        sendMessage(request);
    }

    /**
     * Creates request for getting list of connected devices
     */
    public void requestDeviceList(){
        ClientServerTransferObject request = new GetDataRequest(GetDataRequest.RequestTypes.DEVICES);
        request.setTransferType(ClientServerTransferObject.TransferType.GET);
        sendMessage(request);
    }

    /**
     * Requests schedule from server
     * @return DTO.Schedule that contains ScheduledEvent:s that containt schedule information for
     * each event
     */
    public void requestSchedule(){
        ClientServerTransferObject request = new GetDataRequest(GetDataRequest.RequestTypes.SCHEDULE);
        request.setTransferType(ClientServerTransferObject.TransferType.GET);
        sendMessage(request);
    }

    /**
     * Submit new device data to server
     * @param device - DTO.Device containing new device data
     */
    public void addNewDevice(Device device){
        ClientServerTransferObject request = device;
        request.setTransferType(ClientServerTransferObject.TransferType.ADD_NEW_DEVICE);
        sendMessage(request);
    }

    public void addNewScheduledEvent(ScheduledEvent scheduledEvent){
        ClientServerTransferObject request = scheduledEvent;
        request.setTransferType(ClientServerTransferObject.TransferType.ADD_NEW_SCHEDULED_EVENT);
        sendMessage(request);
    }

    /**
     * Sends messages to server
     * @param request - request of type ClientServerTransferObject
     */
    private void sendMessage(ClientServerTransferObject request){
        try{
            outputStream.writeObject(request);
            outputStream.flush();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * Gets and returns server response
     * @return ClientServerTransferObject
     */
    private void receiveServerMessages(){
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
            while (true){
                try {
                    ClientServerTransferObject response = (ClientServerTransferObject)inputStream.readObject();
                    handleMessage(response);
                }catch (ClassNotFoundException cnfEx) {
                    log.log(Level.SEVERE, cnfEx.toString(), cnfEx);
                }catch(EOFException eofEx){
                    log.log(Level.WARNING, "Lost connection to server!");
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
            }
        });
    }

    /**
     * This method handles messages receive from server
     * @param response - received response from server
     */
    private void handleMessage(ClientServerTransferObject response){
        switch(response.getTransferType()){
            case DEVICE_LIST_RESPONSE:
                if(response instanceof Devices)
                    notifyAllDeviceListObservers((Devices)response);
            break;

            case SCHEDULE_RESPONSE:
                if(response instanceof Schedule)
                    notifyAllScheduleObservers((Schedule)response);
            break;
        }
    }
}