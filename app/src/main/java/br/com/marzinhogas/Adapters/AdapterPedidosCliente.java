package br.com.marzinhogas.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import br.com.marzinhogas.Models.Pedido;
import br.com.marzinhogas.R;

public class AdapterPedidosCliente extends FirestoreRecyclerAdapter<Pedido, AdapterPedidosCliente.PedidosClienteHolder> {


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AdapterPedidosCliente(@NonNull FirestoreRecyclerOptions<Pedido> options) {
        super(options);
    }

    @NonNull
    @Override
    public PedidosClienteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mostra_dados_pedidos,parent,false);

        return new PedidosClienteHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull PedidosClienteHolder holder, int position, @NonNull Pedido model) {

        holder.nome.setText(model.getNome());
        holder.produtos.setText(model.getProduto());
        holder.qtd_gas.setText(String.valueOf(model.getQuantidade_gas()));
        holder.qtd_agua.setText(String.valueOf(model.getQuatidade_agua()));
        holder.data.setText(model.getData());
        holder.endereco.setText(model.getEndereco());

    }


    public class PedidosClienteHolder extends RecyclerView.ViewHolder{

        TextView nome,produtos,data,qtd_gas,qtd_agua,endereco;

        public PedidosClienteHolder(@NonNull View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.txt_nome_do_pedido);
            produtos = itemView.findViewById(R.id.txt_produto_pedido);
            data = itemView.findViewById(R.id.txt_data_pedido);
            qtd_agua = itemView.findViewById(R.id.txt_qtd_agua);
            qtd_gas = itemView.findViewById(R.id.txt_qtd_gas);
            endereco = itemView.findViewById(R.id.txt_endereco_cliente);
        }
    }
}
