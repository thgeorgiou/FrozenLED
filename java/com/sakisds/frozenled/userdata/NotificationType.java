package com.sakisds.frozenled.userdata;

/**
 * Created by stratisg on 11/9/2013.
 */
public enum NotificationType {
    ANY, BY_NAME, BY_PACKAGE;

    public int convertToInt() {
        switch (this) {
            default:
                return -1;
            case ANY:
                return 0;
            case BY_NAME:
                return 1;
            case BY_PACKAGE:
                return 2;
        }
    }

    public static NotificationType convertFromInt(int i) {
        switch (i) {
            default:
                return null;
            case 0:
                return ANY;
            case 1:
                return BY_NAME;
            case 2:
                return BY_PACKAGE;
        }
    }
}
