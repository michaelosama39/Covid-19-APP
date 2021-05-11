package com.example.covid_19app.api;
import com.example.covid_19app.model.CountryData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    @GET("countries")
    Call<List<CountryData>> getCountryData();
}
