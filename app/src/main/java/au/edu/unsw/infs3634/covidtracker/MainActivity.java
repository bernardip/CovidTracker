package au.edu.unsw.infs3634.covidtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private String TAG = "HelloThere";
    private int age = 21;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate Line 14: Age = " + age);
        setContentView(R.layout.activity_main);
    }
}