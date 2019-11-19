package br.com.marzinhogas.Fragments.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.io.FileReader;

import br.com.marzinhogas.Adapters.AdapterPedidosCliente;
import br.com.marzinhogas.Models.Pedido;
import br.com.marzinhogas.R;

public class GalleryFragment extends Fragment {

    GalleryViewModel galleryViewModel;

    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    FirebaseAuth fb_user_id = FirebaseAuth.getInstance();

    Query query;
    FirestoreRecyclerOptions<Pedido> fro_pedidos;
    AdapterPedidosCliente adapterPedidosCliente;
    RecyclerView rc_pedidos_feitos;

    FirebaseFirestore firebaseAuth = FirebaseFirestore.getInstance();
    CollectionReference cl_pedidos = firebaseAuth.collection("Pedidos")
            .document(fb_user_id.getUid())
            .collection("pedidoscliente");

    String id_user;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        rc_pedidos_feitos = root.findViewById(R.id.rc_pedidos);

        lerpedidosfeitos();

        return root;
    }

    public void lerpedidosfeitos() {

        if (firebaseUser != null) {

            id_user = firebaseUser.getUid();

        }

        query = cl_pedidos
                .orderBy("data", Query.Direction.ASCENDING);

        fro_pedidos = new FirestoreRecyclerOptions.Builder<Pedido>()
                .setQuery(query, Pedido.class)
                .build();

        adapterPedidosCliente = new AdapterPedidosCliente(fro_pedidos);
        rc_pedidos_feitos.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rc_pedidos_feitos.setAdapter(adapterPedidosCliente);
        rc_pedidos_feitos.setHasFixedSize(true);

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
    }
}