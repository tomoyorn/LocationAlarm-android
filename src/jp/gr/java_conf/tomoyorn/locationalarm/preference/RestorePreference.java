package jp.gr.java_conf.tomoyorn.locationalarm.preference;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.Preference;
import android.util.AttributeSet;

import com.activeandroid.Model;
import com.activeandroid.query.Delete;

import java.util.ArrayList;

import jp.gr.java_conf.tomoyorn.locationalarm.R;
import jp.gr.java_conf.tomoyorn.locationalarm.model.Alarm;

// TODO SettingsActivityのネストクラスにできないかを検討
public class RestorePreference extends Preference {

    public RestorePreference(Context context) {
        super(context);
    }

    public RestorePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RestorePreference(Context context, AttributeSet attrs,
            int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onClick() {
        super.onClick();
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setMessage(R.string.message_confirm_restoration);
        alert.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Notificationと近接アラートのクリアも必要か
                        clearDatabase();
                        clearPreferences();
                        callChangeListener(null);
                    }
                });
        alert.setNegativeButton(android.R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alert.show();
    }

    private void clearDatabase() {
        Alarm.deleteAll();
    }

    private void clearPreferences() {
        getSharedPreferences().edit().clear().commit();
    }
}
