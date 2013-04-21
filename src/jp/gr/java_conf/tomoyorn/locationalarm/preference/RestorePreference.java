package jp.gr.java_conf.tomoyorn.locationalarm.preference;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.preference.Preference;
import android.util.AttributeSet;

import jp.gr.java_conf.tomoyorn.locationalarm.ProximityAlertReceiver;
import jp.gr.java_conf.tomoyorn.locationalarm.R;
import jp.gr.java_conf.tomoyorn.locationalarm.model.Alarm;

import java.util.ArrayList;

// TODO SettingsActivityのネストクラスにできないかを検討
/**
 * アプリケーションをインストール時の状態に戻すPreferenceです。
 *
 * @author tomoyorn
 */
public class RestorePreference extends Preference {

    private Context mContext;
    private NotificationManager mNotificationManager;
    private LocationManager mLocationManager;

    public RestorePreference(Context context) {
        this(context, null);
    }

    public RestorePreference(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.preferenceStyle);
    }

    public RestorePreference(Context context, AttributeSet attrs,
            int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        mLocationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    protected void onClick() {
        super.onClick();
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setMessage(R.string.message_confirm_restoration);
        alert.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        clearNotification();
                        clearProximityAlert();
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

    private void clearNotification() {
        mNotificationManager.cancelAll();
    }

    private void clearProximityAlert() {
        ArrayList<Alarm> alarms = Alarm.findAll();
        for (Alarm alarm : alarms) {
            Intent intent = new Intent(mContext, ProximityAlertReceiver.class)
                    .putExtra("alarm.id", alarm.getId());
            mLocationManager.removeProximityAlert(PendingIntent.getBroadcast(
                    mContext, 0, intent, 0));
        }
    }
}
