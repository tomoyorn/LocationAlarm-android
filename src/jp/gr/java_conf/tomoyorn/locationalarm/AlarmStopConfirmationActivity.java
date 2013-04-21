package jp.gr.java_conf.tomoyorn.locationalarm;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import jp.gr.java_conf.tomoyorn.locationalarm.model.Alarm;
import jp.gr.java_conf.tomoyorn.locationalarm.util.Log;

/**
 * アラーム停止確認画面のActivityです。
 *
 * @author tomoyorn
 *
 */
public class AlarmStopConfirmationActivity extends Activity {

    private static final String TAG = "AlarmStopConfirmationActivity";

    private NotificationManager mNotificationManager;
    private LocationManager mLocationManager;
    private TextView mText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_stop_confirmation);
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mText = (TextView) findViewById(R.id.text);
        Alarm alarm = Alarm.find(getIntent().getExtras().getLong("alarm.id"));
        mText.setText("アラーム「" + alarm.getLavel() + "」を停止します。"); // TODO リソースへ
    }

    // Callback method for the ok button.
    public void ok(View v) {
        Alarm alarm = Alarm.find(getIntent().getExtras().getLong("alarm.id"));
        cancelNotification(alarm);
        removeProximityAlert(alarm);
        Log.d(TAG, "Stoped the alarm.: " + alarm.dump());
        finish();
    }

    // Callback method for the cancel button.
    public void cancel(View v) {
        finish();
    }

    private void cancelNotification(Alarm alarm) {
        mNotificationManager.cancel(alarm.hashCode());
    }

    private void removeProximityAlert(Alarm alarm) {
        Intent intent = new Intent(this, ProximityAlertReceiver.class)
                .putExtra("alarm.id", alarm.getId());
        mLocationManager.removeProximityAlert(
                PendingIntent.getBroadcast(this, 0, intent, 0));
    }
}
