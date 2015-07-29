package com.sailrobotics.posterize;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

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
    Button takePictureButton, aspectWidth, aspectHeight;
    SharedPreferences mySharedpreferences;
    String distance;
    String posterHeightString;
    String posterWidthString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poster_measurements);

        path = getIntent().getStringExtra("filePath");
        bitmapWidth = Double.parseDouble(getIntent().getStringExtra("bitmapWidth"));
        bitmapHeight = Double.parseDouble(getIntent().getStringExtra("bitmapHeight"));


        previousActivityButton = (ImageButton) findViewById(R.id.previousButton);
        nextActivityButton = (ImageButton) findViewById(R.id.nextButton);
        aspectWidth = (Button) findViewById(R.id.aspectWidth);
        aspectHeight = (Button) findViewById(R.id.aspectHeight);
        takePictureButton = (Button) findViewById(R.id.invokeCameraButton);


        width = (EditText) findViewById(R.id.editWidth);
        height = (EditText) findViewById(R.id.editHeight);


        aspectWidth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                posterWidthString = width.getText().toString();
                if(posterWidthString.equals("")) {
                    Toast.makeText(getApplicationContext(),"Enter width",Toast.LENGTH_SHORT).show();
                }
                else {
                    posterWidth = Double.parseDouble(width.getText().toString());
                    posterHeight = aspectRatio(bitmapWidth, bitmapHeight, posterWidth, true);
                    posterHeight = (double) Math.round(posterHeight * 100) / 100;   //two decimal places
                    height.setText(posterHeight.toString());
                }

            }
        });

        aspectHeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                posterHeightString = height.getText().toString();
                if(posterHeightString.equals("")) {
                    Toast.makeText(getApplicationContext(),"Enter height",Toast.LENGTH_SHORT).show();
                }
                else {
                    posterHeight = Double.parseDouble(height.getText().toString());
                    posterWidth = aspectRatio(bitmapWidth, bitmapHeight, posterHeight, false);
                    posterWidth = (double) Math.round(posterWidth * 100) / 100;     //two decimal places
                    width.setText(posterWidth.toString());
                }
            }
        });

        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent invokeCameraIntent = new Intent(PosterMeasurementsActivity.this, TakeCameraMeasurement.class);
                startActivity(invokeCameraIntent);
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
                posterWidthString = width.getText().toString();
                posterHeightString = height.getText().toString();
                if (posterHeightString.equals("") && posterWidthString.equals("")) {
                    Toast.makeText(getApplicationContext(),"Enter poster dimensions",Toast.LENGTH_SHORT).show();
                }
                else {
                    posterWidth = Double.parseDouble(width.getText().toString());
                    posterHeight = Double.parseDouble(height.getText().toString());
                    nextIntent = new Intent(PosterMeasurementsActivity.this, PosterizeActivity.class);
                    nextIntent.putExtra("filePath", path);
                    nextIntent.putExtra("bitmapWidth", posterWidth + "");
                    nextIntent.putExtra("bitmapHeight", posterHeight + "");
                    startActivity(nextIntent);
                }
            }
        });
    }

    double aspectRatio(double oldWidth, double oldHeight, double newSize, boolean isWidth) {
        double factor;
        if (isWidth == true) {
            factor = newSize / oldWidth;
            return factor * oldHeight;
        }
        factor = newSize / oldHeight;
        return factor * oldWidth;
    }

    public void showAlertDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.setTitle("Set dimension as");
        dialog.setCancelable(true);
        // there are a lot of settings, for dialog, check them all out!
        // set up radiobutton
        final RadioButton radioWidth = (RadioButton) dialog.findViewById(R.id.radio_width);
        final RadioButton radioHeight = (RadioButton) dialog.findViewById(R.id.radio_height);
        Button dialogOk = (Button) dialog.findViewById(R.id.dialog_ok_button);

        dialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radioWidth.isChecked())
                {
                    double widthDouble= Math.round(Double.parseDouble(distance)*100)/100;
                    width.setText(widthDouble+ "");
                    posterHeight = aspectRatio(bitmapWidth, bitmapHeight, Double.parseDouble(distance), true);
                    double heightDouble= Math.round(posterHeight*100)/100;
                    height.setText(heightDouble+ "");
                    dialog.dismiss();
                }
                else if(radioHeight.isChecked())
                {
                    double heightDouble= Math.round(Double.parseDouble(distance)*100)/100;
                    height.setText(heightDouble+ "");
                    posterWidth = aspectRatio(bitmapWidth, bitmapHeight, Double.parseDouble(distance), false);
                    double widthDouble = Math.round(posterWidth*100)/100;
                    width.setText(widthDouble+"");
                    dialog.dismiss();
                }
            }
        });

        // now that the dialog is set up, it's time to show it
        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        mySharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);
        distance = mySharedpreferences.getString("Distance", "");
        //Toast.makeText(getApplicationContext(),""+distance, Toast.LENGTH_SHORT).show();
        if(distance.length()!=0)
        {
            showAlertDialog();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = mySharedpreferences.edit();
        editor.clear();
        editor.commit();
        posterWidth = 0.0;
        posterHeight = 0.0;
        posterWidthString = null;
        posterHeightString = null;
        width.setText("");
        height.setText("");
        width.isFocused();
    }
}