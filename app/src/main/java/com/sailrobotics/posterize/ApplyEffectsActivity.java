package com.sailrobotics.posterize;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
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
    Bitmap baseBitmap;
    Bitmap editedBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_effects);

        previousActivityButton = (ImageButton)findViewById(R.id.imageButtonLeftEffect);
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

        /*previousActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousIntent = new Intent(ApplyEffectsActivity.this, .class);
                startActivity(previousIntent);
            }
        });*/

        nextActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = savePictureAfterCrop();

                /*nextIntent = new Intent(CropImageActivity.this,PosterizeActivity.class);
                nextIntent.putExtra("filePath", file.getPath());
                startActivity(nextIntent);*/

                nextIntent = new Intent(ApplyEffectsActivity.this,PosterizeActivity.class);
                nextIntent.putExtra("filePath", file.getPath());
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
                Log.d("MyCameraApp", "failed to create directory");
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
            editedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();
        }
        catch (Exception e)
        {

        }
        return mediaFile;
    }

}
