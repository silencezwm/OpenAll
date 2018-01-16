package com.silencezwm.opentemp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.silencezwm.openall.OpenAll;
import com.silencezwm.openallannotation.OpenActivity;

@OpenActivity(target = MainActivity.class)
public class TwoActivity extends Activity {

    private static final String TAG = "TwoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
        Log.d(TAG, getIntent().getIntExtra("one", 0)
                + "--"
                + getIntent().getIntExtra("two", 0)
                + "--"
                + getIntent().getIntExtra("three", 0)
                + ""
                + getIntent().getStringExtra("four"));
    }

    public void openOne(View view) {
        OpenAll.getInstance().open(this, MainActivity.class);
    }

}
