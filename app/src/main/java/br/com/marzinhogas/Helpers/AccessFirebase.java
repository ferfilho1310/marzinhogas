package br.com.marzinhogas.Helpers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.widget.EditText;
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
import com.google.firebase.firestore.QuerySnapshot;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.marzinhogas.Controlers.EntrarUser;
import br.com.marzinhogas.Controlers.MainActivity;
import br.com.marzinhogas.Models.Entregadores;
import br.com.marzinhogas.Models.Notification;
import br.com.marzinhogas.Models.Pedido;
import br.com.marzinhogas.Models.Usuario;

public class AccessFirebase implements IAccessFirebase {

    private static AccessFirebase accessFirebase;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    CollectionReference db_users = FirebaseFirestore.getInstance().collection("Users");
    CollectionReference db_pedido = FirebaseFirestore.getInstance().collection("Pedidos");
    CollectionReference db_notificacoes = FirebaseFirestore.getInstance().collection("notifications");
    CollectionReference db_pedido_temp = FirebaseFirestore.getInstance().collection("PedidoTemp");
    CollectionReference db_pedido_permanente = FirebaseFirestore.getInstance().collection("PedidoPerm");

    ProgressDialog progressDialog;

    private AccessFirebase() {
    }

    public static synchronized AccessFirebase getInstance() {
        if (accessFirebase == null) {
            accessFirebase = new AccessFirebase();
        }
        return accessFirebase;
    }


    @Override
    public void notificacoes(String user_token, Notification notification) {
        db_notificacoes.document(user_token).set(notification);
    }

    @Override
    public void pedidos(Pedido pedido) {

        Map<String, Object> map = new HashMap<>();

        map.put("id_user", pedido.getUser_id_pedido());
        map.put("nome", pedido.getNome());
        map.put("endereco", pedido.getEndereco());
        map.put("bairro", pedido.getBairro());
        map.put("numero", pedido.getNumero());
        map.put("data", pedido.getData());
        map.put("produto", pedido.getProduto());
        map.put("quantidade_gas", pedido.getQuantidade_gas());
        map.put("quantidade_agua", pedido.getQuantidade_agua());
        map.put("horario", pedido.getHorario());
        map.put("entregue", pedido.getEntregue());

        db_pedido.add(map);
    }

    @Override
    public void pedidos_temporarios(Pedido pedido) {

        Map<String, Object> map = new HashMap<>();

        map.put("id_user", pedido.getUser_id_pedido());
        map.put("nome", pedido.getNome());
        map.put("endereco", pedido.getEndereco());
        map.put("bairro", pedido.getBairro());
        map.put("numero", pedido.getNumero());
        map.put("data", pedido.getData());
        map.put("produto", pedido.getProduto());
        map.put("quantidade_gas", pedido.getQuantidade_gas());
        map.put("quantidade_agua", pedido.getQuantidade_agua());
        map.put("horario", pedido.getHorario());
        map.put("entregue", pedido.getEntregue());

        db_pedido_temp.add(map);
    }

    @Override
    public void pedidos_permanentes(Pedido pedido) {

        Map<String, Object> map = new HashMap<>();

        map.put("id_user", pedido.getUser_id_pedido());
        map.put("nome", pedido.getNome());
        map.put("endereco", pedido.getEndereco());
        map.put("bairro", pedido.getBairro());
        map.put("numero", pedido.getNumero());
        map.put("data", pedido.getData());
        map.put("produto", pedido.getProduto());
        map.put("quantidade_gas", pedido.getQuantidade_gas());
        map.put("quantidade_agua", pedido.getQuantidade_agua());
        map.put("horario", pedido.getHorario());
        map.put("entregue", pedido.getEntregue());

        db_pedido_permanente.add(map);
    }

    @Override
    public void BuscaUser(final Pedido pedido, String id_user_logado) {

        FirebaseFirestore.getInstance().collection("Users").whereEqualTo("id_user", id_user_logado)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        String recuperar_endereco = "";
                        String recuperar_nome = "";
                        String recuperar_bairro = "";
                        String recuperar_numero = "";

                        if (task.isSuccessful()) {
                            QuerySnapshot queryDocumentSnapshots = task.getResult();

                            for (Usuario usuario_banco : queryDocumentSnapshots.toObjects(Usuario.class)) {

                                recuperar_endereco = usuario_banco.getEndereco();
                                recuperar_nome = usuario_banco.getNome();
                                recuperar_bairro = usuario_banco.getBairro();
                                recuperar_numero = usuario_banco.getNumero();

                            }

                            pedido.setEndereco(recuperar_endereco);
                            pedido.setNome(recuperar_nome);
                            pedido.setBairro(recuperar_bairro);
                            pedido.setNumero(recuperar_numero);
                        }
                    }
                });
    }

    @Override
    public void buscarentregadores() {

        FirebaseFirestore.getInstance().collection("Entregadores")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        String token_entregador;
                        String id_entregador;

                        List<String> ls_token_entregador = new ArrayList<>();
                        List<String> ls_id_entregador = new ArrayList<>();

                        QuerySnapshot queryDocumentSnapshots = task.getResult();

                        if (queryDocumentSnapshots != null) {
                            for (Entregadores entregadores : queryDocumentSnapshots.toObjects(Entregadores.class)) {

                                token_entregador = entregadores.getToken();
                                id_entregador = entregadores.getId_user();

                                ls_token_entregador.add(token_entregador);
                                ls_id_entregador.add(id_entregador);
                            }
                        }

                        for (int i = 0; i < ls_token_entregador.size(); i++) {

                            Notification notification = new Notification();
                            Entregadores entregadores = new Entregadores();

                            entregadores.setToken(ls_token_entregador.get(i));
                            entregadores.setId_user(ls_id_entregador.get(i));
                            notification.setBody_pedido("Acesse o app para verificar.");
                            notification.setId_cliente(entregadores.getId_user());

                            AccessFirebase.getInstance().notificacoes(entregadores.getToken(), notification);
                        }
                    }
                });
    }

    @Override
    public void cadastrar_user(final Usuario usuario, final Activity activity) {

        FirebaseApp.initializeApp(activity);

        if (TextUtils.isEmpty(usuario.getNome())) {
            Toast.makeText(activity, "Digite seu nome", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(usuario.getEmail())) {
            Toast.makeText(activity, "Informe um e-mail.", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(usuario.getBairro())) {
            Toast.makeText(activity, "Informe o bairro", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(usuario.getNumero())) {
            Toast.makeText(activity, "Informe o numero", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(usuario.getSenha())) {
            Toast.makeText(activity, "Informe uma senha.", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(usuario.getConfirmarsenha())) {
            Toast.makeText(activity, "Confirme a senha", Toast.LENGTH_LONG).show();
            return;
        }

        if (usuario.getSenha().equals(usuario.getConfirmarsenha())) {

            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("Cadastrando...");
            progressDialog.show();

            firebaseAuth.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {

                        Map<String, String> map = new HashMap<>();

                        map.put("id_user", firebaseAuth.getUid());
                        map.put("nome", usuario.getNome());
                        map.put("endereco", usuario.getEndereco());
                        map.put("email", usuario.getEmail());
                        map.put("complemento",usuario.getComplemento());
                        map.put("senha", AccessResources.getInstance().criptografiadesenha(usuario.getNome(), usuario.getSenha()));
                        map.put("bairro", usuario.getBairro());
                        map.put("numero", usuario.getNumero());
                        map.put("confirmarsenha", AccessResources.getInstance().criptografiadesenha(usuario.getNome(), usuario.getConfirmarsenha()));
                        map.put("sexo", usuario.getSexo());
                        map.put("token", usuario.getToken());

                        Intent intent = new Intent(activity, MainActivity.class);
                        activity.startActivity(intent);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        activity.finish();

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

                            Toast.makeText(activity, "Ops!Erro a cadastrar o usuário" + e, Toast.LENGTH_SHORT).show();
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

    @Override
    public void persistir_usuer(Activity activity) {

        FirebaseApp.initializeApp(activity);

        if (firebaseAuth.getCurrentUser() != null) {

            Intent intent = new Intent(activity, MainActivity.class);
            activity.startActivity(intent);
            activity.finish();
        }
    }

    @Override
    public void sign_out_firebase(Activity activity) {

        Intent intent = new Intent(activity, EntrarUser.class);
        activity.startActivity(intent);
        activity.finish();

        firebaseAuth.signOut();
    }

    @Override
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
                        activity.startActivity(i_entrar_prof);
                        i_entrar_prof.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
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

    @Override
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

    @Override
    public boolean isOnline(Activity activity) {

        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = null;
        if (cm != null) {
            netInfo = cm.getActiveNetworkInfo();
        }
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void lerdadosusuario(final EditText ed_nome, final EditText ed_endereco, final EditText ed_numero, final EditText ed_bairro, String id_user_logado) {

        FirebaseFirestore.getInstance().collection("Users").whereEqualTo("id_user", id_user_logado)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        String recuperar_endereco = "";
                        String recuperar_nome = "";
                        String recuperar_numero = "";
                        String recuperar_bairro = "";

                        QuerySnapshot queryDocumentSnapshots = task.getResult();

                        for (Usuario usuario_banco : queryDocumentSnapshots.toObjects(Usuario.class)) {

                            recuperar_endereco = usuario_banco.getEndereco();
                            recuperar_nome = usuario_banco.getNome();
                            recuperar_bairro = usuario_banco.getBairro();
                            recuperar_numero = usuario_banco.getNumero();
                        }

                        ed_nome.setText(recuperar_nome);
                        ed_endereco.setText(recuperar_endereco);
                        ed_bairro.setText(recuperar_bairro);
                        ed_numero.setText(recuperar_numero);
                    }
                });
    }

    @Override
    public void alterardadosuser(EditText ed_nome, EditText ed_endereco, EditText ed_numero, EditText ed_bairro, FirebaseAuth auth, Activity context) {

        String uid = auth.getUid();

        Map<String, Object> map = new HashMap<>();

        map.put("nome", ed_nome.getText().toString());
        map.put("endereco", ed_endereco.getText().toString());
        map.put("bairro", ed_bairro.getText().toString());
        map.put("numero", ed_numero.getText().toString());

        if (uid != null) {

            FirebaseFirestore.getInstance().collection("Users")
                    .document(uid)
                    .update(map);
        }

        Intent i_altera_perfil = new Intent(context, MainActivity.class);
        context.startActivity(i_altera_perfil);
        context.finish();

        Toast.makeText(context, "Dados alterados com sucesso", Toast.LENGTH_LONG).show();

    }
}
