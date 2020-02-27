package br.com.marzinhogas.Helpers;

import android.app.Activity;

import java.util.List;
import java.util.Map;

import br.com.marzinhogas.Models.Notification;

public interface IAccessFirebase {

    void notificacoes(String user_token, Notification notification);

    void pedidos(String id_user, String nome, String endereco, String data,
                 String produto, int quantidade_gas, int quantidade_agua,
                 String horario, Boolean entregue);

    void pedidos_temporarios(String id_user, String nome, String endereco, String data,
                             String produto, int quantidade_gas, int quantidade_agua,
                             String horario, Boolean entregue);

    void pedidos_permanentes(String id_user, String nome, String endereco, String data,
                             String produto, int quantidade_gas, int quantidade_agua,
                             String horario, Boolean entregue);

    void cadastrar_user(final String nome, final String endereco, final String email, final String senha,
                        final String senhaconfir, final String sexo, final String token, final Activity activity);

    void persistir_usuer(Activity activity);

    void sign_out_firebase(Activity activity);

    void entrar_firebase(final String email, String senha, final Activity activity);

    void reset_senha(final String email, final Activity context);

}
