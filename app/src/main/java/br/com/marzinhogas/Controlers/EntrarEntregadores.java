package br.com.marzinhogas.Controlers;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import br.com.marzinhogas.Helpers.AccessFirebase;
import br.com.marzinhogas.R;

public class EntrarEntregadores extends AppCompatActivity {

    EditText email_entregador, senha_entregador;
    TextView ser_entregador;
    Button entrar_entregador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrar_entregadores);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        email_entregador = findViewById(R.id.ed_email_entrar_entregador);
        senha_entregador = findViewById(R.id.ed_senha_entrar_entregador);
        ser_entregador = findViewById(R.id.txt_ser_entregador);
        entrar_entregador = findViewById(R.id.btn_entrar_entregador);

        setTitle("");

        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        entrar_entregador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AccessFirebase().entrar_firebase_entregador(email_entregador.getText().toString(),
                        senha_entregador.getText().toString(), EntrarEntregadores.this);

            }
        });

        ser_entregador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i_ser_entregador = new Intent(EntrarEntregadores.this, CadastroEntregadores.class);
                startActivity(i_ser_entregador);

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i_home = new Intent(EntrarEntregadores.this, EntrarUser.class);
                startActivity(i_home);
                break;
            default:
                break;

        }

        return true;
    }
}
