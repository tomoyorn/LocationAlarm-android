package jp.gr.java_conf.tomoyorn.locationalarm;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import jp.gr.java_conf.tomoyorn.locationalarm.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DestinationSelectionActivity extends ListActivity {

    public static final int REQUEST_CODE = DestinationSelectionActivity.class.hashCode();

    public static final String EXTRA_ADDRESS = "EXTRA_ADDRESS";
    public static final String EXTRA_LATITUDE_E6 = "EXTRA_LATITUDE_E6";
    public static final String EXTRA_LONGITUDE_E6 = "EXTRA_LONGITUDE_E6";

    private static final List<Destination> DESTINATIONS = new ArrayList<Destination>();
    static {
        DESTINATIONS.add(new Destination("国分寺駅", 35700256, 139480257));
        DESTINATIONS.add(new Destination("新宿駅", 35690921, 139700258));
        DESTINATIONS.add(new Destination("品川シーサイド駅", 35609715, 139749688));
        DESTINATIONS.add(new Destination("sandbox", 0, 0));
    }

    private ListView mDestinationListView;
    @SuppressWarnings("unused")
    private Button mOkButton;
    @SuppressWarnings("unused")
    private Button mCancelButton;
    private Destination mSelectedItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("onCreateOptionsMenu()");
        setContentView(R.layout.destination_selection);

        mDestinationListView = getListView();
        mOkButton = (Button) findViewById(R.id.ok);
        mCancelButton = (Button) findViewById(R.id.cancel);

        mDestinationListView.setAdapter(new ArrayAdapter<Destination>(this,
                android.R.layout.simple_list_item_single_choice, DESTINATIONS));
        mDestinationListView.setItemsCanFocus(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("onCreateOptionsMenu()");
        getMenuInflater().inflate(R.menu.destination_selection, menu);
        return true;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        mSelectedItem = (Destination) mDestinationListView.getAdapter().getItem(position);
    }

    // Callback method for the OK button.
    public void ok(View v) {
        if (mSelectedItem == null) {
            Toast.makeText(this, "Unselected item", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Selected item:" + mSelectedItem, Toast.LENGTH_LONG).show();
            Intent intent = new Intent();
            intent.putExtra(EXTRA_ADDRESS, mSelectedItem.getAddress());
            intent.putExtra(EXTRA_LATITUDE_E6, mSelectedItem.getLatitudeE6());
            intent.putExtra(EXTRA_LONGITUDE_E6, mSelectedItem.getLongitudeE6());
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    // Callback method for the Cancel button.
    public void cancel(View v) {
        Toast.makeText(this, "Puchedd Cancel button.", Toast.LENGTH_LONG).show();
        setResult(RESULT_CANCELED);
        finish();
    }

//    private static class Destination implements Serializable {
    private static class Destination {

        private String address;
        private int latitudeE6;
        private int longitudeE6;

        public Destination(String address, int latitudeE6, int longitudeE6) {
           this.address = address;
           this.latitudeE6 = latitudeE6;
           this.longitudeE6 = longitudeE6;
        }

        public String getAddress() {
            return address;
        }

        public int getLatitudeE6() {
            return latitudeE6;
        }

        public int getLongitudeE6() {
            return longitudeE6;
        }
        @Override
        public String toString() {
            return address;
        }
    }
}

