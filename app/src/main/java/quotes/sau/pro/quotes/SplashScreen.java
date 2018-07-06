package quotes.sau.pro.quotes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import quotes.sau.pro.quotes.quotes.R;

public class SplashScreen extends AppCompatActivity {
    private Handler mHandler = new Handler();
    SharedPreferences.Editor editor;
    private Runnable mRunnable;
    private static int SPLASH_TIME_OUT = 3000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
    @Override
    protected void onDestroy ()
    {
        if (mHandler != null && mRunnable != null)
        {
            mHandler.removeCallbacks(mRunnable);
        }
        super.onDestroy();
    }
}
