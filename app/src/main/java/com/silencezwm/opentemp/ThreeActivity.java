package com.silencezwm.opentemp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.silencezwm.openallannotation.OpenActivity;

@OpenActivity(target = {TwoActivity.class})
public class ThreeActivity extends Activity {

    private static final String TAG = "ThreeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three);
        Log.d(TAG, getIntent().getIntExtra("one", 0)
                + "--"
                + getIntent().getIntExtra("two", 0)
                + "--"
                + getIntent().getIntExtra("three", 0)
                + ""
                + getIntent().getStringExtra("four"));
    }
}
