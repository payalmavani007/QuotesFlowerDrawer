package quotes.pro.sau.quotes;


import android.annotation.SuppressLint;
import android.app.Dialog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
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


public class MainActivity extends AppCompatActivity implements View.OnClickListener, ConnectivityReceiver.ConnectivityReceiverListener {
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
    ImageView imgHome, imgCategoies, imgAuthor, imgLogin;
    TextView txtHome, txtCategoies, txtAuthor, txtLogin;
    LinearLayout clickHome, clickCategoies, clickAuthor, clickLogin;
    FragmentTransaction tx;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialization();

    }

    private void initialization() {
        checkConnection();
        SharedPreferences pref = getApplicationContext().getSharedPreferences("status", MODE_PRIVATE);
        Constant.mLoginId = pref.getString("id", "");
        frameLayout = findViewById(R.id.frame_containt);
        Constant.mDrawer = findViewById(R.id.drawerlayout);
        Constant.mDrawer = findViewById(R.id.drawerlayout);
        Constant.mToolbar = (Toolbar) findViewById(R.id.toolbar);
        Constant.mDrawer.setTouchMode(ElasticDrawer.TOUCH_MODE_BEZEL);
        android_id = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.frame_containt, new HomeFragment());
        tx.commit();
        imgHome = (ImageView) findViewById(R.id.imgeHome);
        imgCategoies = (ImageView) findViewById(R.id.imgCategoies);
        imgAuthor = (ImageView) findViewById(R.id.imgAuthor);
        imgLogin = (ImageView) findViewById(R.id.imgLogin);
        txtHome = (TextView) findViewById(R.id.txtHome);
        imgAuthor.setColorFilter(getResources().getColor(R.color.colorBlack));
        Constant.mToolbar.setTitle("Home");

        txtCategoies = (TextView) findViewById(R.id.txtCategoies);
        txtAuthor = (TextView) findViewById(R.id.txtAuthor);
        txtLogin = (TextView) findViewById(R.id.txtLogin);
        clickHome = (LinearLayout) findViewById(R.id.clickHome);
        clickCategoies = (LinearLayout) findViewById(R.id.clickAuthor);
        clickAuthor = (LinearLayout) findViewById(R.id.clickCategories);
        clickLogin = (LinearLayout) findViewById(R.id.clickLogin);
        imgHome.setClickable(false);
        imgLogin.setClickable(false);
        imgCategoies.setClickable(false);
        imgAuthor.setClickable(false);
        clickCategoies.setOnClickListener(this);
        clickAuthor.setOnClickListener(this);
        clickLogin.setOnClickListener(this);
        clickHome.setOnClickListener(this);
        txtHome.setTextColor(getResources().getColor(R.color.colorwhite));
        imgHome.setColorFilter(getResources().getColor(R.color.colorwhite));

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
    }

    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    private void showSnack(boolean isConnected) {
        String message = null;
        int color;
        if (!isConnected) {

            message = "Sorry! Not connected to internet";
            color = Color.RED;
            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.fab), message, Snackbar.LENGTH_LONG);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(color);
            snackbar.show();
        }


    }

    @Override
    public void onClick(View view) {
        Fragment fragment = null;
        switch (view.getId()) {
            case R.id.clickHome:
                checkConnection();
                Constant.mToolbar.setTitle("Home");
                homeClickChangeColor();
                fragment = new HomeFragment();

                break;
            case R.id.clickCategories:
                checkConnection();
                Constant.mToolbar.setTitle("Category");
                CategoriesClickchangeColor();
                fragment = new CategoryFragment();

                break;
            case R.id.clickAuthor:
                checkConnection();
                Constant.mToolbar.setTitle("Author");
                authorClickChangeColor();
                fragment = new AuthorFragment();
                break;
            case R.id.clickLogin:
                checkConnection();
                Constant.mToolbar.setTitle("Login");
                loginClickChangeColor();
                if (Constant.mLoginId.equals("")) {
                    fragment = new UserLoginFragment();
                } else {
                    fragment = new UploadCategoryFragment();
                }
                break;
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_containt, fragment);
            fragmentTransaction.commit();
        }
    }

    private void loginClickChangeColor() {
        imgHome.setColorFilter(getResources().getColor(R.color.colorBlack));
        imgCategoies.setColorFilter(getResources().getColor(R.color.colorBlack));
        imgAuthor.setColorFilter(getResources().getColor(R.color.colorBlack));
        imgLogin.setColorFilter(getResources().getColor(R.color.colorwhite));

        txtHome.setTextColor(getResources().getColor(R.color.colorBlack));
        txtCategoies.setTextColor(getResources().getColor(R.color.colorBlack));
        txtAuthor.setTextColor(getResources().getColor(R.color.colorBlack));
        txtLogin.setTextColor(getResources().getColor(R.color.colorwhite));
    }

    private void authorClickChangeColor() {
        imgHome.setColorFilter(getResources().getColor(R.color.colorBlack));
        imgCategoies.setColorFilter(getResources().getColor(R.color.colorBlack));
        imgAuthor.setColorFilter(getResources().getColor(R.color.colorwhite));
        imgLogin.setColorFilter(getResources().getColor(R.color.colorBlack));

        txtHome.setTextColor(getResources().getColor(R.color.colorBlack));
        txtCategoies.setTextColor(getResources().getColor(R.color.colorBlack));
        txtAuthor.setTextColor(getResources().getColor(R.color.colorwhite));
        txtLogin.setTextColor(getResources().getColor(R.color.colorBlack));
    }

    private void CategoriesClickchangeColor() {
        imgHome.setColorFilter(getResources().getColor(R.color.colorBlack));
        imgCategoies.setColorFilter(getResources().getColor(R.color.colorwhite));
        imgAuthor.setColorFilter(getResources().getColor(R.color.colorBlack));
        imgLogin.setColorFilter(getResources().getColor(R.color.colorBlack));

        txtHome.setTextColor(getResources().getColor(R.color.colorBlack));
        txtCategoies.setTextColor(getResources().getColor(R.color.colorwhite));
        txtAuthor.setTextColor(getResources().getColor(R.color.colorBlack));
        txtLogin.setTextColor(getResources().getColor(R.color.colorBlack));
    }

    private void homeClickChangeColor() {
        imgHome.setColorFilter(getResources().getColor(R.color.colorwhite));
        imgCategoies.setColorFilter(getResources().getColor(R.color.colorBlack));
        imgAuthor.setColorFilter(getResources().getColor(R.color.colorBlack));
        imgLogin.setColorFilter(getResources().getColor(R.color.colorBlack));

        txtHome.setTextColor(getResources().getColor(R.color.colorwhite));
        txtCategoies.setTextColor(getResources().getColor(R.color.colorBlack));
        txtAuthor.setTextColor(getResources().getColor(R.color.colorBlack));
        txtLogin.setTextColor(getResources().getColor(R.color.colorBlack));

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
                Log.e(TAG, "onFailure: ");
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
        Quotes.getInstance().setConnectivityListener(this);
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
    public boolean onCreateOptionsMenu(Menu menu) {

        Log.e(TAG, "onCreateOptionsMenu: " + id);
        if (!Constant.mLoginId.equals("")) {
            getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sign_out: {
                checkConnection();
                SharedPreferences pref = getApplicationContext().getSharedPreferences("status", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.apply();
                finish();
                startActivity(getIntent());
                break;
            }
        }
        return true;
    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }
}
