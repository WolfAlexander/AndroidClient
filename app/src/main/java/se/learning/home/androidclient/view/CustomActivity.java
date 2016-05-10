package se.learning.home.androidclient.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;

import se.learning.home.androidclient.controller.Controller;

/**
 * This class will be extended by all activities in this app
 * This class contains controller reference and alert box functionality
 */
public class CustomActivity extends AppCompatActivity{
    private final Controller controller = Controller.getInstance();

    /**
     * @return Controller instance
     */
    public final Controller getController(){
        return this.controller;
    }

    /**
     * This method will show a message to user in an alert box
     * and a button to close the box
     * @param message - String message that will be shown to user
     */
    public final void showAlertMessage(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builder.create();
        builder.show();

    }
}