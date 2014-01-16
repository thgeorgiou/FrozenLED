package com.sakisds.frozenled;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.DialogInterface;

/**
 * Created by stratisg on 17/9/2013.
 */
public class TestNotification {
    public static void show(Activity activity, int color, int pulseSpeed) {
        final NotificationManager nm = (NotificationManager) activity.getSystemService(activity.NOTIFICATION_SERVICE);
        nm.cancelAll(); // Cancel other notifications that might exist.

        // Display test notification
        Notification notif = new Notification();
        notif.ledARGB = color;
        notif.flags = Notification.FLAG_SHOW_LIGHTS;
        notif.ledOnMS = pulseSpeed;
        notif.ledOffMS = pulseSpeed;
        notif.priority = Notification.PRIORITY_MAX;
        nm.notify(102, notif);

        // Show dialog
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(activity);

        dlgAlert.setMessage(activity.getString(R.string.test_dialog));
        dlgAlert.setTitle("Test");
        dlgAlert.setPositiveButton("Dismiss",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        nm.cancelAll();
                    }
                });
        dlgAlert.setCancelable(false);
        dlgAlert.create().show();
    }
}
