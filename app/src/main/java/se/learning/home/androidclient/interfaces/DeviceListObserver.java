package se.learning.home.androidclient.interfaces;

import DTO.Devices;

/**
 * Created by Alexander on 2016-04-28.
 */
public interface DeviceListObserver {
    void updateDeviceList(Devices devices);
}
