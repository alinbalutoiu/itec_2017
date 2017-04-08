package itec.routeapp.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mihaela Ilin on 12/30/2016.
 */

public class PermissionUtils {

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 124;
    private static Map<String, Integer> supportedPermissionRequests = new HashMap<>();

    private static final String PERMISSION_RATIONALE_TITLE = "Permission necessary";
    private static final Map<String, String> permissionRationaleMessages = new HashMap<>();

    private static boolean initialized = false;

    /**
     * This method will check permission at runtime for Marshmallow & greater than Marshmallow version.
     *
     * If current API version is less than Marshmallow, then checkPermission() will return true, which
     * means permission is granted (in Manifest file, no support for runtime permission).
     *
     * If current API version is Marshmallow or greater, and if permission is already granted,
     * then the method returns true. Otherwise, the method returns false and will show a dialog box to
     * a user with allow or deny options.
     */
    public static boolean checkPermission(final Context context, final String permissionCode) {
        if(!initialized){
            initialize();
        }

        if(!permissionRequestSupported(permissionCode)){
            return false;
        }

        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion < Build.VERSION_CODES.M){
            return true;
        }

        if (ContextCompat.checkSelfPermission(context, permissionCode)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permissionCode)){
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                alertBuilder.setCancelable(true);
                alertBuilder.setTitle(PERMISSION_RATIONALE_TITLE);
                alertBuilder.setMessage(permissionRationaleMessages.get(permissionCode));
                alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[]{permissionCode},
                                supportedPermissionRequests.get(permissionCode));
                    }
                });
                AlertDialog alert = alertBuilder.create();
                alert.show();
            } else {
                ActivityCompat.requestPermissions(
                        (Activity) context,
                        new String[]{permissionCode},
                        supportedPermissionRequests.get(permissionCode));
            }

            return false;
        } else {
            return true;
        }
    }

    private static boolean permissionRequestSupported(String permissionCode){
        return supportedPermissionRequests.containsKey(permissionCode);
    }

    private static void initialize(){
        supportedPermissionRequests.put(Manifest.permission.READ_EXTERNAL_STORAGE,
                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        supportedPermissionRequests.put(Manifest.permission.CAMERA, MY_PERMISSIONS_REQUEST_CAMERA);

        permissionRationaleMessages.put(Manifest.permission.READ_EXTERNAL_STORAGE,
                "External storage permission is necessary");
        permissionRationaleMessages.put(Manifest.permission.READ_EXTERNAL_STORAGE,
                "Camera permission is necessary");

        initialized = true;
    }

}
