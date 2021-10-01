package com.example.worldofshapes.AdsPackage;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.ResponseInfo;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;


public class AdsHandler {
     Context context;
     AdsInterface adsInterface;
      Activity activity;
    public AdsHandler(Context context, AdsInterface adsInterface, Activity activity) {
        this.context = context;
        this.adsInterface = adsInterface;
        this.activity = activity;
    }
   public void InterstitialAdLoad(Context context) {
        AdManagerAdRequest adRequest = new AdManagerAdRequest.Builder().build();
        InterstitialAd.load(context, "/6499/example/interstitial", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        ResponseInfo responseInfo = interstitialAd.getResponseInfo();
                        Log.d(TAG, responseInfo.toString());
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        super.onAdLoaded(interstitialAd);
                        adsInterface.OnLoadAd(interstitialAd, activity);
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i(TAG, loadAdError.getMessage());
                        super.onAdFailedToLoad(loadAdError);
                        adsInterface.OnError(loadAdError, activity);
                    }
                });

    }
    public boolean checkInternetConenction() {
        // get Connectivity Manager object to check connection
        ConnectivityManager connec
                =(ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);
        // Check for network connections
        if ( connec.getNetworkInfo(0).getState() ==
                android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() ==
                        android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() ==
                        android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {
            //Toast.makeText(this, " Connected ", Toast.LENGTH_LONG).show();

            return true;
        }else if (
                connec.getNetworkInfo(0).getState() ==
                        android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() ==
                                android.net.NetworkInfo.State.DISCONNECTED  ) {
            //Toast.makeText(this, " Not Connected ", Toast.LENGTH_LONG).show();
//            progressDialog = new ProgressDialog(this);
//            progressDialog.setMessage("Not Connected");
//            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            progressDialog.show();
            return false;
        }
        return false;

    }
}
