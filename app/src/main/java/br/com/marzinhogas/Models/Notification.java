package br.com.marzinhogas.Models;

public class Notification extends Pedido{

    private String id_cliente;

    public Notification() {

    }

    public String getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(String id_cliente) {
        this.id_cliente = id_cliente;
    }
}
