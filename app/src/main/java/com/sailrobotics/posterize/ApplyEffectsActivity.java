package com.sailrobotics.posterize;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by arjuns on 6/28/2015.
 */
public class ApplyEffectsActivity extends Activity {
    ImageButton previousActivityButton, nextActivityButton;
    Intent nextIntent, previousIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_effects);

        previousActivityButton = (ImageButton)findViewById(R.id.previousButton);
        nextActivityButton = (ImageButton)findViewById(R.id.nextButton);

        /*previousActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousIntent = new Intent(ApplyEffectsActivity.this, .class);
                startActivity(previousIntent);
            }
        });

        nextActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextIntent = new Intent(ApplyEffectsActivity.this, .class);
                startActivity(nextIntent);
            }
        });*/
    }
}
