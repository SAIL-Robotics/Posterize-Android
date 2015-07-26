package com.sailrobotics.posterize;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by asanthan on 7/25/15.
 */
public class MarkReferenceActivity extends Activity {

    ImageView plotImageView;
    Button doneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_reference);

        plotImageView = (ImageView)findViewById(R.id.markImageView);
        doneButton = (Button)findViewById(R.id.proceedButton);

        Bundle extras = getIntent().getExtras();

        if(getIntent().hasExtra("ImagePATH"))
        {
            try {
                String imagePath = extras.getString("ImagePATH");
                Log.e("Image Uri", imagePath);
                BitmapFactory.Options myBitmapOptions = new BitmapFactory.Options();
                myBitmapOptions.inSampleSize = 2;
                Bitmap imageBitmap = BitmapFactory.decodeFile(imagePath, myBitmapOptions);
                plotImageView.setImageBitmap(imageBitmap);
            }
            catch(Exception i){
                Log.i("ERROR","Can't find image");
            }
        }

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


}
