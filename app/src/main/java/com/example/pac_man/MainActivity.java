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
        finish();
    }

    public void exit(View view){
        finish();
        System.exit(0);

    }
}
