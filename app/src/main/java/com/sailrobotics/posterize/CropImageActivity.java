package com.sailrobotics.posterize;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by arjuns on 6/28/2015.
 */
public class CropImageActivity extends Activity {
    ImageButton previousActivityButton, nextActivityButton;
    Intent nextIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_image);

        previousActivityButton = (ImageButton)findViewById(R.id.previousButtonOne);
        nextActivityButton = (ImageButton)findViewById(R.id.nextButtonOne);
        ImageView cropImage = (ImageView)findViewById(R.id.imageView2);

        if(getIntent().hasExtra("ImageURI"))
        {
            try {
                Uri imageUri = getIntent().getData();
            // Log.i("Image Uri",imageUri.toString());
                Bitmap bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageUri);
                cropImage.setImageBitmap(bm);
            }
            catch(IOException i){
                Log.i("ERROR","Unable to find image");
            }

            /*File imageFile = new File(imageUri.getPath());
            if(imageFile.exists())
            {
                Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                BitmapDrawable drawable = new BitmapDrawable(this.getResources(), bitmap);
                int nh = (int) (drawable.getIntrinsicHeight() * (510.0 / drawable.getIntrinsicWidth()) );
                Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 510, nh, true);
                cropImage.setImageBitmap(scaled);

            }*/
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
