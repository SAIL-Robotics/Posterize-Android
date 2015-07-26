package com.sailrobotics.posterize;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anandh on 07-26-15.
 */
public class PlotPoint extends SurfaceView
{
    private final SurfaceHolder surfaceHolder;
    private Context context;
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    float x, y;
    List<Point> listPoints;

    public PlotPoint(Context context) {
        super(context);
        this.context = context;
        surfaceHolder = getHolder();
        setWillNotDraw (false);
        listPoints = new ArrayList<Point>();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e("post", "draw");
        // Setting the color of the circle

        paint.setStyle(Paint.Style.FILL);
        // Draw the circle at (x,y) with radius 15

        for (int i=0;i < listPoints.size(); i++)
        {
            if(i == 0 || i == 1)
            {
                paint.setColor(Color.RED);
            }
            else
            {
                paint.setColor(Color.GREEN);
            }
            canvas.drawCircle(listPoints.get(i).x, listPoints.get(i).y, 10, paint);
            paint.setStrokeWidth(3);
            if(i ==1)
            {
                canvas.drawLine(listPoints.get(0).x, listPoints.get(0).y, listPoints.get(1).x, listPoints.get(1).y, paint);
            }
            if(i == 3)
            {
                canvas.drawLine(listPoints.get(2).x, listPoints.get(2).y, listPoints.get(3).x, listPoints.get(3).y, paint);
            }
            Log.e("post", listPoints.get(i).x + " " + listPoints.get(i).y + "");
        }
        // Redraw the canvas
        //invalidate();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(event.getAction() == MotionEvent.ACTION_DOWN)
        {
            if (surfaceHolder.getSurface().isValid()) {
                x = event.getX();
                y = event.getY();
                if(listPoints.size() > 3)
                {
                    Log.e("post", "not allowed");
                    return false;
                }
                listPoints.add(new Point((int)x, (int)y));

                invalidate();
            }
            Log.e("post", event.getX() + " " + event.getXPrecision() + " " + + event.getRawX() + " " + event.getY() + " " + event.getYPrecision() + " ");
        }
        return false;
    }

    public void resetCanvas()
    {
        listPoints.clear();
        invalidate();
    }

    public double calculateDistance(double known)
    {
        if(listPoints.size() < 3)
        {
            Log.e("post", "Not enough values");
            return 0;
        }
        FindDistanceUtil dist = new FindDistanceUtil(listPoints.get(0), listPoints.get(1), listPoints.get(2), listPoints.get(3), known);
        Log.e("post", dist.calculateDistance() + "");
        return dist.calculateDistance();
    }
}
