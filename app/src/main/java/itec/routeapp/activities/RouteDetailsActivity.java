package itec.routeapp.activities;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

import itec.routeapp.R;
import itec.routeapp.newroute.LocationRecordingActivity;

public class RouteDetailsActivity extends FragmentActivity implements OnMapReadyCallback {

    private List<PolylineOptions> polylineOptionsList = null;
    private static List<Location> points = null;
    private GoogleMap mMap;
    private LatLngBounds.Builder builder = new LatLngBounds.Builder();
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        mMap = googleMap;
        //todo Alin, nu se coloreaza aici!
        drawPolyLineOnMap(points, mMap);
    }

    static public void set_points(List<Location> locationList) {
        points = locationList;
    }

    private void drawPolyLineOnMap(List<Location> locationList, GoogleMap googleMap) {

        PolylineOptions polyOptions = new PolylineOptions();
//        polyOptions.color(Color.GREEN);
//        polyOptions.width(8);
//        polyOptions.addAll(list);
        boolean switched_color = false;
        int previous_color = 0;
        int new_color = LocationRecordingActivity.chooseColor((int) locationList.get(0).getSpeed());
        LatLng first = new LatLng(locationList.get(0).getLatitude(), locationList.get(0).getLongitude());
        LatLng last = first;
        polyOptions.add(first);
        builder.include(first);
        mMap.addMarker(new MarkerOptions().position(first).title("Starting point"));
        locationList.remove(0);
        for (Location x : locationList) {
            LatLng ll = new LatLng(x.getLatitude(), x.getLongitude());
            previous_color = new_color;
            new_color = LocationRecordingActivity.chooseColor((int) locationList.get(0).getSpeed());
            switched_color = previous_color != new_color;
            polyOptions.add(ll);
            if (switched_color) {
                polyOptions.color(previous_color);
                polyOptions.width(24f);
                googleMap.addPolyline(polyOptions);
                polyOptions = new PolylineOptions();
            }
            builder.include(ll);
            last = ll;
        }
        mMap.addMarker(new MarkerOptions().position(last).title("Finishing point"));
        LatLngBounds bounds = builder.build();
        int padding = 20; // offset from edges of the map in pixels
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));
    }
}
