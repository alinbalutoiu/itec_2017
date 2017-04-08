package itec.routeapp.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Mihaela Ilin on 4/8/2017.
 */

@IgnoreExtraProperties
public class Reading implements Serializable{

    private long elapsedRealtimeNanos;

    private Double latitude;
    private Double longitude;
    private Double altitude;

    private float speed;
//    private Double legalSpeedLimit;

    public Reading() {
    }

    public Reading(long elapsedRealtimeNanos, Double latitude, Double longitude, Double altitude,
                   float speed) {
        this.elapsedRealtimeNanos = elapsedRealtimeNanos;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.speed = speed;
    }

    public long getElapsedRealtimeNanos() {
        return elapsedRealtimeNanos;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getAltitude() {
        return altitude;
    }

    public float getSpeed() {
        return speed;
    }
}
