package itec.routeapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import itec.routeapp.AppState;
import itec.routeapp.R;
import itec.routeapp.utils.AuthUtils;

public class MainActivity extends AppCompatActivity {

    // Firebase authentication
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private DatabaseReference databaseReference;

    private static final String TAG = "MainActivity";

    // Request codes
    private static final int REQ_SIGNIN = 3;
//    private static final int REQUEST_CAMERA = 4;
//    private static final int SELECT_FILE = 5;

    private String userChosenTask;

    // Hack for authStateChangeListener called multiple times
    private String latestUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        AppState.get().setAuth(mAuth);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    if(!user.getUid().equals(latestUserId)){
                        latestUserId = user.getUid();
                        AppState.get().setUserId(user.getUid());
                        attachDBListener(user.getUid());
                    }
                } else {
                    latestUserId = null;
                    startActivityForResult(new Intent(getApplicationContext(), LoginActivity.class),
                            REQ_SIGNIN);
                }
            }
        };

//        createStartRecordingButtonListener();
    }

    private void attachDBListener(String uid) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);
        databaseReference = database.getReference();
        AppState.get().setDatabaseReference(databaseReference);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sign_out:
                AuthUtils.signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //todo
    public void clicked(View v){
        switch (v.getId()){
            case R.id.all_routes_image:
//                startActivity(new Intent(this, RouteListActivity.class));
                break;
            case R.id.new_route_image:
                createRecordingLocationActivity();
                break;
            case R.id.stats_image:
                break;
            case R.id.achievements_image:
                break;
        }
    }

    public void createRecordingLocationActivity() {
        Intent myIntent = new Intent(MainActivity.this, LocationRecordingActivity.class);
        // For extra parameters
        // myIntent.putExtra("key", value); //Optional parameters
        MainActivity.this.startActivity(myIntent);
    }
}
