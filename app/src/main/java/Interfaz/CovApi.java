package Interfaz;

import java.util.ArrayList;

import Model.CountryHistoryItem;
import Model.CountryItem;
import retrofit2.Call;
import retrofit2.http.GET;

//import Model.CountrySummary;
public interface CovApi {
    @GET("countries?sort=cases")
    Call<ArrayList<CountryItem>> getCountriesSortetByCaseDes();

    @GET("status/confirmed")
    Call<ArrayList<CountryHistoryItem>> getCountryHistoricalAll();



}
