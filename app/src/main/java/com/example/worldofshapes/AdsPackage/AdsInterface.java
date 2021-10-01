package com.example.worldofshapes.AdsPackage;

import android.app.Activity;

import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;

public interface AdsInterface {

    public  void  OnLoadAd(InterstitialAd interstitialAd , Activity activity);
    public  void  OnError(LoadAdError error, Activity activity);
    }

