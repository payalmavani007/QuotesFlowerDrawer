package quotes.pro.sau.quotes;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mxn.soul.flowingdrawer_core.ElasticDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;

import quotes.pro.sau.quotes.model.InsertTokenModel;
import quotes.pro.sau.quotes.retrofit.ApiClient;
import quotes.pro.sau.quotes.retrofit.ApiInterface;
import quotes.pro.sau.quotes.util.Config;
import quotes.pro.sau.quotes.util.Constant;
import quotes.pro.sau.quotes.util.NotificationUtils;
import retrofit2.Call;
import retrofit2.Callback;


public class MainActivity extends AppCompatActivity {
    private FrameLayout frameLayout;
    Toolbar toolbar;
    ImageView logout;
    ListView lv, lvdot;
    ImageView dot_logout;
    String refreshedToken;
    ApiInterface apiService;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private String android_id;
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        frameLayout = findViewById(R.id.frame_containt);
        Constant.mDrawer = findViewById(R.id.drawerlayout);
        Constant.mDrawer = findViewById(R.id.drawerlayout);
        Constant.mDrawer.setTouchMode(ElasticDrawer.TOUCH_MODE_BEZEL);
        android_id = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);

        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e(TAG, "onCreate: " + refreshedToken);
        Log.e(TAG, "onCreate: " + android_id);
        tokenInsertApiCall();
        setupToolbar();

        setupMenu();
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                }
            }
        };

     /*   android.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment fragment = new HomeFragment();
        fragmentTransaction.replace(R.id.frame_containt,fragment);
        fragmentTransaction.commit();*/

    }
    private void tokenInsertApiCall() {
        apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<InsertTokenModel> modelCall = apiService.getToken(android_id, refreshedToken);
        modelCall.enqueue(new Callback<InsertTokenModel>() {
            @Override
            public void onResponse(Call<InsertTokenModel> call, retrofit2.Response<InsertTokenModel> response) {
              //  Log.e(TAG, "onResponse: "+response.body().getMsg());
            }

            @Override
            public void onFailure(Call<InsertTokenModel> call, Throwable t) {
                Log.e(TAG, "onFailure: " );
            }
        });
    }

    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", "");


    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    @SuppressLint("ResourceType")
    protected void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        //  dot_logout = findViewById(R.id.dot_logout);
     /*   dot_logout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RtlHardcoded")
            @Override
            public void onClick(View v) {
                final String name[] = {"Logout"};

                final Dialog dialog = new Dialog(getApplicationContext());
                dialog.setContentView(R.layout.listdot);
                lvdot = dialog.findViewById(R.id.lvdot);
                dialog.setCancelable(true);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, name);
                lvdot.setAdapter(adapter);
                WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
                wmlp.width = WindowManager.LayoutParams.WRAP_CONTENT;
                wmlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                wmlp.gravity = Gravity.TOP | Gravity.RIGHT;
                wmlp.x = 10;   //x position
                wmlp.y = 0;
                dialog.show();
                lvdot.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        SharedPreferences preferences = getApplicationContext().getSharedPreferences("status", MODE_PRIVATE);

                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear().apply();
                        Toast.makeText(getApplicationContext(), "clear"+
                                editor, Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(MainActivity.this,UserLoginFragment.class);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });
            }
        });*/
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.mDrawer.toggleMenu();
            }

        });
    }

    private void setupMenu() {
        FragmentManager fm = getSupportFragmentManager();
        MenuListFragment mMenuFragment = (MenuListFragment) fm.findFragmentById(R.id.id_container_menu);
        if (mMenuFragment == null) {
            mMenuFragment = new MenuListFragment();
            fm.beginTransaction().add(R.id.id_container_menu, mMenuFragment).commit();
        }

        Constant.mDrawer = (FlowingDrawer) findViewById(R.id.drawerlayout);
        Constant.mDrawer.setTouchMode(ElasticDrawer.TOUCH_MODE_BEZEL);
        Constant.mDrawer.setOnDrawerStateChangeListener(new ElasticDrawer.OnDrawerStateChangeListener() {
            @Override
            public void onDrawerStateChange(int oldState, int newState) {
                if (newState == ElasticDrawer.STATE_CLOSED) {
                    Log.i("MainActivity", "Drawer STATE_CLOSED");
                }
            }

            @Override
            public void onDrawerSlide(float openRatio, int offsetPixels) {
                Log.i("MainActivity", "openRatio=" + openRatio + " ,offsetPixels=" + offsetPixels);
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (Constant.mDrawer.isMenuVisible()) {
            Constant.mDrawer.closeMenu();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sign_out: {
                // do your sign-out stuff
                break;
            }
            // case blocks for other MenuItems (if any)
        }
        return false;
    }
}
