package itec.routeapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import itec.routeapp.AppState;
import itec.routeapp.R;
import itec.routeapp.model.MonthlyAchievement;
import itec.routeapp.ui.AchievementAdapter;
import itec.routeapp.utils.DateUtils;

public class AchievementListActivity extends AppCompatActivity {

    private List<MonthlyAchievement> achievements = new ArrayList<>();

    private TextView dailyAchText;
    private ExpandableHeightListView achievementList;

    private static final String TAG = "AchievementListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement_list);

        achievementList = (ExpandableHeightListView)findViewById(R.id.expandable_listview);
        final AchievementAdapter adapter = new AchievementAdapter(this, R.layout.monthly_award, achievements);
        achievementList.setAdapter(adapter);
        achievementList.setExpanded(true);

        // display daily achievements
        dailyAchText = (TextView)findViewById(R.id.counter_today);
        AppState.get().getDatabaseReference().child(AppState.get().getUserId())
                .child("achievements").child(DateUtils.getCurrentYear())
                .child(DateUtils.getCurrentMonth()).child(DateUtils.getDayOfMonthForToday())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        updateTodayCounter((Integer)dataSnapshot.getValue());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        dbCancelled(databaseError);
                    }
                });

        // display achievements history list
        String userId = AppState.get().getUserId();
        DatabaseReference databaseReference = AppState.get().getDatabaseReference();
        databaseReference.child(userId).child("achievements")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        try{
                            MonthlyAchievement achievement = dataSnapshot.getValue(MonthlyAchievement.class);
                            achievement.setMonth(Integer.parseInt(dataSnapshot.getKey()));
                            achievements.add(achievement);
                            adapter.notifyDataSetChanged();

                        }catch(Exception e){
                            Log.e(TAG, e.getStackTrace().toString());
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                            /*Outfit o = dataSnapshot.getValue(Outfit.class);
                            o.outfitId = dataSnapshot.getKey();
                            outfits.remove(o);
                            adapter.notifyDataSetChanged();*/
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        dbCancelled(databaseError);
                    }
                });
    }

    private void dbCancelled(DatabaseError databaseError){
        int code = databaseError.getCode();
        String message = databaseError.getMessage();
        String details = databaseError.getDetails();
        Log.e(TAG, code + "\n" + message + "\n" + details);
    }

    private void updateTodayCounter(Integer counter){
        dailyAchText.setText(String.valueOf(counter));
    }

}
