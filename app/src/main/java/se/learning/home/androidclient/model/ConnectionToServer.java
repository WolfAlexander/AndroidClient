package se.learning.home.androidclient.model;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;

import javax.net.SocketFactory;

import DTO.ClientServerTransferObject;
import DTO.ControlDevice;
import DTO.Device;
import DTO.Devices;
import DTO.GetDataRequest;
import DTO.Schedule;
import DTO.ScheduledEvent;
import DTO.ServerData;

/**
 * Singleton class that handles connection and conversation with server
 */
public final class ConnectionToServer implements Runnable{
    private static ConnectionToServer serverInstance = new ConnectionToServer();
    private ServerData serverData;
    private Socket connection;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

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
    public Devices requestDeviceList(){
        ClientServerTransferObject request = new GetDataRequest("devices");
        sendMessage(request);
        return (Devices)getUserResponse();
    }

    /**
     * Requests schedule from server
     * @return DTO.Schedule that contains ScheduledEvent:s that containt schedule information for
     * each event
     */
    public Schedule requestSchedule(){
        ClientServerTransferObject request = new GetDataRequest("schedule");
        sendMessage(request);
        return (Schedule) getUserResponse();
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
}
