package jp.gr.java_conf.tomoyorn.locationalarm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import jp.gr.java_conf.tomoyorn.locationalarm.model.Alarm;
import jp.gr.java_conf.tomoyorn.locationalarm.util.Log;

/**
 * アラーム編集画面のActivityです。
 *
 * @author tomoyorn
 */
public class AlarmEditActivity extends Activity {

    private static final String TAG = "AlarmEditActivity";

    public static final String EXTRA_ALARM_ID = "EXTRA_ALARM_ID";

    private EditText mEditTextLabel;
    private EditText mEditTextDestination;

    Alarm mAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_edit);

        mAlarm = Alarm.find(getIntent().getExtras().getLong(EXTRA_ALARM_ID));
        mEditTextDestination = (EditText) findViewById(R.id.edit_text_destination);
        mEditTextDestination.setText(toDisplayLatitudeAndLongitude(
                mAlarm.getLatitudeE6(), mAlarm.getLongitudeE6()));
        mEditTextLabel = (EditText) findViewById(R.id.edit_text_label);
        mEditTextLabel.setText(mAlarm.getLavel());
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause()");
        super.onPause();
        int[] latitudeE6AndlongitudeE6 = splitDisplayLatitudeAndLongitude(mEditTextDestination
                .getText().toString());
        mAlarm.setLatitudeE6(latitudeE6AndlongitudeE6[0]);
        mAlarm.setLongitudeE6(latitudeE6AndlongitudeE6[1]);
        mAlarm.setLavel(mEditTextLabel.getText().toString().trim());
        mAlarm.save();
        Log.d(TAG, "Saved the alarm settings.: " + mAlarm.dump());
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult()");
        if (requestCode == DestinationSelectionActivity.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // String address = data.getExtras().getString(
                // DestinationSelectionActivity.EXTRA_ADDRESS);
                int latitudeE6 = data.getExtras().getInt(
                        DestinationSelectionActivity.EXTRA_LATITUDE_E6);
                int longitudeE6 = data.getExtras().getInt(
                        DestinationSelectionActivity.EXTRA_LONGITUDE_E6);
                mEditTextDestination.setText(toDisplayLatitudeAndLongitude(
                        latitudeE6, longitudeE6));
            } else if (resultCode == RESULT_CANCELED) {
                // Do nothing.
            }
        }
    }

    private String toDisplayLatitudeAndLongitude(int latitudeE6, int longitudeE6) {
        return (latitudeE6 / 1E6) + ", " + (longitudeE6 / 1E6);
    }

    private int[] splitDisplayLatitudeAndLongitude(String displayLatitudeAndLongitude) {
        String[] latitudeAndlongitude = displayLatitudeAndLongitude.split(", ");
        int latitudeE6 = (int) (Double.valueOf(latitudeAndlongitude[0]) * 1E6);
        int longitudeE6 = (int) (Double.valueOf(latitudeAndlongitude[1]) * 1E6);
        return new int[] { latitudeE6, longitudeE6 };
    }

    // Callback method for the MAP button.
    public void startDestinationSelectionActivity(View v) {
        Intent intent = new Intent(this, DestinationSelectionActivity.class);
        startActivityForResult(intent,
                DestinationSelectionActivity.REQUEST_CODE);
    }
}
