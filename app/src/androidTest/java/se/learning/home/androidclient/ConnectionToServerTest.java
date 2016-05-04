package se.learning.home.androidclient;

import android.test.InstrumentationTestCase;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;

import DTO.Device;
import DTO.Devices;
import DTO.Schedule;
import DTO.ScheduledEvent;
import se.learning.home.androidclient.interfaces.DeviceListObserver;
import se.learning.home.androidclient.interfaces.ScheduleObserver;
import se.learning.home.androidclient.model.ConnectionToServer;

public class ConnectionToServerTest extends InstrumentationTestCase{
    private final String serverIP = "10.0.2.2";
    private ConnectionToServer connectionToServer;
    private boolean devicesNotified = false;
    private boolean scheduleNotified = false;

    /**
     * Will be called before every test
     * @throws Exception
     */
    @Override
    public void setUp() throws Exception {
        connectionToServer = ConnectionToServer.getInstance();
    }

    /**
     * Testing if getInstance() method is actually returns wright instance
     */
    public void testGetInstance(){
        Assert.assertEquals(ConnectionToServer.class, connectionToServer.getClass());
    }

    /**
     * Testing if connectToServer() does not throw any exceptions if connection was made
     */
    public void testConnectToServer(){
        assertTrue(tryToConnectToServer());
    }

    private boolean tryToConnectToServer(){
        try {
            Method connectToServerMethod = ConnectionToServer.class.getDeclaredMethod("connectToServer", null);
            connectToServerMethod.setAccessible(true);
            connectToServerMethod.invoke(connectionToServer);
            return true;
        } catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Testing if createIOStreams() method does not throw ant exception if IO streams created
     */
    public void testCreateIOStreams(){
        assertTrue(tryToCreateIOStreams());
    }

    private boolean tryToCreateIOStreams(){
        try{
            Method createIOStreamsMethod = ConnectionToServer.class.getDeclaredMethod("createIOStreams", null);
            createIOStreamsMethod.setAccessible(true);
            createIOStreamsMethod.invoke(connectionToServer);
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Tests if isConnected() method returns true is all other tests are passed
     */
    public void testIsConnected(){
        assertEquals(true, connectionToServer.isConnected());
    }

    /**
     * Test if addDeviceListObserver() will fail if null is entered and succeed if wright object is send
     */
    public void testAddDeviceListObserver(){
        DeviceListObserver deviceListObserver = new DeviceListObserver() {
            @Override
            public void updateDeviceList(Devices devices) {
                System.out.println("Updated!");
            }
        };

        connectionToServer.addDeviceListObserver(deviceListObserver);
        connectionToServer.addDeviceListObserver(null);
    }

    /**
     * Tests if addScheduleObserver() will fail if null is entered and succeed if wright object is send
     */
    public void testAddScheduleObserver(){
        ScheduleObserver so = new ScheduleObserver() {
            @Override
            public void updateSchedule(Schedule schedule) {
                System.out.println("Updated!");
            }
        };

        connectionToServer.addScheduleObserver(so);
        connectionToServer.addScheduleObserver(null);
    }

    /**
     * Tests if observers will be notified when new devices are available
     */
    public void testNotifyAllDeviceListObservers(){
        DeviceListObserver deviceListObserver = new DeviceListObserver() {
            @Override
            public void updateDeviceList(Devices devices) {
                devicesNotified = true;
            }
        };

        connectionToServer.addDeviceListObserver(deviceListObserver);
        tryNotifyAllDeviceListsObservers();
        assertTrue(devicesNotified);
    }

    private void tryNotifyAllDeviceListsObservers(){
        ArrayList<Device> deviceList = new ArrayList<>();
        deviceList.add(new Device(1, "TestName", true));
        Devices devices = new Devices(deviceList);

        try{
            Method notifyAllDeviceListObserversMethod = ConnectionToServer.class.getDeclaredMethod("notifyAllDeviceListObservers", Devices.class);
            notifyAllDeviceListObserversMethod.setAccessible(true);
            notifyAllDeviceListObserversMethod.invoke(connectionToServer, devices);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * Tests if observers will be notified when new schedule is available
     */
    public void testNotifyAllScheduleObservers(){
        ScheduleObserver scheduleObserver = new ScheduleObserver() {
            @Override
            public void updateSchedule(Schedule schedule) {
                scheduleNotified = true;
            }
        };

        connectionToServer.addScheduleObserver(scheduleObserver);
        tryNotifyAllScheduleObservers();
        assertTrue(scheduleNotified);
    }

    private void tryNotifyAllScheduleObservers(){
        ArrayList<ScheduledEvent> scheduledEvents = new ArrayList<>();
        scheduledEvents.add(new ScheduledEvent(1, "TestName", "2016-05-04 14:37", null, "ON"));
        Schedule schedule = new Schedule(scheduledEvents);

        try{
            Method notifyAllScheduleObservers = ConnectionToServer.class.getDeclaredMethod("notifyAllScheduleObservers", Schedule.class);
            notifyAllScheduleObservers.setAccessible(true);
            notifyAllScheduleObservers.invoke(connectionToServer, schedule);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}