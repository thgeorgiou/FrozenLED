package com.sakisds.frozenled.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.larswerkman.colorpicker.ColorPicker;
import com.larswerkman.colorpicker.OpacityBar;
import com.larswerkman.colorpicker.SVBar;
import com.sakisds.frozenled.R;
import com.sakisds.frozenled.TestNotification;
import com.sakisds.frozenled.adapters.PulseHelpButtonOnClickListener;
import com.sakisds.frozenled.adapters.PulseSeekBarListener;
import com.sakisds.frozenled.userdata.DataManager;
import com.sakisds.frozenled.userdata.NotificationPref;
import com.sakisds.frozenled.userdata.NotificationType;

import java.util.List;

/**
 * Created by stratisg on 13/9/2013.
 */
public class EditDialog extends DialogFragment {
    // Views
    private ColorPicker mColorPicker;
    private OpacityBar mOpacityBar;
    private SVBar mSVBar;
    private ImageButton mImageButtonPulseHelp;
    private SeekBar mSeekBarPulse;
    private TextView mTextViewPulse;

    // Notification
    private NotificationPref mNotifPref;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create NotificationPref from savedstate if it exists
        if (savedInstanceState != null) {
            if (savedInstanceState.getBoolean("notifExists")) {
                mNotifPref = new NotificationPref(NotificationType.convertFromInt(savedInstanceState.getInt("type")),
                        savedInstanceState.getString("data"), savedInstanceState.getInt("color"), savedInstanceState.getInt("pulse"));
            }
        }

        // Create a builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View rootView = inflater.inflate(R.layout.fragment_edit, null);

        // Prepare dialog with button listeners
        builder .setTitle(R.string.activity_edit)
                .setView(rootView)
                // Add action buttons
                .setPositiveButton(R.string.menu_save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        saveData();
                        List<Fragment> fragments = getActivity().getSupportFragmentManager().getFragments();
                        for (int i = 0; i < fragments.size(); i++) {
                            Fragment fragment = fragments.get(i);
                            if (fragment instanceof DeletableListFragment) {
                                ((DeletableListFragment) fragment).setAdapter();
                            }
                        }
                    }
                })
                .setNegativeButton(R.string.menu_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditDialog.this.getDialog().cancel();
                    }
                });

        // Find views
        mColorPicker = (ColorPicker) rootView.findViewById(R.id.colorpicker);
        mOpacityBar = (OpacityBar) rootView.findViewById(R.id.obar);
        mSVBar = (SVBar) rootView.findViewById(R.id.svbar);
        mImageButtonPulseHelp = (ImageButton) rootView.findViewById(R.id.imageButton_pulseHelp);
        mSeekBarPulse = (SeekBar) rootView.findViewById(R.id.seekBar);
        mTextViewPulse = (TextView) rootView.findViewById(R.id.textView_ms);

        // Setup colorpicker
        mColorPicker.addSVBar(mSVBar);
        mColorPicker.addOpacityBar(mOpacityBar);
        if (savedInstanceState != null) {
            mColorPicker.setColor(savedInstanceState.getInt("newcolor"));
        } else {
            mColorPicker.setColor(mNotifPref.getColor());
        }
        mColorPicker.setOldCenterColor(mNotifPref.getColor());

        // Add listeners
        mImageButtonPulseHelp.setOnClickListener(new PulseHelpButtonOnClickListener(getActivity()));
        mSeekBarPulse.setOnSeekBarChangeListener(new PulseSeekBarListener(mTextViewPulse));

        // Setup seekbar
        mSeekBarPulse.setProgress(mNotifPref.getPulseSpeed());

        // Create dialog
        return builder.create();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) { // Store NotificationPref in state in case of rotation etc
        if (mNotifPref != null) {
            outState.putBoolean("notifExists", true);
            outState.putString("data", mNotifPref.getData());
            outState.putInt("color", mNotifPref.getColor());
            outState.putInt("pulse", mSeekBarPulse.getProgress());
            outState.putInt("type", mNotifPref.getType().convertToInt());
            outState.putInt("newcolor", mColorPicker.getColor());
        }
    }

    public void setSourceNotification(NotificationPref notif) {
        mNotifPref = notif;
    }

    public void saveData() {
        NotificationPref newNotif = new NotificationPref(mNotifPref.getType(), mNotifPref.getData(),
                mColorPicker.getColor(), mSeekBarPulse.getProgress());

        SharedPreferences prefs = getActivity().getSharedPreferences(DataManager.SHAREDPREFS_NOTIFICATIONS, Context.MODE_PRIVATE);
        DataManager.replaceNotification(prefs, mNotifPref, newNotif);
    }
}
