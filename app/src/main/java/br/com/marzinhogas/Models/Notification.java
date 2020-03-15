package br.com.marzinhogas.Models;

public class Notification{

    private String id_cliente;
    private String body_pedido;

    public Notification() {

    }

    public String getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(String id_cliente) {
        this.id_cliente = id_cliente;
    }

    public String getBody_pedido() {
        return body_pedido;
    }

    public void setBody_pedido(String body_pedido) {
        this.body_pedido = body_pedido;
    }
}
