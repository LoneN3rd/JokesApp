package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.android.showjokes.ShowJokesActivity;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.udacity.gradle.builditbigger.backend.myApi.MyApi;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.btn_tell_joke)
    Button btnTellJoke;

    private MyApi myApi = null;

    private InterstitialAd mInterstitialAd = null;

    private boolean isFreeFlavour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        isFreeFlavour = BuildConfig.FLAVOR.equals("free");

        if (isFreeFlavour) {

            MobileAds.initialize(this,
                    "ca-app-pub-3940256099942544~3347511713");

            mInterstitialAd = new InterstitialAd(this);
            mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void tellJoke(View view) {

        new EndpointAsyncTask(MainActivity.this).execute();

    }

    public class EndpointAsyncTask extends AsyncTask<Void, Void, String>{

        private final AtomicReference<Context> mContext = new AtomicReference<>();

        EndpointAsyncTask(Context context){
            this.mContext.set(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setVisibility(View.VISIBLE);
            btnTellJoke.setVisibility(View.INVISIBLE); // hide button to prevent more clicks

        }

        @Override
        protected String doInBackground(Void... params) {

            if(myApi == null) {
                myApi = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(), null)
                        .setRootUrl("http://10.0.2.2:8080/_ah/api/")
                        .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                            @Override
                            public void initialize(AbstractGoogleClientRequest<?> request) throws IOException {
                                request.setDisableGZipContent(true);
                            }
                        }).build();
            }

            try {
                return myApi.getJoke().execute().getData();
            }catch (IOException e){
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(final String joke) {
            super.onPostExecute(joke);

            if (isFreeFlavour) {

                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }

                mInterstitialAd.setAdListener(new AdListener() {

                    @Override
                    public void onAdClosed() {
                        // Code to be executed when when the interstitial ad is closed.
                        ShowJokesActivity.showJokes(mContext.get(), joke);

                        progressBar.setVisibility(View.GONE);
                        btnTellJoke.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAdLoaded() {
                        // Code to be executed when an ad finishes loading.

                        ShowJokesActivity.showJokes(mContext.get(), joke);

                        progressBar.setVisibility(View.GONE);
                        btnTellJoke.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        // Code to be executed when an ad request fails.
                        ShowJokesActivity.showJokes(mContext.get(), joke);

                        progressBar.setVisibility(View.GONE);
                        btnTellJoke.setVisibility(View.VISIBLE);
                    }
                });

            } else {
                ShowJokesActivity.showJokes(mContext.get(), joke);

                progressBar.setVisibility(View.GONE);
                btnTellJoke.setVisibility(View.VISIBLE);
            }

        }
    }
}
