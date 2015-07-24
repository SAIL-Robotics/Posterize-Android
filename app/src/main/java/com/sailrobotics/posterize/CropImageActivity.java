package com.sailrobotics.posterize;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.theartofdev.edmodo.cropper.CropImageView;

/**
 * Created by arjuns on 6/28/2015.
 */
public class CropImageActivity extends Activity {
    ImageButton previousActivityButton, nextActivityButton;
    Intent nextIntent;
    CropImageView cropImageView;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_image);

        previousActivityButton = (ImageButton)findViewById(R.id.previousButtonOne);
        nextActivityButton = (ImageButton)findViewById(R.id.nextButtonOne);

        cropImageView = (CropImageView) findViewById(R.id.imgView);
        cropImageView.setAspectRatio(5, 10);
        cropImageView.setFixedAspectRatio(false);
        cropImageView.setGuidelines(1);
        cropImageView.setCropShape(CropImageView.CropShape.RECTANGLE);
        cropImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

        Button cutImg = (Button) findViewById(R.id.button);

        Button cropImage = (Button) findViewById(R.id.Button_crop);
        cropImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                try
                {
                    bitmap = cropImageView.getCroppedImage();
                    cropImageView.setImageBitmap(bitmap);

                    double imgDensity = bitmap.getDensity();
                    double heightCM = (bitmap.getHeight()) / imgDensity;
                    double widthCM = (bitmap.getWidth()) / imgDensity;

                    Log.e("crop after - pixel", bitmap.getWidth() + " " + bitmap.getHeight());
                    Log.e("crop after - cm", heightCM + " " + widthCM);
                    Log.e("crop - density", bitmap.getDensity() + "");

                   /* bitmap.setDensity(10);
                    imgDensity = bitmap.getDensity();
                    heightCM = (bitmap.getHeight()) / imgDensity;
                    widthCM = (bitmap.getWidth()) / imgDensity;

                    Log.e("1 crop after - pixel", bitmap.getHeight() + " " + bitmap.getWidth());
                    Log.e("1 crop after - cm", heightCM + " " + widthCM);
                    Log.e("1 crop  - density", bitmap.getDensity() + "");*/
                    //size.setText(bitmap.getWidth() + " " + bitmap.getHeight());
                }
                catch (Exception e)
                {
                    Log.e("crop after", "Too small!!");
                }

            }
        });

        Bundle extras = getIntent().getExtras();

        if(getIntent().hasExtra("ImagePATH"))
        {
            try {
                String imagePath = extras.getString("ImagePATH");
                Log.e("Image Uri", imagePath);
                BitmapFactory.Options myBitmapOptions = new BitmapFactory.Options();
                myBitmapOptions.inSampleSize = 2;
                Bitmap imageBitmap = BitmapFactory.decodeFile(imagePath, myBitmapOptions);
                cropImageView.setImageBitmap(imageBitmap);
                Log.e("crop before", bitmap.getHeight() + " " + bitmap.getWidth());
                Log.e("crop before - density", bitmap.getDensity() + "");
            }
            catch(Exception i){
                Log.i("ERROR","Can't find image");
            }
        }

        nextActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nextIntent = new Intent(CropImageActivity.this,PosterMeasurementsActivity.class);
                startActivity(nextIntent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
