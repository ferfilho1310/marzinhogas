package br.com.marzinhogas.Controlers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;

import br.com.marzinhogas.Helpers.AccessFirebase;
import br.com.marzinhogas.R;

public class EntrarUser extends AppCompatActivity {

    EditText email, senha;
    TextView cadastre, esqueceu_senha;
    Button entrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrar_user);

        FirebaseApp.initializeApp(EntrarUser.this);

        email = findViewById(R.id.ed_email_entrar);
        senha = findViewById(R.id.ed_senha_entrar);
        entrar = findViewById(R.id.btn_entrar);
        cadastre = findViewById(R.id.txt_cadastre_se);
        esqueceu_senha = findViewById(R.id.txt_esqueceu_senha);

        AccessFirebase.getInstance().persistir_usuer(EntrarUser.this);

        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AccessFirebase.getInstance().entrar_firebase(email.getText().toString(), senha.getText().toString(), EntrarUser.this);
            }
        });

        cadastre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i_cadastrar = new Intent(EntrarUser.this, CadastrarUser.class);
                i_cadastrar.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i_cadastrar);
            }
        });

        esqueceu_senha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i_esquece_senha = new Intent(EntrarUser.this, RecuperarSenha.class);
                startActivity(i_esquece_senha);
            }
        });
    }
}
