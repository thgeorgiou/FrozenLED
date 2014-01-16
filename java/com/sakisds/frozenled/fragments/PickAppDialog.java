package com.sakisds.frozenled.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.sakisds.frozenled.R;
import com.sakisds.frozenled.adapters.AppsListAdapter;

public class PickAppDialog extends DialogFragment {
    AppsListAdapter mAdapter;
    PickAppDialogListener mListener;

    public PickAppDialog(AppsListAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_pickapp_title);
        builder.setAdapter(mAdapter, new OnClickListener());
        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (PickAppDialogListener) activity;
    }

    private class OnClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            mListener.onDialogPositiveClick(i);
            PickAppDialog.this.getDialog().cancel();
        }
    }

    public interface PickAppDialogListener {
        public void onDialogPositiveClick(int result);
    }
}