package com.sakisds.frozenled.fragments;

import android.content.Context;
import android.content.SharedPreferences;

import com.sakisds.frozenled.R;
import com.sakisds.frozenled.adapters.AppNotifListAdapter;
import com.sakisds.frozenled.userdata.DataManager;
import com.sakisds.frozenled.userdata.NotificationType;

/**
 * Created by stratisg on 11/9/2013.
 */
public class AppsListFragment extends DeletableListFragment {
    @Override
    public void setAdapter() {
        // Get prefs and list data
        SharedPreferences prefs = getActivity().getSharedPreferences(DataManager.SHAREDPREFS_NOTIFICATIONS, Context.MODE_PRIVATE);
        mData = DataManager.getNotifications(prefs, NotificationType.BY_PACKAGE);

        // Create adapter
        setListAdapter(new AppNotifListAdapter(getActivity(), R.id.app_name, mData));
    }
}
