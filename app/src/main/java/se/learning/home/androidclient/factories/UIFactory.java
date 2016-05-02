package se.learning.home.androidclient.factories;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.TextView;

import DTO.Device;
import DTO.ScheduledEvent;

/**
 * This singleton factory is used to create user interface part
 * Only those parts that need to be created programmatically can be found here
 * Other ways look at xml-files in res/layout folder
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
     * Creates new custom switch that will control
     * @param deviceInformation - information about the device that switch will control
     * @param context - context of the activity where new switch will be created
     * @return new switch
     */
    public View createSwitch(Device deviceInformation, Context context){
        return SwitchFactory.getInstance().createSwitch(deviceInformation, context);
    }

    /**
     * Creates UI for each scheduled event
     * @param scheduledEvent - information about scheduled event
     * @param context - context of the activity where new schedule entry will be created
     * @return layout that will represent GUI schedule entry
     */
    public View createScheduleEntryView(ScheduledEvent scheduledEvent, Context context){
        return ScheduleEntryUIFactory.getInstance().createScheduleBlock(scheduledEvent, context);
    }

    /**
     * Creates pop-up box for time picking
     * @param output - output TextView where users chosen time will be outputted
     * @return reference to DialogFragment for time picking
     */
    public DialogFragment createTimePicker(TextView output){
        TimePickerFactory timePickerFactory = new TimePickerFactory();
        timePickerFactory.setOutputElement(output);
        return timePickerFactory;
    }

    /**
     * Creates pop-up box for date picking
     * @param output - output TextView where users chosen date will be outputted
     * @return reference to DialogFragment for date picking
     */
    public DialogFragment createDatePicker(TextView output){
        DatePickerFactory datePickerFactory = new DatePickerFactory();
        datePickerFactory.setOutputElement(output);
        return datePickerFactory;
    }
}
