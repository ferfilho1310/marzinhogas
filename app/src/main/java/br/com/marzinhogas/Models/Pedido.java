package br.com.marzinhogas.Models;

public class Pedido {

    private String nome;
    private String produto;
    private String endereco;
    private int quantidade_gas;
    private int quatidade_agua;
    private String data;

    public Pedido(){

    }

    public Pedido(String nome, String produto, String endereco, int quantidade_gas, int quatidade_agua, String data) {
        this.nome = nome;
        this.produto = produto;
        this.endereco = endereco;
        this.quantidade_gas = quantidade_gas;
        this.quatidade_agua = quatidade_agua;
        this.data = data;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getProduto() {
        return produto;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public int getQuantidade_gas() {
        return quantidade_gas;
    }

    public void setQuantidade_gas(int quantidade_gas) {
        this.quantidade_gas = quantidade_gas;
    }

    public int getQuatidade_agua() {
        return quatidade_agua;
    }

    public void setQuatidade_agua(int quatidade_agua) {
        this.quatidade_agua = quatidade_agua;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
