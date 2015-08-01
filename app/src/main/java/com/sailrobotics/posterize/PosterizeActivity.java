package com.sailrobotics.posterize;

import android.content.Intent;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class PosterizeActivity extends ActionBarActivity {
    ImageButton nextActivityButton;
    Intent nextIntent, previousIntent;
    private static int RESULT_LOAD_IMAGE = 1;
    static Image image;
    ImageView imageView;
    TextView beforeOptimize, afterOptimize;
    Bitmap bitmap;
    private static String FILEPATH,FILENAME;
    String orientation = "portrait";
    private static double newHeight;
    private static double newWidth;

    private static double a4Height = 11;
    private static double a4Width = 8.27;

    double totalA4Width;
    double totalA4Height;

    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posterize);

        path = getIntent().getStringExtra("filePath");
        newWidth = Double.parseDouble(getIntent().getStringExtra("bitmapWidth"));
        newHeight = Double.parseDouble(getIntent().getStringExtra("bitmapHeight"));

        nextActivityButton = (ImageButton)findViewById(R.id.nextButton);
        imageView = (ImageView) findViewById(R.id.posterImageView);

        imageView.setImageURI(Uri.parse(path));
        bitmap = BitmapFactory.decodeFile(path);

        final double oldWidth = bitmap.getWidth();
        final double oldHeight = bitmap.getHeight();

        if(newWidth > newHeight)        //landscape is best. swap values
        {
            double tmp = a4Width;
            a4Width = a4Height;
            a4Height = tmp;
            orientation = "landscape";
        }

        totalA4Width = newWidth / a4Width;
        totalA4Height = newHeight / a4Height;

        //beforeOptimize.setText("Before Optimization \n " + Math.ceil(totalA4Width) * Math.ceil(totalA4Height));

        drawCutLine(oldWidth, oldHeight, totalA4Width, totalA4Height);

        Button posterize = (Button) findViewById(R.id.optimizeButton);
        posterize.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                bitmap = BitmapFactory.decodeFile(path);
                imageOptimize();
            }
        });

        nextActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat myDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
                String currentTime = myDateFormat.format(new Date());
                FILEPATH = "mnt/sdcard/Poster_"+currentTime+".pdf";
                FILENAME = "Poster_"+currentTime+".pdf";
                imageDivision(oldWidth, oldHeight, totalA4Width, totalA4Height);
                nextIntent = new Intent(PosterizeActivity.this, PosterSummaryActivity.class);
                nextIntent.putExtra("pdfPath", FILEPATH);
                nextIntent.putExtra("FileName",FILENAME);
                nextIntent.putExtra("sheets", Math.ceil(totalA4Width) * Math.ceil(totalA4Height) + "");
                nextIntent.putExtra("orientation", orientation + "");
                startActivity(nextIntent);
            }
        });
    }

    void imageOptimize()
    {
        double oldWidth = bitmap.getWidth();
        double oldHeight = bitmap.getHeight();

        totalA4Width = newWidth / a4Width;
        totalA4Height = newHeight / a4Height;

        int totalPapers = (int)(Math.ceil(totalA4Width) * Math.ceil(totalA4Height));
        int newTotalPapers;

        Log.e("CutImage", totalA4Width +" "+ totalA4Height + " " + totalPapers);

        double diffWidth = totalA4Width - (int)totalA4Width;
        double diffHeight = totalA4Height - (int)totalA4Height;

        if(diffHeight == 0)
        {
            Log.e("CutImage", "no height change");
        }
        else if(diffHeight > 0.5)
        {
            Log.e("CutImage", "No change in height");
        }
        else
        {
            Log.e("CutImage", "height decrease");
            totalA4Height = Math.floor(totalA4Height) - 0.001;
        }

        if(diffWidth == 0)
        {
            Log.e("CutImage", "no width change");
        }
        else if(diffWidth > 0.5)
        {
            Log.e("CutImage", "No change in weight");
        }
        else
        {
            Log.e("CutImage", "width decrease");
            totalA4Width = Math.floor(totalA4Width) - 0.001;
        }

        newTotalPapers = (int)(Math.ceil(totalA4Width) * Math.ceil(totalA4Height));

        Log.e("CutImage", totalA4Width +" "+ totalA4Height + " " + newTotalPapers);

        if(totalPapers == newTotalPapers)
        {
            Toast.makeText(getApplication(), "Good to go, optimization not required!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getApplication(), "You just saved "+ (totalPapers - newTotalPapers) + " papers", Toast.LENGTH_SHORT).show();
        }

        Log.e("CutImage", totalA4Width +" "+ totalA4Height);
        //afterOptimize.setText(totalA4Width + "  " + totalA4Height);
        drawCutLine(oldWidth, oldHeight, totalA4Width, totalA4Height);
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

    void imageDivision(double oldWidth, double oldHeight, double totalA4Width, double totalA4Height)
    {
        double loopWidth = oldWidth / totalA4Width;
        double loopHeight = oldHeight / totalA4Height;

        int alignment = Image.MIDDLE;
        Log.e("CutImage", "Loop - " + loopWidth + " " + loopHeight);

        double edgeWidth = loopWidth * (totalA4Width - (int) totalA4Width);
        double edgeHeight = loopHeight * (totalA4Height - (int) totalA4Height);

        Log.e("CutImage", "Edge - " + edgeWidth + " " + edgeHeight);

        try
        {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream(FILEPATH));
            document.open();

            int xStart = 0, yStart = 0, xEnd = (int)(loopWidth), yEnd = (int)(loopHeight);
            boolean isPartWidth = false;
            boolean isPartHeight = false;

            for(int j = 0; j <= (int) totalA4Height; j++)
            {
                for(int i=0; i <= (int) totalA4Width; i++)
                {
                    isPartWidth = false;
                    isPartHeight = false;

                    xEnd = (int)(loopWidth);
                    yEnd = (int)(loopHeight);

                    if(i == (int) totalA4Width)
                    {
                        isPartWidth = true;
                        xEnd = (int)edgeWidth;
                        alignment = Image.LEFT;
                    }
                    if(j == (int) totalA4Height)
                    {
                        isPartHeight = true;
                        yEnd = (int)edgeHeight;
                        alignment = Image.TOP;
                    }
                    xStart = (int)(i * (int)(loopWidth));
                    yStart = (int)(j * (int)(loopHeight));

                    bitmap = BitmapFactory.decodeFile(path);
                    Bitmap newMap = Bitmap.createBitmap(bitmap, xStart, yStart, xEnd, yEnd);

                    addImage(document, newMap, isPartWidth, isPartHeight, (totalA4Width - (int) totalA4Width), (totalA4Height - (int) totalA4Height), alignment);
                }
            }
            document.close();
        }
        catch (Exception e)
        {
            Log.e("poster", e.getMessage());
            e.printStackTrace();
        }
    }

    private static void addImage(Document document, Bitmap newMap, boolean isPartWidth, boolean isPartHeight, double edgeWidth, double edgeHeight, int alignment)
    {
        float finalPDFWidth = 585f;
        float finalPDFHeight = 832f;
        try
        {
            //converting bitmap to byte array
            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            newMap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            image = Image.getInstance(stream.toByteArray());
            image.setAlignment(alignment);


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

   /*     image.setAbsolutePosition(
                (PageSize.A4.getWidth() - image.getScaledWidth()) / 2,
                (PageSize.A4.getHeight() - image.getScaledHeight()) / 2);
     */
        try
        {
            Log.e("CutImage", document.getPageSize() + "");
            image.setAlignment(Element.ALIGN_CENTER);
            if(isPartWidth == true)
            {
                image.setAlignment(Element.ALIGN_LEFT);
            }
            if(isPartHeight == true)
            {
                image.setAlignment(Element.ALIGN_TOP);
            }
            document.setMargins(2,2,2,2);
            document.add(image);
            document.newPage();

        }catch (DocumentException e) {
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
