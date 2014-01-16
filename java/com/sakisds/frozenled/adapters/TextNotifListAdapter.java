package com.sakisds.frozenled.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sakisds.frozenled.R;
import com.sakisds.frozenled.comparatos.NamesComparator;
import com.sakisds.frozenled.comparatos.NotificationPrefComparator;
import com.sakisds.frozenled.userdata.NotificationPref;
import com.sakisds.frozenled.views.ColorSwatch;

import java.util.Collections;
import java.util.List;

public class TextNotifListAdapter extends ArrayAdapter<NotificationPref> {
    private List<NotificationPref> mList = null;
    private Context mContext;

    public TextNotifListAdapter(Context context, int textViewResourceId,
                                List<NotificationPref> list) {
        super(context, textViewResourceId, list);
        this.mContext = context;
        this.mList = list;

        // Sort names
        Collections.sort(mList, new NamesComparator());
    }

    @Override
    public int getCount() {
        return ((null != mList) ? mList.size() : 0);
    }

    @Override
    public NotificationPref getItem(int position) {
        return ((null != mList) ? mList.get(position) : null);
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
            view = layoutInflater.inflate(R.layout.list_item_name_color, null);
        }

        NotificationPref data = mList.get(position);

        TextView appName = (TextView) view.findViewById(R.id.name);
        ColorSwatch colorSwatch = (ColorSwatch) view.findViewById(R.id.colorswatch);

        appName.setText(data.getData());
        colorSwatch.setColor(data.getColor());

        return view;
    }
};