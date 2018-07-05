package quotes.sau.pro.quotes.quotes;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import com.mxn.soul.flowingdrawer_core.ElasticDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;

import quotes.sau.pro.quotes.quotes.util.Constant;

public class MainActivity extends AppCompatActivity {
    private FrameLayout frameLayout;
    Toolbar toolbar;
    ImageView logout;
    ListView lv,lvdot;
    ImageView dot_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        frameLayout =  findViewById(R.id.frame_containt);
        Constant.mDrawer =  findViewById(R.id.drawerlayout);
        Constant.mDrawer =  findViewById(R.id.drawerlayout);
        Constant.mDrawer.setTouchMode(ElasticDrawer.TOUCH_MODE_BEZEL);
        setupToolbar();
        setupMenu();
        
     /*   android.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment fragment = new HomeFragment();
        fragmentTransaction.replace(R.id.frame_containt,fragment);
        fragmentTransaction.commit();*/

    }

    @SuppressLint("ResourceType")
    protected void setupToolbar() {
         toolbar =  findViewById(R.id.toolbar);
        dot_logout = findViewById(R.id.dot_logout);
        dot_logout.setOnClickListener(new View.OnClickListener() {
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
        });
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
    public void onBackPressed () {
        if ( Constant.mDrawer.isMenuVisible()) {
            Constant.mDrawer.closeMenu();
        } else {
            super.onBackPressed();
        }
    }
}
