package com.sakisds.frozenled.comparatos;

import com.sakisds.frozenled.userdata.NotificationPref;

import java.util.Comparator;

/**
 * Created by stratisg on 15/9/2013.
 */
public class NamesComparator implements Comparator<NotificationPref> {
    @Override
    public int compare(NotificationPref notif1, NotificationPref notif2) {
        return notif1.getData().toLowerCase().compareTo(notif2.getData().toLowerCase());
    }
}
