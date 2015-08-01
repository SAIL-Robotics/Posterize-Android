package com.sailrobotics.posterize;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

import java.text.DecimalFormat;

/**
 * Created by arjuns on 6/28/2015.
 */
public class PosterMeasurementsActivity extends Activity {
    ImageButton nextActivityButton;
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
    Button instructionButton;
    DecimalFormat dec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poster_measurements);

        dec = new DecimalFormat("#.##");

        path = getIntent().getStringExtra("filePath");
        bitmapWidth = Double.parseDouble(getIntent().getStringExtra("bitmapWidth"));
        bitmapHeight = Double.parseDouble(getIntent().getStringExtra("bitmapHeight"));

        nextActivityButton = (ImageButton) findViewById(R.id.nextButton);
        aspectWidth = (Button) findViewById(R.id.aspectWidth);
        aspectHeight = (Button) findViewById(R.id.aspectHeight);
        takePictureButton = (Button) findViewById(R.id.invokeCameraButton);
        instructionButton = (Button) findViewById(R.id.instructionButton);

        width = (EditText) findViewById(R.id.editWidth);
        height = (EditText) findViewById(R.id.editHeight);

        instructionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent instructionsIntent = new Intent(PosterMeasurementsActivity.this, InstructionActivity.class);
                startActivity(instructionsIntent);

            }
        });

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
                    posterHeight = Double.valueOf(dec.format(posterHeight));   //two decimal places
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
                    posterWidth = Double.valueOf(dec.format(posterWidth));     //two decimal places
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
                    double widthDouble= Double.valueOf(dec.format(Double.parseDouble(distance)));
                    width.setText(widthDouble+ "");
                    posterHeight = aspectRatio(bitmapWidth, bitmapHeight, Double.parseDouble(distance), true);
                    double heightDouble= Double.valueOf(dec.format(posterHeight));
                    height.setText(heightDouble+ "");
                    dialog.dismiss();
                }
                else if(radioHeight.isChecked())
                {
                    double heightDouble= Double.valueOf(dec.format(Double.parseDouble(distance)));
                    height.setText(heightDouble+ "");
                    posterWidth = aspectRatio(bitmapWidth, bitmapHeight, Double.parseDouble(distance), false);
                    double widthDouble = Double.valueOf(dec.format(posterWidth));
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
            Log.e("CutImage", distance);
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