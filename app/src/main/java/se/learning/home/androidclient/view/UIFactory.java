package se.learning.home.androidclient.view;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import DTO.Device;
import DTO.ScheduledEvent;

/**
 * IN DEVELOPMENT - ALMOST READY!
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
     * This method create new UI structure that will contain schedule event information
     * @param scheduledEvent
     * @param context
     * @return
     */
    public LinearLayout createScheduleBlock(ScheduledEvent scheduledEvent, Context context){
        LinearLayout scheduleBlock = createOuterParentBlock(context);
        LinearLayout nameDateBlock = createInnerNameAndDateBlock(context);
        setDataToInnerBlock(nameDateBlock, scheduledEvent, context);
        scheduleBlock.addView(nameDateBlock);
        setDataToOuterBlock(scheduleBlock, scheduledEvent, context);

        return scheduleBlock;
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

    /**
     * Creates
     * @param context
     * @return
     */
    private LinearLayout createOuterParentBlock(Context context){
        LinearLayout block = new LinearLayout(context);
        block.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        block.setLayoutParams(params);

        return block;
    }

    private LinearLayout createInnerNameAndDateBlock(Context context){
        LinearLayout nameDateBlock = new LinearLayout(context);
        nameDateBlock.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        nameDateBlock.setLayoutParams(params);

        return nameDateBlock;
    }

    private void setDataToInnerBlock(LinearLayout nameDateBlock, ScheduledEvent data, Context context){
        TextView deviceNameTextView = new TextView(context);
        deviceNameTextView.setText(data.getDeviceName());
        deviceNameTextView.setTextColor(Color.BLACK);
        deviceNameTextView.setShadowLayer(10, 0, 0, Color.WHITE);
        nameDateBlock.addView(deviceNameTextView);

        TextView scheduleDateTextView = new TextView(context);
        scheduleDateTextView.setText(data.getScheduleDate());
        scheduleDateTextView.setGravity(Gravity.RIGHT);
        scheduleDateTextView.setTextColor(Color.WHITE);
        scheduleDateTextView.setShadowLayer(10, 0, 0, Color.BLACK);
        nameDateBlock.addView(scheduleDateTextView);
    }

    /**
     * Sets description data to schedule block
     */
    private void setDataToOuterBlock(LinearLayout block, ScheduledEvent data, Context context){
        TextView descriptionTextView = new TextView(context);
        descriptionTextView.setText(data.getNewDeviceStatus());
        block.addView(descriptionTextView);
    }
}
