package com.example.motoriza;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface LevesServices {
    @GET("montadora?pm.type=LEVES")
    Call<List<Dados>> getLista();
}
