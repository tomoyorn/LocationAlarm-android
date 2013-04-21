package jp.gr.java_conf.tomoyorn.locationalarm;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.app.Activity;
import android.view.Menu;

public class SettingsActivity extends PreferenceActivity {
    private static final String KEY_RESTORE = "restore";

    Preference mRestorePreference;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // XXX PreferenceFragmentの利用を推奨されているが、Compatibility Package
        // に存在しなかった。
        addPreferencesFromResource(R.xml.preferences);
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

//    public class RestorePreference extends Preference {
//
//        public RestorePreference(Context context) {
//            super(context);
//        }
//
//        public RestorePreference(Context context, AttributeSet attrs) {
//            super(context, attrs);
//        }
//
//        public RestorePreference(Context context, AttributeSet attrs,
//                int defStyle) {
//            super(context, attrs, defStyle);
//        }
//
//        @Override
//        protected void onClick() {
//            // TODO Auto-generated method stub
//            super.onClick();
//        }
//    }
}

//public class SettingsActivity extends PreferenceActivity {
//    private static final String KEY_RESTORE = "restore";
//
//    @SuppressWarnings("deprecation")
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        // XXX PreferenceFragmentの利用を推奨されているが、Compatibility Package
//        // に存在しなかった。
//        addPreferencesFromResource(R.xml.preferences);
//        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(listener);
//    }
//
////    @Override
////    protected void onResume() {
////        super.onResume();
////        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(listener);
////    }
////    @Override
////    protected void onPause() {
////        super.onPause();
////        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(listener);
////
////    }
//
// // ここで summary を動的に変更
//    private SharedPreferences.OnSharedPreferenceChangeListener listener =
//        new SharedPreferences.OnSharedPreferenceChangeListener() {
//
//        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//            if (KEY_RESTORE.equals(key)) {
//                finish();
//            }
//        }
//    };
//
////    public class RestorePreference extends Preference {
////
////        public RestorePreference(Context context) {
////            super(context);
////        }
////
////        public RestorePreference(Context context, AttributeSet attrs) {
////            super(context, attrs);
////        }
////
////        public RestorePreference(Context context, AttributeSet attrs,
////                int defStyle) {
////            super(context, attrs, defStyle);
////        }
////
////        @Override
////        protected void onClick() {
////            // TODO Auto-generated method stub
////            super.onClick();
////        }
////    }
//}
