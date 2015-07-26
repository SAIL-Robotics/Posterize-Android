package com.sailrobotics.posterize;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.MetadataChangeSet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by asanthan on 7/26/15.
 */
public class GoogleDriveActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    static GoogleApiClient mGoogleApiClient;
    File myfile;
    static final int RESOLVE_CONNECTION_REQUEST_CODE = 1;
    static final int REQUEST_CODE_CREATOR = 2;
    private static String filePath = "mnt/sdcard/FirstPdf3.pdf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poster_summary);
        uploadToDrive();
    }

    public void uploadToDrive() {

    mGoogleApiClient = new GoogleApiClient.Builder(this)
            .addApi(Drive.API)
            .addScope(Drive.SCOPE_FILE)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, RESOLVE_CONNECTION_REQUEST_CODE);
            } catch (IntentSender.SendIntentException e) {
                // Unable to resolve, message user appropriately
            }
        } else {
            GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), this, 0).show();
        }
    }

    private void saveFileToDrive() {
        myfile = new File(filePath);
        // Start by creating a new contents, and setting a callback.
        Log.i("Post", "Creating new contents.");
        Drive.DriveApi.newDriveContents(mGoogleApiClient)
                .setResultCallback(new ResultCallback<DriveApi.DriveContentsResult>() {

                    @Override
                    public void onResult(DriveApi.DriveContentsResult result) {

                        if (!result.getStatus().isSuccess()) {
                            Log.i("Post", "Failed to create new contents.");
                            return;
                        }
                        // Otherwise, we can write our data to the new contents.
                        Log.i("Post", "New contents created.");
                        // Get an output stream for the contents.
                        OutputStream outputStream = result.getDriveContents().getOutputStream();
                        byte[] data = new byte[(int) myfile.length()];
                        try {
                            FileInputStream fileInputStream = new FileInputStream(myfile);
                            fileInputStream.read(data);
                            outputStream.write(data);
                            //finish();
                        } catch (IOException e1) {
                            Log.i("Post", "Unable to write file contents.");
                        }
                        // Create the initial metadata - MIME type and title.
                        MetadataChangeSet metadataChangeSet = new MetadataChangeSet.Builder()
                                .setMimeType("application/pdf").setTitle("test.pdf").build();

                        // Create an intent for the file chooser, and start it.
                        IntentSender intentSender = Drive.DriveApi
                                .newCreateFileActivityBuilder()
                                .setInitialMetadata(metadataChangeSet)
                                .setInitialDriveContents(result.getDriveContents())
                                .build(mGoogleApiClient);
                        try {
                            startIntentSenderForResult(
                                    intentSender, REQUEST_CODE_CREATOR, null, 0, 0, 0);
                            finish();
                        } catch (IntentSender.SendIntentException e) {
                            Log.i("Post", "Failed to launch file chooser.");
                        }
                    }
                });
    }


    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i("Post", "API client connected.");
        saveFileToDrive();
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.i("Post", "GoogleApiClient connection suspended");
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        switch (requestCode) {
            case RESOLVE_CONNECTION_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    mGoogleApiClient.connect();
                }
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }


}
