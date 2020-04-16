package com.mariokirven.covidscore;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import Interfaz.CovApi;
import Model.CountryItem;
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


        //List<CountryItem> countries = getCountries();
        getCountries();


    }


    private void setAdapter(List<CountryItem> countries) {
        myadapter = new MyAdapter(countries);
        recycler_view.setAdapter(myadapter);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        recycler_view.setHasFixedSize(true);

    }


    private void getCountries() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://corona.lmao.ninja/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CovApi myCovApi = retrofit.create(CovApi.class);

        Call<ArrayList<CountryItem>> call = myCovApi.getCountriesSortetByCaseDes();
        Log.e("myCode", "WE are Before the Onresponse ");

        call.enqueue(new Callback<ArrayList<CountryItem>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<ArrayList<CountryItem>> call, Response<ArrayList<CountryItem>> response) {
                ArrayList<CountryItem> myCountryArray = response.body();

                setAdapter(myCountryArray);
                Log.e("myCode", "WE are Inside OnResponse " + response.code());
            }

            @Override
            public void onFailure(Call<ArrayList<CountryItem>> call, Throwable t) {
                Log.e("myCode", "WE are Inside Onfailure " + t.getMessage());

            }
        });


    }


}
