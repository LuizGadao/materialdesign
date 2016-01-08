package br.com.luizgadao.materialdesign.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Luiz on 05/01/16.
 */
public class Disk implements Serializable {

    public String titulo;
    public String capa;
    @SerializedName("capa_big")
    public String capaGrande;
    public int ano;
    public String gravadora;
    public String[] formacao;
    public String[] faixas;
}
