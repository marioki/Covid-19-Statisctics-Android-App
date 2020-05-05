package com.mariokirven.covidscore;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import Interfaz.CovApi;
import model.CountryItem;
import model.GlobalStats;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {
    private RecyclerView recycler_view;
    private MyAdapter myadapter;
    private SwipeRefreshLayout swipeContainer;
    private TextView global_confirmed, global_deaths, global_recovered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        global_confirmed = findViewById(R.id.global_confirmed_info);
        global_deaths = findViewById(R.id.global_deaths_info);
        global_recovered = findViewById(R.id.global_recovered_info);

        Toolbar myToolBar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolBar);


        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchTimelineAsync();
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
                if (myadapter != null) {
                    myadapter.getFilter().filter(newText);
                }

                return false;
            }
        };

        country_search.setOnQueryTextListener(object);


        //List<CountryItem> countries = getCountries();
        getCountries();
        getGlobalStats();


    }

    private void getGlobalStats() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://disease.sh/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CovApi myCovApi = retrofit.create(CovApi.class);

        Call<GlobalStats> call = myCovApi.getGlobalStatistics();
        Log.e("myCode", "WE are Before the Onresponse ");

        call.enqueue(new Callback<GlobalStats>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(@NotNull Call<GlobalStats> call, @NotNull Response<GlobalStats> response) {
                GlobalStats myGlobalStats = response.body();
                assert myGlobalStats != null;
                String formattedCases = NumberFormat.getNumberInstance(Locale.getDefault()).format(myGlobalStats.getCases());
                String formattedDeaths = NumberFormat.getNumberInstance(Locale.getDefault()).format(myGlobalStats.getDeaths());
                String formattedRecovered = NumberFormat.getNumberInstance(Locale.getDefault()).format(myGlobalStats.getRecovered());
                global_confirmed.setText(formattedCases);
                global_deaths.setText(formattedDeaths);
                global_recovered.setText(formattedRecovered);

                Log.e("myCode", "WE are Inside Global OnResponse " + response.code());
            }

            @Override
            public void onFailure(@NotNull Call<GlobalStats> call, @NotNull Throwable t) {
                Log.e("myCode", "WE are Inside Global Onfailure " + t.getMessage());

            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "https://galaxy.store/covid19wo");
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
                return true;

//            case R.id.item2:
//                Toast.makeText(this, "Item 2 selected", Toast.LENGTH_SHORT).show();
//                return true;
//
//            case R.id.item3:
//                Toast.makeText(this, "Item 3 selected", Toast.LENGTH_SHORT).show();
//                return true;
//
//            case R.id.subItem1:
//                Toast.makeText(this, "Sub item 1  selected", Toast.LENGTH_SHORT).show();
//                return true;
//
//            case R.id.subItem2:
//                Toast.makeText(this, "Sub item 2  selected", Toast.LENGTH_SHORT).show();
//                return true;
            default:
                return super.onOptionsItemSelected(item);


        }
    }

    private void fetchTimelineAsync() {
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
                .baseUrl("https://disease.sh/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CovApi myCovApi = retrofit.create(CovApi.class);

        Call<ArrayList<CountryItem>> call = myCovApi.getCountriesSortedByCaseDes();
        Log.e("myCode", "WE are Before the Onresponse ");

        call.enqueue(new Callback<ArrayList<CountryItem>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(@NotNull Call<ArrayList<CountryItem>> call, @NotNull Response<ArrayList<CountryItem>> response) {
                ArrayList<CountryItem> myCountryArray = response.body();

                // Remember to CLEAR OUT old items before appending in the new ones
                if (myadapter != null) {
                    myadapter.clear();
                }

                // ...the data has come back, add new items to your adapter...
                setAdapter(myCountryArray);

                // Now we call setRefreshing(false) to signal refresh has finished
                swipeContainer.setRefreshing(false);

                Log.e("myCode", "WE are Inside OnResponse " + response.code());
            }

            @Override
            public void onFailure(@NotNull Call<ArrayList<CountryItem>> call, @NotNull Throwable t) {
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
            public void onResponse(@NotNull Call<ArrayList<CountryItem>> call, @NotNull Response<ArrayList<CountryItem>> response) {
                ArrayList<CountryItem> myCountryArray = response.body();


                setAdapter(myCountryArray);
                Log.e("myCode", "WE are Inside OnResponse " + response.code());
            }

            @Override
            public void onFailure(@NotNull Call<ArrayList<CountryItem>> call, @NotNull Throwable t) {
                Log.e("myCode", "WE are Inside Onfailure " + t.getMessage());

            }
        });


    }


}
