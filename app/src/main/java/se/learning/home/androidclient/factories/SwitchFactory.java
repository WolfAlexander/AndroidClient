package se.learning.home.androidclient.factories;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;

import DTO.Device;

/**
 * This factory creates custom representation of the switch
 * In this case it is default android.widget.Switch layout filled with custom information
 */
public class SwitchFactory {
    private static SwitchFactory switchFactory = new SwitchFactory();

    private SwitchFactory(){}

    public static SwitchFactory getInstance(){
        return switchFactory;
    }

    /**
     * This method will create and return a new custom made switch
     * @param deviceInformation - information about a device that this switch will control
     * @return instance of created Switch
     */
    public Switch createSwitch(Device deviceInformation, Context context){
        Switch s = new Switch(context);
        setNonLayoutSwitchParams(s, deviceInformation);
        setLayoutSwitchParams(s);

        return s;
    }

    /**
     * Sets functional parameters to the Switch - id, name, checked/unchecked
     * @param s - Switch instance
     * @param deviceInformation - DTO.Device information about the device
     */
    private void setNonLayoutSwitchParams(Switch s, Device deviceInformation){
        s.setId(deviceInformation.getId());
        s.setText(deviceInformation.getName());
        s.setChecked(deviceInformation.getStatus());
    }

    /**
     * Sets layout parameter to the Switch
     * @param s - Switch instance
     */
    private void setLayoutSwitchParams(Switch s){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        s.setLayoutParams(params);
        s.setPadding(0, 30, 0, 30);

    }
}
