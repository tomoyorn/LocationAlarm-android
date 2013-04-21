package jp.gr.java_conf.tomoyorn.locationalarm;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.ConditionVariable;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import jp.gr.java_conf.tomoyorn.locationalarm.model.Alarm;

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
        boolean isStop = intent.getExtras().getBoolean("action.stop"); // TODO Intent.Actionで判定したい
        if (isStop) {
            stopAlarm(alarm);
        } else {
            startAlarm(alarm);
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

    private void startAlarm(Alarm alarm) {
        // TODO foregroundにすることを検討する。
        // foregroundにする場合は目的地到着のNotificationもここで行うべきか。
        // APIではonStartCommand()の間はforegroundと定義されているので問題ない
        // はずだが。Vibratorのスレッドまで停止されることがあるかもしれない。
        // startForeground(id, notification);
        // stopForeground(removeNotification);
        mCondition.close();
        vibrate(alarm);
    }

    private void stopAlarm(Alarm alarm) {
        mVibrator.cancel();
        mCondition.open();
        mNotificationManager.cancel(alarm.hashCode());
        stopSelf();
    }

    @SuppressWarnings("deprecation")
    private void showNotification(Alarm alarm) {
        String title = getString(R.string.app_name) + ": " + alarm.getLavel();
        String message = "アラームは10分間鳴って停止しました。";
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
                long timeout =1 * 60 * 1000; // milliseconds
                boolean isTimeout = !mCondition.block(timeout);
                stopAlarm(alarm);
                if (isTimeout) {
                    showNotification(alarm);
                }
            }
        };
        new Thread(null, task, getClass().getSimpleName()).start();
    }

//////// AsyncTaskを用いた方法。しかし、AsyncTaskはどうやらUIスレッド向けのようだ。
//  private void startAlarm(int alarmId) {
//  // TODO foregroundにすることを検討する。
//  // foregroundにする場合は目的地到着のNotificationもここで行うべきか。
//  // APIではonStartCommand()の間はforegroundと定義されているので問題ない
//  // はずだが。Vibratorのスレッドまで停止されることがあるかもしれない。
//  // startForeground(id, notification);
//  // stopForeground(removeNotification);
//  new Task().execute(String.valueOf(alarmId));
//}

//  private void vibrate(int alarmId) {
//  long[] pattern = { 0, 200, 500 };
//  int infinite = 0;
//  mVibrator.vibrate(pattern, infinite);
//}

//    // TODO AsyncTaskはUIスレッドのためのクラス？大したことしてないし普通のRunnableで良いかも
//    // その場合はNotifyingService.javaガ参考になる
//    private class Task extends AsyncTask<String, Void, Void>
//    {
//        @Override
//        protected void onPreExecute() {
//            mCondition.close();
//        }
//
//        @Override
//        protected Void doInBackground(String... params) {
//            int alarmId = Integer.valueOf(params[0]);
//            vibrate(alarmId);
//            long timeout =1 * 60 * 1000; // milliseconds
//            boolean isTimeout = !mCondition.block(timeout);
//            if (isTimeout) {
//                ProximityAlertService.this.showNotification(alarmId);
//            }
//            mVibrator.cancel();
//            return null;
//        }
//
////        @Override
////        protected void onPostExecute(Void result) {
////            mCondition.open(); // TODO stopAlarm()が呼ばれなかった時（停止されなかった）のためにここでもopen()しておくべき
////            stopSelf();        // →いや、stopAlarm()を呼んでやればいいのか→いや、Notificationは消したらダメだ→stopAlarm();showNotification();で良いのか
////        }
//    }
}
