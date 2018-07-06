package quotes.sau.pro.quotes;


import android.os.Bundle;
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

import quotes.sau.pro.quotes.quotes.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {
    RecyclerView category_recycler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_category, container, false);
        category_recycler=view.findViewById(R.id.category_list);
        setHasOptionsMenu(true);
        category_recycler.setLayoutManager(new GridLayoutManager(getContext(),3));

        String url="http://rajviinfotech.in/quotes/getdata_categories";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response ",response);
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    if (jsonObject.getInt("status")==0)
                    {
                        JSONArray dataAry=jsonObject.getJSONArray("data");
                        CategoryAdapter Adapter=new CategoryAdapter(getContext(),dataAry,getFragmentManager());
                        category_recycler.setAdapter(Adapter);

                    }else {
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
