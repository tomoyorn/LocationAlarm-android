package jp.gr.java_conf.tomoyorn.locationalarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;

import jp.gr.java_conf.tomoyorn.locationalarm.model.Alarm;
import jp.gr.java_conf.tomoyorn.locationalarm.util.Log;

// TODO ProximityAlertServerのネストクラスとして定義することを検討する
public class ProximityAlertReceiver extends BroadcastReceiver {

    private static final String TAG = "ProximityAlertReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Start onReceive()");
        boolean isEntering = intent.getExtras().getBoolean(
                LocationManager.KEY_PROXIMITY_ENTERING);
//        if (!isEntering) { // TODO どちらが精度が良いのだろうか？
//            return;
//        }

        Alarm alarm = Alarm.find(intent.getExtras().getLong("alarm.id"));
        showNotification(alarm, context);
        removeProximityAlert(alarm, context);
        startService(alarm, context);
    }

    private void startService(Alarm alarm, Context context) {
        Intent intent = new Intent(context, ProximityAlertService.class)
                .putExtra("alarm.id", alarm.getId());
        context.startService(intent);
    }

    @SuppressWarnings("deprecation")
    private void showNotification(Alarm alarm, Context context) {
        String title = context.getString(R.string.app_name) + ": " + alarm.getLavel();
        String message = "目的地周辺に到着しました。アラームを停止する場合に選択します。";
        String ticker = title;

        Notification notification = new Notification(R.drawable.ic_launcher,
                ticker, System.currentTimeMillis());
        notification.flags = notification.flags
                | Notification.FLAG_NO_CLEAR
                | Notification.FLAG_ONGOING_EVENT;
        notification.defaults = Notification.DEFAULT_ALL;
        Intent intent = new Intent(context, ProximityAlertService.class)
            .putExtra("alarm.id", alarm.getId())
            .putExtra("action.stop", true); // TODO Intent.ACTIONについて調べてみる
        notification.setLatestEventInfo(context, title, message,
                PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(alarm.hashCode(), notification);
    }

    private void removeProximityAlert(Alarm alarm, Context context) {
        LocationManager locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        Intent intent = new Intent(context, ProximityAlertReceiver.class)
                .putExtra("alarm.id", alarm.getId());
        locationManager.removeProximityAlert(PendingIntent.getBroadcast(context, 0, intent, 0));
    }
}
