package jp.gr.java_conf.tomoyorn.locationalarm;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
// DestinationSelectionActivity

import jp.gr.java_conf.tomoyorn.locationalarm.util.Log;

public class DestinationSelectionActivity extends MapActivity {

    private static final String TAG = "DestinationSelectionActivity";

    public static final int REQUEST_CODE = DestinationSelectionActivity.class.hashCode();
//    public static final String EXTRA_ADDRESS = "EXTRA_ADDRESS";
    public static final String EXTRA_LATITUDE_E6 = "EXTRA_LATITUDE_E6";
    public static final String EXTRA_LONGITUDE_E6 = "EXTRA_LONGITUDE_E6";
    private static final int DEFAULT_ZOOM_LEVEL = 16;

    MapView mMapView;
    MyLocationOverlay mMyLocationOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.destination_selection);

        double[] shinjyuku = { 35.690921, 139.700258 }; // TODO 仮実装。新宿駅
        double latitude = shinjyuku[0];
        double longitude = shinjyuku[1];

        boolean hasLocation = (getIntent().getExtras() != null);
        if (hasLocation) {
            latitude = getIntent().getExtras().getInt(EXTRA_LATITUDE_E6);
            longitude = getIntent().getExtras().getInt(EXTRA_LONGITUDE_E6);
        } else {
            // TODO  MyLocationOverlayから現在位置を初期表示する
        }

        mMapView = (MapView) findViewById(R.id.map);

        mMapView.setBuiltInZoomControls(true);
        mMapView.getController().setZoom(DEFAULT_ZOOM_LEVEL);

        mMyLocationOverlay = new MyLocationOverlay(this, mMapView);
        mMyLocationOverlay.enableMyLocation();
        mMapView.getOverlays().add(mMyLocationOverlay);

        mMapView.getOverlays().add(new DestinationOverlay());

        moveToCenter(new GeoPoint(
                (int) (latitude * 1E6),
                (int) (longitude * 1E6)));

        // 自前のMapControllerクラスでも作成しようか → それほど大きいクラスでもないのでやらない
        // クラス名どうしよう。
        // MapController: ふさわしいけどMaps APIのクラスと被る。MapViewControllerもあり
        // MapOperator: controlのシノニム。いささか大げさか。
        // MapHelper: ヘルパーだと意味が大きすぎる。しかしネストクラスだから良いかな → 有力
        // MapViewController → これも有力
//        mMapHelper = new MapHelper(mMapView);
//        mMapHelper.useZoom(true, DEFAULT_ZOOM_LEVEL);
//        mMapHelper.moveToCenter(latitude, longitude, true);
//        mMapHelper.addOverlay(overlay);
//        mMapHelper. .....;
    }

    @Override
    public void onPause() {
        super.onPause();
        mMyLocationOverlay.disableMyLocation();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMyLocationOverlay.enableMyLocation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.destination_selection, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // none
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_my_location:
            mMyLocationOverlay.enableMyLocation();
            mMyLocationOverlay.runOnFirstFix(new Runnable() {
                @Override
                public void run() {
                    moveToCenter(mMyLocationOverlay.getMyLocation(), true);
                    mMyLocationOverlay.disableMyLocation();
                }
            });
            return true;
        case R.id.menu_search_location:
            // TODO 検索機能を将来サポートする。
            //     「検索」メニューを押すとテキスト入力ダイアログが現れて、
            //     マップを検索できる。
            Toast.makeText(this,
                    "検索機能は、現在サポートしていません。将来サポートする予定です。",
                    Toast.LENGTH_LONG).show();
        default:
            Log.w("Unknown selected menu.");
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

    // Callback method for the ok button.
    public void ok(View v) {
        GeoPoint center = mMapView.getMapCenter();
        Intent data = new Intent()
            .putExtra(EXTRA_LATITUDE_E6, center.getLatitudeE6())
            .putExtra(EXTRA_LONGITUDE_E6, center.getLongitudeE6());
        setResult(RESULT_OK, data);
        finish();
    }

    // Callback method for the cancel button.
    public void cancel(View v) {
        setResult(RESULT_CANCELED);
        finish();
    }

    private void moveToCenter(GeoPoint geoPoint) {
        moveToCenter(geoPoint, false);
    }

    private void moveToCenter(GeoPoint geoPoint, boolean animate) {
        MapController mapController = mMapView.getController();
        if (animate) {
            mapController.animateTo(geoPoint);
        } else {
            mapController.setCenter(geoPoint);
        }
    }

    /**
     * マップの中心に目的地マークを描画するためのオーバーレイ
     */
    private class DestinationOverlay extends Overlay {

        private Bitmap icon;

        public DestinationOverlay() {
            this.icon = BitmapFactory.decodeResource(getResources(),
                    android.R.drawable.ic_menu_mylocation);
        }

        @Override
        public void draw(Canvas canvas, MapView mapView, boolean shadow) {
            if (!shadow) {
                canvas.drawBitmap(icon,
                        mapView.getWidth() / 2 - icon.getWidth() / 2,
                        mapView.getHeight() / 2 - icon.getHeight() / 2,
                        null);
            }
        }
    }
}
//public class DestinationSelectionActivity extends ListActivity {
//
//    public static final int REQUEST_CODE = DestinationSelectionActivity.class.hashCode();
//
//    public static final String EXTRA_ADDRESS = "EXTRA_ADDRESS";
//    public static final String EXTRA_LATITUDE_E6 = "EXTRA_LATITUDE_E6";
//    public static final String EXTRA_LONGITUDE_E6 = "EXTRA_LONGITUDE_E6";
//
//    private static final List<Destination> DESTINATIONS = new ArrayList<Destination>();
//    static {
//        DESTINATIONS.add(new Destination("国分寺駅", 35700256, 139480257));
//        DESTINATIONS.add(new Destination("新宿駅", 35690921, 139700258));
//        DESTINATIONS.add(new Destination("品川シーサイド駅", 35609715, 139749688));
//        DESTINATIONS.add(new Destination("sandbox", 0, 0));
//    }
//
//    private ListView mDestinationListView;
//    @SuppressWarnings("unused")
//    private Button mOkButton;
//    @SuppressWarnings("unused")
//    private Button mCancelButton;
//    private Destination mSelectedItem;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        Log.d("onCreateOptionsMenu()");
//        setContentView(R.layout.destination_selection);
//
//        mDestinationListView = getListView();
//        mOkButton = (Button) findViewById(R.id.ok);
//        mCancelButton = (Button) findViewById(R.id.cancel);
//
//        mDestinationListView.setAdapter(new ArrayAdapter<Destination>(this,
//                android.R.layout.simple_list_item_single_choice, DESTINATIONS));
//        mDestinationListView.setItemsCanFocus(false);
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        Log.d("onCreateOptionsMenu()");
//        getMenuInflater().inflate(R.menu.destination_selection, menu);
//        return true;
//    }
//
//    @Override
//    protected void onListItemClick(ListView l, View v, int position, long id) {
//        mSelectedItem = (Destination) mDestinationListView.getAdapter().getItem(position);
//    }
//
//    // Callback method for the OK button.
//    public void ok(View v) {
//        if (mSelectedItem == null) {
//            Toast.makeText(this, "Unselected item", Toast.LENGTH_LONG).show();
//        } else {
//            Toast.makeText(this, "Selected item:" + mSelectedItem, Toast.LENGTH_LONG).show();
//            Intent intent = new Intent();
//            intent.putExtra(EXTRA_ADDRESS, mSelectedItem.getAddress());
//            intent.putExtra(EXTRA_LATITUDE_E6, mSelectedItem.getLatitudeE6());
//            intent.putExtra(EXTRA_LONGITUDE_E6, mSelectedItem.getLongitudeE6());
//            setResult(RESULT_OK, intent);
//            finish();
//        }
//    }
//
//    // Callback method for the Cancel button.
//    public void cancel(View v) {
//        Toast.makeText(this, "Puchedd Cancel button.", Toast.LENGTH_LONG).show();
//        setResult(RESULT_CANCELED);
//        finish();
//    }
//
////    private static class Destination implements Serializable {
//    private static class Destination {
//
//        private String address;
//        private int latitudeE6;
//        private int longitudeE6;
//
//        public Destination(String address, int latitudeE6, int longitudeE6) {
//           this.address = address;
//           this.latitudeE6 = latitudeE6;
//           this.longitudeE6 = longitudeE6;
//        }
//
//        public String getAddress() {
//            return address;
//        }
//
//        public int getLatitudeE6() {
//            return latitudeE6;
//        }
//
//        public int getLongitudeE6() {
//            return longitudeE6;
//        }
//        @Override
//        public String toString() {
//            return address;
//        }
//    }
//}
