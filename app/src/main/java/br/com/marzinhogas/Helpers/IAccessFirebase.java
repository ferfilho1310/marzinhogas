package br.com.marzinhogas.Helpers;

import android.app.Activity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;
import br.com.marzinhogas.Models.Notification;
import br.com.marzinhogas.Models.Pedido;
import br.com.marzinhogas.Models.Usuario;

public interface IAccessFirebase {

    void notificacoes(String user_token, Notification notification);

    void pedidos(Pedido pedido);

    void pedidos_temporarios(Pedido pedido);

    void pedidos_permanentes(Pedido pedido);

    void cadastrar_user(Usuario pedido, final Activity activity);

    void persistir_usuer(Activity activity);

    void sign_out_firebase(Activity activity);

    void entrar_firebase(final String email, String senha, final Activity activity);

    void reset_senha(final String email, final Activity context);

    void buscarentregadores();

    void BuscaUser(Pedido pedido,String id);

    boolean isOnline(Activity activity);

}
