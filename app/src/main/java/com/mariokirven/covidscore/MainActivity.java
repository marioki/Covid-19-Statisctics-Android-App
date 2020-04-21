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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
    private RecyclerView recycler_view;
    private MyAdapter myadapter;
    private SwipeRefreshLayout swipeContainer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Lookup the swipe container view
         swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchTimelineAsync(0);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);







        recycler_view = findViewById(R.id.recycler_view);

        SearchView country_search = findViewById(R.id.searchView);
        SearchView.OnQueryTextListener object = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (myadapter != null){
                    myadapter.getFilter().filter(newText);
                }

                return false;
            }
        };

        country_search.setOnQueryTextListener(object);


        //List<CountryItem> countries = getCountries();
        getCountries();


    }

    private void fetchTimelineAsync(int i) {
        getCountriesRefresh();
    }


    private void setAdapter(List<CountryItem> countries) {
        myadapter = new MyAdapter(countries);
        recycler_view.setAdapter(myadapter);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        recycler_view.setHasFixedSize(true);

    }



    private void getCountriesRefresh() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://corona.lmao.ninja/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CovApi myCovApi = retrofit.create(CovApi.class);

        Call<ArrayList<CountryItem>> call = myCovApi.getCountriesSortedByCaseDes();
        Log.e("myCode", "WE are Before the Onresponse ");

        call.enqueue(new Callback<ArrayList<CountryItem>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<ArrayList<CountryItem>> call, Response<ArrayList<CountryItem>> response) {
                ArrayList<CountryItem> myCountryArray = response.body();

                // Remember to CLEAR OUT old items before appending in the new ones
                if (myadapter != null){
                    myadapter.clear();
                }

                // ...the data has come back, add new items to your adapter...
                setAdapter(myCountryArray);

                // Now we call setRefreshing(false) to signal refresh has finished
                swipeContainer.setRefreshing(false);

                Log.e("myCode", "WE are Inside OnResponse " + response.code());
            }

            @Override
            public void onFailure(Call<ArrayList<CountryItem>> call, Throwable t) {
                Log.e("myCode", "WE are Inside Onfailure " + t.getMessage());

            }
        });


    }





    private void getCountries() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://corona.lmao.ninja/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CovApi myCovApi = retrofit.create(CovApi.class);

        Call<ArrayList<CountryItem>> call = myCovApi.getCountriesSortedByCaseDes();
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
