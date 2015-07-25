package com.sailrobotics.posterize;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;


public class PosterizeActivity extends ActionBarActivity {
    private static int RESULT_LOAD_IMAGE = 1;
    static Image image;
    ImageView imageView;
    TextView size;
    Bitmap bitmap;
    private static String FILE = "mnt/sdcard/FirstPdf3.pdf";

    private static double newHeight;
    private static double newWidth;

    private static double a4Height = 11;
    private static double a4Width = 8.27;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posterize);

        String path = getIntent().getStringExtra("filePath");
        newWidth = Double.parseDouble(getIntent().getStringExtra("bitmapWidth"));
        newHeight = Double.parseDouble(getIntent().getStringExtra("bitmapHeight"));

        imageView = (ImageView) findViewById(R.id.posterImageView);

        imageView.setImageURI(Uri.parse(path));
        bitmap = BitmapFactory.decodeFile(path);

        double oldWidth = bitmap.getWidth();
        double oldHeight = bitmap.getHeight();

        if(newWidth > newHeight)        //landscape is best. swap values
        {
            double tmp = a4Width;
            a4Width = a4Height;
            a4Height = tmp;
        }

        double totalA4Width = newWidth / a4Width;
        double totalA4Height = newHeight / a4Height;



        Button posterize = (Button) findViewById(R.id.Button_crop);
        posterize.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                drawCutLine(bitmap.getWidth(), bitmap.getHeight(), newWidth / a4Width, newHeight / a4Height);
            }
        });

    }

    void drawCutLine(double oldWidth, double oldHeight, double totalA4Width, double totalA4Height)
    {
        Canvas canvas = new Canvas();

        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        imageView.setImageBitmap(bitmap);
        canvas.setBitmap(bitmap);
        double loopWidth = oldWidth / totalA4Width;
        double loopHeight = oldHeight / totalA4Height;

        Log.e("CutImage", "Loop - " + loopWidth + " " + loopHeight);

        try
        {
            int xStart = 0, yStart = 0, xEnd = bitmap.getWidth(), yEnd = bitmap.getHeight();
            boolean isPartWidth = false;
            boolean isPartHeight = false;

            // set drawing colour
            Paint p = new Paint();
            p.setColor(Color.RED);
            p.setStyle(Paint.Style.FILL);
            p.setStrokeWidth(3.0f);

            for(int i=1; i <= (int) totalA4Width; i++)
            {
                xStart = (int)(i * (int)(loopWidth));

                // draw a line onto the canvas
                canvas.drawLine(xStart, 0, xStart, yEnd, p);
            }

            for(int j = 1; j <= (int) totalA4Height; j++)
            {
                yStart = (int)(j * (int)(loopHeight));

                //Bitmap newMap = Bitmap.createBitmap(bitmap, xStart, yStart, xEnd, yEnd);

                // draw a line onto the canvas
                canvas.drawLine(0, yStart, xEnd, yStart, p);
            }
        }

        catch (Exception e)
        {
            Log.e("poster", e.getMessage());
            e.printStackTrace();
        }
    }

    double aspectRatio(double oldWidth, double oldHeight, double newSize, boolean isWidth)
    {
        double factor;
        if(isWidth == true)
        {
            factor = newSize/oldWidth;
            return factor * oldHeight;
        }
        factor = newSize/oldHeight;
        return factor * oldWidth;
    }

    private static void addImage(Document document, Bitmap newMap, boolean isPartWidth, boolean isPartHeight, double edgeWidth, double edgeHeight)
    {
        float finalPDFWidth = 585f;
        float finalPDFHeight = 832f;
        try
        {
            //converting bitmap to byte array
            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            newMap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            image = Image.getInstance(stream.toByteArray());
            image.setAlignment(Image.MIDDLE);


            Log.e("poster", image.getHeight() + "");
        }
        catch (BadElementException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (MalformedURLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // image.scaleAbsolute(150f, 150f);
        if(isPartWidth == true)
        {
            finalPDFWidth = (float)(finalPDFWidth * edgeWidth);
        }
        if(isPartHeight == true)
        {
            finalPDFHeight = (float)(finalPDFHeight * edgeHeight);
        }
        image.scaleAbsolute(finalPDFWidth, finalPDFHeight);
        try
        {
            Log.e("CutImage", document.getPageSize() + "");
            image.setAbsolutePosition(
                    (PageSize.A4.getWidth() - image.getScaledWidth()) / 2,
                    (PageSize.A4.getHeight() - image.getScaledHeight()) / 2);
            document.add(image);
            document.newPage();

        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_posterize, menu);
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
