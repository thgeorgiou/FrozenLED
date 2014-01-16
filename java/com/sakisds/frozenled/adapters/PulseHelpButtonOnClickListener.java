package com.sakisds.frozenled.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;

import com.sakisds.frozenled.R;

/**
 * Created by stratisg on 13/9/2013.
 */
public class PulseHelpButtonOnClickListener implements View.OnClickListener{
    Activity mActivity;

    public PulseHelpButtonOnClickListener(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void onClick(View view) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(mActivity);

        dlgAlert.setMessage(mActivity.getString(R.string.pulse_help));
        dlgAlert.setTitle("Help");
        dlgAlert.setPositiveButton("Got it",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }
}
