package jp.gr.java_conf.tomoyorn.locationalarm;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;

/**
 * 設定画面のActivityです。
 *
 * @author tomoyorn
 */
public class SettingsActivity extends PreferenceActivity {

    private static final String TAG = "SettingsActivity";
    private static final String KEY_RESTORE = "restore";

    Preference mRestorePreference;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // XXX PreferenceFragmentの利用を推奨されているが、Compatibility Package
        // に存在しなかった。
        addPreferencesFromResource(R.xml.settings);
        mRestorePreference = findPreference(KEY_RESTORE);
        mRestorePreference.setOnPreferenceChangeListener(listener);
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(listener);
//    }
//    @Override
//    protected void onPause() {
//        super.onPause();
//        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(listener);
//
//    }

 // ここで summary を動的に変更
    private Preference.OnPreferenceChangeListener listener =
        new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference,
                    Object newValue) {
                finish();
                return true;
            }

    };
}
