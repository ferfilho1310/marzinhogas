package br.com.marzinhogas.Fragments.fragments_usuario.Perfil;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import br.com.marzinhogas.Controlers.MainActivity;
import br.com.marzinhogas.Helpers.AccessFirebase;
import br.com.marzinhogas.Models.Usuario;
import br.com.marzinhogas.R;

public class AlterarDadosPerfil extends Fragment {

    private EditText nome_update, endereco_update, bairro_update, numero_update,complemento;
    private Button alterar_dados;

    private FirebaseAuth auth = FirebaseAuth.getInstance();

    private String id_user_logado;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        View root = inflater.inflate(R.layout.fragment_perfil, container, false);

        nome_update = root.findViewById(R.id.ed_nome_update);
        endereco_update = root.findViewById(R.id.btn_endereço_update);
        bairro_update = root.findViewById(R.id.ed_bairro_update);
        numero_update = root.findViewById(R.id.ed_numero_update);
        alterar_dados = root.findViewById(R.id.btn_alterar_dados);
        complemento = root.findViewById(R.id.ed_complemento);

        if (firebaseUser != null) {
            id_user_logado = firebaseUser.getUid();
        }

        AccessFirebase.getInstance().lerdadosusuario(nome_update, endereco_update, numero_update, bairro_update,complemento, id_user_logado);

        alterar_dados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(getActivity())
                        .setTitle("Atenção")
                        .setMessage("Deseja realmente fazer a alteração do dados ?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                AccessFirebase.getInstance().alterardadosuser(nome_update, endereco_update, numero_update,
                                        bairro_update,complemento, auth, getActivity());

                            }
                        }).setNegativeButton("Cancelar", null).show();
            }
        });

        return root;
    }
}