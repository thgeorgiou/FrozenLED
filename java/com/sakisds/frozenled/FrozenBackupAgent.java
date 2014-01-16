package com.sakisds.frozenled;

import android.app.backup.BackupAgentHelper;
import android.app.backup.SharedPreferencesBackupHelper;

import com.sakisds.frozenled.userdata.DataManager;

public class FrozenBackupAgent extends BackupAgentHelper {
    static final String MY_PREFS_BACKUP_KEY = "frozenledprefs";

    public void onCreate() {
        SharedPreferencesBackupHelper helper =
                new SharedPreferencesBackupHelper(this, DataManager.SHAREDPREFS_NOTIFICATIONS);
        addHelper(MY_PREFS_BACKUP_KEY, helper);
    }
}