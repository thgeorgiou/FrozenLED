package com.sakisds.frozenled.adapters;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sakisds.frozenled.R;
import com.sakisds.frozenled.comparatos.ApplicationInfoComparator;
import com.sakisds.frozenled.comparatos.NotificationPrefComparator;
import com.sakisds.frozenled.userdata.NotificationPref;
import com.sakisds.frozenled.views.ColorSwatch;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AppNotifListAdapter extends ArrayAdapter<NotificationPref> {
    private List<NotificationPref> mListData = null;
    private Context mContext;
    private PackageManager mPackageName;

    public AppNotifListAdapter(Context context, int textViewResourceId,
                               List<NotificationPref> listData) {
        super(context, textViewResourceId, listData);
        mContext = context;
        mPackageName = context.getPackageManager();
        mListData = listData;

        // Fill in names
        for (NotificationPref notif : listData) {
            try {
                ApplicationInfo data = notif.getApplicationInfo(mPackageName);
                notif.storeName(data.loadLabel(mPackageName).toString());
            } catch (PackageManager.NameNotFoundException e) {
                notif.storeName("");
            }
        }

        // Sort list
        Collections.sort(mListData, new NotificationPrefComparator());
    }

    @Override
    public int getCount() {
        return ((null != mListData) ? mListData.size() : 0);
    }

    @Override
    public NotificationPref getItem(int position) {
        return ((null != mListData) ? mListData.get(position) : null);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (null == view) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.list_item_app_color, null);
        }

        TextView appName = (TextView) view.findViewById(R.id.app_name);
        TextView packageName = (TextView) view.findViewById(R.id.app_package);
        ImageView iconview = (ImageView) view.findViewById(R.id.app_icon);
        ColorSwatch colorView = (ColorSwatch) view.findViewById(R.id.colorswatch);

        NotificationPref notifData = mListData.get(position);
        ApplicationInfo data = null;
        try { // If the app is found
            data = notifData.getApplicationInfo(mPackageName);

            // Prepare list item
            appName.setText(notifData.getStoredName());
            packageName.setText(data.packageName);
            iconview.setImageDrawable(data.loadIcon(mPackageName));
            colorView.setColor(notifData.getColor());
        } catch (PackageManager.NameNotFoundException e) {
            appName.setText("Application uninstalled or inaccessible.");
            packageName.setText(data.packageName);
        }
        return view;
    }
}