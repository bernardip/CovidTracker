package au.edu.unsw.infs3634.covidtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailActivity extends AppCompatActivity {
    public static final String INTENT_MESSAGE = "au.edu.unsw.infs3634.covidtracker.intent_message";
    private static final String TAG = "DetailActivity";

    private TextView mCountry;
    private TextView mNewCases;
    private TextView mTotalCases;
    private TextView mNewDeaths;
    private TextView mTotalDeaths;
    private TextView mNewRecovered;
    private TextView mTotalRecovered;
    private ImageView mFlag;
    private ImageView mSearch;
    private CountryDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mFlag = findViewById(R.id.ivFlag);
        mCountry = findViewById(R.id.tvCountryName);
        mNewCases = findViewById(R.id.tvNewCasesContent);
        mTotalCases = findViewById(R.id.tvTotalCasesContent);
        mNewDeaths = findViewById(R.id.tvNewDeathsContent);
        mTotalDeaths = findViewById(R.id.tvTotalDeathsContent);
        mNewRecovered = findViewById(R.id.tvNewRecoveredContent);
        mTotalRecovered = findViewById(R.id.tvTotalRecoveredContent);
        mSearch = findViewById(R.id.btnSearchIcon);

        Intent intent = getIntent();

        final String countryCode = intent.getStringExtra(INTENT_MESSAGE);

        Glide.with(mFlag)
                .load("https://www.countryflags.io/" + countryCode + "/shiny/64.png")
                .into(mFlag);

        mDb = Room.databaseBuilder(getApplicationContext(), CountryDatabase.class, "country-database").build();

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                //find the country with countryCode from database
                Country country = mDb.countryDao().getCountry(countryCode);

                DecimalFormat df = new DecimalFormat("#,###,###,###");

                mCountry.setText(country.getCountry());
                mNewCases.setText(df.format(country.getNewConfirmed()));
                mTotalCases.setText(df.format(country.getTotalConfirmed()));
                mNewDeaths.setText(df.format(country.getNewDeaths()));
                mTotalDeaths.setText(df.format(country.getTotalDeaths()));
                mNewRecovered.setText(df.format(country.getNewRecovered()));
                mTotalRecovered.setText(df.format(country.getTotalRecovered()));

                //make button
                mSearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        searchCountry(country.getCountry());
                    }

                });
            }
        });
    }

    private void searchCountry(String country) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=covid " + country));
        startActivity(intent);
    }
}