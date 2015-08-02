package com.sailrobotics.posterize;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by arjun on 8/1/2015.
 */
public class RulerActivity extends Activity{
    Button rulerCloseButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruler);
        rulerCloseButton = (Button)findViewById(R.id.closeRulerButton);

        rulerCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
