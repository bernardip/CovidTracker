package au.edu.unsw.infs3634.covidtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private RecyclerView mRecyclerView;
    private CountryAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private CountryDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //instantiate recyclerview (empty place holders)
        mRecyclerView = findViewById(R.id.rvList);
        mRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        //make a listener to handle click action
        CountryAdapter.Listener listener = new CountryAdapter.Listener() {
            @Override
            public void onClick(View view, String countryCode) {
                launchDetailActivity(countryCode);
            }
        };

        //instantiate adapter and set to recycler view

        //mAdapter = new CountryAdapter(Country.getCountries(), listener);
        //change mAdapter to get stuff from gson instead

        //Gson gson = new Gson();
        //Response response = gson.fromJson(Response.json, Response.class);

        //Implement Retrofit instead of the above - calls json using html instead of hardcoded json in Response class

        mAdapter = new CountryAdapter(new ArrayList<Country>(), listener);
        mRecyclerView.setAdapter(mAdapter);

        //Create a CountryDatabase Room database
        mDb = Room.databaseBuilder(getApplicationContext(), CountryDatabase.class, "country-database").build();

        //On MainActivity start up, get list of all countries from the database and set into adapter
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                //set the adapter with a list of countries from the database
                mAdapter.setData(mDb.countryDao().getCountries());

                //sort the RecyclerView list
                mAdapter.sort(2);
            }
        });

        //implement a retrofit instance to make API call
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.covid19api.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //implement a service interface
        CovidService service = retrofit.create(CovidService.class);

        Call<Response> responseCall = service.getResponse();

        responseCall.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                Log.d(TAG, "onResponse: API call succeeded!");
                // Get list of countries from body of response
                List<Country> countries = response.body().getCountries();
                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        //Delete all countries from country entity
                        mDb.countryDao().deleteAll(mDb.countryDao().getCountries().toArray(new Country[0]));
                        //Insert the new list from API call into country entity
                        mDb.countryDao().insertAll(countries.toArray(new Country[0]));

                    }
                });
                mAdapter.setData(countries);
                mAdapter.sort(2);

            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Log.d(TAG, "onFailure: API call failed.");
            }
        });



    }

    private void launchDetailActivity(String message) {
        //Declare an intent to launch DetailActivity
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);

        //put message in intent
        intent.putExtra(DetailActivity.INTENT_MESSAGE, message);

        startActivity(intent);
    }

    //Instantiate menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }

    //React to interaction
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.sort_new:
                mAdapter.sort(1);
                return true;
            case R.id.sort_total:

                mAdapter.sort(2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }
}