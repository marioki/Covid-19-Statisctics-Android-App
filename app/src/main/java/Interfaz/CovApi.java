package Interfaz;

import java.util.ArrayList;

import model.CountryDeathsHistoryItem;
import model.CountryHistoryItem;
import model.CountryItem;
import model.GlobalStats;
import retrofit2.Call;
import retrofit2.http.GET;

//import Model.CountrySummary;
public interface CovApi {
    @GET("countries?sort=cases")
    Call<ArrayList<CountryItem>> getCountriesSortedByCaseDes();

    @GET("status/confirmed")
    Call<ArrayList<CountryHistoryItem>> getCountryHistoricalAll();

    @GET("status/deaths")
    Call<ArrayList<CountryDeathsHistoryItem>> getCountryDeathsHistoricalAll();

    @GET("all/")
    Call<GlobalStats> getGlobalStatistics();



}
