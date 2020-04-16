package Interfaz;

import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.List;

import Model.CountryInfo;
import Model.CountryItem;
//import Model.CountrySummary;
import Model.CountryList;
import retrofit2.Call;
import retrofit2.http.GET;
public interface CovApi {
    @GET("countries?sort=cases")
    Call<ArrayList<CountryItem>> getCountries();

//    @GET("summary")
//    Call<CountrySummary> getSummary();


}
