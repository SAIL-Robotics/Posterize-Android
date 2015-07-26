package com.sailrobotics.posterize;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import java.io.File;

/**
 * Created by asanthan on 7/25/15.
 */
public class MarkReferenceActivity extends Activity {

    FrameLayout plotImageView;
    Button doneButton;
    PlotPoint plot;
    SharedPreferences mySharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_reference);

        plotImageView = (FrameLayout)findViewById(R.id.posterImageView);
        doneButton = (Button)findViewById(R.id.proceedButton);
        mySharedpreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

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
                    return;
                }
                double distance = plot.calculateDistance(known);

                if(distance > 0) {
                    SharedPreferences.Editor editor = mySharedpreferences.edit();
                    editor.putString("Distance", distance + " ");
                    editor.commit();
                    Log.i("DISTANCE", " " + distance);
                }
                finish();
            }
        });

    }


}
