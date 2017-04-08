package itec.routeapp.utils;

import android.location.Location;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import itec.routeapp.AppState;
import itec.routeapp.model.DayOfWeek;
import itec.routeapp.model.MeansOfTransport;
import itec.routeapp.model.Reading;
import itec.routeapp.model.Route;

/**
 * Created by Mihaela Ilin on 4/8/2017.
 */

public class PersistenceUtils {

    public static void saveRoute(List<Location> points, MeansOfTransport meansOfTransport){
        if(points == null || points.size() == 0){
            return;
        }
        Route route = new Route();

        long routeStartingTime = points.get(0).getTime();
        Date startDate = new Date(routeStartingTime);
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);

        route.setTimeMillis(routeStartingTime);
        route.setElapsedRealtimeNanos(points.get(0).getElapsedRealtimeNanos());
        route.setDayOfWeek(DayOfWeek.values()[cal.get(Calendar.DAY_OF_WEEK)]);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        route.setDay(day);
        int month = cal.get(Calendar.MONTH);
        route.setMonth(month);
        int year = cal.get(Calendar.YEAR);
        route.setYear(year);
        route.setTransport(meansOfTransport);

        List<Reading> readings = new ArrayList<>();
        route.setReadings(readings);
        for(Location point : points){
            readings.add(new Reading(point.getElapsedRealtimeNanos(), point.getLatitude(),
                    point.getLongitude(), point.getAltitude(), point.getSpeed()));
        }

        // persist to firebase
        AppState appState = AppState.get();
        DatabaseReference database = appState.getDatabaseReference();
        database.child(appState.getUserId()).child(String.valueOf(year))
                .child(String.valueOf(month)).child(String.valueOf(day))
                .child(String.valueOf(routeStartingTime))
                .setValue(route);
    }

}
