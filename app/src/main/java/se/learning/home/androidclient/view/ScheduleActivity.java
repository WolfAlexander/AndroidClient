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
 * server
 */
public class ScheduleActivity extends CustomActivity implements ScheduleObserver {
    private final Controller controller = super.getController();
    private final ScheduleObserver scheduleObserver = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        eventBttn();
        showSchedule();
    }

    private void showSchedule() {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                controller.requestScheduleFromServer(scheduleObserver);
                return null;
            }
        }.execute();
    }

    private void eventBttn() {
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

    @Override
    public void updateSchedule(Schedule schedule) {
        ScheduleActivity.this.runOnUiThread(new ShowSchedule(schedule, this));
    }

    private class ShowSchedule implements Runnable {
        private Schedule schedule;
        private Context context;

        public ShowSchedule(Schedule schedule, Context context) {
            this.schedule = schedule;
            this.context = context;
        }

        @Override
        public void run() {
            LinearLayout scheduleLayout = (LinearLayout) findViewById(R.id.scheduleLayout);
            for (ScheduledEvent se : schedule.getSchedule()) {
                System.out.println(se.getDeviceName());
                scheduleLayout.addView(UIFactory.getInstance().createScheduleBlock(se, context));
            }
        }
    }
}