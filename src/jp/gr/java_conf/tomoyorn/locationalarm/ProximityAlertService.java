package jp.gr.java_conf.tomoyorn.locationalarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.ConditionVariable;
import android.os.IBinder;
import android.os.Vibrator;

import jp.gr.java_conf.tomoyorn.locationalarm.model.Alarm;
import jp.gr.java_conf.tomoyorn.locationalarm.util.Log;

/**
 * 近接アラートを鳴らすServiceです。
 *
 * @author tomoyorn
 */
public class ProximityAlertService extends Service {

    private static final String TAG = "ProximityAlertService";

    private NotificationManager mNotificationManager;
    Vibrator mVibrator;
    private ConditionVariable mCondition;

    @Override
    public void onCreate() {
        Log.d(TAG, "Start onCreate()");
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mCondition = new ConditionVariable();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Start onStartCommand()");
        Alarm alarm = Alarm.find(intent.getExtras().getLong("alarm.id"));
        boolean isStop = intent.getExtras().getBoolean("action.stop"); // TODO Intent.ACTIONを使ったほうが良い？
        if (isStop) {
            stopAlert(alarm);
        } else {
            startAlert(alarm);
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Start onDestroy()");
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException();
    }

    private void startAlert(Alarm alarm) {
        // TODO 勝手にアラートが停止するようなら、foregroundにすることを検討。
        // foregroundにする場合は目的地到着のNotificationもここで行うべきか。
        // APIではonStartCommand()の間はforegroundと定義されているので問題ない
        // はずだが。Vibratorのスレッドまで停止されることがあるかもしれない。
        // startForeground(id, notification);
        // stopForeground(removeNotification);
        mCondition.close();
        vibrate(alarm);
        Log.d(TAG, "Started the alert.: " + alarm.dump());
    }

    private void stopAlert(Alarm alarm) {
        mVibrator.cancel();
        mCondition.open();
        mNotificationManager.cancel(alarm.hashCode());
        Log.d(TAG, "Stoped the alert.: " + alarm.dump());
        stopSelf();
    }

    @SuppressWarnings("deprecation")
    private void showNotification(Alarm alarm) {
        String title = getString(R.string.app_name) + ": " + alarm.getLavel();
        String message = "アラームは" + alarm.getDuration() + "分間鳴って停止しました。";
        String ticker = title;

        Notification notification = new Notification(R.drawable.ic_launcher,
                ticker, System.currentTimeMillis());
        Intent doNothing = new Intent();
        notification.setLatestEventInfo(this, title, message,
                PendingIntent.getActivity(this, 0, doNothing, 0));

        mNotificationManager.notify(alarm.hashCode(), notification);
    }

    private void vibrate(final Alarm alarm) {
        Runnable task = new Runnable() {
            long[] pattern = { 0, 200, 500 };
            int infinite = 0;

            @Override
            public void run() {
                mVibrator.vibrate(pattern, infinite);
                long timeout = alarm.getDuration() * 60 * 1000; // milliseconds
                boolean isTimeout = !mCondition.block(timeout);
                stopAlert(alarm);
                if (isTimeout) {
                    showNotification(alarm);
                }
            }
        };
        new Thread(null, task, getClass().getSimpleName()).start();
    }
}
