package se.learning.home.androidclient.model;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.SocketFactory;

import DTO.ClientServerTransferObject;
import DTO.ControlDevice;
import DTO.Device;
import DTO.Devices;
import DTO.GetDataRequest;
import DTO.Schedule;
import DTO.ScheduledEvent;
import DTO.ServerData;
import se.learning.home.androidclient.interfaces.DeviceListObserver;
import se.learning.home.androidclient.interfaces.ScheduleObserver;

/**
 * Singleton class that handles connection and conversation with server
 */
public final class ConnectionToServer implements Runnable{
    private static ConnectionToServer serverInstance = new ConnectionToServer();
    private ServerData serverData;
    private Socket connection;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private ArrayList<DeviceListObserver> deviceListObservers = new ArrayList<>();
    private ArrayList<ScheduleObserver> scheduleObservers = new ArrayList<>();

    private ConnectionToServer(){}

    /**
     * @return the only instance of this class
     */
    public static ConnectionToServer getInstance(){
        return serverInstance;
    }

    /**
     * Initializes server data
     * @param serverData - DTO.ServerData that contains information for connection to a server
     */
    public void setServerData(ServerData serverData){
        this.serverData = serverData;
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
        }catch (Exception ex){
            System.err.println(ex.getMessage());
        }
    }

    /**
     * Connecting to server using sockets
     * @throws Exception
     */
    private void connectToServer() throws Exception{
        try{
            SocketFactory socketFactory = SocketFactory.getDefault();
            connection = socketFactory.createSocket(serverData.getServerIP(), serverData.getPortNr());
            System.out.println("----------Socket created!------------");
        }catch (ConnectException conEx){
            System.out.println("TIME OUT");
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
        System.out.println("----------IO Streams created!------------");
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
        deviceListObservers.add(observer);
    }

    /**
     * This method adds new schedule observer to list of schedule observers
     * @param observer - class that waits for schedule
     */
    public void addScheduleObserver(ScheduleObserver observer){
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
        sendMessage(request);
    }

    /**
     * Creates request for getting list of connected devices
     */
    public void requestDeviceList(){
        ClientServerTransferObject request = new GetDataRequest(GetDataRequest.RequestTypes.DEVICES);
        sendMessage(request);
    }

    /**
     * Requests schedule from server
     * @return DTO.Schedule that contains ScheduledEvent:s that containt schedule information for
     * each event
     */
    public void requestSchedule(){
        ClientServerTransferObject request = new GetDataRequest(GetDataRequest.RequestTypes.SCHEDULE);
        sendMessage(request);
    }

    /**
     * Submit new device data to server
     * @param device - DTO.Device containing new device data
     */
    public void addNewDevice(Device device){
        ClientServerTransferObject request = device;
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
            System.out.println("--------- Message sended");
        }catch (Exception ex){
            ex.printStackTrace();
            System.err.println("----------ERROR SENDING !!!!!!!!!! ------------ :((((");
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
                        System.out.println("Waiting...");
                        ClientServerTransferObject response = (ClientServerTransferObject)inputStream.readObject();
                        System.out.println("Got something!");
                        handleMessage(response);
                    }catch (ClassNotFoundException cnfEx){
                        System.out.println("------- Don't understand what server tells me! -------------");
                    }catch (Exception ex){
                        ex.printStackTrace();
                        System.out.println("-----------Aaaaaaaa... Something really wrong!!!!");
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
        if(response instanceof Devices){
            notifyAllDeviceListObservers((Devices)response);
        }else if(response instanceof Schedule){
            System.out.println("Received schedule");
            notifyAllScheduleObservers((Schedule)response);
        }
    }
}
