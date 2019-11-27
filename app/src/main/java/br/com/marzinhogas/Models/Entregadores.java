package br.com.marzinhogas.Models;

public class Entregadores extends Usuario {

    private boolean estado;

    public Entregadores() {
    }

    public Entregadores(String nome, String endereco, String email,
                        String senha, String confirmarsenha, String sexo, String token) {
        super(nome, endereco, email, senha, confirmarsenha, sexo, token);

    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

}
