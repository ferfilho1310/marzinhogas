package br.com.marzinhogas.Fragments.fragments_usuario.Perfil;

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
import br.com.marzinhogas.Models.Usuario;
import br.com.marzinhogas.R;

public class AlterarDadosPerfil extends Fragment {

    EditText nome_update, endereco_update,bairro_update,numero_update;
    Button alterar_dados;

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

        if (firebaseUser != null) {
            id_user_logado = firebaseUser.getUid();
        }

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

                        nome_update.setText(recuperar_nome);
                        endereco_update.setText(recuperar_endereco);
                        bairro_update.setText(recuperar_bairro);
                        numero_update.setText(recuperar_numero);
                    }
                });

        alterar_dados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uid = auth.getUid();

                Map<String, Object> map = new HashMap<>();

                map.put("nome", nome_update.getText().toString());
                map.put("endereco", endereco_update.getText().toString());
                map.put("bairro",bairro_update.getText().toString());
                map.put("numero",numero_update.getText().toString());

                if (uid != null) {

                    FirebaseFirestore.getInstance().collection("Users")
                            .document(uid)
                            .update(map);
                }

                Intent i_altera_perfil = new Intent(getActivity(), MainActivity.class);
                startActivity(i_altera_perfil);
                getActivity().finish();

                Toast.makeText(getActivity(), "Dados alterados com sucesso", Toast.LENGTH_LONG).show();
            }
        });

        return root;
    }
}