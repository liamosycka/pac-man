package com.example.pac_man;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.Toast;

public class PlayActivity extends AppCompatActivity {
    DrawingView drawV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        drawV=new DrawingView(this);
        setContentView(drawV);
    }
    /*@Override
    protected void onPause(){
        super.onPause();
        drawV.pause();
    }
    @Override
    protected void onResume(){
        super.onResume();
        drawV.resume();
    }*/

}
