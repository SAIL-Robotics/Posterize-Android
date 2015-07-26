package com.sailrobotics.posterize;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by asanthan on 7/25/15.
 */
public class TakeCameraMeasurement extends Activity {
    private static int TAKE_PICTURE = 1;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        takePhoto();
        //setContentView(R.layout.activity_mark_reference_point);
    }//onCreate

    public void takePhoto() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE"); //tells Android that we are going to use the image capture function.
        //File photo = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"picture.jpg"); //must define our own naming convention later
        //imageUri = Uri.fromFile(photo);
        imageUri = getOutputMediaFileUri(); // Allocate file uri and assign file name
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PICTURE); //pass number 1, which represents that the request code for different devices we wanna use.
    }//takePhoto

    /** Create a file Uri for saving an image*/
    private static Uri getOutputMediaFileUri(){
        return Uri.fromFile(getOutputMediaFile());
    }

    /** Create a File for saving an image*/
    private static File getOutputMediaFile(){
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
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        return mediaFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if(resultCode == Activity.RESULT_OK) {
            Uri selectedImage = imageUri; //designate Uri for an image
            File imageFile = new File(selectedImage.getPath());
            if(imageFile.exists())
            {
                Intent cropImageIntent = new Intent(TakeCameraMeasurement.this,MarkReferenceActivity.class);
                cropImageIntent.putExtra("ImagePATH", selectedImage.getPath());
                startActivity(cropImageIntent);
                finish();
                Log.e("IMAGE", selectedImage.getPath());
            }
        }
    }
}
