package de.alxmtzr.freshify.ui.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import de.alxmtzr.freshify.R;

public class SettingsFragment extends Fragment {

    static final String PREF_DAYS_UNTIL_EXPIRY = "days_until_expiry";

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SeekBar daysUntilExpirySeekBar = view.findViewById(R.id.daysUntilExpirySeekBar);
        TextView daysUntilExpiryValue = view.findViewById(R.id.daysUntilExpiryValue);

        // load saved value from SharedPreferences
        SharedPreferences preferences = requireContext().getSharedPreferences("prefs_freshify", 0);
        int savedDays = preferences.getInt(PREF_DAYS_UNTIL_EXPIRY, 3); // Default to 3 days
        daysUntilExpirySeekBar.setProgress(savedDays);
        daysUntilExpiryValue.setText(String.valueOf(savedDays));

        // update SharedPreferences when the user changes the value
        daysUntilExpirySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                daysUntilExpiryValue.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Do nothing
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                preferences.edit().putInt(PREF_DAYS_UNTIL_EXPIRY, seekBar.getProgress()).apply();
            }
        });
    }
}
