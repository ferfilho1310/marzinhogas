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
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.marzinhogas.Helpers.AccessFirebase;
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
    private Entregadores entregadorestoken = new Entregadores();
    private Notification notification = new Notification();

    LinearLayout lout_agua, lout_gas;

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
        lout_agua = root.findViewById(R.id.layout_qtd_agua);
        lout_gas = root.findViewById(R.id.layout_qtd_gas);

        lout_agua.setVisibility(View.GONE);
        lout_gas.setVisibility(View.GONE);

        nb_qtd_agua.setMinValue(1);
        nb_qtd_agua.setMaxValue(20);

        nb_qtd_gas.setMinValue(1);
        nb_qtd_gas.setMaxValue(20);

        FirebaseFirestore.getInstance().collection("Users").whereEqualTo("id_user", id_user_logado)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        String recuperar_endereco;
                        String recuperar_nome;

                        QuerySnapshot queryDocumentSnapshots = task.getResult();

                        for (Usuario usuario_banco : queryDocumentSnapshots.toObjects(Usuario.class)) {

                            recuperar_endereco = usuario_banco.getEndereco();
                            recuperar_nome = usuario_banco.getNome();

                            pedido.setEndereco(recuperar_endereco);
                            pedido.setNome(recuperar_nome);
                        }
                    }
                });

        spinner();
        number_pickers();


        pedir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(getActivity());

                dialog.setContentView(R.layout.dialog_confirma_pedido);

                TextView confirma_nome = dialog.findViewById(R.id.conf_nome);
                TextView confirma_endereco = dialog.findViewById(R.id.conf_endereco);
                TextView confirma_qtd_agua = dialog.findViewById(R.id.conf_qtd_agua);
                TextView confirma_qts_gas = dialog.findViewById(R.id.conf_qtd_gas);

                confirma_nome.setText(pedido.getNome());
                confirma_endereco.setText(pedido.getEndereco());
                confirma_qtd_agua.setText(String.valueOf(pedido.getQuantidade_agua()));
                confirma_qts_gas.setText(String.valueOf(pedido.getQuantidade_gas()));

                Button ok = dialog.findViewById(R.id.btn_OK);
                Button cancelar = dialog.findViewById(R.id.btn_cancelar);

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        picktokenentregador();

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

    private void picktokenentregador() {

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String data = dateFormat.format(date);

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat horasFormat = new SimpleDateFormat("HH:mm:ss");
        Date horas = Calendar.getInstance().getTime();
        String horario = horasFormat.format(horas);

        pedido.setData(data);
        pedido.setUser_id_pedido(auth.getUid());
        pedido.setHorario(horario);
        pedido.setEntregue(false);

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

                        for (Entregadores entregadores : queryDocumentSnapshots.toObjects(Entregadores.class)) {

                            token_entregador = entregadores.getToken();
                            id_entregador = entregadores.getId_user();

                            ls_token_entregador.add(token_entregador);
                            ls_id_entregador.add(id_entregador);
                        }

                        for (int i = 0; i < ls_token_entregador.size(); i++) {

                            entregadorestoken.setToken(ls_token_entregador.get(i));
                            entregadorestoken.setId_user(ls_id_entregador.get(i));
                            notification.setBody_pedido("Acesse o app para verificar.");
                            notification.setId_cliente(entregadorestoken.getId_user());

                            //notification.setUser_id_pedido(pedido.getUser_id_pedido());

                            /*notification.setNome("teste");
                            notification.setEndereco("teste");
                            notification.setProduto(pedido.getProduto());
                            notification.setData(pedido.getData());
                            notification.setQuantidade_agua(pedido.getQuantidade_agua());
                            notification.setQuantidade_gas(pedido.getQuantidade_gas());
                            notification.setHorario(pedido.getHorario());
                            notification.setEntregue(pedido.getEntregue());
                            */

                            AccessFirebase.getInstance().notificacoes(entregadorestoken.getToken(), notification);
                        }
                    }
                });

        AccessFirebase.getInstance().pedidos(pedido.getUser_id_pedido(), pedido.getNome(), pedido.getEndereco(),
                pedido.getData(), pedido.getProduto(),
                pedido.getQuantidade_gas(), pedido.getQuantidade_agua(), pedido.getHorario(), pedido.getEntregue());

        AccessFirebase.getInstance().pedidos_permanentes(pedido.getUser_id_pedido(), pedido.getNome(), pedido.getEndereco(),
                pedido.getData(), pedido.getProduto(),
                pedido.getQuantidade_gas(), pedido.getQuantidade_agua(), pedido.getHorario(), pedido.getEntregue());

        AccessFirebase.getInstance().pedidos_temporarios(pedido.getUser_id_pedido(), pedido.getNome(), pedido.getEndereco(),
                pedido.getData(), pedido.getProduto(),
                pedido.getQuantidade_gas(), pedido.getQuantidade_agua(), pedido.getHorario(), pedido.getEntregue());

    }

    private void updatetoken() {

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {

                String uid = auth.getUid();
                String token = instanceIdResult.getToken();
                usuario.setToken(token);

                if (uid != null) {

                    FirebaseFirestore.getInstance().collection("Users")
                            .document(uid)
                            .update("token", token);

                }
            }
        });
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

                if (nome_produto.equals("Água")) {

                    lout_agua.setVisibility(View.VISIBLE);
                    lout_gas.setVisibility(View.GONE);
                    pedido.setProduto(nome_produto);
                }

                if (nome_produto.equals("Gás")) {

                    lout_gas.setVisibility(View.VISIBLE);
                    lout_agua.setVisibility(View.GONE);
                    pedido.setProduto(nome_produto);
                }

                if (nome_produto.equals("Água e Gás")) {

                    lout_agua.setVisibility(View.VISIBLE);
                    lout_gas.setVisibility(View.VISIBLE);
                    pedido.setProduto(nome_produto);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}