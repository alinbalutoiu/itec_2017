package itec.routeapp.utils;

import android.icu.text.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Mihaela Ilin on 4/9/2017.
 */

public class DateUtils {

    public static String getDayOfMonthForToday(){
        return String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
    }

    public static String getCurrentMonth(){
        return String.valueOf(Calendar.getInstance().get(Calendar.MONTH));
    }

    public static String getCurrentYear(){
        return String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
    }

    public static String getFormattedStringFromMillis(long millis){
        Date date = new Date(millis);
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdfDate.format(date);
    }

}
