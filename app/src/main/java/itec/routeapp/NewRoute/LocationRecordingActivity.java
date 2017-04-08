package itec.routeapp.NewRoute;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import itec.routeapp.R;
import itec.routeapp.model.MeansOfTransport;
import itec.routeapp.utils.PersistenceUtils;


public class LocationRecordingActivity extends Activity implements OnMapReadyCallback {
    private static final String TAG = "LocRecActivity: ";
    private static List<Location> location_points_list = new ArrayList<>();
    private static MapFragment mMap = null;
    private static GoogleMap gMap = null;
    private static boolean map_inited = false;
    private static List<PolylineOptions> mPolylineOptionsList = new ArrayList<>();
    private static PolylineOptions mPolylineOptions = null;
    private static LatLng startingPoint = null;
    private static List<Location> pointsToBeUpdated = new ArrayList<>();
    private static int zoomCamera = 16;
    private static boolean isRealTimeOn = false;
    private static int lineWidth = 24;
    private int minTime = 8; // Default to 8 seconds
    private int minDistance = 100; // Default to 100 meters
    private Intent serviceIntent = null;

    static public void add_location_to_list(Location loc) {
        Log.e(TAG, "Current location: " + loc.toString());
        location_points_list.add(loc);
        if (isMapVisible() && gMap != null) {
            if (pointsToBeUpdated.size() > 0) {
                // We have multiple points to update
                updateGMaps(null, pointsToBeUpdated);
                pointsToBeUpdated = new ArrayList<>();
            }
            updateGMaps(loc, null);
        } else {
            pointsToBeUpdated.add(loc);
        }
    }

    static private void updateGMaps(Location singleLoc, List<Location> locationList) {
        if (locationList == null) {
            Log.e(TAG, "Updating GMaps with single element");
            LatLng latLng = new LatLng(singleLoc.getLatitude(), singleLoc.getLongitude());
            updatePolyline(singleLoc, location_points_list.get(location_points_list.size() - 2));
            updateMarker(latLng);
            updateCamera(latLng);
        } else {
            Log.e(TAG, "Updating GMaps with list");
            LatLng last_point = null;
            Location previousLoc = location_points_list.get(location_points_list.size() - locationList.size());
            for (Location y : locationList) {
                LatLng x = new LatLng(y.getLatitude(), y.getLongitude());
                updatePolyline(y, previousLoc);
                updateMarker(x);
                last_point = x;
                previousLoc = y;
            }
            updateCamera(last_point);
        }
    }

    static public void clear_points_list() {
        // TODO: choose between car, bus and foot.
        location_points_list = null;
        location_points_list = new ArrayList<>();
        mPolylineOptions = new PolylineOptions();
        if (gMap != null) {
            if (isRealTimeOn) {
                Log.e(TAG, "REALTIME " + isRealTimeOn);
                gMap.clear();
                mPolylineOptionsList = new ArrayList<>();
            } else {
                Log.e(TAG, "REALTIME " + isRealTimeOn);
                gMap.clear();
                mPolylineOptionsList = new ArrayList<>();
                gMap = null;
                map_inited = false;
            }
        }
    }

    static public List<Location> get_current_points_list() {
        if (location_points_list.size() > 0)
            return location_points_list;
        else
            return null;
    }

    static public int chooseColor(int speed) {
        if (speed < 15)
            return Color.GREEN;
        else if (speed < 30)
            return Color.BLUE;
        else if (speed < 45)
            return Color.YELLOW;
        else if (speed < 60)
            return Color.CYAN;
        else if (speed < 75)
            return Color.RED;
        else
            return Color.BLACK;
    }

    static private void updatePolyline(Location mLoc, Location previousLoc) {
        gMap.clear();
        LatLng mLatLng = new LatLng(mLoc.getLatitude(), mLoc.getLongitude());
        LatLng mLatLngPrev = new LatLng(previousLoc.getLatitude(), previousLoc.getLongitude());
        int new_color = chooseColor((int) mLoc.getSpeed());
        boolean switched_color = false;
        if (mPolylineOptions.getColor() != new_color) {
            mPolylineOptionsList.add(mPolylineOptions);
            mPolylineOptions = new PolylineOptions();
            switched_color = true;
        }
        Log.e(TAG, "CURRENT SPEED FROM GPS: " + (int) mLoc.getSpeed() + " km/h");
        mPolylineOptions.color(new_color);
        mPolylineOptions.width(lineWidth);
        mPolylineOptions.add(mLatLngPrev);
        mPolylineOptions.add(mLatLng);
        for (PolylineOptions x : mPolylineOptionsList) {
            Log.e(TAG, "ADDED POLYLINE______");
            gMap.addPolyline(x);
        }
        if (!switched_color) {
            gMap.addPolyline(mPolylineOptions);
        }
    }

    static private void updateMarker(LatLng mLatLng) {
        gMap.addMarker(new MarkerOptions().position(mLatLng));
    }

    static private void updateCamera(LatLng mLatLng) {
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, zoomCamera));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        check_permissions();

        Intent intent = getIntent();
        minTime = intent.getIntExtra("minTime", 8);
        minDistance = intent.getIntExtra("minDistance", 100);
        setContentView(R.layout.activity_location_recording);
        //        Retrieve extra parameters received from the other side
//        Intent intent = getIntent();
//        String value = intent.getStringExtra("key"); //if it's a string you stored.
        init_recordingButtonAndMessage();
        init_realtimeViewer();
        mMap = (MapFragment) getFragmentManager().findFragmentById(R.id.mapView);
        mMap.getView().setVisibility(View.GONE);
    }

    private void init_recordingButtonAndMessage() {
        final Button start_recording = (Button) findViewById(R.id.start_recording_button);
        final TextView alert_location_recorded = (TextView) findViewById(R.id.location_recorded);
        start_recording.setOnClickListener(new View.OnClickListener() {
            int enabled = 0;

            @Override
            public void onClick(View view) {
                if (enabled == 0) {
                    init_recording();
                    start_recording.setText(getString(R.string.stop_recording_location));
                    alert_location_recorded.setVisibility(View.VISIBLE);
                    enabled = 1;
                } else {
                    uninit_recording();
                    start_recording.setText(getString(R.string.start_recording_location));
                    alert_location_recorded.setVisibility(View.INVISIBLE);
                    enabled = 0;
                }
            }
        });
    }

    private void init_realtimeViewer() {
        final Button realtimeViewer = (Button) findViewById(R.id.realtime_viewer);
        final TextView realtimeReq = (TextView) findViewById(R.id.internet_required_texthelp);
        realtimeViewer.setOnClickListener(new View.OnClickListener() {
            int enabled = 0;
            Toast previousToast = null;

            @Override
            public void onClick(View view) {
                if (enabled == 0 && !isRecordingLocation() && !isNetworkConnected()) {
                    if (previousToast != null) {
                        previousToast.cancel();
                    }
                    previousToast = Toast.makeText(view.getContext(), "Internet must be enabled for this feature. Data might not be accurate.", Toast.LENGTH_SHORT);
                    previousToast.show();
                }
                if (isRecordingLocation() && !isMapShown()) {
                    if (previousToast != null) {
                        previousToast.cancel();
                    }
                    previousToast = Toast.makeText(view.getContext(), "Location recording must be enabled", Toast.LENGTH_LONG);
                    previousToast.show();
                    return;
                }
                if (enabled == 0) {
                    realtimeViewer.setText(getString(R.string.realtime_viewing_off));
                    realtimeReq.setVisibility(View.GONE);
                    init_realtime();
                    enabled = 1;
                    isRealTimeOn = true;
                } else {
                    realtimeViewer.setText(getString(R.string.realtime_viewing_on));
                    uninit_realtime();
                    realtimeReq.setVisibility(View.VISIBLE);
                    enabled = 0;
                    isRealTimeOn = false;
                }
            }
        });
    }

    private void init_realtime() {
//        LocationManager lm
        Log.e(TAG, "init_realtime");
        init_map();
    }

    private void uninit_realtime() {
        Log.e(TAG, "uninit_realtime");
        hide_map();
    }

    private void init_recording() {
        Log.e(TAG, "Starting Recording Location");
        clear_points_list();
        startRecording();
    }

    private void uninit_recording() {
        Log.e(TAG, "Stopping Recording Location");
        stopRecording();
        get_current_points_list();
    }

    private void startRecording() {
        LocationService.setMinTime(minTime);
        LocationService.setMinDistance(minDistance);
        serviceIntent = new Intent(this, LocationService.class);
        startService(serviceIntent);
    }

    private void stopRecording() {
        if (serviceIntent != null) {
            stopService(serviceIntent);
            serviceIntent = null;
        }
        if (location_points_list != null)
            PersistenceUtils.saveRoute(location_points_list, MeansOfTransport.CAR);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;

        // Add a marker in Sydney and move the camera
//        Location cLoc = getLastKnownLocation();
//        LatLng sydney = new LatLng(-34, 151);
//        LatLng cLatLng = new LatLng(cLoc.getLatitude(), cLoc.getLongitude());
//        gMap.addMarker(new MarkerOptions().position(cLatLng).title("Starting position"));
//        gMap.moveCamera(CameraUpdateFactory.newLatLng(cLatLng));
//        gMap.animateCamera(CameraUpdateFactory.zoomTo(16));
//        add_location_to_list(cLoc);
        if (startingPoint == null) {
            Location cLoc = getLastKnownLocation();
            add_location_to_list(cLoc);
        }
        mMap.getView().setVisibility(View.VISIBLE);
    }

//    //Draw polyline on the map
//    static public void drawPolyLineOnMap(List<Location> list, GoogleMap googleMap) {
//        polyOptions = new PolylineOptions();
//        // TODO: Change the color regarding to speed
//        polyOptions.color(Color.GREEN);
//        polyOptions.width(16);
//        List<LatLng> latLngs = new ArrayList<>();
//        for (Location loc : list) {
//            latLngs.add(new LatLng(loc.getLatitude(), loc.getLongitude()));
//        }
//        polyOptions.addAll(latLngs);
//        googleMap.addPolyline(polyOptions);
//        LatLngBounds.Builder builder = new LatLngBounds.Builder();
//        for (LatLng latLng : latLngs) {
//            builder.include(latLng);
//        }
//        builder.build();
//    }

    private void init_map() {
        if (mMap == null) {
            return;
        }
        if (!map_inited) {
            mMap.getMapAsync(this);
            map_inited = true;
            mPolylineOptions = new PolylineOptions();
            mPolylineOptions.color(Color.BLUE).width(lineWidth);
        } else {
            mMap.getView().setVisibility(View.VISIBLE);
//            drawPolyLineOnMap(location_points_list, gMap);
        }
    }

    private void hide_map() {
        if (mMap == null)
            return;
        if (mMap.getView().isShown())
            mMap.getView().setVisibility(View.GONE);
    }

    private boolean isMapShown() {
        return mMap.getView().isShown();
    }

    private boolean isRecordingLocation() {
        Button start_recording = (Button) findViewById(R.id.start_recording_button);
        Log.e(TAG, "isRecordingLocation: " + (start_recording.getText().equals(getString(R.string.start_recording_location))));
        return start_recording.getText().equals(getString(R.string.start_recording_location));
    }

    private boolean isRealTimeViewerOn() {
        Button realtimeViewer = (Button) findViewById(R.id.realtime_viewer);
        return realtimeViewer.getText().equals(getString(R.string.realtime_viewing_on));
    }

    private static boolean isMapVisible() {
        if (mMap != null) {
            return mMap.getView().isShown();
        }
        return false;
    }

    private Location getLastKnownLocation() {
        Location cLoc;
        if (location_points_list.size() != 0)
            cLoc = location_points_list.get(location_points_list.size() - 1);
        else
            cLoc = pointsToBeUpdated.get(0);
        return cLoc;
    }

    private void check_permissions() {
        int checkPermission = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);

        if (checkPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int i = 0; i < permissions.length; i++) {
            String permission = permissions[i];
            int grantResult = grantResults[i];

            if (permission.equals(android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocationService.cleanup();
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        Log.e(TAG, "isNetworkConnected: " + (cm.getActiveNetworkInfo() != null));
        return cm.getActiveNetworkInfo() != null;
    }
}



