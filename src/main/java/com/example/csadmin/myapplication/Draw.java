package com.example.csadmin.myapplication;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

import com.example.csadmin.myapplication.game.Game;

/**
 * Created by student on 5/15/17.
 */

public class Draw extends View implements View.OnTouchListener {
    private double size;
    private Game game;
    int countdown;
    Paint p;
    boolean moveLeft;
    boolean moveRight;
    boolean over;
    boolean started;
    public Draw(Context c){
        super(c);
    }
    public Draw(Context context, double s, SharedPreferences sp){
        super(context);
        size = s;
        p = new Paint();
        countdown = 10;
        moveLeft = false;
        moveRight = false;
        over = false;
        started = false;
        game = new Game(size, sp);

    }

    @Override
    public void onDraw(Canvas canvas) {
        if(!game.move(canvas, p, moveRight, moveLeft) || over){
            over = true;
            p.setColor(Color.RED);
            canvas.drawRect((int)(size/5), (int)(3*size/7), (int)(4*size/5), (int)(4*size/7), p);
            p.setColor(Color.WHITE);
            p.setTextSize((float)size/12);
            canvas.drawText("RESTART?",(int)(25*size/80), (int)(15*size/28), p);

            p.setColor(Color.WHITE);
            p.setStrokeWidth(5);
            p.setStyle(Paint.Style.STROKE);
            canvas.drawRect((int)(size/5), (int)(3*size/7), (int)(4*size/5), (int)(4*size/7), p);
            p.setStyle(Paint.Style.FILL);

        }
        else{
            if (started) countdown--;
            else{
                p.setColor(Color.WHITE);
                p.setTextSize((float)size/12);
                canvas.drawText("TAP SCREEN TO START",(int)(5*size/80), (int)(15*size/28), p);
            }
            if (countdown == 0){ //create a new enemy
                game.newEnemy();
                countdown = (int)(Math.random()*50+75);
            }
            invalidate();
        }

    }
    public void moveRight(boolean r){
        moveRight = r;
    }
    public void moveLeft(boolean l){
        moveLeft = l;
    }
    private void restart(){
        countdown = 50;
        moveLeft = false;
        moveRight = false;
        over = false;
        game.restart();
        invalidate();
    }
    public void start(){
        started = true;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        started = true;
        float x = event.getX();
        float y = event.getY();
        if (over && x >= size/5 && x <= 4*size/5 &&
                y >= 3*size/7 && y <= 4*size/7) {
            restart();
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}
