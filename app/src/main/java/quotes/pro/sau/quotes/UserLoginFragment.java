package quotes.pro.sau.quotes;


import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import quotes.pro.sau.quotes.util.Constant;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserLoginFragment extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener {
    EditText edt_email;
    Button btn_login, txt_reghere;
    TextInputEditText edt_password;
    FragmentManager fragmentManager;
    FragmentTransaction ft;
    FragmentManager fm;
    boolean isConnected;
    private static final String TAG = "UserLoginFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_login, container, false);
        edt_email = view.findViewById(R.id.email);
        fm = getActivity().getSupportFragmentManager();
        setHasOptionsMenu(true);
        edt_password = view.findViewById(R.id.password);
        txt_reghere = view.findViewById(R.id.register_here);
        //  txt_reghere.setPaintFlags(txt_reghere.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txt_reghere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ft = getFragmentManager().beginTransaction();
                RegisterFragment registerFragment = new RegisterFragment();
                ft.replace(R.id.frame_containt, registerFragment).addToBackStack("tag").commit();
            }
        });
        btn_login = view.findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkConnection();
                if (!isConnected) {

                } else {
                    if (edt_email.getText().toString().equals("")) {
                        //edt_email.setError("Enter Email.");
                        Toast.makeText(getContext(), "Enter Email Address.", Toast.LENGTH_SHORT).show();
                    } else if (edt_password.getText().toString().equals("")) {
                        Toast.makeText(getContext(), "Enter Password", Toast.LENGTH_SHORT).show();

                    } else if (!Patterns.EMAIL_ADDRESS.matcher(edt_email.getText().toString()).matches()) {
                        //edt_email.setError("Enter Valid E-mail.");
                        Toast.makeText(getContext(), "Enter Valid E-mail.", Toast.LENGTH_SHORT).show();
                    } else {
                        String url = "http://rajviinfotech.in/quotes/user_login?" + "email=" + edt_email.getText().toString() +
                                "&password=" + edt_password.getText().toString();
                        final KProgressHUD hud = KProgressHUD.create(getContext())
                                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                                .setCancellable(false)
                                .setAnimationSpeed(2)
                                .setDimAmount(0.5f)
                                .show();
                        Log.e("login url", url);
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject resp = new JSONObject(response);
                                    if (resp.getInt("status") == 0) {
                                        JSONArray data = resp.getJSONArray("data");
                                        JSONObject object = (JSONObject) data.get(0);
                                        SharedPreferences preferences = getContext().getSharedPreferences("status", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = preferences.edit();
                                        Constant.mLoginId = object.getString("id");
                                        editor.putString("id", object.getString("id"));
                                        editor.putString("firstname", object.getString("firstname"));
                                        editor.putString("lastname", object.getString("lastname"));
                                        editor.putString("email", edt_email.getText().toString());
                                        editor.putString("password", edt_password.getText().toString());
                                        editor.putString("contact", object.getString("contact")).apply();

                                        hud.dismiss();
                                        FragmentTransaction ft = getFragmentManager().beginTransaction();

                                        UploadCategoryFragment uploadCategoryFragment = new UploadCategoryFragment();
                                        ft.replace(R.id.frame_containt, uploadCategoryFragment)
                                                .commit();
                                        Log.e(TAG, "Login_UserId: " + object.getString("id"));

                                    } else {

                                        hud.dismiss();
                                        Toast.makeText(getContext(), "Invalid login", Toast.LENGTH_SHORT).show();
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

                    }
                }

            }
        });

        return view;

    }

    private void checkConnection() {
        isConnected = ConnectivityReceiver.isConnected();
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Log In");

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }
}
