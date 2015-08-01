package com.sailrobotics.posterize;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by arjun on 8/1/2015.
 */
public class InstructionActivity extends Activity {
    TextView myTextView;
    Button closeButton;
    String path;
    String bitmapWidth, bitmapHeight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction);
        myTextView = (TextView) findViewById(R.id.textView1);
        closeButton =(Button)findViewById(R.id.closeButton);

        Intent intent = getIntent();
        path = intent.getStringExtra("filePath");
        bitmapWidth = intent.getStringExtra("bitmapWidth");
        bitmapHeight = intent.getStringExtra("bitmapHeight");

        myTextView.setText("1. Press the Camera Ruler button to invoke the camera. Take a picture of the area where the poster needs to be placed.\n\n" +
                "2. Place two markers on the image. Remember you must know the distance between the two markers. Enter that distance(in cms) under 'Enter measurements'\n\n" +
                "3. Place next set of two markers on the image. These two end points could be either the width or height of the actual poster.\n\n"+
                "4. Now press the calculate button. You will receive a message that represents one of the dimensions.\n\n"+
                "5. If you feel that the poster width or height is inaccurate, you can either re-enter the distance under 'Enter Measurements' or press the reset marker button and repeat the process.\n\n"+
                "6. If you feel that the poster width or height is accurate, click the proceed button. Now will be asked whether to set the dimension as width or height.\n\n"+
                "7. According select one of the dimension and press 'OK' button.");

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
