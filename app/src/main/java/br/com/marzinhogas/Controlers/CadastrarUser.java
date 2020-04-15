package br.com.marzinhogas.Controlers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import br.com.marzinhogas.Helpers.AccessFirebase;
import br.com.marzinhogas.Models.Usuario;
import br.com.marzinhogas.R;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class CadastrarUser extends AppCompatActivity {

    EditText nome, endereco, email_cad, senha_cad, confirma_senha, numero, bairro, complemento;
    Button cadatrar_user;
    RadioGroup rg_sexo;
    RadioButton fem, masc;

    String masculino, feminino;

    Usuario usuario = new Usuario();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_user);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        setTitle("Cadastre-se");

        nome = findViewById(R.id.ed_nome);
        endereco = findViewById(R.id.btn_endereço);
        complemento = findViewById(R.id.ed_complemento);
        email_cad = findViewById(R.id.ed_email);
        senha_cad = findViewById(R.id.ed_senha);
        confirma_senha = findViewById(R.id.ed_confirmar_senha);
        numero = findViewById(R.id.ed_numero);
        bairro = findViewById(R.id.ed_bairro);
        cadatrar_user = findViewById(R.id.btn_cadastrar_user);
        rg_sexo = findViewById(R.id.rg_sexos);
        fem = findViewById(R.id.rd_feminino);
        masc = findViewById(R.id.rd_masculino);

        AccessFirebase.getInstance().persistir_usuer(CadastrarUser.this);

        rg_sexo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                if (fem.isChecked()) {

                    feminino = "Feminino";
                    usuario.setSexo(feminino);

                }

                if (masc.isChecked()) {

                    masculino = "Masculino";
                    usuario.setSexo(masculino);
                }
            }
        });

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {

                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Falha ao obter o token", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        usuario.setToken(token);

                        // Log and toast
                        Log.d("Token", token);
                    }
                });

        cadatrar_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                usuario.setNome(nome.getText().toString());
                usuario.setEndereco(endereco.getText().toString());
                usuario.setEmail(email_cad.getText().toString());
                usuario.setSenha(senha_cad.getText().toString());
                usuario.setConfirmarsenha(confirma_senha.getText().toString());
                usuario.setBairro(bairro.getText().toString());
                usuario.setNumero(numero.getText().toString());
                usuario.setComplemento(complemento.getText().toString());

                AccessFirebase.getInstance().cadastrar_user(usuario, CadastrarUser.this);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i_cad_user = new Intent(CadastrarUser.this, EntrarUser.class);
                startActivity(i_cad_user);
                finish();
                break;
            default:
                break;
        }

        return true;
    }

}

