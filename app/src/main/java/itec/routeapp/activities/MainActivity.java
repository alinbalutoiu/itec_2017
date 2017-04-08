package itec.routeapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import itec.routeapp.AppState;
import itec.routeapp.R;

public class MainActivity extends AppCompatActivity {

    // Firebase authentication
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private DatabaseReference databaseReference;

    private TextView email;
    private GridView itemGrid;

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

//        email = (TextView)findViewById(R.id.welcome);

        mAuth = FirebaseAuth.getInstance();
        AppState.get().setAuth(mAuth);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    if(!user.getUid().equals(latestUserId)){
                        latestUserId = user.getUid();
//                        email.setText("Welcome, " + user.getEmail() + " !");
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

        /*itemGrid = (GridView)findViewById(R.id.itemsGrid);

        itemGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AppState.get().setCurrentClothingItem(clothingItems.get(i));
                startActivity(new Intent(getApplicationContext(), ItemDetailsActivity.class));
            }
        });*/
    }

    private void attachDBListener(String uid) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        // todo must do for offline persistence
        //database.setPersistenceEnabled(true);
        databaseReference = database.getReference();
        AppState.get().setDatabaseReference(databaseReference);


        //todo
        /*if (!AppState.isNetworkAvailable(this)) {
            // has local storage already
            if (AppState.get().hasLocalStorage(this)) {
                List<Payment> newPayments = AppState.get().loadFromLocalBackup(this, currentMonth);
                payments.clear();
                if(newPayments != null){
                    payments.addAll(newPayments);
                }
                tStatus.setText("Found " + payments.size() + " payments for " + Month.intToMonthName(currentMonth) + ".");
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "This app needs an internet connection!", Toast.LENGTH_SHORT).show();
                return;
            }
        }*/

        /*itemGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), "Clicked on " + clothingItems.get(i).itemId,
                        Toast.LENGTH_SHORT).show();
//                AppState.get().setCurrentPayment(payments.get(i));
//                startActivity(new Intent(getApplicationContext(), AddPaymentActivity.class));
            }
        });*/
    }

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE){
                onSelectFromGalleryResult(data);
            }
            else if (requestCode == REQUEST_CAMERA){
                onCaptureImageResult(data);
            }
        }
    }*/

    /*private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            Uri photoUri = data.getData();
            if (photoUri != null) {
                bm = ImageUtils.loadAndScaleBitmapFromUri(getApplicationContext(), photoUri);
            }
        }
        AppState.get().setNewItemImage(bm);
        AppState.get().setCurrentClothingItem(null);
        startActivity(new Intent(getApplicationContext(), EditItemActivity.class));
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap image = (Bitmap) data.getExtras().get("data");
        Bitmap resizedImage = ImageUtils.getResizedBitmap(image);
        image.recycle(); //todo try-catch?
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        resizedImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        AppState.get().setNewItemImage(resizedImage);
        AppState.get().setCurrentClothingItem(null);
        startActivity(new Intent(getApplicationContext(), EditItemActivity.class));
    }*/

    /*private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image*//*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }*/

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

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav_menu, menu);
        menu.removeItem(R.id.action_view_items);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.action_view_items:
                // do nothing, on this page
//                return true;
            case R.id.action_view_outfits:
                startActivity(new Intent(this, OutfitListActivity.class));
                return true;
            case R.id.action_scheduled_outfits:
                startActivity(new Intent(this, CalendarActivity.class));
                return true;
            case R.id.action_sign_out:
                AuthUtils.signOut();
                email.setText(null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/

    //todo
    public void clicked(View v){
        switch (v.getId()){
            case R.id.all_routes_image:
                startActivity(new Intent(this, RouteListActivity.class));
                break;
            case R.id.new_route_image:
                break;
            case R.id.stats_image:
                break;
            case R.id.achievements_image:
                break;
        }
    }
}
