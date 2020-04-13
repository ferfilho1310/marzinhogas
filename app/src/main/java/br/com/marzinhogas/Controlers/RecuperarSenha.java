package br.com.marzinhogas.Controlers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import br.com.marzinhogas.Helpers.AccessFirebase;
import br.com.marzinhogas.R;

public class RecuperarSenha extends AppCompatActivity {

    Button alterar_senha;
    EditText email_alterar_senha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_senha);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        setTitle("Altere sua senha");

        email_alterar_senha = findViewById(R.id.ed_recuperar_senha);
        alterar_senha = findViewById(R.id.btn_alterar_senha);

        alterar_senha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AccessFirebase.getInstance().reset_senha(email_alterar_senha.getText().toString(), RecuperarSenha.this);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i_cad_user = new Intent(RecuperarSenha.this, EntrarUser.class);
                startActivity(i_cad_user);
                finish();
                break;
            default:
                break;
        }

        return true;
    }
}
