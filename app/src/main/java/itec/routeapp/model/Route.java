package itec.routeapp.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Mihaela Ilin on 4/8/2017.
 */

@IgnoreExtraProperties
public class Route implements Serializable{

    private int year, month, day;

    private String timestamp;

    private List<Reading> readings;

    private String name;

//    private weather

    private MeansOfTransport transport;

    private int percentGoodDriver;

    public Route() {
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public String getName() {
        return name;
    }

    public int getPercentGoodDriver() {
        return percentGoodDriver;
    }

    public List<Reading> getReadings() {
        return readings;
    }

    public int getYear() {
        return year;
    }

    public MeansOfTransport getTransport() {
        return transport;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
