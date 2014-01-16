package com.sakisds.frozenled.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.larswerkman.colorpicker.ColorPicker;
import com.larswerkman.colorpicker.OpacityBar;
import com.larswerkman.colorpicker.SVBar;
import com.sakisds.frozenled.TestNotification;
import com.sakisds.frozenled.comparatos.ApplicationInfoComparator;
import com.sakisds.frozenled.R;
import com.sakisds.frozenled.adapters.AppsListAdapter;
import com.sakisds.frozenled.adapters.PulseHelpButtonOnClickListener;
import com.sakisds.frozenled.adapters.PulseSeekBarListener;
import com.sakisds.frozenled.fragments.PickAppDialog;
import com.sakisds.frozenled.userdata.DataManager;
import com.sakisds.frozenled.userdata.NotificationPref;
import com.sakisds.frozenled.userdata.NotificationType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by stratisg on 5/9/2013.
 */
public class AddActivity extends FragmentActivity implements PickAppDialog.PickAppDialogListener {
    // Views
    private RadioButton mRadioButtonByApp;
    private ColorPicker mColorPicker;
    private SVBar mSVBar;
    private OpacityBar mOpacityBar;
    private TextView mTextViewAppName, mTextViewPackageName, mTextViewPulseSpeed, mActionBarTextDone;
    private ImageView mImageViewAppIcon;
    private Button mButtonSelectApp;
    private EditText mEditTextName;
    private SeekBar mSeekBarPulseSpeed;
    private ImageButton mImageButtonPulseHelp;
    private View mActionBarDone, mActionBarCancel, mActionBarTest;

    // List adapters and data
    List<ApplicationInfo> mAppInfoList;
    AppsListAdapter mListAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        // Add actionbar buttons
        final LayoutInflater inflater = (LayoutInflater) getActionBar().getThemedContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        final View customActionBarView = inflater.inflate(
                R.layout.view_actionbar_buttons, null);

        // Find views
        mRadioButtonByApp = (RadioButton) findViewById(R.id.radioButton_byApp);
        mColorPicker = (ColorPicker) findViewById(R.id.colorpicker);
        mSVBar = (SVBar) findViewById(R.id.svbar);
        mOpacityBar = (OpacityBar) findViewById(R.id.obar);
        mButtonSelectApp = (Button) findViewById(R.id.button_selectapp);
        mEditTextName = (EditText) findViewById(R.id.editText_name);
        mTextViewAppName = (TextView) findViewById(R.id.app_name);
        mTextViewPackageName = (TextView) findViewById(R.id.app_package);
        mImageViewAppIcon = (ImageView) findViewById(R.id.app_icon);
        mSeekBarPulseSpeed = (SeekBar) findViewById(R.id.seekBar);
        mTextViewPulseSpeed = (TextView) findViewById(R.id.textView_ms);
        mImageButtonPulseHelp = (ImageButton) findViewById(R.id.imageButton_pulseHelp);
        mActionBarDone = customActionBarView.findViewById(R.id.actionbar_done);
        mActionBarCancel = customActionBarView.findViewById(R.id.actionbar_cancel);
        mActionBarTest = customActionBarView.findViewById(R.id.actionbar_test);
        mActionBarTextDone = (TextView) mActionBarDone.findViewById(R.id.actionbar_text_done);

        // Setup colorpicker
        mColorPicker.addSVBar(mSVBar);
        mColorPicker.addOpacityBar(mOpacityBar);

        // Disable buttons
        mActionBarDone.setEnabled(false);

        // Add listeners
        OnClickListener onClickListener = new OnClickListener(this);
        ((RadioGroup) findViewById(R.id.radioGroup)).setOnCheckedChangeListener(new RadioGroupListener());
        mButtonSelectApp.setOnClickListener(onClickListener);
        mSeekBarPulseSpeed.setOnSeekBarChangeListener(new PulseSeekBarListener(mTextViewPulseSpeed));
        mActionBarDone.setOnClickListener(onClickListener);
        mActionBarCancel.setOnClickListener(onClickListener);
        mActionBarTest.setOnClickListener(onClickListener);
        mEditTextName.addTextChangedListener(new TextChangedListener());
        mImageButtonPulseHelp.setOnClickListener(new PulseHelpButtonOnClickListener(this));

        // Show the custom action bar view and hide the normal Home icon and title.
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayOptions(
                ActionBar.DISPLAY_SHOW_CUSTOM,
                ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME
                        | ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setCustomView(customActionBarView,
                new ActionBar.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    public void onDialogPositiveClick(int result) {
        ApplicationInfo data = mAppInfoList.get(result);
        if (null != data) {
            mTextViewAppName.setText(data.loadLabel(getPackageManager()));
            mTextViewPackageName.setText(data.packageName);
            mImageViewAppIcon.setImageDrawable(data.loadIcon(getPackageManager()));

            // Enable actionbar button
            mActionBarDone.setEnabled(true);
        }
    }

    private void saveUserData() {
        SharedPreferences prefs = getSharedPreferences(DataManager.SHAREDPREFS_NOTIFICATIONS, MODE_PRIVATE);

        NotificationType type;
        String data;
        if (mRadioButtonByApp.isChecked()) {
            type = NotificationType.BY_PACKAGE;
            data = mTextViewPackageName.getText().toString();
        } else {
            type = NotificationType.BY_NAME;
            data = mEditTextName.getText().toString();
        }

        NotificationPref notif = new NotificationPref(type, data, mColorPicker.getColor(), mSeekBarPulseSpeed.getProgress());
        DataManager.addNotification(prefs, notif);
    }

    private void testSettings() {
        TestNotification.show(this, mColorPicker.getColor(), mSeekBarPulseSpeed.getProgress());
    }

    private class RadioGroupListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int id) {
            if (mRadioButtonByApp.isChecked()) {
                // Enable
                mButtonSelectApp.setEnabled(true);
                mTextViewAppName.setTextColor(getResources().getColor(android.R.color.primary_text_light));
                mTextViewPackageName.setTextColor(getResources().getColor(android.R.color.primary_text_light));
                // Disable
                mEditTextName.setEnabled(false);
                // Actionbar
                if (mTextViewPackageName.getText().toString().equals("")) {
                    mActionBarDone.setEnabled(false);
                    mActionBarTextDone.setTextColor(getResources().getColor(android.R.color.darker_gray));
                } else {
                    mActionBarDone.setEnabled(true);
                    mActionBarTextDone.setTextColor(getResources().getColor(android.R.color.primary_text_dark));
                }
            } else {
                // Enable
                mEditTextName.setEnabled(true);
                // Disable
                mButtonSelectApp.setEnabled(false);
                mTextViewAppName.setTextColor(getResources().getColor(android.R.color.darker_gray));
                mTextViewPackageName.setTextColor(getResources().getColor(android.R.color.darker_gray));
                // Actionbar
                if (mEditTextName.getText().toString().equals("")) {
                    mActionBarDone.setEnabled(false);
                    mActionBarTextDone.setTextColor(getResources().getColor(android.R.color.darker_gray));
                } else {
                    mActionBarDone.setEnabled(true);
                    mActionBarTextDone.setTextColor(getResources().getColor(android.R.color.primary_text_dark));
                }
            }
        }
    }

    private class TextChangedListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.toString().equals("")) {
                mActionBarDone.setEnabled(false);
                mActionBarTextDone.setTextColor(getResources().getColor(android.R.color.darker_gray));
            } else {
                mActionBarDone.setEnabled(true);
                mActionBarTextDone.setTextColor(getResources().getColor(android.R.color.primary_text_dark));
            }
        }
    }

    private class OnClickListener implements View.OnClickListener {
        Activity mActivity;

        public OnClickListener(Activity activity) {
            mActivity = activity;
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.actionbar_done:
                    saveUserData();
                    finish();
                    break;
                case R.id.actionbar_cancel:
                    finish();
                    break;
                case R.id.actionbar_test:
                    testSettings();
                    break;
                case R.id.button_selectapp:
                    new LoadApplications().execute();
                    break;
            }
        }
    }

    private List<ApplicationInfo> checkForLaunchIntent(List<ApplicationInfo> list) {
        ArrayList<ApplicationInfo> applist = new ArrayList<ApplicationInfo>();
        for (ApplicationInfo info : list) {
            try {
                if (null != getPackageManager().getLaunchIntentForPackage(info.packageName)) {
                    applist.add(info);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return applist;
    }

    private class LoadApplications extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progress = null;

        @Override
        protected Void doInBackground(Void... params) {
            mAppInfoList = checkForLaunchIntent(getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA));

            // Sort list
            Collections.sort(mAppInfoList, new ApplicationInfoComparator(getPackageManager()));

            // Create adapter
            mListAdapter = new AppsListAdapter(AddActivity.this,
                    R.layout.list_item_app, mAppInfoList);
            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(Void result) {
            PickAppDialog dialog = new PickAppDialog(mListAdapter);
            dialog.show(getSupportFragmentManager(), "pickappdialog");
            progress.dismiss();
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(AddActivity.this, null,
                    "Loading application info...");
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}
