package com.example.motoriza;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface PesadosServices {
    @GET("montadora?pm.type=PESADOS")
    Call<List<Dados>> getLista();
}
