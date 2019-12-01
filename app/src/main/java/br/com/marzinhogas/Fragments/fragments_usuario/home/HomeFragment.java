package br.com.marzinhogas.Fragments.fragments_usuario.home;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.com.marzinhogas.Helpers.AccessFirebase;
import br.com.marzinhogas.Helpers.PedidoAplication;
import br.com.marzinhogas.Models.Entregadores;
import br.com.marzinhogas.Models.Notification;
import br.com.marzinhogas.Models.Pedido;
import br.com.marzinhogas.Models.Usuario;
import br.com.marzinhogas.R;

public class HomeFragment extends Fragment {

    private Spinner sp_produtos;
    private NumberPicker nb_qtd_agua, nb_qtd_gas;
    private Button pedir;

    private String id_user_logado;

    private FirebaseAuth auth = FirebaseAuth.getInstance();

    private FirebaseFirestore db_user = FirebaseFirestore.getInstance();
    private CollectionReference cl_user = db_user.collection("Users");

    private Pedido pedido = new Pedido();
    private Usuario usuario = new Usuario();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        FirebaseApp.initializeApp(getActivity());

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        View root = inflater.inflate(R.layout.fragment_pedir, container, false);

        if (firebaseUser != null) {

            id_user_logado = firebaseUser.getUid();

        }

        sp_produtos = root.findViewById(R.id.spinner);
        nb_qtd_agua = root.findViewById(R.id.nb_qtd_agua);
        nb_qtd_gas = root.findViewById(R.id.nb_qtd_gas);
        pedir = root.findViewById(R.id.btn_pedir);

        nb_qtd_agua.setMinValue(0);
        nb_qtd_agua.setMaxValue(20);

        nb_qtd_gas.setMinValue(0);
        nb_qtd_gas.setMaxValue(20);

        spinner();
        number_pickers();
        lerdadosusuario(id_user_logado);

        pedir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(getActivity());

                dialog.setContentView(R.layout.dialog_confirma_pedido);

                Button ok = dialog.findViewById(R.id.btn_OK);
                Button cancelar = dialog.findViewById(R.id.btn_cancelar);

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        @SuppressLint("SimpleDateFormat")
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        Date date = new Date();
                        String data = dateFormat.format(date);

                        SimpleDateFormat horasFormat = new SimpleDateFormat("HH:mm:ss");
                        Date horas = Calendar.getInstance().getTime();
                        String horario = horasFormat.format(horas);

                        pedido.setData(data);
                        pedido.setUser_id_pedido(auth.getUid());
                        pedido.setHorario(horario);
                        pedido.setEntregue(false);

                        new AccessFirebase().pedidos(pedido.getUser_id_pedido(), pedido.getNome(), pedido.getEndereco(),
                                pedido.getData(), pedido.getProduto(),
                                pedido.getQuantidade_gas(), pedido.getQuantidade_agua(),pedido.getHorario(),pedido.getEntregue());

                            Notification notification = new Notification();

                            notification.setUser_id_pedido(pedido.getUser_id_pedido());
                            notification.setNome(pedido.getNome());
                            notification.setEndereco(pedido.getEndereco());
                            notification.setProduto(pedido.getProduto());
                            notification.setEndereco(pedido.getEndereco());
                            notification.setData(pedido.getData());
                            notification.setQuantidade_agua(pedido.getQuantidade_agua());
                            notification.setQuantidade_gas(pedido.getQuantidade_gas());
                            notification.setHorario(pedido.getHorario());
                            notification.setEntregue(pedido.getEntregue());

                            new AccessFirebase().notificacoes(usuario.getToken(), notification);

                        dialog.dismiss();
                    }
                });

                cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialog.dismiss();
                    }
                });

                dialog.show();

            }
        });

        updatetoken();

        return root;
    }

    private void updatetoken() {

        String token = FirebaseInstanceId.getInstance().getToken();
        String uid = auth.getUid();

        usuario.setToken(token);

        if (uid != null) {

            FirebaseFirestore.getInstance().collection("Users")
                    .document(uid)
                    .update("token", token);

        }
    }

    public void number_pickers() {

        nb_qtd_agua.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {

                pedido.setQuantidade_agua(numberPicker.getValue());

            }
        });

        nb_qtd_gas.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {

                pedido.setQuantidade_gas(numberPicker.getValue());

            }
        });
    }

    public void spinner() {

        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.sp_produtos, android.R.layout.simple_spinner_dropdown_item);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp_produtos.setAdapter(arrayAdapter);

        sp_produtos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String nome_produto = sp_produtos.getSelectedItem().toString();

                if (nome_produto == "Selecione") {

                    Toast.makeText(getActivity(), "Informe o produto desejado", Toast.LENGTH_SHORT).show();

                } else {

                    pedido.setProduto(nome_produto);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void lerdadosusuario(String id_usuario_logado) {

        //buscar os dados do usuário logado Ex.: nome, endereço

        cl_user.whereEqualTo("id_user", id_usuario_logado)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        QuerySnapshot queryDocumentSnapshots = task.getResult();

                        for (Usuario usuario_banco : queryDocumentSnapshots.toObjects(Usuario.class)) {

                            String recuperar_endereco = usuario_banco.getEndereco();
                            String recuperar_nome = usuario_banco.getNome();

                            pedido.setEndereco(recuperar_endereco);
                            pedido.setNome(recuperar_nome);

                        }
                    }
                });
    }
}