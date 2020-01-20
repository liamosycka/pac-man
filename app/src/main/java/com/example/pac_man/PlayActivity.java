package com.example.pac_man;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.Toast;

public class PlayActivity extends AppCompatActivity {

    //private Bitmap[] map = new Bitmap[];
    DrawingView drawV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_play);
        Display display = getWindowManager().getDefaultDisplay();
        // Carga la resolucion dentro de un objeto Point
        Point size = new Point();
        display.getSize(size);
        drawV=new DrawingView(this,size.x,size.y);
        setContentView(drawV);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth = metrics.widthPixels;
        int blockSize = screenWidth/17;
        blockSize = (blockSize / 5) * 5;
        Toast.makeText(PlayActivity.this,"screenwidht: "+screenWidth+"\n blocksize: "+blockSize,Toast.LENGTH_SHORT).show();
    }



}
