package com.silencezwm.opentemp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.silencezwm.openall.OpenAll;
import com.silencezwm.openallannotation.OpenActivity;

@OpenActivity(target = {TwoActivity.class, ThreeActivity.class})
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openTwo(View view) {
        OpenAll.getInstance().addIntParam("one", 1)
                .addIntParam("two", 2)
                .addIntParam("three", 3)
                .addStringParam("four", "success")
                .open(this, TwoActivity.class);
    }

    public void openThree(View view) {
        OpenAll.getInstance().open(this, ThreeActivity.class);
    }

}
