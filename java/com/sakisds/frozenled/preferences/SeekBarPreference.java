package com.sakisds.frozenled.preferences;

import android.content.Context;
import android.content.res.TypedArray;
import android.media.MediaMetadataRetriever;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.sakisds.frozenled.R;
import com.sakisds.frozenled.adapters.PulseSeekBarListener;

/**
 * Created by stratisg on 13/9/2013.
 */
public class SeekBarPreference extends DialogPreference {
    SeekBar mSeekBar;
    int mProgress;

    public SeekBarPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Set dialog properties
        setDialogLayoutResource(R.layout.preference_seekbar);
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);
    }

    @Override
    public View onCreateDialogView() {
        View dialogView = super.onCreateDialogView();

        // Setup seekbar
        TextView textView = (TextView) dialogView.findViewById(R.id.textView_ms);
        mSeekBar = (SeekBar) dialogView.findViewById(R.id.seekBar);
        mSeekBar.setOnSeekBarChangeListener(new PulseSeekBarListener(textView));
        mSeekBar.setProgress(mProgress);

        return dialogView;
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        // When the user selects "OK", persist the new value
        if (positiveResult) {
            mProgress = mSeekBar.getProgress();
            persistInt(mProgress);
            setSummary(String.valueOf(mProgress)+"ms");
        }
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if (restorePersistedValue) {
            // Restore existing state
            mProgress = getPersistedInt(1000);
            setSummary(String.valueOf(mProgress)+"ms");
        } else {
            // Set default state from the XML attribute
            mProgress = (Integer) defaultValue;
            persistInt(mProgress);
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInteger(index, 1000);
    }
}
