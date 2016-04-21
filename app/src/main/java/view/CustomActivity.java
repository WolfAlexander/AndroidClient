package view;

import android.support.v7.app.AppCompatActivity;

import se.learning.home.androidclient.controller.Controller;

/**
 * Created by Alexander on 2016-04-21.
 */
public abstract class CustomActivity extends AppCompatActivity{
    private final Controller controller = new Controller();

    public Controller getController(){
        return this.controller;
    }
}
