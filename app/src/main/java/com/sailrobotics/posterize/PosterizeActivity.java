package com.sailrobotics.posterize;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;


public class PosterizeActivity extends ActionBarActivity {
    private static int RESULT_LOAD_IMAGE = 1;
    static Image image;
    ImageView imageView;
    TextView size;
    Bitmap bitmap;
    private static String FILE = "mnt/sdcard/FirstPdf3.pdf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posterize);

        String path = getIntent().getStringExtra("filePath");
        Log.e("post",path);
        imageView = (ImageView) findViewById(R.id.posterImageView);

        imageView.setImageURI(Uri.parse(path));
        bitmap = BitmapFactory.decodeFile(path);

        Button posterize = (Button) findViewById(R.id.Button_crop);
        posterize.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                double a4Width = 8.27;
                double a4Height = 11;
                double oldWidth = bitmap.getWidth();
                double oldHeight = bitmap.getHeight();

                Log.e("CutImage", oldWidth + " - " + oldHeight);
                double newHeight = 23.3;
                double newWidth = aspectRatio(oldWidth, oldHeight, newHeight, false);
                Log.e("CutImage", newWidth + " width")                ;

                if(newWidth > newHeight)        //landscape is best. swap values
                {
                    double tmp = a4Width;
                    a4Width = a4Height;
                    a4Height = tmp;
                }

                double totalA4Width = newWidth / a4Width;
                double totalA4Height = newHeight / a4Height;

                Log.e("CutImage", "Sheets - " + totalA4Width + " " + totalA4Height);

                double loopWidth = oldWidth / totalA4Width;
                double loopHeight = oldHeight / totalA4Height;

                Log.e("CutImage", "Loop - " + loopWidth + " " + loopHeight);

                double edgeWidth = loopWidth * (totalA4Width - (int) totalA4Width);
                double edgeHeight = loopHeight * (totalA4Height - (int) totalA4Height);

                Log.e("CutImage", "Edge - " + edgeWidth + " " + edgeHeight);



                try
                {
                    Document document = new Document(PageSize.A4);
                    PdfWriter.getInstance(document, new FileOutputStream(FILE));
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
                            }
                            if(j == (int) totalA4Height)
                            {
                                isPartHeight = true;
                                yEnd = (int)edgeHeight;
                            }
                            xStart = (int)(i * (int)(loopWidth));
                            yStart = (int)(j * (int)(loopHeight));

                            Bitmap newMap = Bitmap.createBitmap(bitmap, xStart, yStart, xEnd, yEnd);
                            //cropImageView.setImageBitmap(newMap);

                            /*float scaleWidth = (float) (595 / newMap.getWidth());
                            float scaleHeight = (float) (842 / newMap.getHeight());

                            // createa matrix for the manipulation
                            Matrix matrix = new Matrix();
                            // resize the bit map
                            matrix.postScale(scaleWidth, scaleHeight);

                            // recreate the new Bitmap
                            Bitmap resizedBitmap = Bitmap.createBitmap(newMap, 0, 0, newMap.getWidth(), newMap.getHeight(), matrix, true);*/

                            //Bitmap resizedBitmapNew = Bitmap.createScaledBitmap(newMap, 530, 820, false);

                            addImage(document, newMap, isPartWidth, isPartHeight, (totalA4Width - (int) totalA4Width), (totalA4Height - (int) totalA4Height));
                            //addImage(document, resizedBitmap);
                            //addImage(document, resizedBitmapNew);

                            Log.e("CutImage", "Coords - " + xStart + " " + yStart + ", " + xEnd + " " + yEnd + " density - " + newMap.getDensity());
                            Log.e("CutImage", "W x H - " + newMap.getWidth() + " " + newMap.getHeight() + " density - " + newMap.getDensity());
                            //Log.e("CutImage", "W x H - Latest - " + resizedBitmapNew.getWidth()  + " " + resizedBitmapNew.getHeight() + " density - " + resizedBitmapNew.getDensity());
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
        });

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
