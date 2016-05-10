package se.learning.home.androidclient.factories;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import DTO.ScheduledEvent;

/**
 * This factory will create new GUI representation of schedule entry
 * This representation of schedule entry contains device name, start and end date and time
 * of the event and status of the device (on/off) during event time
 */
public class ScheduleEntryUIFactory {
    private static ScheduleEntryUIFactory scheduleEntry = new ScheduleEntryUIFactory();

    private ScheduleEntryUIFactory(){}

    /**
     * @return only instance of this object
     */
    public static ScheduleEntryUIFactory getInstance(){
        return scheduleEntry;
    }

    /**
     * This method create new UI structure that will contain schedule event information
     * @param scheduledEvent - data of the event
     * @param context - UI context
     * @return block that shows sheduled event data
     */
    public LinearLayout createScheduleBlock(ScheduledEvent scheduledEvent, Context context){
        LinearLayout scheduleBlock = createOuterParentBlock(context);
        TextView nameBlock = createInnerNameBlock(context, scheduledEvent);
        TextView dateBlock = createInnerDateBlock(context, scheduledEvent);

        scheduleBlock.addView(nameBlock);
        scheduleBlock.addView(dateBlock);

        setDeviceStatus(scheduleBlock, scheduledEvent, context);

        return scheduleBlock;
    }

    /**
     * Creates layout which will show event data
     * @param context - UI context
     * @return layout to show scheduled event
     */
    private LinearLayout createOuterParentBlock(Context context){
        LinearLayout block = new LinearLayout(context);
        block.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 20, 0, 0);
        block.setLayoutParams(params);

        return block;
    }

    /**
     * Creates a block that will contain device name inside outer schedule block
     * @param context - UI context
     * @param data - data of type ScheduledEvent that contains device name
     * @return created block with device name
     */
    private TextView createInnerNameBlock(Context context, ScheduledEvent data){
        TextView deviceNameTextView = new TextView(context);
        deviceNameTextView.setText(data.getDeviceName());

        return deviceNameTextView;
    }

    /**
     * Creates block and fills in date for event execution and if exists end date
     * @param context - UI context
     * @param data - data about the event that contains date data
     * @return block with date
     */
    private TextView createInnerDateBlock(Context context, ScheduledEvent data){
        TextView scheduleDateTextView = new TextView(context);
        String dateTimeAsText;

        if(data.getEndDateTime() != null)
             dateTimeAsText = data.getStartDateTime() + " - " + data.getEndDateTime();
        else
            dateTimeAsText = data.getStartDateTime();

        scheduleDateTextView.setText(dateTimeAsText);

        return scheduleDateTextView;
    }

    /**
     * Sets description data to schedule block
     */
    private void setDeviceStatus(LinearLayout block, ScheduledEvent data, Context context){
        TextView descriptionTextView = new TextView(context);
        String deviceText = "Device shall be: " + data.getNewDeviceStatus();
        descriptionTextView.setText(deviceText);
        block.addView(descriptionTextView);
    }
}
