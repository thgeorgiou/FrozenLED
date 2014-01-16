package com.sakisds.frozenled;

import android.*;
import android.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import com.sakisds.frozenled.userdata.DataManager;
import com.sakisds.frozenled.userdata.NotificationPref;
import com.sakisds.frozenled.userdata.NotificationType;

import java.util.List;

/**
 * Created by stratisg on 5/9/2013.
 */
public class FrozenLEDService extends NotificationListenerService {
    @Override
    public void onNotificationPosted(StatusBarNotification statusBarNotification) {
        SharedPreferences appPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // Skip if notification is out of stuff that we should handle
        if (Integer.valueOf(appPrefs.getString("pref_key_notif_priority", "0")) > statusBarNotification.getNotification().priority) {
            return;
        }
        if (appPrefs.getBoolean("pref_key_notif_ongoing", true) && statusBarNotification.isOngoing()) {
            return;
        }

        // Get data
        String packageName = statusBarNotification.getPackageName();
        String text = statusBarNotification.getNotification().tickerText.toString().toLowerCase();
        SharedPreferences prefs = getSharedPreferences(DataManager.SHAREDPREFS_NOTIFICATIONS, MODE_PRIVATE);
        NotificationPref result = null;

        // Check if it's a missed call
        if(packageName.contains("com.android.phone") && appPrefs.getBoolean("pref_missedcalls_handle", false)) {
            lightLED(prefs.getInt("pref_missedcalls_color", getResources().getColor(R.color.holo_red_light)),
                    prefs.getInt("pref_missedcalls_pulse", 1000));
            return;
        }

        // Test for name
        List<NotificationPref> notificationPrefs = DataManager.getNotifications(prefs, NotificationType.BY_NAME);
        for (int i = 0; i < notificationPrefs.size() && result == null; i++) {
            NotificationPref notif = notificationPrefs.get(i);
            if (text.contains(notif.getData().toLowerCase())) {
                result = notif;
            }
        }

        // Test for package if name is not found
        if (result == null)
            notificationPrefs = DataManager.getNotifications(prefs, NotificationType.BY_PACKAGE);
        for (int i = 0; i < notificationPrefs.size() && result == null; i++) {
            NotificationPref notif = notificationPrefs.get(i);
            if (packageName.contains(notif.getData().toLowerCase())) {
                result = notif;
            }
        }
        // If there is a result handle LED
        if (result != null) {
            lightLED(result.getColor(), result.getPulseSpeed());
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification statusBarNotification) {
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.cancelAll(); // Cancel other notifications that might exist.
    }

    private void lightLED(int color, int pulseSpeed) {
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.cancelAll(); // Cancel other notifications that might exist.

        Notification notif = new Notification();
        notif.ledARGB = color;
        notif.flags = Notification.FLAG_SHOW_LIGHTS;
        notif.ledOnMS = pulseSpeed;
        notif.ledOffMS = pulseSpeed;
        notif.priority = Notification.PRIORITY_MAX;
        nm.notify(101, notif);
    }
}
