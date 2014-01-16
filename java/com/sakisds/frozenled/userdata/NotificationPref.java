package com.sakisds.frozenled.userdata;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

/**
 * Created by stratisg on 7/9/2013.
 */
public class NotificationPref {
    private NotificationType mType;
    private String mData; // If type is by package, this stores the app package. Otherwise it stores
    // user's selected text.
    private int mColor;
    private int mPulseSpeed;
    private String mAppName; // Temporary used to sort list

    public NotificationPref(NotificationType type, String data, int color, int pulseSpeed) {
        mType = type;
        mData = data;
        mColor = color;
        mPulseSpeed = pulseSpeed;
    }

    public NotificationType getType() {
        return mType;
    }

    public int getColor() {
        return mColor;
    }

    public String getData() {
        return mData;
    }

    public int getPulseSpeed() {
        return mPulseSpeed;
    }

    public ApplicationInfo getApplicationInfo(PackageManager pm) throws PackageManager.NameNotFoundException {
        if (mType == NotificationType.BY_PACKAGE) {
            return pm.getApplicationInfo(mData, PackageManager.GET_META_DATA);
        } else {
            return null;
        }
    }

    public void storeName(String name) {
        mAppName = name;
    }

    public String getStoredName() {
        return mAppName;
    }
}
