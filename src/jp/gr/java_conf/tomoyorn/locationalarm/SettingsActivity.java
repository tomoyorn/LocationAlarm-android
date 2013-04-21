package jp.gr.java_conf.tomoyorn.locationalarm;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import jp.gr.java_conf.tomoyorn.locationalarm.util.Log;

/**
 * 設定画面のActivityです。
 *
 * @author tomoyorn
 */
public class SettingsActivity extends PreferenceActivity {

    private static final String TAG = "SettingsActivity";

    public static final String KEY_ALARM_DISTANCE = "alarm_distance";
    public static final String KEY_ALARM_DURATION = "alarm_duration";

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Start onCreate()");
        addPreferencesFromResource(R.xml.settings);
    }
}
