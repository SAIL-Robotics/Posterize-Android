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
    Intent nextIntent, previousIntent;
    ImageView baseImage;
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_effects);

        previousActivityButton = (ImageButton)findViewById(R.id.imageButtonLeftEffect);
        nextActivityButton = (ImageButton)findViewById(R.id.imageButtonRightEffect);
        baseImage = (ImageView)findViewById(R.id.effectsImage);

        //ImageEffects addEffect = new ImageEffects();
        String path = getIntent().getStringExtra("filePath");
        Log.e("post", path);

        baseImage.setImageURI(Uri.parse(path));
        bitmap = BitmapFactory.decodeFile(path);

        Bitmap editedBitmap = doInvert(bitmap);
        baseImage.setImageBitmap(editedBitmap);
        //addEffect.doInvert(baseBitmap);


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

    public static Bitmap doInvert(Bitmap src) {
        // create new bitmap with the same settings as source bitmap
        Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
        // color info
        int A, R, G, B;
        int pixelColor;
        // image size
        int height = src.getHeight();
        int width = src.getWidth();

        // scan through every pixel
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                // get one pixel
                pixelColor = src.getPixel(x, y);
                // saving alpha channel
                A = Color.alpha(pixelColor);
                // inverting byte for each R/G/B channel
                R = 255 - Color.red(pixelColor);
                G = 255 - Color.green(pixelColor);
                B = 255 - Color.blue(pixelColor);
                // set newly-inverted pixel to output image
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }

        // return final bitmap
        return bmOut;
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
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();
        }
        catch (Exception e)
        {

        }
        return mediaFile;
    }

}
