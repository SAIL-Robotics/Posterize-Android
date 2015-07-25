package com.sailrobotics.posterize;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;


public class PosterSummaryActivity extends Activity {

    TextView imagePath,numberOfSheets,orientationType;
    String pdfPath,orientation;
    String sheetsCount;
    Button openPdfButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poster_summary);

        imagePath = (TextView)findViewById(R.id.filePath);
        numberOfSheets = (TextView)findViewById(R.id.totalSheets);
        orientationType = (TextView)findViewById(R.id.imageOrientation);
        openPdfButton = (Button)findViewById(R.id.generatePdfButton);

        Intent intent = getIntent();
        pdfPath = intent.getStringExtra("pdfPath");
        sheetsCount = intent.getStringExtra("sheets");
        orientation = intent.getStringExtra("orientation");

        numberOfSheets.setText(sheetsCount);
        imagePath.setText(pdfPath);
        orientationType.setText(orientation);

        openPdfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(pdfPath);
                Intent target = new Intent(Intent.ACTION_VIEW);
                target.setDataAndType(Uri.fromFile(file),"application/pdf");
                target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                Intent intent = Intent.createChooser(target, "Open File");
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    // Instruct the user to install a PDF reader here, or something
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_poster_summary, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
