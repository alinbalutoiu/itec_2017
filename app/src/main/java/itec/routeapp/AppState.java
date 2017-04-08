package itec.routeapp;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import itec.routeapp.model.Route;

/**
 * Created by Mihaela Ilin on 12/29/2016.
 */
public class AppState extends Application{

    private static AppState singletonInstance;

    private String userId;

    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
//    private FirebaseStorage storage;
//    private StorageReference storageRef;

    private static final String subfolderName = "routes";

    public static synchronized AppState get() {
        if(singletonInstance == null){
            singletonInstance = new AppState();
            // Firebase storage setup
//            singletonInstance.setStorage(FirebaseStorage.getInstance());
//            singletonInstance.setStorageRef(FirebaseStorage.getInstance()
//                    .getReferenceFromUrl("gs://closettedbtest.appspot.com/closette"));
        }
        return singletonInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // offline persistence of data
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    public void saveRouteToFile(Context context, Route route){
        String fileName = route.getTimestamp();

        // todo check permissions
        File folder = new File(context.getFilesDir() + File.separator + subfolderName);
        if(!folder.exists()){
            folder.mkdir();
        }
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(route);
            os.close();
            fos.close();
        } catch (IOException e) {
            Toast.makeText(context, "Cannot access local data.", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(
                context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public boolean hasLocalStorage(Context context) {
        return context.getFilesDir().listFiles().length > 0;
    }

    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    public void setDatabaseReference(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public FirebaseAuth getAuth() {
        return auth;
    }

    public void setAuth(FirebaseAuth auth) {
        this.auth = auth;
    }
}
