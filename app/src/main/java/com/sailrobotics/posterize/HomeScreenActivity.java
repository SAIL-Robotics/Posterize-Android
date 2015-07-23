package com.sailrobotics.posterize;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;



public class HomeScreenActivity extends Activity {
    float x = 0;
    float y = 0;
    AttributeSet myAttributeSet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        CameraActivity myCameraActivity = new CameraActivity();
        HomeScreenCanvas myHomeScreenCanvas = new HomeScreenCanvas(getApplicationContext(), myAttributeSet);
        FrameLayout homeScreenLayout = (FrameLayout)findViewById(R.id.home_screen); //homescreen is the id of the frame layout used in the home screen.
        homeScreenLayout.setOnTouchListener((HomeScreenCanvas)findViewById(R.id.img_view_home_screen)); //setting the on touch listener for the homescreen.
        //img_view_home_screen is an image view that will be updated dynamically
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
