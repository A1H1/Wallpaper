package in.devco.creativesuperheroes4kartworks.config;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by kingdov on 17/01/2017.
 */

public class admob {

	public static int mCount = 0;
    public static InterstitialAd mInterstitialAd;

	public static void admobBannerCall(Activity activity , final LinearLayout linearLayout){
        AdView adView = new AdView(activity);
        adView.setAdUnitId(Config.admBanner);
        adView.setAdSize(AdSize.BANNER);
        AdRequest.Builder builder = new AdRequest.Builder();
        adView.loadAd(builder.build());
        linearLayout.setVisibility(View.VISIBLE);
        linearLayout.addView(adView);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                linearLayout.setVisibility(View.VISIBLE);
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
