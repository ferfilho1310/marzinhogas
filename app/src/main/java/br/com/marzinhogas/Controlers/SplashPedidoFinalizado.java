package br.com.marzinhogas.Controlers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import br.com.marzinhogas.R;

public class SplashPedidoFinalizado extends AppCompatActivity {

    ImageButton finalizar;
    InterstitialAd interstitial;
    AdView ad_bloco_1,ad_bloco_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_pedido_finalizado);

        finalizar = findViewById(R.id.btn_finalizar);
        ad_bloco_1 = findViewById(R.id.ad_bloco_1);
        ad_bloco_2 = findViewById(R.id.ad_bloco_2);

        Handler h_ad_blocos = new Handler();

        h_ad_blocos.postDelayed(new Runnable() {
            @Override
            public void run() {
                adsblocosbanner();
            }
        },500);

        Handler h_ads = new Handler();
        h_ads.postDelayed(new Runnable() {
            @Override
            public void run() {
                ads();
            }
        },500);

        Handler hd_splahs = new Handler();
        hd_splahs.postDelayed(new Runnable() {
            @Override
            public void run() {
                finalizar.setVisibility(View.VISIBLE);
            }
        }, 6000);

        finalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i_splash = new Intent(SplashPedidoFinalizado.this, MainActivity.class);
                startActivity(i_splash);
                finish();
            }
        });
    }

    private void ads() {
        MobileAds.initialize(this, "ca-app-pub-2528240545678093~9230634671");
        AdRequest adIRequest = new AdRequest.Builder().build();

        // Prepare the Interstitial Ad Activity
        interstitial = new InterstitialAd(SplashPedidoFinalizado.this);

        // Insert the Ad Unit ID
        interstitial.setAdUnitId("ca-app-pub-2528240545678093/8500264091");

        // Interstitial Ad load Request
        interstitial.loadAd(adIRequest);

        // Prepare an Interstitial Ad Listener
        interstitial.setAdListener(new AdListener() {
            public void onAdLoaded() {
                // Call displayInterstitial() function when the Ad loads
                displayInterstitial();
            }
        });
    }

    public void displayInterstitial() {
        // If Interstitial Ads are loaded then show else show nothing.
        if (interstitial.isLoaded()) {
            interstitial.show();
        }
    }

    private void adsblocosbanner(){
        MobileAds.initialize(this, "ca-app-pub-2528240545678093~9230634671");

        AdRequest adRequest = new AdRequest.Builder()
                .build();
        ad_bloco_1.loadAd(adRequest);
        ad_bloco_2.loadAd(adRequest);
    }
}
