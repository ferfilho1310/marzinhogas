package br.com.marzinhogas.Helpers;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class AccessResources implements IAccessResources {

    public static AccessResources accessResources;
    RewardedVideoAdListener rewardedVideoAdListener;

    private AccessResources() {
    }

    public static synchronized AccessResources getInstance() {
        if (accessResources == null) {
            accessResources = new AccessResources();
        }
        return accessResources;
    }

    @Override
    public void Ads(final Activity activity) {

        final RewardedVideoAd mRewardedVideoAd;

        MobileAds.initialize(activity, "ca-app-pub-3940256099942544~3347511713");


        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(activity);
        mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917", new AdRequest.Builder().build());
        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        }
        mRewardedVideoAd.setRewardedVideoAdListener(rewardedVideoAdListener);

        rewardedVideoAdListener = new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {
                Toast.makeText(activity, "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onRewardedVideoAdOpened() {
                Toast.makeText(activity, "onRewardedVideoAdOpened", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoStarted() {
                Toast.makeText(activity, "onRewardedVideoStarted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdClosed() {
                Toast.makeText(activity, "onRewardedVideoAdClosed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewarded(RewardItem rewardItem) {
                String msg = "onRewarded! currency: " + rewardItem.getType() + "  amount: " + rewardItem.getAmount();
                Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
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
        };
    }

    @Override
    public String criptografiadesenha(String user,String senha) {
        String sign = user + senha;

        try {
            java.security.MessageDigest md =
                    java.security.MessageDigest.getInstance("MD5");
            md.update(sign.getBytes());
            byte[] hash = md.digest();
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < hash.length; i++) {
                if ((0xff & hash[i]) < 0x10)
                    hexString.append(
                            "0" + Integer.toHexString((0xFF & hash[i])));
                else
                    hexString.append(Integer.toHexString(0xFF & hash[i]));
            }
            sign = hexString.toString();
        }
        catch (Exception nsae) {
            nsae.printStackTrace();
        }
        return sign;
    }
}
