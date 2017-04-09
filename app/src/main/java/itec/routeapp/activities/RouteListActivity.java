package itec.routeapp.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import itec.routeapp.AppState;
import itec.routeapp.R;
import itec.routeapp.model.MonthlyAchievement;
import itec.routeapp.model.Route;
import itec.routeapp.ui.RouteListAdapter;
import itec.routeapp.utils.DateUtils;
import itec.routeapp.utils.PersistenceUtils;

public class RouteListActivity extends AppCompatActivity {

    private Context c;

    private List<Route> routes;

    private ListView routeList;

    private static final String TAG = "RouteListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_list);
        c = this;

        routeList = (ListView)findViewById(R.id.routeList);
        routes = new ArrayList<>();
        final RouteListAdapter adapter = new RouteListAdapter(c, R.layout.route_detail, routes);
        routeList.setAdapter(adapter);

        routeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RouteDetailsActivity.set_points(PersistenceUtils.getLocationPointsFromReadingsList(
                        routes.get(position).getReadings()
                ));
                startActivity(new Intent(getApplicationContext(), RouteDetailsActivity.class));
            }
        });

        if(routes.isEmpty()){
            // attach listener
            String userId = AppState.get().getUserId();
            DatabaseReference databaseReference = AppState.get().getDatabaseReference();
            databaseReference.child(userId).child("routes")
                    .child(DateUtils.getCurrentYear())
                    .child(DateUtils.getCurrentMonth())
                    .addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            try{
                                Route route = dataSnapshot.getValue(Route.class);
                                route.setTimeMillis(Long.parseLong(dataSnapshot.getKey()));
                                routes.add(route);
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
                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            int code = databaseError.getCode();
                            String message = databaseError.getMessage();
                            String details = databaseError.getDetails();
                            Log.e(TAG, code + "\n" + message + "\n" + details);
                        }
                    });
        }

    }
}
