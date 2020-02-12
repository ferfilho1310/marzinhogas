package br.com.marzinhogas.Fragments.fragments_usuario.ui.home_entregador;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import br.com.marzinhogas.Models.Pedido;
import br.com.marzinhogas.R;

public class HomeFragment extends Fragment {
/*
    RecyclerView pedido_para_entregar;

    Query query;
    FirestoreRecyclerOptions<Pedido> fro_pedidos;
    AdapterPedidoEntregador adapterPedidosCliente;

    FirebaseFirestore firebaseAuth = FirebaseFirestore.getInstance();
    CollectionReference cl_pedidos = firebaseAuth.collection("Pedidos");*/

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home_usuario, container, false);
/*
        pedido_para_entregar = root.findViewById(R.id.rc_pedidos_para_entregar);

        lerpedidosfeitos();*/

        return root;
    }

   /* public void lerpedidosfeitos() {

        query = cl_pedidos.orderBy("data", Query.Direction.DESCENDING)
                .orderBy("horario", Query.Direction.DESCENDING);

        fro_pedidos = new FirestoreRecyclerOptions.Builder<Pedido>()
                .setQuery(query, Pedido.class)
                .build();

        adapterPedidosCliente = new AdapterPedidoEntregador(fro_pedidos);
        pedido_para_entregar.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        pedido_para_entregar.setAdapter(adapterPedidosCliente);
        pedido_para_entregar.setHasFixedSize(true);

    }

    @Override
    public void onStart() {
        super.onStart();
        adapterPedidosCliente.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapterPedidosCliente.stopListening();
    }*/

}