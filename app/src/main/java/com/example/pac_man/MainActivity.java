package com.example.pac_man;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void playGame (View view){
        Intent play = new Intent(this, PlayActivity.class);
        startActivity(play);
    }

    /*public void highScore(View view){
        Intent score = new Intent(this,HighScore.class);
        startActivity(score);
    }*/

    public void exit(View view){
     finish();
    }
}
