package com.kingdov.goldwallpapers.config;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by kingdov on 17/01/2017.
 */

public class admob {

	public static int mCount = 0;
    public static InterstitialAd mInterstitialAd;

	public static void admobBannerCall(Activity acitivty , final LinearLayout linerlayout){
		
        AdView adView = new AdView(acitivty);
        adView.setAdUnitId(Config.admBanner);
        adView.setAdSize(AdSize.SMART_BANNER);
        AdRequest.Builder builder = new AdRequest.Builder();
        adView.loadAd(builder.build());
        linerlayout.setVisibility(View.GONE);
        linerlayout.addView(adView);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                linerlayout.setVisibility(View.GONE);
            }
        });

	}

	public static void initialInterstitial(Activity activity){
        mInterstitialAd = new InterstitialAd(activity);
        mInterstitialAd.setAdUnitId(Config.Interstitial);
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
            }
        });
        requestNewInterstitial();
    }

    public static void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);
    }

    public static void showInterstitial(boolean count){
	    if(count){
	        mCount++;
	        if(Config.interstitialFrequence == mCount) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                    mCount=0;
                }else mCount--;
            }
        } else if (mInterstitialAd.isLoaded()) mInterstitialAd.show();
    }
}
