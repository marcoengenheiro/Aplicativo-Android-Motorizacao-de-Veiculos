package com.example.motoriza;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface MotosServices {
    @GET("montadora?pm.type=MOTOS")
    Call<List<Dados>> getLista();
}
