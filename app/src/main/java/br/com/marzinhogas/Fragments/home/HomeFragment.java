package br.com.marzinhogas.Fragments.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
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
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;

import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.marzinhogas.Helpers.AccessFirebase;
import br.com.marzinhogas.Models.Pedido;
import br.com.marzinhogas.Models.Usuario;
import br.com.marzinhogas.R;

public class HomeFragment extends Fragment {

    HomeViewModel homeViewModel;

    private Spinner sp_produtos;
    private NumberPicker nb_qtd_agua, nb_qtd_gas;
    private Button pedir;

    String name_user_logado, id_user_logado;

    FirebaseAuth auth = FirebaseAuth.getInstance();

    FirebaseFirestore db_user = FirebaseFirestore.getInstance();
    CollectionReference cl_user = db_user.collection("Users");

    private Pedido pedido = new Pedido();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        FirebaseApp.initializeApp(getActivity());

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

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

        //buscar os dados do usuário logado Ex.: nome, endereço

        cl_user.whereEqualTo("id_user", id_user_logado)
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

                            Log.d("Nome e endereco", "\n" + recuperar_nome);
                        }
                    }
                });

        pedir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date date = new Date();
                String data = dateFormat.format(date);
                pedido.setData(data);


                new AccessFirebase().pedidos(auth.getUid(), pedido.getNome(), pedido.getEndereco(),
                        pedido.getData(), pedido.getProduto(), pedido.getQuantidade_gas(), pedido.getQuatidade_agua());
            }
        });

        return root;
    }

    public void number_pickers() {

        nb_qtd_agua.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {

                pedido.setQuatidade_agua(numberPicker.getValue());

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
}