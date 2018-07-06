package quotes.sau.pro.quotes;

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

import quotes.sau.pro.quotes.quotes.R;

class RecyclarAdapter extends RecyclerView.Adapter<RecyclarAdapter.ViewHolder> {

    Context context;
    JSONArray array;
    String id;


    private static final String TAG = "RecyclarAdapter";
    public RecyclarAdapter(Context context, JSONArray array, String id)
    {
        this.context = context;
        this.array = array;
        this.id = id;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        // final Homelist_model grid_model = grid_models.get(position);
        try {
            final JSONObject o = array.getJSONObject(position);
            Picasso.get().load("http://rajviinfotech.in/quotes/public/uploads/"+o.getString("quotes_image"))
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(holder.imageView);
            holder.textView.setText(o.getString("quotes"));
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PreviewViewPager.class);


                    try {
                        intent.putExtra("SelectedId",o.getString("id"));
                        intent.putExtra("quotes",o.getString("quotes"));

                        Log.e(TAG, "selctedid===: " +id);
                        context.startActivity(intent);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


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

    class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imageView;
        TextView textView;

        public ViewHolder(View itemView)
        {
            super(itemView);
            textView = itemView.findViewById(R.id.text);
            imageView = itemView.findViewById(R.id.bcgrnd_img);
        }
    }
}

