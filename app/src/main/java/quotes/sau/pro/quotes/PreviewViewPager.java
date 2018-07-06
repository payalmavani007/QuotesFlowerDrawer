package quotes.sau.pro.quotes;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.json.JSONArray;

import quotes.sau.pro.quotes.model.HomePreviewClass;
import quotes.sau.pro.quotes.quotes.R;

import quotes.sau.pro.quotes.retrofit.ApiClient;
import quotes.sau.pro.quotes.retrofit.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;

public class PreviewViewPager extends AppCompatActivity {
    ImageView download;
    RelativeLayout layout;
    Context context;
    JSONArray array;
    String SelectedId,quotes,image;
    RecyclerView recyclar;

    SlidingImage_Adapter adapter;
    private static final String TAG = "PreviewViewPager";
    private static ViewPager mPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preview_viewpager);
        mPager = (ViewPager) findViewById(R.id.pager);
        Bundle extras = getIntent().getExtras();
        assert extras != null;
        SelectedId = extras.getString("SelectedId");
        quotes = extras.getString("quotes");

        Log.e(TAG, "iddddddddddd: "+ SelectedId);
        init();
    }

    //-----------------------viewpager--------------


    private void init() {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<HomePreviewClass> homelist_modelCall = apiService.getDufultQutes(SelectedId);
        homelist_modelCall.enqueue(new Callback<HomePreviewClass>() {
            @Override
            public void onResponse(Call<HomePreviewClass> call, retrofit2.Response<HomePreviewClass> response)
            {
                mPager.setAdapter(new SlidingImage_Adapter(PreviewViewPager.this, response.body().getData()));
            }

            @Override
            public void onFailure(Call<HomePreviewClass> call, Throwable t) {

            }
        });

    }
}

