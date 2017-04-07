package itec.routeapp.utils;

import android.location.Location;

/**
 * Created by Alin on 4/7/2017.
 */

public class LocationUtils extends Location {
    // Use metric units
    private boolean bUseMetricUnits = true;

    public LocationUtils(Location location) {
        super(location);
    }

    @Override
    public float distanceTo(Location dest)  {
        float nDistance = super.distanceTo(dest);
        return nDistance;
    }

    @Override
    public float getAccuracy() {
        float nAccuracy = super.getAccuracy();
        return nAccuracy;
    }

    @Override
    public double getAltitude() {
        double nAltitude = super.getAltitude();
        return nAltitude;
    }

    @Override
    public float getSpeed() {
        // Speed in meters per second
        float nSpeed = super.getSpeed() * 3.6f;
        return nSpeed;
    }
}
