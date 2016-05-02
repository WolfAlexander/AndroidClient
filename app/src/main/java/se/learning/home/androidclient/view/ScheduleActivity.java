package se.learning.home.androidclient.view;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;;
import android.widget.Button;
import android.widget.LinearLayout;

import DTO.Schedule;
import DTO.ScheduledEvent;
import se.learning.home.androidclient.R;
import se.learning.home.androidclient.controller.Controller;
import se.learning.home.androidclient.interfaces.ScheduleObserver;

/**
 * This activity will show schedule of events that are scheduled on
 * server and a button for adding new event to schedule
 */
public class ScheduleActivity extends CustomActivity implements ScheduleObserver {
    private final Controller controller = super.getController();
    private final ScheduleObserver scheduleObserver = this;

    /**
     * This method will be called when this activity is launched
     * It will create add button and call nessesary method for showing schedule to user
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        createEventBttnListener();
        showSchedule();
    }

    /**
     * Creates new thread that will request server for schedule and register
     * this activity as new schedule observers
     */
    private void showSchedule() {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                controller.requestScheduleFromServer(scheduleObserver);
                return null;
            }
        }.execute();
    }

    /**
     * Creates user listener for add new event button
     */
    private void createEventBttnListener() {
        final Button eventButton = (Button) findViewById(R.id.addNewEvent);
        if (eventButton != null) {
            eventButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent nextScreen = new Intent(getApplicationContext(), AddNewEventToSchedule.class);
                    startActivity(nextScreen);
                }
            });
        }
    }

    /**
     * Part of observable pattern - this method will be called when new schedule list will be
     * received from server
     * @param schedule
     */
    @Override
    public void updateSchedule(Schedule schedule) {
        ScheduleActivity.this.runOnUiThread(new ShowSchedule(schedule, this));
    }

    /**
     * This schedule to the user in another in a UI thread
     */
    private class ShowSchedule implements Runnable {
        private Schedule schedule;
        private Context context;

        public ShowSchedule(Schedule schedule, Context context) {
            this.schedule = schedule;
            this.context = context;
        }

        /**
         * This method launched when this thread launches
         * It will create new UI block for each entry in schedule list and add them to the screen
         */
        @Override
        public void run() {
            LinearLayout scheduleLayout = (LinearLayout) findViewById(R.id.scheduleLayout);
            for (ScheduledEvent se : schedule.getSchedule()) {
                scheduleLayout.addView(UIFactory.getInstance().createScheduleBlock(se, context));
            }
        }
    }
}