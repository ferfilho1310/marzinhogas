package br.com.marzinhogas.Controlers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import br.com.marzinhogas.R;

public class SplashPedidoFinalizado extends AppCompatActivity {

    FloatingActionButton finalizar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_pedido_finalizado);

        finalizar = findViewById(R.id.btn_finalizar);

        Handler hd_splahs = new Handler();
        hd_splahs.postDelayed(new Runnable() {
            @Override
            public void run() {
                finalizar.setVisibility(View.VISIBLE);

            }
        }, 5000);

        finalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i_splash = new Intent(SplashPedidoFinalizado.this, MainActivity.class);
                startActivity(i_splash);
                i_splash.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();

            }
        });

    }
}
