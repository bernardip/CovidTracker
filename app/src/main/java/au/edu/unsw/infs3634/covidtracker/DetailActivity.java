package au.edu.unsw.infs3634.covidtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {
    public static final String INTENT_MESSAGE = "au.edu.unsw.infs3634.covidtracker.intent_message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //Get the intent
        Intent intent = getIntent();
        //Retrieve correct country from intent
        final Country correctCountry = (Country) intent.getSerializableExtra("country");

        //Country name text view
        TextView tvCountryName = findViewById(R.id.tvCountryName);
        //Retrieve country name from correctCountry
        String title = correctCountry.getCountry();
        tvCountryName.setText(title);

        //New cases text view
        TextView tvNewCases = findViewById(R.id.tvNewCasesContent);
        //Retrieve new cases from correctCountry
        String newCases = Integer.toString(correctCountry.getNewConfirmed());
        tvNewCases.setText(newCases);

        //Total cases text view
        TextView tvTotalCases = findViewById(R.id.tvTotalCasesContent);
        //Retrieve total cases
        String totalCases = Integer.toString(correctCountry.getTotalConfirmed());
        tvTotalCases.setText(totalCases);

        //New deaths text view
        TextView tvNewDeaths = findViewById(R.id.tvNewDeathsContent);
        //Retrieve new deaths
        String newDeaths = Integer.toString(correctCountry.getNewDeaths());
        tvNewDeaths.setText(newDeaths);

        //Total deaths text view
        TextView tvTotalDeaths = findViewById(R.id.tvTotalDeathsContent);
        //Retrieve total deaths
        String totalDeaths = Integer.toString(correctCountry.getTotalDeaths());
        tvTotalDeaths.setText(totalDeaths);

        //New recovered text view
        TextView tvNewRecovered = findViewById(R.id.tvNewRecoveredContent);
        //Retrieve new recovered
        String newRecovered = Integer.toString(correctCountry.getNewRecovered());
        tvNewRecovered.setText(newRecovered);

        //Total recovered text view
        TextView tvTotalRecovered = findViewById(R.id.tvTotalRecoveredContent);
        //Retrieve total recovered
        String totalRecovered = Integer.toString(correctCountry.getTotalRecovered());
        tvTotalRecovered.setText(totalRecovered);

        //Find search button
        ImageButton btnSearchIcon = findViewById(R.id.btnSearchIcon);
        //Search button functionality
        btnSearchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchUrl = "https://google.com/search?q=covid+" + correctCountry.getCountry();

                //Declare an intent object
                //Search on google by using intent object
                Intent webintent = new Intent(Intent.ACTION_VIEW, Uri.parse(searchUrl));
                startActivity(webintent);
            }
        });
    }
}