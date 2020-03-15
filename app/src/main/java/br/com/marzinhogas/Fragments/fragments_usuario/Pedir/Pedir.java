package br.com.marzinhogas.Fragments.fragments_usuario.Pedir;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
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
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import br.com.marzinhogas.Controlers.SplashPedidoFinalizado;
import br.com.marzinhogas.Helpers.AccessFirebase;
import br.com.marzinhogas.Models.Entregadores;
import br.com.marzinhogas.Models.Notification;
import br.com.marzinhogas.Models.Pedido;
import br.com.marzinhogas.Models.Usuario;
import br.com.marzinhogas.R;

public class Pedir extends Fragment {

    private Spinner sp_produtos;
    private NumberPicker nb_qtd_agua, nb_qtd_gas;
    private Button pedir;

    private String id_user_logado;

    private FirebaseAuth auth = FirebaseAuth.getInstance();

    private Pedido pedido = new Pedido();
    private Usuario usuario = new Usuario();

    LinearLayout lout_agua, lout_gas;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        FirebaseApp.initializeApp(getActivity());

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final View root = inflater.inflate(R.layout.fragment_pedir, container, false);

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

        nb_qtd_agua.setMinValue(0);
        nb_qtd_agua.setMaxValue(20);

        nb_qtd_gas.setMinValue(0);
        nb_qtd_gas.setMaxValue(20);

        AccessFirebase.getInstance().BuscaUser(pedido,id_user_logado);

        pedir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(Objects.requireNonNull(getActivity()));

                dialog.setContentView(R.layout.dialog_confirma_pedido);

                String endereco = pedido.getEndereco() + ", " + pedido.getBairro() + ", " + pedido.getNumero();

                TextView confirma_nome = dialog.findViewById(R.id.conf_nome);
                TextView confirma_endereco = dialog.findViewById(R.id.conf_endereco);
                TextView confirma_qtd_agua = dialog.findViewById(R.id.conf_qtd_agua);
                TextView confirma_qts_gas = dialog.findViewById(R.id.conf_qtd_gas);

                confirma_nome.setText(pedido.getNome());
                confirma_endereco.setText(endereco);
                confirma_qtd_agua.setText(String.valueOf(pedido.getQuantidade_agua()));
                confirma_qts_gas.setText(String.valueOf(pedido.getQuantidade_gas()));

                Button ok = dialog.findViewById(R.id.btn_OK);
                Button cancelar = dialog.findViewById(R.id.btn_cancelar);

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        picktokenentregador();

                        Intent i_splash = new Intent(getActivity(), SplashPedidoFinalizado.class);
                        startActivity(i_splash);
                        getActivity().finish();

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

        spinner();
        number_pickers();
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

        AccessFirebase.getInstance().buscarentregadores();

        AccessFirebase.getInstance().pedidos(pedido);

        AccessFirebase.getInstance().pedidos_permanentes(pedido);

        AccessFirebase.getInstance().pedidos_temporarios(pedido);

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

                if (nome_produto.equals("Selecione")) {

                    lout_agua.setVisibility(View.GONE);
                    lout_gas.setVisibility(View.GONE);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}