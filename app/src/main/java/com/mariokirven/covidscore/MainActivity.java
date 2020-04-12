package com.mariokirven.covidscore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.List;

import Interfaz.CovApi;
import Model.CountryItem;
import Model.CountrySummary;
import Model.CountryX;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private TextView mainTextView;
    private RecyclerView recycler_view;
    private MyAdapter myadapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recycler_view = findViewById(R.id.recycler_view);

        SearchView country_search = findViewById(R.id.searchView);
        SearchView.OnQueryTextListener object = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                myadapter.getFilter().filter(newText);
                return false;
            }
        };

        country_search.setOnQueryTextListener(object);



        List<CountryItem> countries = getCountries();


    }



    private void setAdapter(List<CountryX> countries) {
        myadapter = new MyAdapter(countries);
        recycler_view.setAdapter(myadapter);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        recycler_view.setHasFixedSize(true);

    }


    private List<CountryItem> getCountries() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.covid19api.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CovApi myCovApi = retrofit.create(CovApi.class);

        Call<CountrySummary> call = myCovApi.getSummary();

        call.enqueue(new Callback<CountrySummary>() {

            @Override
            public void onResponse(Call<CountrySummary> call, Response<CountrySummary> response) {
                if (!response.isSuccessful()) {
                    mainTextView.setText("Codigo" + response.code());
                    return;
                }

                CountrySummary mySummary = response.body();
                List<CountryX> myCountryList = mySummary.getCountries();
                setAdapter(myCountryList);

//
            }

            @Override
            public void onFailure(Call<CountrySummary> call, Throwable t) {
                mainTextView.setText(t.getMessage());
            }
        });

        return null;
    }


}
