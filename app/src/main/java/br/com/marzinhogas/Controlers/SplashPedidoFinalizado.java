package br.com.marzinhogas.Controlers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import br.com.marzinhogas.R;

public class SplashPedidoFinalizado extends AppCompatActivity {

    FloatingActionButton finalizar;
    InterstitialAd interstitial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_pedido_finalizado);

        finalizar = findViewById(R.id.btn_finalizar);

        ads();

        Handler hd_splahs = new Handler();
        hd_splahs.postDelayed(new Runnable() {
            @Override
            public void run() {
                finalizar.setVisibility(View.VISIBLE);

            }
        }, 3000);

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
}
