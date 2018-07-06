package quotes.pro.sau.quotes;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;




class CatListAdapter extends RecyclerView.Adapter<CatListAdapter.ViewHolder> {
    private static final String TAG = "CatListAdapter";
    Context context;
    JSONArray array;
    String id;

    public CatListAdapter(Context context, JSONArray array, String id) {
        this.context = context;
        this.array = array;
        this.id = id;
    }

    @Override
    public CatListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list, parent, false);
        return new CatListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CatListAdapter.ViewHolder holder, final int position) {

        try {
            final JSONObject o = array.getJSONObject(position);
            Picasso.get().load("http://rajviinfotech.in/quotes/public/uploads/" + o.getString("quotes_image")).into(holder.imageView);
            holder.textView.setText(o.getString("quotes_name"));
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                /*    Intent intent = new Intent(context,Preview.class);

                    try {
                        Log.e(TAG, "onClick: "+"http://192.168.1.200/quotesmanagement/public/uploads/"+o.getString("quotes_image"));
                        intent.putExtra("quotes_image", "http://192.168.1.200/quotesmanagement/public/uploads/"+o.getString("quotes_image"));
                        intent.putExtra("quotes",o.getString("quotes_name"));
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    context.startActivity(intent);*/
                    Intent intent = new Intent(context, SwipeDeckActivitycopy.class);
                    intent.putExtra("id", id);
                    Log.e(TAG, "hiiiiiiiiiii: "+id );
                    try {
                        intent.putExtra("catagoryId", o.getString("id"));
                        Log.e(TAG, "hiiiiiiiiiii: "+o.getString("id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    context.startActivity(intent);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return array.length();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text);
            imageView = itemView.findViewById(R.id.bcgrnd_img);
        }
    }
}
