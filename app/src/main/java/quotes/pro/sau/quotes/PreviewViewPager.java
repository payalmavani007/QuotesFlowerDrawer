package quotes.pro.sau.quotes;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.json.JSONArray;

import quotes.pro.sau.quotes.model.HomePreviewClass;
import quotes.pro.sau.quotes.retrofit.ApiClient;
import quotes.pro.sau.quotes.retrofit.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;


public class PreviewViewPager extends Fragment {
    ImageView download;
    RelativeLayout layout;
    Context context;
    JSONArray array;
    String SelectedId, quotes, image;
    RecyclerView recyclar;

    SlidingImage_Adapter adapter;
    private static final String TAG = "PreviewViewPager";
    private static ViewPager mPager;
    View mView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.preview_viewpager, container, false);
        mPager = (ViewPager) mView.findViewById(R.id.pager);
       /* Bundle extras = getActivity().getIntent().getExtras();
        assert extras != null;
        SelectedId = extras.getString("SelectedId");
        quotes = extras.getString("quotes");*/
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            SelectedId = bundle.getString("SelectedId");
            quotes = bundle.getString("quotes");
        }
        Log.e(TAG, "iddddddddddd: " + SelectedId);
        init();
        return mView;
    }



    //-----------------------viewpager--------------


    private void init() {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<HomePreviewClass> homelist_modelCall = apiService.getDufultQutes(SelectedId);
        homelist_modelCall.enqueue(new Callback<HomePreviewClass>() {
            @Override
            public void onResponse(Call<HomePreviewClass> call, retrofit2.Response<HomePreviewClass> response) {
                mPager.setAdapter(new SlidingImage_Adapter(getActivity(), response.body().getData()));
            }

            @Override
            public void onFailure(Call<HomePreviewClass> call, Throwable t) {

            }
        });

    }
}

