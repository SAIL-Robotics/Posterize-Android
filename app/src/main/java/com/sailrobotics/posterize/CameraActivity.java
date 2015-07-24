package com.sailrobotics.posterize;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;

/**
 * Created by arjuns on 6/26/2015.
 */
public class CameraActivity extends Activity {
    private static int TAKE_PICTURE = 1;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        takePhoto();
        setContentView(R.layout.activity_crop_image);
    }//onCreate

    public void takePhoto() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE"); //tells Android that we are going to use the image capture function.
        File photo = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"picture.jpg"); //must define our own naming convention later
        imageUri = Uri.fromFile(photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PICTURE); //pass number 1, which represents that the request code for different devices we wanna use.
    }//takePhoto

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if(resultCode == Activity.RESULT_OK) {
            Uri selectedImage = imageUri; //designate Uri for an image
            //getContentResolver().notifyChange(selectedImage,null);

            //ImageView myImageView = (ImageView)findViewById(R.id.imageView2);
            File imageFile = new File(selectedImage.getPath());
            if(imageFile.exists())
            {
                /*Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                BitmapDrawable drawable = new BitmapDrawable(this.getResources(), bitmap);
                bitmap = scaleDownBitmap(bitmap,100,getApplicationContext());
                int nh = (int) (drawable.getIntrinsicHeight() * (510.0 / drawable.getIntrinsicWidth()) );
                Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 510, nh, true);
                myImageView.setImageBitmap(scaled);*/
                Intent cropImageIntent = new Intent(CameraActivity.this,CropImageActivity.class);
                cropImageIntent.putExtra("ImagePATH", selectedImage.getPath());
                startActivity(cropImageIntent);
                Log.e("IMAGE", selectedImage.getPath());
            }
        }
    }//onActivityResult

    public static Bitmap scaleDownBitmap(Bitmap photo, int newHeight, Context context) {
        final float densityMultiplier = context.getResources().getDisplayMetrics().density;
        int h= (int) (newHeight*densityMultiplier);
        int w= (int) (h * photo.getWidth()/((double) photo.getHeight()));
        photo=Bitmap.createScaledBitmap(photo, w, h, true);
        return photo;
    }
}//CameraActivity
