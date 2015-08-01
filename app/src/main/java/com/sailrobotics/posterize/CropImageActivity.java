package com.sailrobotics.posterize;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by arjuns on 6/28/2015.
 */
public class CropImageActivity extends Activity {
    ImageButton previousActivityButton, nextActivityButton;
    Intent nextIntent;
    CropImageView cropImageView;
    Bitmap imageBitmap;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_image);
        nextActivityButton = (ImageButton)findViewById(R.id.nextButtonCrop);

        cropImageView = (CropImageView) findViewById(R.id.imgView);
        cropImageView.setAspectRatio(5, 10);
        cropImageView.setFixedAspectRatio(false);
        cropImageView.setGuidelines(1);
        cropImageView.setCropShape(CropImageView.CropShape.RECTANGLE);
        cropImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

        Button cropImage = (Button) findViewById(R.id.optimizeButton);
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
                }
                catch (Exception e)
                {
                    Toast.makeText(getBaseContext(), "Image too small to crop!", Toast.LENGTH_SHORT).show();
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
                imageBitmap = BitmapFactory.decodeFile(imagePath, myBitmapOptions);
                cropImageView.setImageBitmap(imageBitmap);
                bitmap = cropImageView.getCroppedImage();
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

                Log.e("Post", "asd");
                File file = savePictureAfterCrop();

                nextIntent = new Intent(CropImageActivity.this,ApplyEffectsActivity.class);
                nextIntent.putExtra("filePath", file.getPath());
                Log.e("CutImage", file.getPath());
                startActivity(nextIntent);
            }
        });
    }

    /** Create a File for saving an image*/
    private File savePictureAfterCrop(){

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Posterize");

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("Posterize", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "afterCrop.png");


        try {
            OutputStream stream = new FileOutputStream(mediaFile);
            /* Write bitmap to file using JPEG or PNG and 80% quality hint for JPEG. */
            if(bitmap == null)
            {
                bitmap = imageBitmap;
            }
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();
        }
        catch (Exception e)
        {

        }
        return mediaFile;
    }
    @Override
    public void onResume() {
        super.onResume();
        Bundle extras = getIntent().getExtras();
    }

    @Override
    public void onBackPressed() {
        Intent homescreenIntent = new Intent(CropImageActivity.this,HomeScreenActivity.class);
        startActivity(homescreenIntent);
    }

}
