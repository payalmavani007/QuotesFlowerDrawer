
package quotes.sau.pro.quotes;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import quotes.sau.pro.quotes.quotes.R;

import quotes.sau.pro.quotes.quotes.util.Constant;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by mxn on 2016/12/13.
 * MenuListFragment
 */

public class MenuListFragment extends Fragment {

    private ImageView ivMenuUserProfilePhoto;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container,
                false);
        RelativeLayout relativeLayout = view.findViewById(R.id.relative);

        ivMenuUserProfilePhoto = view.findViewById(R.id.ivMenuUserProfilePhoto);
        NavigationView vNavigation = view.findViewById(R.id.vNavigation);
        vNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int id = menuItem.getItemId();
                Fragment fragment = null;
                if (id == R.id.home) {
                    fragment = new HomeFragment();
                } else if (id == R.id.categories) {
                    fragment = new CategoryFragment();
                } else if (id == R.id.author) {
                    fragment = new AuthorFragment();
                } else if (id == R.id.user_login) {
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


}
