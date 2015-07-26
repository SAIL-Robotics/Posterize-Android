package com.sailrobotics.posterize;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;

/**
 * Created by asanthan on 7/25/15.
 */
public class MarkReferenceActivity extends Activity {

    FrameLayout plotImageView;
    Button doneButton, resetButton, redoButton;
    ImageButton nextButton;
    PlotPoint plot;
    SharedPreferences mySharedpreferences;
    double distance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_reference);

        plotImageView = (FrameLayout)findViewById(R.id.posterImageView);
        doneButton = (Button)findViewById(R.id.proceedButton);
        resetButton = (Button)findViewById(R.id.reset);
        redoButton = (Button)findViewById(R.id.redo);
        nextButton = (ImageButton)findViewById(R.id.nextButton);
        //mySharedpreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        mySharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);

        Bundle extras = getIntent().getExtras();

        if(getIntent().hasExtra("ImagePATH"))
        {
            try {
                String imagePath = extras.getString("ImagePATH");
                Log.e("Image Uri", imagePath);
                File tmp = new File(imagePath);
                ImageSurface image = new ImageSurface(this, tmp);
                plotImageView.addView(image);
                plot = new PlotPoint(getBaseContext());
                plotImageView.addView(plot);
            }
            catch(Exception i){
                Log.i("ERROR","Can't find image");
            }
        }

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText knownLength = (EditText)findViewById(R.id.knownLength);
                double known = 0d;
                if(knownLength.getText().length() != 0)
                {
                    known = Double.parseDouble(knownLength.getText().toString());
                }
                else
                {
                    Log.e("post", "Fill the textbox with some value");
                    return;
                }
                distance = plot.calculateDistance(known);
                Toast.makeText(getApplication(), distance + "", Toast.LENGTH_SHORT).show();
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plot.resetCanvas();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (distance > 0) {
                    SharedPreferences.Editor editor = mySharedpreferences.edit();
                    editor.putString("Distance", distance + " ");
                    editor.apply();
                    Log.i("DISTANCE", " " + distance);
                } else {
                    Log.e("post", "Some error on placing points/distance calculation");
                    return;
                }
                finish();
            }
        });

        redoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent invokeCameraIntent = new Intent(MarkReferenceActivity.this,TakeCameraMeasurement.class);
                startActivity(invokeCameraIntent);
            }
        });

    }
}