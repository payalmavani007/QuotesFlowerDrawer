package quotes.pro.sau.quotes;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import quotes.pro.sau.quotes.util.Constant;


/**
 * A simple {@link Fragment} subclass.
 */
public class AuthorFragment extends Fragment {
    RecyclerView category_recycler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_author, container, false);
        category_recycler=view.findViewById(R.id.author_list);
        setHasOptionsMenu(true);
        Constant.mToolbar.setTitle("Author");
        category_recycler.setLayoutManager(new GridLayoutManager(getContext(),3));
        String url="http://rajviinfotech.in/quotes/getdata_author";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response ",response);
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    if (jsonObject.getInt("status")==0)
                    {
                        JSONArray dataAry=jsonObject.getJSONArray("data");
                        AuthorAdapter Adapter=new AuthorAdapter(getContext(),dataAry,getFragmentManager());
                        category_recycler.setAdapter(Adapter);
                    }
                    else
                    {
                        Toast.makeText(getContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
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
        return  view;
    }

}
