package com.sakisds.frozenled.comparatos;

import com.sakisds.frozenled.userdata.NotificationPref;

import java.util.Comparator;

/**
 * Created by stratisg on 15/9/2013.
 */
public class NotificationPrefComparator implements Comparator<NotificationPref> {
    @Override
    public int compare(NotificationPref notif1, NotificationPref notif2) {
        return notif1.getStoredName().toLowerCase().compareTo(notif2.getStoredName().toLowerCase());
    }
}
