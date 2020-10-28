package au.edu.unsw.infs3634.covidtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private CountryAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

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

        Gson gson = new Gson();
        Response response = gson.fromJson(Response.json, Response.class);
        mAdapter = new CountryAdapter(response.getCountries(), listener);
        mRecyclerView.setAdapter(mAdapter);

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