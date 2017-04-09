package itec.routeapp.utils;

import android.location.Location;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

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

    private static final String TAG = "PersistenceUtils";

    private static Integer monthlyAchievementCounter;
    private static Integer dailyAchievementCounter;

    public static void saveRoute(List<Location> points, int meansOfTransport){
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
        route.setDayOfWeek(cal.get(Calendar.DAY_OF_WEEK));
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
        database.child(appState.getUserId()).child("routes")
                .child(String.valueOf(year))
                .child(String.valueOf(month)).child(String.valueOf(day))
                .child(String.valueOf(routeStartingTime))
                .setValue(route);
    }

    public static void addAchievement(){
        DatabaseReference database = AppState.get().getDatabaseReference();
        Calendar cal = Calendar.getInstance();
        String year = String.valueOf(cal.get(Calendar.YEAR));
        String month = String.valueOf(cal.get(Calendar.MONTH));
        String day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
        // read current value of the monthly counter
        DatabaseReference monthRef = database.child(AppState.get().getUserId()).child("achievements")
                .child(year).child(month);
        monthRef.child("total").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                setMonthlyAchievementCounter((Integer)(dataSnapshot.getValue()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Could not read monthly achievement count from the database!");
            }
        });

        DatabaseReference dayRef = monthRef.child(day);
        dayRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        setDailyAchievementCounter((Integer)(dataSnapshot.getValue()));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, "Could not read daily achievement count from the database!");
                    }
                });

        // increment the counters
        monthRef.setValue(monthlyAchievementCounter+1);
        dayRef.setValue(dailyAchievementCounter+1);
    }

    private static void setMonthlyAchievementCounter(int counter){
        monthlyAchievementCounter = counter;
    }

    private static void setDailyAchievementCounter(int counter){
        dailyAchievementCounter = counter;
    }

}
