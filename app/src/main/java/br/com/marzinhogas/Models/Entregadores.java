package br.com.marzinhogas.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Entregadores extends Usuario implements Parcelable {

    private boolean estado;

    public Entregadores() {
    }

    public Entregadores(String nome, String endereco, String email,
                        String senha, String confirmarsenha, String sexo, String token) {
        super(nome, endereco, email, senha, confirmarsenha, sexo, token);

    }

    protected Entregadores(Parcel in) {
        estado = in.readByte() != 0;
    }

    public static final Creator<Entregadores> CREATOR = new Creator<Entregadores>() {
        @Override
        public Entregadores createFromParcel(Parcel in) {
            return new Entregadores(in);
        }

        @Override
        public Entregadores[] newArray(int size) {
            return new Entregadores[size];
        }
    };

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (estado ? 1 : 0));
    }
}
