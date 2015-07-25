package com.sailrobotics.posterize;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

/**
 * Created by arjuns on 6/28/2015.
 */
public class PosterMeasurementsActivity extends Activity {
    ImageButton previousActivityButton, nextActivityButton;
    Intent nextIntent, previousIntent;
    String path;
    Double bitmapWidth, bitmapHeight;
    Double posterWidth, posterHeight;
    EditText width, height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poster_measurements);

        path = getIntent().getStringExtra("filePath");
        bitmapWidth = Double.parseDouble(getIntent().getStringExtra("bitmapWidth"));
        bitmapHeight = Double.parseDouble(getIntent().getStringExtra("bitmapHeight"));


        previousActivityButton = (ImageButton)findViewById(R.id.previousButton);
        nextActivityButton = (ImageButton)findViewById(R.id.nextButton);
        Button aspectWidth = (Button) findViewById(R.id.aspectWidth);
        Button aspectHeight = (Button) findViewById(R.id.aspectHeight);
        width = (EditText)findViewById(R.id.editWidth);
        height = (EditText)findViewById(R.id.editHeight);

        aspectWidth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                posterWidth = Double.parseDouble(width.getText().toString());
                posterHeight = aspectRatio(bitmapWidth, bitmapHeight, posterWidth, true);
                posterHeight = (double) Math.round(posterHeight * 100) / 100;   //two decimal places
                height.setText(posterHeight.toString());
             }
        });

        aspectHeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                posterHeight  = Double.parseDouble(height.getText().toString());
                posterWidth= aspectRatio(bitmapWidth, bitmapHeight, posterHeight, false);
                posterWidth = (double) Math.round(posterWidth * 100) / 100;     //two decimal places
                width.setText(posterWidth.toString());
            }
        });

        previousActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousIntent = new Intent(PosterMeasurementsActivity.this, CropImageActivity.class);
                startActivity(previousIntent);
            }
        });

        nextActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextIntent = new Intent(PosterMeasurementsActivity.this, PosterizeActivity.class);
                nextIntent.putExtra("filePath", path);
                nextIntent.putExtra("bitmapWidth", posterWidth + "");
                nextIntent.putExtra("bitmapHeight", posterHeight + "");
                startActivity(nextIntent);
            }
        });
    }

    double aspectRatio(double oldWidth, double oldHeight, double newSize, boolean isWidth)
    {
        double factor;
        if(isWidth == true)
        {
            factor = newSize/oldWidth;
            return factor * oldHeight;
        }
        factor = newSize/oldHeight;
        return factor * oldWidth;
    }

}
