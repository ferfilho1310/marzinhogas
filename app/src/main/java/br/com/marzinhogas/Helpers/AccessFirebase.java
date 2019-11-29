package br.com.marzinhogas.Helpers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import br.com.marzinhogas.Controlers.EntrarUser;
import br.com.marzinhogas.Controlers.MainActivity;
import br.com.marzinhogas.Models.Notification;

public class AccessFirebase {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    CollectionReference db_users = FirebaseFirestore.getInstance().collection("Users");
    CollectionReference db_pedido = FirebaseFirestore.getInstance().collection("Pedidos");
    CollectionReference db_notificacoes = FirebaseFirestore.getInstance().collection("notifications");
    CollectionReference db_entregadores= FirebaseFirestore.getInstance().collection("Entregadores");

    ProgressDialog progressDialog;

    public void noitificacoes(String user_token, Notification notification){

        db_notificacoes.document(user_token).set(notification);


    }

    public void pedidos(String id_user, String nome, String endereco, String data,
                        String produto, int quantidade_gas, int quantidade_agua) {

        Map<String, Object> map = new HashMap<>();

        map.put("id_user", id_user);
        map.put("nome", nome);
        map.put("endereco", endereco);
        map.put("data", data);
        map.put("produto", produto);
        map.put("quantidade_gas", quantidade_gas);
        map.put("quantidade_agua", quantidade_agua);

        db_pedido.add(map);

    }


    public void cadastrar_user(final String nome, final String endereco, final String email, final String senha,
                               final String senhaconfir, final String sexo, final String token, final Activity activity) {

        FirebaseApp.initializeApp(activity);

        if (TextUtils.isEmpty(nome)) {
            Toast.makeText(activity, "Digite seu nome", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(activity, "Informe um e-mail.", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(senha)) {
            Toast.makeText(activity, "Informe uma senha.", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(senhaconfir)) {

            Toast.makeText(activity, "Confirme a senha", Toast.LENGTH_LONG).show();
            return;
        }

        if (senha.equals(senhaconfir)) {

            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("Cadastrando...");
            progressDialog.show();

            firebaseAuth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {

                        Map<String, String> map = new HashMap<>();

                        map.put("id_user", firebaseAuth.getUid());
                        map.put("nome", nome);
                        map.put("endereco", endereco);
                        map.put("email", email);
                        map.put("senha", senha);
                        map.put("confirmarsenha", senhaconfir);
                        map.put("sexo", sexo);
                        map.put("token", token);

                        Intent intent = new Intent(activity, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        activity.startActivity(intent);

                        db_users.document(firebaseAuth.getUid()).set(map);

                        Toast.makeText(activity, "Usuário cadastrado com sucesso.", Toast.LENGTH_LONG).show();

                    } else if (!task.isSuccessful()) {
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthWeakPasswordException e) {

                            Toast.makeText(activity, "Senha inferior a 6 caracteres", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        } catch (FirebaseAuthInvalidCredentialsException e) {

                            Toast.makeText(activity, "E-mail inválido", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        } catch (FirebaseAuthUserCollisionException e) {

                            Toast.makeText(activity, "Usuário já cadastrado", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        } catch (Exception e) {

                            Toast.makeText(activity, "Ops!Erro a cadastrar o usuário", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                }

            });
        } else {

            Toast.makeText(activity, "As senhas estão diferentes.", Toast.LENGTH_LONG).show();
            return;

        }
    }

    public void cadastrar_entregador(final String nome, final String email, final String senha,
                                     final String senhaconfir, final String token, final String sexo, final boolean online,
                                     final Activity activity) {

        FirebaseApp.initializeApp(activity);

        if (TextUtils.isEmpty(nome)) {
            Toast.makeText(activity, "Digite seu nome", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(activity, "Informe um e-mail.", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(senha)) {
            Toast.makeText(activity, "Informe uma senha.", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(senhaconfir)) {

            Toast.makeText(activity, "Confirme a senha", Toast.LENGTH_LONG).show();
            return;
        }

        if (senha.equals(senhaconfir)) {

            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("Cadastrando...");
            progressDialog.show();

            firebaseAuth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {

                        Map<String, Object> map = new HashMap<>();

                        map.put("online",online);
                        map.put("id_user", firebaseAuth.getUid());
                        map.put("nome", nome);
                        map.put("email", email);
                        map.put("senha", senha);
                        map.put("confirmarsenha", senhaconfir);
                        map.put("sexo", sexo);
                        map.put("token", token);

                        /*Intent intent = new Intent(activity, MainEntregadores.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        activity.startActivity(intent);*/

                        db_entregadores.document(firebaseAuth.getUid()).set(map);

                        Toast.makeText(activity, "Usuário cadastrado com sucesso.", Toast.LENGTH_LONG).show();

                    } else if (!task.isSuccessful()) {
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthWeakPasswordException e) {

                            Toast.makeText(activity, "Senha inferior a 6 caracteres", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        } catch (FirebaseAuthInvalidCredentialsException e) {

                            Toast.makeText(activity, "E-mail inválido", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        } catch (FirebaseAuthUserCollisionException e) {

                            Toast.makeText(activity, "Usuário já cadastrado", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        } catch (Exception e) {

                            Toast.makeText(activity, "Ops!Erro a cadastrar o usuário", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                }

            });
        } else {

            Toast.makeText(activity, "As senhas estão diferentes.", Toast.LENGTH_LONG).show();
            return;

        }
    }

    public void persistir_usuer(Activity activity) {

        FirebaseApp.initializeApp(activity);

        if (firebaseAuth.getCurrentUser() != null) {

            Intent intent = new Intent(activity, MainActivity.class);
            activity.startActivity(intent);
            activity.finish();
        }
    }

    public void sign_out_firebase(Activity activity) {

        Intent intent = new Intent(activity, EntrarUser.class);
        activity.startActivity(intent);
        activity.finish();

        firebaseAuth.signOut();
    }

    public void entrar_firebase(final String email, String senha, final Activity activity) {

        FirebaseApp.initializeApp(activity);

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(activity, "Digite seu e-mail", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(senha)) {
            Toast.makeText(activity, "Informe uma senha.", Toast.LENGTH_LONG).show();
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(activity);

        progressDialog.setMessage("Entrando...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                try {
                    if (task.isSuccessful()) {

                        Intent i_entrar_prof = new Intent(activity, MainActivity.class);
                        i_entrar_prof.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        activity.startActivity(i_entrar_prof);
                        activity.finish();

                        Toast.makeText(activity, "Login efetuado com sucesso", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(activity, "Erro ao efetuar o login. Verifique os dados digitados", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {

                    Toast.makeText(activity, "Ops! Ocorreu um erro inesperado.", Toast.LENGTH_LONG).show();
                }

                progressDialog.dismiss();
            }
        });
    }

    public void entrar_firebase_entregador(final String email, String senha, final Activity activity) {

        FirebaseApp.initializeApp(activity);

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(activity, "Digite seu e-mail", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(senha)) {
            Toast.makeText(activity, "Informe uma senha.", Toast.LENGTH_LONG).show();
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(activity);

        progressDialog.setMessage("Entrando...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                try {
                    if (task.isSuccessful()) {

                        /*Intent i_entrar_prof = new Intent(activity, MainEntregadores.class);
                        i_entrar_prof.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        activity.startActivity(i_entrar_prof);
                        activity.finish();*/

                        Toast.makeText(activity, "Login efetuado com sucesso", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(activity, "Erro ao efetuar o login. Verifique os dados digitados", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {

                    Toast.makeText(activity, "Ops! Ocorreu um erro inesperado.", Toast.LENGTH_LONG).show();
                }

                progressDialog.dismiss();
            }
        });
    }

    public void reset_senha(final String email, final Activity context) {

        if (TextUtils.isEmpty(email)) {

            Toast.makeText(context, "Informe um e-mail.", Toast.LENGTH_LONG).show();
            return;
        }

        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                try {
                    if (task.isSuccessful()) {

                        Intent intent = new Intent(context, EntrarUser.class);
                        context.startActivity(intent);
                        context.finish();

                        Toast.makeText(context, "Enviado e-mail para reset de senha para " + email, Toast.LENGTH_LONG).show();

                    } else {

                        Toast.makeText(context, "E-mail inválido", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {

                    Toast.makeText(context, "Erro ao enviar e-mail de recuperação:" + e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

}
