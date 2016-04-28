package se.learning.home.androidclient.view;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import DTO.Device;

/**
 * IN DEVELOPMENT - NOT READY TO BE USED!!!
 * This singleton factory is responsible for creating user interface objects
 * This factory now can create a switch
 */
public class UIFactory {
    private static UIFactory uiFactory = new UIFactory();

    private UIFactory(){}

    /**
     * @return the only instance of this object
     */
    public static UIFactory getInstance(){
        return uiFactory;
    }

    /**
     * Creates custom switches
     * @param deviceInformation - information about a device that this switch will control
     * @return instance of created Switch
     */
    public Switch createSwitch(Device deviceInformation, Context context){
        Switch s = new Switch(context);
        setNonLayoutParams(s, deviceInformation);
        setLayoutParams(s);
        setEventListener(s);

        return s;
    }

    /**
     * Sets functional parameters to the Switch - id, name, checked/unchecked
     * @param s - Switch instance
     * @param deviceInformation - DTO.Device information about the device
     */
    private void setNonLayoutParams(Switch s, Device deviceInformation){
        s.setId(deviceInformation.getId());
        s.setText(deviceInformation.getName());
        s.setChecked(deviceInformation.getStatus());
    }

    /**
     * Sets layout parameter to the Switch
     * @param s - Switch instance
     */
    private void setLayoutParams(Switch s){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        s.setLayoutParams(params);
        s.setPadding(0, 30, 0, 30);

    }

    /**
     * Sets a listener on the Switch that will wait for user action on the Switch
     * @param s - Switch instance
     */
    private void setEventListener(Switch s){
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //new SwitchDevice(buttonView.getId()).execute();
            }
        });
    }
}
