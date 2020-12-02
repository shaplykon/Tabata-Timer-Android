package com.example.timer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;



public class SettingsActivity extends AppCompatActivity implements OnDataReset{

    private SettingsFragment settingsFragment;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();
        settingsFragment = new SettingsFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, settingsFragment)
                .commit();

        float size_coef = sharedPreferences.getFloat("sizeCoef", 1f);

        Configuration configuration = getResources().getConfiguration();
        configuration.fontScale = size_coef; //0.85 small size, 1 normal size, 1,15 big etc

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        metrics.scaledDensity = configuration.fontScale * metrics.density;
        getBaseContext().getResources().updateConfiguration(configuration, metrics);
    }

    @Override
    protected void onResume() {
        Preference themePreference = settingsFragment.findPreference("darkMode");
        assert themePreference != null;
        themePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if ((boolean) newValue) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor.putString("theme", "dark");
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor.putString("theme", "light");
                }
                editor.apply();
                return true;
            }
        });

        Preference localePreference = settingsFragment.findPreference("engMode");
        assert localePreference != null;
        localePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if ((boolean) newValue) {
                    LocaleHelper.setLocale(getBaseContext(),"en");
                } else {
                    LocaleHelper.setLocale(getBaseContext(),"ru");
                }
                finish();
                startActivity(new Intent(SettingsActivity.this, SettingsActivity.this.getClass()));
                return true;

            }
        });

        final ListPreference fontSizePreference = settingsFragment.findPreference("fontSize");
        assert fontSizePreference != null;

        fontSizePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                float size_coef = 0;
                switch (fontSizePreference.findIndexOfValue(newValue.toString())) {
                    case 0: {
                        size_coef = 0.85f;
                        break;
                    }
                    case 1: {
                        size_coef = 1.0f;
                        break;
                    }
                    case 2: {
                        size_coef = 1.15f;
                        break;
                    }
                }

                editor.putFloat("sizeCoef", size_coef);
                editor.apply();

                Configuration configuration = getResources().getConfiguration();
                configuration.fontScale = size_coef; //0.85 small size, 1 normal size, 1,15 big etc

                DisplayMetrics metrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics);
                metrics.scaledDensity = configuration.fontScale * metrics.density;
                getBaseContext().getResources().updateConfiguration(configuration, metrics);

                finish();
                startActivity(new Intent(SettingsActivity.this, SettingsActivity.this.getClass()));
                return true;

            }
        });


        Preference resetPreference = settingsFragment.findPreference("resetData");
        resetPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                ResetDialogFragment resetDialogFragment = new ResetDialogFragment();
                resetDialogFragment.show(getSupportFragmentManager(), "reset");
                return true;
            }
        });

        Preference versionPreference = settingsFragment.findPreference("versionName");
        versionPreference.setSummary(BuildConfig.VERSION_NAME);
        super.onResume();
    }

    @Override
    public void reset() {
        setResult(RESULT_OK);
        finish();
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.settings, rootKey);
        }
    }
}

interface OnDataReset{
    void reset();
}

