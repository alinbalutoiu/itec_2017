package itec.routeapp.NewRoute;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;

public class LocationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Location loc = (Location) intent.getExtras().get(android.location.LocationManager.KEY_LOCATION_CHANGED);
        LocationRecordingActivity.add_location_to_list(loc);
//        Toast.makeText(context, loc.toString(), Toast.LENGTH_SHORT).show();
        Log.e("LocationReceiver: ", loc.toString());
    }
}
