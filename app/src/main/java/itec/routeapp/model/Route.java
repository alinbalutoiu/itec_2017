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

    private long timeMillis;
    // used to compare location fixes
    private long elapsedRealtimeNanos;

    private Integer dayOfWeek;

    private List<Reading> readings;

//    private String name;

//    private weather

    private int transport;

//    private int percentGoodDriver;

    public Route() {
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    /*public String getName() {
        return name;
    }*/

    public List<Reading> getReadings() {
        return readings;
    }

    public int getYear() {
        return year;
    }

    public int getTransport() {
        return transport;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public long getTimeMillis() {
        return timeMillis;
    }

    public void setTimeMillis(long timeMillis) {
        this.timeMillis = timeMillis;
    }

    public void setReadings(List<Reading> readings) {
        this.readings = readings;
    }

    /*public void setName(String name) {
        this.name = name;
    }*/

    public void setTransport(int transport) {
        this.transport = transport;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public long getElapsedRealtimeNanos() {
        return elapsedRealtimeNanos;
    }

    public void setElapsedRealtimeNanos(long elapsedRealtimeNanos) {
        this.elapsedRealtimeNanos = elapsedRealtimeNanos;
    }

    public Integer getDayOfWeek() {
        return dayOfWeek;
    }
}
