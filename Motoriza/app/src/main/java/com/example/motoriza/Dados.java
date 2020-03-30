package com.example.motoriza;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Dados {
    //anotação para serializar o termo "id" (identificação da montadora)
    // do conjunto de dados lidos do servidor
    @SerializedName("id")
    int mId;
    //anotação para serializar o termo "nome" (nome da montadora)
    // do conjunto de dados lidos do servidor
    @SerializedName("nome")
    String mNome;

    public Dados(int id, String nome) {
        this.mId = id;
        this.mNome = nome;
    }

    public int getmId() {
        return mId;
    }
    public String getmNome() {
        return mNome;
    }

}
