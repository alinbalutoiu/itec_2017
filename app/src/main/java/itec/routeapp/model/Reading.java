package itec.routeapp.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Mihaela Ilin on 4/8/2017.
 */

@IgnoreExtraProperties
public class Reading implements Serializable{

    private Date timestamp;

    private String latitude;
    private String longitude;
    private String altitude;

    private Double avgSpeed;
    private Double legalSpeedLimit;

    public Reading() {
    }

    public String getAltitude() {
        return altitude;
    }

    public Double getAvgSpeed() {
        return avgSpeed;
    }

    public String getLatitude() {
        return latitude;
    }

    public Double getLegalSpeedLimit() {
        return legalSpeedLimit;
    }

    public String getLongitude() {
        return longitude;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
