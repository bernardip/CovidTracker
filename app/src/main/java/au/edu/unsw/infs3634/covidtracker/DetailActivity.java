package au.edu.unsw.infs3634.covidtracker;

import androidx.appcompat.app.AppCompatActivity;

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
        String countryCode = intent.getStringExtra(INTENT_MESSAGE);

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

                for (final Country country : countries) {
                    if (country.getCountryCode().equals(countryCode)) {
                        DecimalFormat df = new DecimalFormat("#,###,###,###");
                        setTitle(country.getCountryCode());
                        Glide.with(mFlag).load("https://www.countryflags.io/" + country.getCountryCode() + "/shiny/64.png").into(mFlag);
                        mCountry.setText(country.getCountry());
                        mNewCases.setText(df.format(country.getNewConfirmed()));
                        mTotalCases.setText(df.format(country.getTotalConfirmed()));
                        mNewDeaths.setText(df.format(country.getNewDeaths()));
                        mTotalDeaths.setText(df.format(country.getTotalDeaths()));
                        mNewRecovered.setText(df.format(country.getNewRecovered()));
                        mTotalRecovered.setText(df.format(country.getTotalRecovered()));
                        mSearch.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                searchCountry(country.getCountry());
                            }

                            private void searchCountry(String country) {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=covid " + country));
                                startActivity(intent);
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Log.d(TAG, "onFailure: API call failed.");
            }
        });


    }
}