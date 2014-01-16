package com.sakisds.frozenled.adapters;

import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by stratisg on 13/9/2013.
 */
public class PulseSeekBarListener implements SeekBar.OnSeekBarChangeListener {
    TextView mTextView;

    public PulseSeekBarListener(TextView textView) {
        mTextView = textView;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mTextView.setText(progress + "MS");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }
}
