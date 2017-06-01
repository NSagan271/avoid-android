package com.example.csadmin.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {
    Button left;
    Button right;
    RelativeLayout screen;
    RelativeLayout game;
    RelativeLayout.LayoutParams params;
    ImageButton unPause;
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
        screen = (RelativeLayout) findViewById(R.id.activity_main);

        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        boolean portrait = (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);
        if(portrait) {
            s = displayMetrics.widthPixels - 16 * getResources().getDisplayMetrics().scaledDensity;
            screen.setBackgroundResource(R.drawable.background);
        }
        else{
            s = displayMetrics.heightPixels - 56*getResources().getDisplayMetrics().scaledDensity;
            screen.setBackgroundResource(R.drawable.landscape);
        }

        game = (RelativeLayout) findViewById(R.id.game);

        Button l2;
        Button r2;
        if (portrait){

            l2 = (Button) findViewById(R.id.left2);
            l2.setMinimumWidth(0);
            l2.setWidth(0);
            left = (Button) findViewById(R.id.left);
            left.setWidth((int)s/2);

            r2 = (Button) findViewById(R.id.right2);
            r2.setMinimumWidth(0);
            r2.setWidth(0);
            right = (Button) findViewById(R.id.right);
            right.setWidth((int)s/2);
        }

        else{
            l2 = (Button) findViewById(R.id.left);
            l2.setWidth(0);
            l2.setMinimumWidth(0);
            left = (Button) findViewById(R.id.left2);

            r2 = (Button) findViewById(R.id.right);
            r2.setMinimumWidth(0);
            r2.setWidth(0);
            right = (Button) findViewById(R.id.right2);
        }


         sp = this.getSharedPreferences(
                "com.example.app", Context.MODE_PRIVATE);
        boolean p = (savedInstanceState != null && savedInstanceState.getBoolean("paused"));
        if (savedInstanceState == null || savedInstanceState.getBoolean("restart"))
            d = new Draw(getApplicationContext(), s, sp);
        else d = new Draw(getApplicationContext(), s, savedInstanceState.getDouble("playerX"),
                savedInstanceState.getDoubleArray("enemyX"), savedInstanceState.getDoubleArray("enemyY"),
                savedInstanceState.getDoubleArray("enemyV"), savedInstanceState.getDouble("score"),
                p, sp);

        paused = (RelativeLayout) findViewById(R.id.paused);
        if (p) paused.setVisibility(View.VISIBLE);

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

        unPause = (ImageButton) findViewById(R.id.unpause);
        unPause.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                paused.setVisibility(View.INVISIBLE);
                pause.setEnabled(true);
                d.unPause();
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

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        if (!d.isOver()){
            outState.putDouble("playerX",d.playerX());
            outState.putDoubleArray("enemyX",d.enemyX());
            outState.putDoubleArray("enemyY",d.enemyY());
            outState.putDoubleArray("enemyV",d.enemyV());
            outState.putDouble("score",d.score());
            outState.putBoolean("paused",true);
            outState.putBoolean("restart", false);
        }
        else outState.putBoolean("restart", true);

    }
}
