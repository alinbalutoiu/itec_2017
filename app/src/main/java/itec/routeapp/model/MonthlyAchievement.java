package itec.routeapp.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Mihaela Ilin on 4/8/2017.
 */

// use this for displaying achievements obtained in the current year
@IgnoreExtraProperties
public class MonthlyAchievement {

    private Integer month;

    // should use a map if we have more types of achievements
    private Integer totalAchievements;

    public MonthlyAchievement() {

    }

    public Integer getMonth() {
        return month;
    }

    public Integer getTotalAchievements() {
        return totalAchievements;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }
}
