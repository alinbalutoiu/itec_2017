package itec.routeapp;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

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
