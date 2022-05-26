package com.example.chat_2022_eleves;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import androidx.annotation.Nullable;

public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //noinspection deprecation
        addPreferencesFromResource(R.xml.preferences);
    }
}
