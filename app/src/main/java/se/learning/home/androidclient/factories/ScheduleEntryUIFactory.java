package se.learning.home.androidclient.factories;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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
        scheduleDateTextView.setText(data.getEndDateTime());
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
