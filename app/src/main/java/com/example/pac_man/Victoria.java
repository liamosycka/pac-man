package com.example.pac_man;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class Victoria extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_victoria);
        Bundle bundle = getIntent().getExtras();
        int score = bundle.getInt("score");
        TextView textView = (TextView)findViewById(R.id.score);
        textView.setText("" + score);
        final Intent inicio = new Intent(this,MainActivity.class);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Globals.getInstance().setScore(0);
              startActivity(inicio);
              finish();

            }
        }, 3000);
    }


}
