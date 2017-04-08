package itec.routeapp.activities;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class LocationService extends Service {
    private static final String TAG = "GPS_Test";
    // long: minimum time interval between location updates, in milliseconds
    private static final int minTime = 1000;
    // float: minimum distance between location updates, in meters
    private static final float minDistance = 10f;
    // int: Private request code for the sender
    private static final int requestCode = 0;
    // int: May be FLAG_ONE_SHOT, FLAG_NO_CREATE, FLAG_CANCEL_CURRENT, FLAG_UPDATE_CURRENT,
    // FLAG_IMMUTABLE or any of the flags as supported by Intent.fillIn() to control which
    // unspecified parts of the intent that can be supplied when the actual send happens.
    private static final int flags = PendingIntent.FLAG_CANCEL_CURRENT;
    private LocationManager mLocationManager = null;
    private PendingIntent pendingIntent;


    @Nullable
    @Override
    public IBinder onBind(Intent arg0) {
        Log.e(TAG, "TESTTTTT");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate");
        initializeLocationManager();
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        Intent intent = new Intent(this, LocationReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(
                getApplicationContext(), requestCode, intent, flags);
        try {
            mLocationManager.requestLocationUpdates(
                    minTime,
                    minDistance,
                    criteria,
                    pendingIntent);
        } catch (SecurityException se) {
            Log.e(TAG, "NO LOCATION PERMISSION, close application");
        } catch (Exception e) {
            Log.e(TAG, "Error happened when starting location manager");
        }
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            mLocationManager.removeUpdates(pendingIntent);
        }
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }
}

