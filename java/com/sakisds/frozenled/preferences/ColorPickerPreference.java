package com.sakisds.frozenled.preferences;

import android.content.Context;
import android.content.res.TypedArray;
import android.media.MediaMetadataRetriever;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.larswerkman.colorpicker.ColorPicker;
import com.larswerkman.colorpicker.OpacityBar;
import com.larswerkman.colorpicker.SVBar;
import com.sakisds.frozenled.R;
import com.sakisds.frozenled.adapters.PulseSeekBarListener;

/**
 * Created by stratisg on 13/9/2013.
 */
public class ColorPickerPreference extends DialogPreference {
    ColorPicker mColorPicker;
    int mColor;

    public ColorPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Set dialog properties
        setDialogLayoutResource(R.layout.preference_colorpicker);
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);
    }

    @Override
    public View onCreateDialogView() {
        View dialogView = super.onCreateDialogView();

        // Setup color picker
        SVBar svBar = (SVBar) dialogView.findViewById(R.id.svbar);
        OpacityBar opacityBar = (OpacityBar) dialogView.findViewById(R.id.obar);
        mColorPicker = (ColorPicker) dialogView.findViewById(R.id.colorpicker);
        mColorPicker.addSVBar(svBar);
        mColorPicker.addOpacityBar(opacityBar);
        mColorPicker.setColor(mColor);
        mColorPicker.setOldCenterColor(mColor);

        return dialogView;
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        // When the user selects "OK", persist the new value
        if (positiveResult) {
            mColor = mColorPicker.getColor();
            persistInt(mColor);
            setSummary(" ");
        }
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if (restorePersistedValue) {
            // Restore existing state
            mColor = getPersistedInt(getDefaultColor());
            setSummary(" ");
        } else {
            // Set default state from the XML attribute
            mColor = (Integer) defaultValue;
            persistInt(mColor);
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInteger(index, getDefaultColor());
    }

    private int getDefaultColor() {
        return -65505;
    }
}
