package com.mariokirven.covidscore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

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

        Call<ArrayList<CountryItem>> call = myCovApi.getCountries();
        Log.e("myCode", "WE are Before the Onresponse ");

        call.enqueue(new Callback<ArrayList<CountryItem>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<ArrayList<CountryItem>> call, Response<ArrayList<CountryItem>> response) {
                ArrayList<CountryItem> myCountryArray = response.body();
//                List<CountryItem> myItems = new List<CountryItem>() {
//                    @Override
//                    public int size() {
//                        return 0;
//                    }
//
//                    @Override
//                    public boolean isEmpty() {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean contains(@Nullable Object o) {
//                        return false;
//                    }
//
//                    @NonNull
//                    @Override
//                    public Iterator<CountryItem> iterator() {
//                        return null;
//                    }
//
//                    @NonNull
//                    @Override
//                    public Object[] toArray() {
//                        return new Object[0];
//                    }
//
//                    @NonNull
//                    @Override
//                    public <T> T[] toArray(@NonNull T[] ts) {
//                        return null;
//                    }
//
//                    @Override
//                    public boolean add(CountryItem countryItem) {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean remove(@Nullable Object o) {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean containsAll(@NonNull Collection<?> collection) {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean addAll(@NonNull Collection<? extends CountryItem> collection) {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean addAll(int i, @NonNull Collection<? extends CountryItem> collection) {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean removeAll(@NonNull Collection<?> collection) {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean retainAll(@NonNull Collection<?> collection) {
//                        return false;
//                    }
//
//                    @Override
//                    public void clear() {
//
//                    }
//
//                    @Override
//                    public boolean equals(@Nullable Object o) {
//                        return false;
//                    }
//
//                    @Override
//                    public int hashCode() {
//                        return 0;
//                    }
//
//                    @Override
//                    public CountryItem get(int i) {
//                        return null;
//                    }
//
//                    @Override
//                    public CountryItem set(int i, CountryItem countryItem) {
//                        return null;
//                    }
//
//                    @Override
//                    public void add(int i, CountryItem countryItem) {
//
//                    }
//
//                    @Override
//                    public CountryItem remove(int i) {
//                        return null;
//                    }
//
//                    @Override
//                    public int indexOf(@Nullable Object o) {
//                        return 0;
//                    }
//
//                    @Override
//                    public int lastIndexOf(@Nullable Object o) {
//                        return 0;
//                    }
//
//                    @NonNull
//                    @Override
//                    public ListIterator<CountryItem> listIterator() {
//                        return null;
//                    }
//
//                    @NonNull
//                    @Override
//                    public ListIterator<CountryItem> listIterator(int i) {
//                        return null;
//                    }
//
//                    @NonNull
//                    @Override
//                    public List<CountryItem> subList(int i, int i1) {
//                        return null;
//                    }
//                };
//                myCountryArray.forEach(countryItem -> myItems.add(countryItem));
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
