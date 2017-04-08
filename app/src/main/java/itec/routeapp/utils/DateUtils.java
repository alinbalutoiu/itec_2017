package itec.routeapp.utils;

import java.util.Calendar;

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

}
