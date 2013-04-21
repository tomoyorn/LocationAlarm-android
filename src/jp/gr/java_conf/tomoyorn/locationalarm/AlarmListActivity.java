package jp.gr.java_conf.tomoyorn.locationalarm;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.activeandroid.query.Select;

import jp.gr.java_conf.tomoyorn.locationalarm.model.Alarm;
import jp.gr.java_conf.tomoyorn.locationalarm.util.Log;

import org.apache.commons.lang3.Validate;

import java.util.ArrayList;

public class AlarmListActivity extends ListActivity {

    private static final String TAG = "AlarmListActivity";

    // private ListView mAlarmListView;
    // private ArrayAdapter<Alarm> mAdapter; // TODO
    // mAdapter,mAlarmsをメンバからはずせないかな
    // private ArrayList<Alarm> mAlarms = new ArrayList<Alarm>(); // TODO
    // IDの降順の方が使いやすいかな
    // private Alarm mSelectedAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_list);

        // ArrayList<Alarm> mAlarms = new ArrayList<Alarm>();
        // ArrayAdapter<Alarm> mAdapter = new ArrayAdapter<Alarm>(this,
        // android.R.layout.simple_list_item_activated_1, mAlarms);
        // mAlarmListView = getListView();
        // mAlarmListView.setAdapter(mAdapter);
        // mAlarmListView.setItemsCanFocus(false);

        // refresh();
        // TODO [優先度低] 画面がバックグラウンドから復帰するたびに位置らか作りなおすのがなんか不毛。
        // 選択の状態もリセット視されてしまうし。設定画面での初期化があるからこうせざるをえない。
        // →ま、実装がシンプルになるのでこのままでも良いか
        setupView();
    }

    @Override
    protected void onResume() {
        Log.d("onResume()");
        super.onResume();
        // refresh();
        setupView();
    }

    private void setupView() {
        ArrayList<Alarm> mAlarms = Alarm.findAll();
        ArrayAdapter<Alarm> mAdapter = new ArrayAdapter<Alarm>(this,
                android.R.layout.simple_list_item_activated_1, mAlarms);
        ListView mAlarmListView = getListView();
        setListAdapter(mAdapter);
        // mAlarmListView.setAdapter(mAdapter);
        mAlarmListView.setItemsCanFocus(false);

    }

    private void refreshView() {
        setupView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("onCreateOptionsMenu()");
        getMenuInflater().inflate(R.menu.alarm_list, menu);
        return true;
    }

    public Alarm selectedItem() {
        int selectedItemPosition = getListView().getCheckedItemPosition();
        if (selectedItemPosition == ListView.INVALID_POSITION) {
            return null;
        }
        Alarm item = (Alarm) getListAdapter().getItem(selectedItemPosition);
        return item;
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
                // TODO すでに起動しているアラームが存在していた場合はダイアログを出し確認する
            }
            startAlarm(alarm);
            notiryStartAlarm(alarm);
            Toast.makeText(
                    this,
                    getString(R.string.message_started_alarm, alarm.getLavel()),
                    Toast.LENGTH_LONG).show();
            // Toast.makeText(this,
            // "アラーム「" + alarm.getLavel() + "」を開始しました。",
            // Toast.LENGTH_LONG).show();
            finish();
            return true;
        case R.id.menu_add_alarm:
            startDestinationSelectionActivity();
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
            Log.w("Unknown selected menu"); // TODO あり得ないのでアサーションエラーかな？
            return super.onOptionsItemSelected(item);
        }
    }

    private boolean existsAlreadyStartedAlarm() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Log.d("onListItemClick()");
        // mSelectedAlarm = (Alarm) mAdapter.getItem(position);
        // TODO l.getCheckedItemPosition()で毎回取るの手
        // getListView().setItemChecked(position, true); // TODO 要らないんじゃね？

        // int checkedItemPosition = mAlarmListView.getCheckedItemPosition();
        // Toast.makeText(this, "checkedItemPosition=" + checkedItemPosition,
        // Toast.LENGTH_LONG).show();
        // System.out.println(mAlarmListView.getCheckedItemPosition());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("onActivityResult()");
        // Log.i(TAG, "Return PosAlarmBetaActivity: requestCode=" + requestCode
        // + ", resultCode=" + resultCode);

        if (requestCode == DestinationSelectionActivity.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // String address =
                // data.getStringExtra(DestinationSelectionActivity.EXTRA_ADDRESS);
                // int latitudeE6 =
                // data.getIntExtra(DestinationSelectionActivity.EXTRA_LATITUDE_E6,
                // 0);
                // int longitudeE6 =
                // data.getIntExtra(DestinationSelectionActivity.EXTRA_LONGITUDE_E6,
                // 0);
                // TODO
                // アサーション。座標はInteger.MIN_VALUEでも返すようにしておく。INVALID_LATITUDE_E6とか名前をつけておく
                // 緯度経度がとりうる値の範囲: 緯度: -90～90, 経度: -180～180

                String address = data.getExtras().getString(
                        DestinationSelectionActivity.EXTRA_ADDRESS);
                int latitudeE6 = data.getExtras().getInt(
                        DestinationSelectionActivity.EXTRA_LATITUDE_E6);
                int longitudeE6 = data.getExtras().getInt(
                        DestinationSelectionActivity.EXTRA_LONGITUDE_E6);

                Alarm alarm = new Alarm();
                // alarm.setAddress(address);
                alarm.setLavel(address);
                // TODO 通知距離などの設定
                alarm.setLatitudeE6(latitudeE6);
                alarm.setLongitudeE6(longitudeE6);
                alarm.autoLavel();
                alarm.save();
                // // TODO onResumeでやれば必要ないかな
                // mAlarms.add(alarm);
                // // mAdapter.notifyDataSetChanged();
                // refresh(); // TODO アラーム詳細画面から戻った後にonResumeでされるはずなので要らない

                startAlarmEditActivity(alarm);
                refreshView();
                // String latitudeE6 = data.getStringExtra("latitudeE6");
                // String longitudeE6 = data.getStringExtra("longitudeE6");
                // Validate.isTrue(NumberUtils.isNumber(latitudeE6),
                // "Invalid latitudeE6: " + latitudeE6);
                // Validate.isTrue(NumberUtils.isNumber(longitudeE6),
                // "Invalid longitudeE6: " + longitudeE6);

                // 新しいアラームの生成を、この画面で行うか、アラーム詳細設定画面
                // （または、目的地選択画面）で行うかについて。
                // この画面で、アラームの削除を行うので、対称性の観点から、生成も
                // この画面で行うべきと判断
                // Alarm alarm = createAlarm(Integer.parseInt(latitudeE6),
                // Integer.parseInt(longitudeE6));
                // startAlarmDetailActivity(alarm);
            } else if (resultCode == RESULT_CANCELED) {
                // TODO アラーム設定を削除しないとダメかもね
            }
        }
    }

    // protected void onActivityResult(int requestCode, int resultCode, Intent
    // data) {
    // super.onActivityResult(requestCode, resultCode, data);
    // // Log.i(TAG, "Return PosAlarmBetaActivity: requestCode=" + requestCode
    // // + ", resultCode=" + resultCode);
    // switch (requestCode) {
    // // case AlarmDetailActivity.REQUEST_CODE:
    // // reloadAlarmListView(); // XXX
    // 冗長になるかもしれないが、このメソッドの最後で必ずリロードするようにしたほうが安全かも
    // // break;
    // case DestinationSelectionActivity.REQUEST_CODE:
    // if (resultCode == RESULT_OK) {
    // // String latitudeE6 = data.getStringExtra("latitudeE6");
    // // String longitudeE6 = data.getStringExtra("longitudeE6");
    // // Validate.isTrue(NumberUtils.isNumber(latitudeE6),
    // "Invalid latitudeE6: " + latitudeE6);
    // // Validate.isTrue(NumberUtils.isNumber(longitudeE6),
    // "Invalid longitudeE6: " + longitudeE6);
    //
    // // 新しいアラームの生成を、この画面で行うか、アラーム詳細設定画面
    // //（または、目的地選択画面）で行うかについて。
    // // この画面で、アラームの削除を行うので、対称性の観点から、生成も
    // // この画面で行うべきと判断
    // // Alarm alarm = createAlarm(Integer.parseInt(latitudeE6),
    // // Integer.parseInt(longitudeE6));
    // // startAlarmDetailActivity(alarm);
    // } else if (resultCode == RESULT_CANCELED) {
    // // TODO アラーム設定を削除しないとダメかもね
    // }
    // break;
    // default:
    // throw new AssertionError("requestCode=" + requestCode);
    // }
    // }

    // private void deleteAlarm(Alarm alarm) {
    // // TODO ここもonResume化によって影響を受ける
    // mAlarms.remove(alarm);
    // alarm.delete();
    // mAdapter.notifyDataSetChanged();
    // }
    private void deleteAlarm(Alarm alarm) {
        // TODO 削除されない場合がある。
        // アラームを追加した後、アラーム一覧画面にもどって何もせずに削除したとき。恐らく selectedAlarmあたりがおかしいのかな
        alarm.delete();
        refreshView();
        // setupView();
        // refresh();
    }

    private void startDestinationSelectionActivity() {
        Intent intent = new Intent(this, DestinationSelectionActivity.class);
        startActivityForResult(intent,
                DestinationSelectionActivity.REQUEST_CODE);
    }

    private void startAlarmEditActivity(Alarm alarm) {
        Intent intent = new Intent(this, AlarmEditActivity.class);
        intent.putExtra(AlarmEditActivity.EXTRA_ALARM_ID, alarm.getId());
        startActivity(intent);
    }

    private void startSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    // private Alarm selectedAlarm() {
    // // TODO アラーム一覧で選択されているAlarmを返す。
    // return new Alarm();
    // }

    private void startAlarm(Alarm alarm) {
        // ArrayList<Alarm> alarms = new Select() // TODO こういうのはAlarmsクラスに閉じ込める
        // .from(Alarm.class)
        // .where("Enabled = ?", true)
        // .execute();
        // for (Alarm a : alarms) {
        // a.setEnabled(false);
        // a.save();
        // }
        // alarm.setEnabled(true);
        // TODO サービスを起動する

        // TODO 未実装
        // LocationManager lm = (LocationManager)
        // getSystemService(LOCATION_SERVICE);
        // double latitude = (double) alarm.getDestination().getLatitudeE6() /
        // 1E6;
        // double longitude = (double) alarm.getDestination().getLongitudeE6() /
        // 1E6;
        // float radius = alarm.getNotificationDistance() * 1000;
        // PendingIntent intent = PendingIntent.getBroadcast(this,
        // 0,
        // new Intent(this, ProximityAlertReceiver.class),
        // 0);
        // long noExpiration = -1;
        // lm.addProximityAlert(latitude, longitude, radius, noExpiration ,
        // intent);
    }

    private void notiryStartAlarm(Alarm alarm) {
        // TODO 未実装
        // long when = System.currentTimeMillis();
        // Notification notification = new Notification(R.drawable.icon,
        // "アラーム「" + alarm.getLavel() + "」を開始しました。", when); // TODO strings.xmlへ
        // // 「通知を消去」による消去を抑制
        // notification.flags = Notification.FLAG_ONGOING_EVENT;
        //
        // Intent intent = new Intent(this,
        // AlarmStopConfirmationActivity.class);
        // // 最近起動したアクティビティのリストに表示しない
        // intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        // intent.putExtra("alarm.id", alarm.getId());
        // PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
        // intent, 0);
        // notification.setLatestEventInfo(this, getString(R.string.app_name),
        // "アラーム「" + alarm.getLavel() + "」を停止する場合に選択します。", // TODO strings.xmlへ
        // contentIntent);
        //
        // NotificationManager nm = (NotificationManager)
        // getSystemService(NOTIFICATION_SERVICE);
        // int id = alarm.getId(); // TODO
        // アラームIDを使用することで複数のアラームを並行して開始可能になった。でも上限はあったほうがいいな
        // nm.notify(id , notification);
    }
}
