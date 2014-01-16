package com.sakisds.frozenled.userdata;

import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

public class DataManager {
    public static final String SHAREDPREFS_NOTIFICATIONS = "notifstorage";
    public static final String SHAREDPREFS_SETTINGS = "settingsstorage";

    private DataManager() {
    }

    public static void addNotification(SharedPreferences prefs, NotificationPref notif) {
        int currentIndex = prefs.getInt("notifCount", 0) + 1;

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("notif_" + currentIndex + "_data", notif.getData());
        editor.putInt("notif_" + currentIndex + "_type", notif.getType().convertToInt());
        editor.putInt("notif_" + currentIndex + "_color", notif.getColor());
        editor.putInt("notif_" + currentIndex + "_pulse", notif.getPulseSpeed());
        editor.putInt("notifCount", currentIndex);
        editor.commit();
    }

    public static void removeNotification(SharedPreferences prefs, NotificationPref notif) {
        List<NotificationPref> list = getNotifications(prefs, NotificationType.ANY);

        // Remove notification
        for (int i = 0; i < list.size(); i++) {
            NotificationPref notification = list.get(i);

            if (notification.getData().equals(notif.getData())) {
                list.remove(notification);
                i = list.size(); // Stop the loop
            }
        }

        // Clean old data
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();

        // Save new list
        for (int i = 0; i < list.size(); i++) {
            addNotification(prefs, list.get(i));
        }
    }

    public static void replaceNotification(SharedPreferences prefs, NotificationPref oldNotif, NotificationPref newNotif) {
        removeNotification(prefs, oldNotif);
        addNotification(prefs, newNotif);
    }

    public static List<NotificationPref> getNotifications(SharedPreferences prefs, NotificationType wantedType) {
        int wantedTypeInt = wantedType.convertToInt();

        ArrayList<NotificationPref> list = new ArrayList<NotificationPref>();

        int totalNotifs = prefs.getInt("notifCount", 0);
        for (int i = 1; i <= totalNotifs; i++) {
            int notifType = prefs.getInt("notif_" + i + "_type", -1);

            if (notifType == wantedTypeInt || wantedType == NotificationType.ANY) {
                String notifData = prefs.getString("notif_" + i + "_data", "InvalidData");
                int notifColor = prefs.getInt("notif_" + i + "_color", -1);
                int notifPulseSpeed = prefs.getInt("notif_" + i + "_pulse", 1000);

                list.add(new NotificationPref(NotificationType.convertFromInt(notifType), notifData,
                        notifColor, notifPulseSpeed));
            }
        }

        return list;
    }
}
