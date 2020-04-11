package br.com.marzinhogas.Helpers;

import android.app.Activity;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public interface IAccessResources {

    String criptografiadesenha(String user,String senha) throws NoSuchAlgorithmException, UnsupportedEncodingException;
}
