package com.sailrobotics.posterize;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by arjuns on 6/28/2015.
 */
public class PosterMeasurementsActivity extends Activity {
    ImageButton previousActivityButton, nextActivityButton;
    Intent nextIntent, previousIntent;
    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poster_measurements);

        path = getIntent().getStringExtra("filePath");
        Log.e("post", path);


        previousActivityButton = (ImageButton)findViewById(R.id.previousButton);
        nextActivityButton = (ImageButton)findViewById(R.id.nextButton);

        previousActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousIntent = new Intent(PosterMeasurementsActivity.this,CropImageActivity.class);
                startActivity(previousIntent);
            }
        });

        nextActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextIntent = new Intent(PosterMeasurementsActivity.this, PosterizeActivity.class);
                nextIntent.putExtra("filePath", path);
                startActivity(nextIntent);
            }
        });
    }
}
