package itec.routeapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import itec.routeapp.R;

public class LocationRecordingActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_location_recording);
        //        Retrieve extra parameters received from the other side
//        Intent intent = getIntent();
//        String value = intent.getStringExtra("key"); //if it's a string you stored.
    }


}
