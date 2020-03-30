package com.example.motoriza;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FonteEndPoints extends MainActivity {
    private static Retrofit retrofit;

    // Define a url (base)
    private static final String BASE_URL = "https://service.tecnomotor.com.br/iRasther/";

    // Cria a instância retofit (biblioteca HTTP Client)
    // usa conversão Gson
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
