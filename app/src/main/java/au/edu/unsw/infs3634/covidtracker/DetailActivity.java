package au.edu.unsw.infs3634.covidtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {
    public static final String INTENT_MESSAGE = "au.edu.unsw.infs3634.covidtracker.intent_message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //Get the intent
        Intent intent = getIntent();

        //Retrieve message from intent
        String message = intent.getStringExtra(INTENT_MESSAGE);

        //Find the TextView object
        TextView tv = findViewById(R.id.tvDetailMessage);

        //Add text to the TextView object
        tv.setText(message);

        //Find button from View
        Button btn = findViewById(R.id.btnPlayVideo);

        //Implement setOnClickListener
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Declare an intent object
                //Play a YouTube video by using intent object
                Intent webintent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtube.com.au"));
                startActivity(webintent);



            }
        });
    }
}