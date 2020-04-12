package br.com.marzinhogas.Controlers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import br.com.marzinhogas.R;

public class SplashScreenEntrar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen_entrar);

        ImageView im = findViewById(R.id.imageView2);

        Handler h_splash = new Handler();

        h_splash.postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent i_splash = new Intent(SplashScreenEntrar.this, EntrarUser.class);
                startActivity(i_splash);
                finish();

            }
        }, 3000);

    }
}
