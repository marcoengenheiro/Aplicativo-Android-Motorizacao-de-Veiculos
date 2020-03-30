package com.example.motoriza;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface AgricolasServices {
    @GET("montadora?pm.type=AGRICOLAS")
    Call<List<Dados>> getLista();
}
