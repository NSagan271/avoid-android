package com.example.csadmin.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {
    Button left;
    Button right;
    RelativeLayout screen;
    RelativeLayout game;
    RelativeLayout.LayoutParams params;
    ImageButton unpause;
    ImageButton pause;
    RelativeLayout paused;
    Draw d;
    double s;
    DisplayMetrics displayMetrics;
    SharedPreferences sp;
    boolean exited;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        exited = false;

        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        s = displayMetrics.widthPixels - 128;

        game = (RelativeLayout) findViewById(R.id.game);

        left = (Button) findViewById(R.id.left);
        left.setWidth((int)s/2);

        right = (Button) findViewById(R.id.right);
        right.setWidth((int)s/2);

         sp = this.getSharedPreferences(
                "com.example.app", Context.MODE_PRIVATE);
        d = new Draw(getApplicationContext(),s, sp);

        paused = (RelativeLayout) findViewById(R.id.paused);

        pause = (ImageButton) findViewById(R.id.pause);
        pause.setEnabled(false);
        pause.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                paused.setVisibility(View.VISIBLE);
                pause.setEnabled(false);
                d.pause();
                return false;
            }
        });

        unpause = (ImageButton) findViewById(R.id.unpause);
        unpause.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                paused.setVisibility(View.INVISIBLE);
                pause.setEnabled(true);
                d.unpause();
                d.start();
                return false;
            }
        });

        left.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                d.start();
                pause.setEnabled(true);
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        d.moveLeft(true);
                        return true;
                    case MotionEvent.ACTION_UP:
                        d.moveLeft(false);
                        return true;
                }
                return false;
            }
        });

        right.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                d.start();
                pause.setEnabled(true);
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        d.moveRight(true);
                        return true;
                    case MotionEvent.ACTION_UP:
                        d.moveRight(false);
                        return true;
                }
                return false;
            }
        });

        screen = (RelativeLayout) findViewById(R.id.activity_main);
        screen.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                pause.setEnabled(true);
                d.start();
                return false;
            }
        });




        params = new RelativeLayout.LayoutParams((int)s, (int)s);
        game.addView(d, params);


    }

    @Override
    public void onPause(){
        super.onPause();
        exited = d.pause();
        if (!exited) pause.setEnabled(false);
    }
    @Override
    public void onResume(){
        super.onResume();
        if (exited){
            paused.setVisibility(View.VISIBLE);
            pause.setEnabled(false);
        }
        exited = false;
    }
}
