package com.sailrobotics.posterize;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by arjuns on 6/28/2015.
 */
public class ApplyEffectsActivity extends Activity {
    ImageButton previousActivityButton, nextActivityButton;
    ImageButton noneImageButton,invertImageButton,sepiaImageButton,greyscaleImageButton;
    ImageButton boostRedImageButton,boostGreenImageButton,boostBlueImageButton;
    Intent nextIntent, previousIntent;
    ImageView baseImageView;
    Bitmap baseBitmap, thumbnailBitmap;
    Bitmap editedBitmap;
    ArrayList<Bitmap> bitmapArray = new ArrayList<Bitmap>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_effects);

        nextActivityButton = (ImageButton)findViewById(R.id.imageButtonRightEffect);
        invertImageButton = (ImageButton)findViewById(R.id.invertIB);
        sepiaImageButton = (ImageButton)findViewById(R.id.sepiaIB);
        noneImageButton = (ImageButton)findViewById(R.id.noEffectIB);
        greyscaleImageButton = (ImageButton)findViewById(R.id.greyScaleIB);
        boostRedImageButton = (ImageButton)findViewById(R.id.boostRedIB);
        boostGreenImageButton = (ImageButton)findViewById(R.id.boostGreenIB);
        boostBlueImageButton = (ImageButton)findViewById(R.id.boostBlueIB);
        baseImageView = (ImageView)findViewById(R.id.effectsImage);

        String path = getIntent().getStringExtra("filePath");
        Log.e("post", path);

        baseImageView.setImageURI(Uri.parse(path));
        baseBitmap = BitmapFactory.decodeFile(path);
        thumbnailBitmap = baseBitmap;

        BitmapFactory.Options myThumbnailOptions = new BitmapFactory.Options();
        myThumbnailOptions.inSampleSize = 7;
        Bitmap thumbnailBitmap = BitmapFactory.decodeFile(path, myThumbnailOptions);

        noneImageButton.setImageBitmap(baseBitmap);
        ApplyEffectsToButon effect = new ApplyEffectsToButon();
        effect.execute();

        noneImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseImageView.setImageBitmap(baseBitmap);
            }
        });

        invertImageButton.setOnClickListener(new View.OnClickListener() {
            ImageEffects addEffect = new ImageEffects();
            @Override
            public void onClick(View v) {
                editedBitmap = addEffect.doInvert(baseBitmap);
                baseImageView.setImageBitmap(editedBitmap);
            }
        });

        sepiaImageButton.setOnClickListener(new View.OnClickListener() {
            ImageEffects addEffect = new ImageEffects();

            @Override
            public void onClick(View v) {
                editedBitmap = addEffect.doSepia(baseBitmap);
                baseImageView.setImageBitmap(editedBitmap);
            }
        });

        greyscaleImageButton.setOnClickListener(new View.OnClickListener() {
            ImageEffects addEffect = new ImageEffects();

            @Override
            public void onClick(View v) {
                editedBitmap = addEffect.doGreyscale(baseBitmap);
                baseImageView.setImageBitmap(editedBitmap);
            }
        });

        boostRedImageButton.setOnClickListener(new View.OnClickListener() {
            ImageEffects addEffect = new ImageEffects();

            @Override
            public void onClick(View v) {
                editedBitmap = addEffect.doColourBoost(baseBitmap,1,5);
                baseImageView.setImageBitmap(editedBitmap);
            }
        });

        boostGreenImageButton.setOnClickListener(new View.OnClickListener() {
            ImageEffects addEffect = new ImageEffects();

            @Override
            public void onClick(View v) {
                editedBitmap = addEffect.doColourBoost(baseBitmap,2,1);
                baseImageView.setImageBitmap(editedBitmap);
            }
        });

        boostBlueImageButton.setOnClickListener(new View.OnClickListener() {
            ImageEffects addEffect = new ImageEffects();

            @Override
            public void onClick(View v) {
                editedBitmap = addEffect.doColourBoost(baseBitmap,3,2);
                baseImageView.setImageBitmap(editedBitmap);
            }
        });

        nextActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = savePictureAfterEffects();

                nextIntent = new Intent(ApplyEffectsActivity.this, PosterMeasurementsActivity.class);
                nextIntent.putExtra("filePath", file.getPath());
                if(editedBitmap == null)
                {
                    nextIntent.putExtra("bitmapWidth", baseBitmap.getWidth() + "");
                    nextIntent.putExtra("bitmapHeight", baseBitmap.getHeight() + "");
                }
                else
                {
                    nextIntent.putExtra("bitmapWidth", editedBitmap.getWidth() + "");
                    nextIntent.putExtra("bitmapHeight", editedBitmap.getHeight() + "");
                }

                startActivity(nextIntent);
            }
        });
    }

    /** Create a File for saving an image*/
    private File savePictureAfterEffects(){

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
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "afterEffect.png");

        try {
            OutputStream stream = new FileOutputStream(mediaFile);
            /* Write bitmap to file using JPEG or PNG and 80% quality hint for JPEG. */
            if(editedBitmap == null) {
                baseBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            }
            else {
                editedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            }
            stream.close();
        }
        catch (Exception e)
        {

        }
        return mediaFile;
    }

    private class ApplyEffectsToButon extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        //declare other objects as per your need
        @Override
        protected void onPreExecute()
        {
            progressDialog= ProgressDialog.show(ApplyEffectsActivity.this, "Posterize","Loading Page...", true);

            //do initialization of required objects objects here
        };
        @Override
        protected Void doInBackground(Void... params)
        {
            ImageEffects horizontalScrollObject = new ImageEffects();

            bitmapArray.add(horizontalScrollObject.doInvert(thumbnailBitmap));
            bitmapArray.add(horizontalScrollObject.doSepia(thumbnailBitmap));
            bitmapArray.add(horizontalScrollObject.doGreyscale(thumbnailBitmap));
            bitmapArray.add(horizontalScrollObject.doColourBoost(thumbnailBitmap, 1, 5));
            bitmapArray.add(horizontalScrollObject.doColourBoost(thumbnailBitmap, 2, 1));
            bitmapArray.add(horizontalScrollObject.doColourBoost(thumbnailBitmap, 3, 2));

            return null;
        }
        @Override
        protected void onPostExecute(Void result)
        {
            invertImageButton.setImageBitmap(bitmapArray.get(0));
            sepiaImageButton.setImageBitmap(bitmapArray.get(1));
            greyscaleImageButton.setImageBitmap(bitmapArray.get(2));
            boostRedImageButton.setImageBitmap(bitmapArray.get(3));
            boostGreenImageButton.setImageBitmap(bitmapArray.get(4));
            boostBlueImageButton.setImageBitmap(bitmapArray.get(5));
            super.onPostExecute(result);
            progressDialog.dismiss();
        };
    }

}
