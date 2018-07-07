package quotes.pro.sau.quotes;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import quotes.pro.sau.quotes.util.Constant;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by mxn on 2016/12/13.
 * MenuListFragment
 */

public class MenuListFragment extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener {

    private ImageView ivMenuUserProfilePhoto;
    TextView txtAuthor, txtHome, txtCategoies, txtLogin;
    ImageView imgCategoies, imgHome, imgAuthor, imgLogin;
    View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_menu, container,
                false);
        RelativeLayout relativeLayout = view.findViewById(R.id.relative);
        txtAuthor = (TextView) ((Activity) getActivity()).findViewById(R.id.txtAuthor);
        txtCategoies = (TextView) ((Activity) getActivity()).findViewById(R.id.txtCategoies);
        txtHome = (TextView) ((Activity) getActivity()).findViewById(R.id.txtHome);
        txtLogin = (TextView) ((Activity) getActivity()).findViewById(R.id.txtLogin);
        imgHome = (ImageView) ((Activity) getActivity()).findViewById(R.id.imgeHome);
        imgAuthor = (ImageView) ((Activity) getActivity()).findViewById(R.id.imgAuthor);
        imgLogin = (ImageView) ((Activity) getActivity()).findViewById(R.id.imgLogin);
        imgCategoies = (ImageView) ((Activity) getActivity()).findViewById(R.id.imgCategoies);
        ivMenuUserProfilePhoto = view.findViewById(R.id.ivMenuUserProfilePhoto);
        NavigationView vNavigation = view.findViewById(R.id.vNavigation);
        vNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int id = menuItem.getItemId();
                Fragment fragment = null;
                if (id == R.id.home) {
                    checkConnection();
                    refreshBotomMenu();
                    txtHome.setTextColor(getResources().getColor(R.color.colorwhite));
                    imgHome.setColorFilter(getResources().getColor(R.color.colorwhite));

                    Constant.mToolbar.setTitle("Home");
                    fragment = new HomeFragment();

                } else if (id == R.id.categories) {
                    checkConnection();
                    refreshBotomMenu();
                    txtCategoies.setTextColor(getResources().getColor(R.color.colorwhite));
                    imgCategoies.setColorFilter(getResources().getColor(R.color.colorwhite));

                    Constant.mToolbar.setTitle("Category");
                    fragment = new CategoryFragment();

                } else if (id == R.id.author) {
                    checkConnection();
                    refreshBotomMenu();
                    txtAuthor.setTextColor(getResources().getColor(R.color.colorwhite));
                    imgAuthor.setColorFilter(getResources().getColor(R.color.colorwhite));

                    Constant.mToolbar.setTitle("Author");
                    fragment = new AuthorFragment();
                } else if (id == R.id.user_login) {
                    checkConnection();
                    refreshBotomMenu();
                    txtLogin.setTextColor(getResources().getColor(R.color.colorwhite));
                    imgLogin.setColorFilter(getResources().getColor(R.color.colorwhite));

                    Constant.mDrawer.closeMenu();
                    Constant.mToolbar.setTitle("Login");
                    final FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    SharedPreferences preferences = getContext().getSharedPreferences("status", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    if (preferences.contains("email") && preferences.contains("password")) {
                        String url = "http://rajviinfotech.in/quotes/user_login?email=" + preferences.getString("email", "") + "&password=" + preferences.getString("password", "");

                        Log.e("login url", url);

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.e("fgsdgb", response);
                                try {
                                    JSONObject resp = new JSONObject(response);
                                    if (resp.getInt("status") == 0) {
                                        JSONArray data = resp.getJSONArray("data");
                                        JSONObject object = (JSONObject) data.get(0);

                                        // hud.dismiss();
                                        SharedPreferences preferences = getContext().getSharedPreferences("status", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = preferences.edit();
                                        preferences.getString("email", "email");
                                        preferences.getString("password", "password");
                                        editor.apply();

                                        fragmentTransaction.replace(R.id.frame_containt, new UploadCategoryFragment()).commit();


                                    } else {


                                        fragmentTransaction.replace(R.id.frame_containt, new UserLoginFragment()).addToBackStack("tag").commit();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });

                        Volley.newRequestQueue(getContext()).add(stringRequest);

                    } else {


                        fragmentTransaction.replace(R.id.frame_containt, new UserLoginFragment()).addToBackStack("tag").commit();
                        return true;
                    }


                    fragment = new UserLoginFragment();
                }
                if (fragment != null) {
                    Constant.mDrawer.closeMenu();
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame_containt, fragment);
                    fragmentTransaction.commit();
                }

                return true;
            }
        });

        return view;
    }

    private void refreshBotomMenu() {

        txtHome.setTextColor(getResources().getColor(R.color.colorBlack));
        txtCategoies.setTextColor(getResources().getColor(R.color.colorBlack));
        txtAuthor.setTextColor(getResources().getColor(R.color.colorBlack));
        txtLogin.setTextColor(getResources().getColor(R.color.colorBlack));
        imgHome.setColorFilter(getResources().getColor(R.color.colorBlack));
        imgCategoies.setColorFilter(getResources().getColor(R.color.colorBlack));
        imgAuthor.setColorFilter(getResources().getColor(R.color.colorBlack));
        imgLogin.setColorFilter(getResources().getColor(R.color.colorBlack));

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
                    .make(((Activity) getActivity()).findViewById(R.id.fab), message, Snackbar.LENGTH_LONG);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(color);
            snackbar.show();
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        Quotes.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }
}
