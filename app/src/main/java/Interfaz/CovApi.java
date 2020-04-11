package Interfaz;

import java.util.List;

import Model.CountryItem;
import Model.CountrySummary;
import retrofit2.Call;
import retrofit2.http.GET;

public interface CovApi {
    @GET("countries")
    Call<List<CountryItem>> getCountries();

    @GET("summary")
    Call<CountrySummary> getSummary();


}
