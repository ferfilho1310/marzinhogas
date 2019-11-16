package br.com.marzinhogas.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Usuario implements Parcelable {

    private String nome;
    private String endereco;
    private String email;
    private String senha;
    private String confirmarsenha;
    private String sexo;

    public Usuario(){

    }

    public Usuario(String nome, String endereco, String email, String senha, String confirmarsenha, String sexo) {
        this.nome = nome;
        this.endereco = endereco;
        this.email = email;
        this.senha = senha;
        this.confirmarsenha = confirmarsenha;
        this.sexo = sexo;
    }

    protected Usuario(Parcel in) {
        nome = in.readString();
        endereco = in.readString();
        email = in.readString();
        senha = in.readString();
        confirmarsenha = in.readString();
        sexo = in.readString();
    }

    public static final Creator<Usuario> CREATOR = new Creator<Usuario>() {
        @Override
        public Usuario createFromParcel(Parcel in) {
            return new Usuario(in);
        }

        @Override
        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getConfirmarsenha() {
        return confirmarsenha;
    }

    public void setConfirmarsenha(String confirmarsenha) {
        this.confirmarsenha = confirmarsenha;
    }

    public String  getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(nome);
        parcel.writeString(endereco);
        parcel.writeString(email);
        parcel.writeString(senha);
        parcel.writeString(confirmarsenha);
        parcel.writeString(sexo);
    }
}
