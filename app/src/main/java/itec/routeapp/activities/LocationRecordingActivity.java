package itec.routeapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import itec.routeapp.R;

public class LocationRecordingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("Blabla", "Test");
        Intent serviceIntent = new Intent(this, LocationService.class);
//        LocationRecordingActivity.this.startActivity(serviceIntent);
        startService(serviceIntent);
        Log.e("After service start", "stat");

        setContentView(R.layout.activity_location_recording);
        //        Retrieve extra parameters received from the other side
//        Intent intent = getIntent();
//        String value = intent.getStringExtra("key"); //if it's a string you stored.

    }
}



