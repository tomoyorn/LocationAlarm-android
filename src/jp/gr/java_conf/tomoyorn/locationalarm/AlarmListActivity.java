package jp.gr.java_conf.tomoyorn.locationalarm;

import android.app.ListActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import jp.gr.java_conf.tomoyorn.locationalarm.model.Alarm;
import jp.gr.java_conf.tomoyorn.locationalarm.util.Log;

import org.apache.commons.lang3.Validate;

import java.util.ArrayList;

/**
 * アラーム一覧画面のActivityです。
 *
 * @author tomoyorn
 */
public class AlarmListActivity extends ListActivity {

    private static final String TAG = "AlarmListActivity";

    private NotificationManager mNotificationManager;
    private LocationManager mLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_list);

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        setupView();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume()");
        super.onResume();
        refreshView();
    }

    private void setupView() {
        ArrayList<Alarm> mAlarms = Alarm.findAll();
        ArrayAdapter<Alarm> mAdapter = new ArrayAdapter<Alarm>(this,
                android.R.layout.simple_list_item_activated_1, mAlarms);
        ListView mAlarmListView = getListView();
        setListAdapter(mAdapter);
        mAlarmListView.setItemsCanFocus(false);
    }

    private void refreshView() {
        setupView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu()");
        getMenuInflater().inflate(R.menu.alarm_list, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.d(TAG, "onPrepareOptionsMenu()");
        if (selectedItem() == null) {
            menu.findItem(R.id.menu_start_alarm).setVisible(false);
            menu.findItem(R.id.menu_edit_alarm).setVisible(false);
            menu.findItem(R.id.menu_delete_alarm).setVisible(false);
        } else {
            menu.findItem(R.id.menu_start_alarm).setVisible(true);
            menu.findItem(R.id.menu_edit_alarm).setVisible(true);
            menu.findItem(R.id.menu_delete_alarm).setVisible(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected()");
        Alarm alarm = selectedItem();
        switch (item.getItemId()) {
        case R.id.menu_start_alarm:
            Validate.notNull(alarm, "Alarm must not be null.");
            if (existsAlreadyStartedAlarm()) {
                // TODO すでに起動しているアラームが存在していた場合はダイアログを出して確認する
            }
            startAlarm(alarm);
            finish();
            return true;
        case R.id.menu_add_alarm:
            startDestinationSelectionActivity(alarm);
            return true;
        case R.id.menu_edit_alarm:
            Validate.notNull(alarm, "Alarm must not be null!");
            startAlarmEditActivity(alarm);
            return true;
        case R.id.menu_delete_alarm:
            Validate.notNull(alarm, "Alarm must not be null!");
            deleteAlarm(alarm);
            return true;
        case R.id.menu_preferences:
            startSettingsActivity();
            return true;
        default:
            Log.w("Unknown selected menu.");
            return super.onOptionsItemSelected(item);
        }
    }

    public Alarm selectedItem() {
        int selectedItemPosition = getListView().getCheckedItemPosition();
        if (selectedItemPosition == ListView.INVALID_POSITION) {
            return null;
        }
        Alarm item = (Alarm) getListAdapter().getItem(selectedItemPosition);
        return item;
    }

    private void startAlarm(Alarm alarm) {
        Toast.makeText(
                this,
                getString(R.string.message_started_alarm, alarm.getLavel()),
                Toast.LENGTH_LONG).show();
        showNotification(alarm);
        addProximityAlert(alarm);
        Log.d(TAG, "Started the alarm.: " + alarm.dump());
    }

    @SuppressWarnings("deprecation")
    private void showNotification(Alarm alarm) {
        // TODO titleもmessageも文字列が長いと見きれてしまう。折り返しか改行できないか
        String title = getString(R.string.app_name) + ": " + alarm.getLavel();
        String message = "アラームを開始しました。アラームを停止する場合に選択します。"; // TODO リソースへ
        String ticker = title;

        Notification notification = new Notification(R.drawable.ic_launcher,
                ticker, System.currentTimeMillis());
        notification.flags = notification.flags
                | Notification.FLAG_NO_CLEAR
                | Notification.FLAG_ONGOING_EVENT;
        Intent intent = new Intent(this, AlarmStopConfirmationActivity.class)
                .putExtra("alarm.id", alarm.getId());
        notification.setLatestEventInfo(this, title, message,
                PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));

        mNotificationManager.notify(alarm.hashCode(), notification);
    }

    private void addProximityAlert(Alarm alarm) {
        double latitude = alarm.getLatitude();
        double longitude = alarm.getLongitude();
        int radius =  alarm.getDistance() * 1000;
        long noExpiration = -1;

        Intent intent = new Intent(this, ProximityAlertReceiver.class)
                .putExtra("alarm.id", alarm.getId());
        mLocationManager.addProximityAlert(latitude, longitude, radius, noExpiration,
                PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));
    }

    private boolean existsAlreadyStartedAlarm() {
        // TODO 未実装
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult()");
        if (requestCode == DestinationSelectionActivity.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
//                String address = data.getExtras().getString(
//                        DestinationSelectionActivity.EXTRA_ADDRESS);
                int latitudeE6 = data.getExtras().getInt(
                        DestinationSelectionActivity.EXTRA_LATITUDE_E6);
                int longitudeE6 = data.getExtras().getInt(
                        DestinationSelectionActivity.EXTRA_LONGITUDE_E6);

                Alarm alarm = new Alarm();
                // alarm.setAddress(address); // TODO 将来、MAPから住所を取得するようにしたい
//                alarm.setLavel(address);
                alarm.setLatitudeE6(latitudeE6);
                alarm.setLongitudeE6(longitudeE6);
                alarm.setDistance(getDefaultDistance()); // TODO アラーム編集画面で個別に指定できるようにする
                alarm.setDuration(getDefaultDuration()); // TODO アラーム編集画面で個別に指定できるようにする
                alarm.autoLavel();
                alarm.save();
                Log.d(TAG, "Created the alarm settings.: " + alarm.dump());
                startAlarmEditActivity(alarm);
                refreshView();
            } else if (resultCode == RESULT_CANCELED) {
                // Do nothing.
            }
        }
    }

    private void startDestinationSelectionActivity(Alarm alarm) {
        Intent intent = new Intent(this, DestinationSelectionActivity.class);
        if (alarm != null) {
            intent.putExtra(DestinationSelectionActivity.EXTRA_LATITUDE_E6, alarm.getLatitudeE6());
            intent.putExtra(DestinationSelectionActivity.EXTRA_LONGITUDE_E6, alarm.getLongitudeE6());
        }
        startActivityForResult(intent,
                DestinationSelectionActivity.REQUEST_CODE);
    }

    private void startAlarmEditActivity(Alarm alarm) {
        Intent intent = new Intent(this, AlarmEditActivity.class).putExtra(
                "alarm.id", alarm.getId());
        intent.putExtra("alarm.id", alarm.getId());
        startActivity(intent);
    }

    private void deleteAlarm(Alarm alarm) {
        alarm.delete();
        refreshView();
        Log.d(TAG, "Deleted the alarm settings.: " + alarm.dump());
    }

    private void startSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private int getDefaultDistance() {
        return PreferenceManager.getDefaultSharedPreferences(this).getInt(
                SettingsActivity.KEY_ALARM_DISTANCE,
                R.string.default_alarm_distance);
    }

    private int getDefaultDuration() {
        return PreferenceManager.getDefaultSharedPreferences(this).getInt(
                SettingsActivity.KEY_ALARM_DURATION,
                R.string.default_alarm_duration);
    }
}
