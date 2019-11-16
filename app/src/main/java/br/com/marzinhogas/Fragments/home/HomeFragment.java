package br.com.marzinhogas.Fragments.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;

import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.marzinhogas.Helpers.AccessFirebase;
import br.com.marzinhogas.Models.Pedido;
import br.com.marzinhogas.R;

public class HomeFragment extends Fragment {

    HomeViewModel homeViewModel;

    private Spinner sp_produtos;
    private NumberPicker nb_qtd_agua, nb_qtd_gas;
    private Button pedir;

    String name,id;

    private Pedido pedido = new Pedido();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        FirebaseApp.initializeApp(getActivity());

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

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

        pedir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date date = new Date();
                String data = dateFormat.format(date);
                pedido.setData(data);

                if(firebaseUser != null){

                    name = firebaseUser.getDisplayName();
                    id = firebaseUser.getUid();

                }

                new AccessFirebase().pedidos(name, pedido.getEndereco(),
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
                pedido.setProduto(nome_produto);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {


            }
        });
    }
}