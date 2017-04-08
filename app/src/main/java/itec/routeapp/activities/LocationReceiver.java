package itec.routeapp.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.widget.Toast;

public class LocationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Location loc = (Location) intent.getExtras().get(android.location.LocationManager.KEY_LOCATION_CHANGED);
        // TODO:
        // SAVE LOCALLY
        Toast.makeText(context, loc.toString(), Toast.LENGTH_SHORT).show();
    }
}
