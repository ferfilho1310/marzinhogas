package br.com.marzinhogas.Helpers;

import android.app.Activity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

public class AccessResources implements IAccessResources {

    public static AccessResources accessResources;

    private AccessResources() {
    }

    public static synchronized AccessResources getInstance() {

        if (accessResources == null) {

            accessResources = new AccessResources();
        }
        return accessResources;
    }


    @Override
    public void Ads(Activity activity) {

        final RewardedVideoAd mRewardedVideoAd;

        MobileAds.initialize(activity, "ca-app-pub-3940256099942544~3347511713");
        // Use an activity context to get the rewarded video instance.
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(activity);

        if(mRewardedVideoAd.isLoaded()){
            mRewardedVideoAd.show();
        }
        mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",
                new AdRequest.Builder().build());

        mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {

            }

            @Override
            public void onRewardedVideoAdOpened() {

            }

            @Override
            public void onRewardedVideoStarted() {

            }

            @Override
            public void onRewardedVideoAdClosed() {

            }

            @Override
            public void onRewarded(RewardItem rewardItem) {

            }

            @Override
            public void onRewardedVideoAdLeftApplication() {

            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {

            }

            @Override
            public void onRewardedVideoCompleted() {

            }
        });
    }
}
