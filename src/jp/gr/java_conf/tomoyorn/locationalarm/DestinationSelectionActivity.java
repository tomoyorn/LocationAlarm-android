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

import jp.gr.java_conf.tomoyorn.locationalarm.util.Log;

/**
 * 目的地選択画面のActivityです。
 *
 * @author tomoyorn
 */
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
