package com.sailrobotics.posterize;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by arjuns on 6/19/2015.
 */
public class HomeScreenCanvas extends ImageView implements View.OnTouchListener {
    public Context myContext;
    private Canvas myCanvas;
    public float x = 0, y = 0;// 345*578
    public float rx;
    public float ry; // rx and ry are used for scaling the bitmaps
    private Handler myhandler;
    private final int FRAME_RATE = 120; //ImageView is visible for this many milliseconds.
    private int touched = 0; //keep track of touch event.
    BitmapDrawable b1,b2,b3; //BitmapDrawable enables you to alter the bitmap.
    Intent intent;



    public HomeScreenCanvas (Context context, AttributeSet myAttributeSet) {
        super(context, myAttributeSet);
        myContext = context;
        myhandler = new Handler();
        b1 = (BitmapDrawable) getContext().getResources().getDrawable(R.drawable.gallery_fade,null);
        b2 = (BitmapDrawable) getContext().getResources().getDrawable(R.drawable.search_fade,null);
        b3 = (BitmapDrawable) getContext().getResources().getDrawable(R.drawable.instagram_fade,null);

    }


    private Runnable r = new Runnable() {

        public void run() {
            invalidate(); //Forcing a view to draw bitmaps
        }
    };

    /*resizeImage returns the image according to the stretched background image in activity_home_screen.xml*/
    public Bitmap resizeImage(Bitmap image)
    {
        Bitmap resized;
        float imageheight=image.getHeight()*ry; //Obtain the image height
        float imagewidth=image.getWidth()*rx; //Obtain the image width
        resized=Bitmap.createScaledBitmap(image, (int) imagewidth, (int)imageheight, true);
        return resized;
    }


    /*onSizeChanged is called by Android once the view is drawn, but before onDraw()->drawing on canvas*/
    @Override
    protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld) {
        super.onSizeChanged(xNew, yNew, xOld, yOld);
        /*rx and ry gives the extent to which the back image has been stretched by Android*/
        rx = (float) xNew / 345; //345 is the width of background image
        ry = (float) yNew / 578; //578 is the width of background image
    }

    @Override
    public boolean onTouch(View arg0, MotionEvent event) {
        myhandler.post(new Runnable() {
            public void run() {
                HomeScreenCanvas.this.setVisibility(View.VISIBLE); //Setting the imageview visible
            }
        });
        // get the coordinates of the portion touched
        x = event.getX();
        y = event.getY();
        touched = 1; // one indicates that touch event has been recognised
        return false;
    }

    protected void onDraw(Canvas c) {
        // super.onDraw(c);
        // Toast.makeText(getContext(), "onDraw()", Toast.LENGTH_SHORT).show();
        HomeScreenCanvas.this.myCanvas = c;

        if (touched == 1) {
            //gallery icon
            if ( x>150*rx && x<250*rx && y>170*ry && y<344*ry)
            {
                final Bitmap currentBitmap;
                x = 178 * rx;
                y = 211 * ry;
                this.setVisibility(View.VISIBLE);
                currentBitmap= BitmapFactory.decodeResource(getResources(), R.drawable.gallery_fade);
                Bitmap scaledBitmap=resizeImage(currentBitmap);
                c.drawBitmap(scaledBitmap, x - scaledBitmap.getWidth() / 2, y - scaledBitmap.getHeight() / 2, null);

            }
            //Instagram icon
            if ( x>40*rx && x<150*rx && y>360*ry && y<454*ry)
            {
                final Bitmap currentBitmap;
                x = 80 * rx;
                y = 362 * ry;
                this.setVisibility(View.VISIBLE);
                currentBitmap= BitmapFactory.decodeResource(getResources(), R.drawable.instagram_fade);
                Bitmap scaledBitmap=resizeImage(currentBitmap);
                c.drawBitmap(scaledBitmap, x - scaledBitmap.getWidth() / 2, y - scaledBitmap.getHeight() / 2, null);

            }

            //WebSearch icon
            if ( (x>250*rx && x<350*rx && y>360*ry && y<454*ry)||(x>200*rx && x<350*rx && y>400*ry && y<454*ry))
            {
                final Bitmap currentBitmap;
                x = 276 * rx;
                y = 363 * ry;
                this.setVisibility(View.VISIBLE);
                currentBitmap= BitmapFactory.decodeResource(getResources(), R.drawable.search_fade);
                Bitmap scaledBitmap=resizeImage(currentBitmap);
                c.drawBitmap(scaledBitmap, x - scaledBitmap.getWidth() / 2, y - scaledBitmap.getHeight() / 2, null);

            }

            //Camera icon
            if ( x>150*rx && x<250*rx && y>494*ry && y<600*ry )
            {
                //Adding shadow effect on press
                final Bitmap currentBitmap;
                x = 173 * rx;
                y = 535 * ry;
                this.setVisibility(View.VISIBLE);
                currentBitmap= BitmapFactory.decodeResource(getResources(), R.drawable.camera_fade);
                Bitmap scaledBitmap=resizeImage(currentBitmap);
                c.drawBitmap(scaledBitmap, x - scaledBitmap.getWidth() / 2, y - scaledBitmap.getHeight() / 2, null);
                intent = new Intent(myContext,CameraActivity.class);
                myContext.startActivity(intent);
            }

            /*postDelayed is added if the user taps continuously. In that case, the canvas must be refreshed within a short span of time. postDelayed
            * lets the handler object remember this event and execute within the time mentioned, that is, 150ms. If the user taps the screen within 150ms,
            * the handler wont recognize it.*/
            myhandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    HomeScreenCanvas.this.setVisibility(View.VISIBLE);
                }
            },150);
            touched=0; //reseting touch event
        }
        this.setOnTouchListener(this);
        myhandler.postDelayed(r,FRAME_RATE);
    }
}
