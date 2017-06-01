package com.example.csadmin.myapplication;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
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
    boolean paused;
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
        paused = false;
        game = new Game(size, sp);

    }
    public Draw(Context context, double s, double playerX, double[] enemyX,
                double[] enemyY, double[]enemyV, double score, boolean paused, SharedPreferences sp){
        super(context);
        size = s;
        p = new Paint();
        countdown = 100;
        moveLeft = false;
        moveRight = false;
        over = false;
        started = true;
        this.paused = paused;
        game = new Game(size, playerX, enemyX, enemyY, enemyV, score, sp);
        if (paused) pause();

    }

    @Override
    public void onDraw(Canvas canvas) {
        if(started && !game.move(canvas, p, moveRight, moveLeft) || over){
            over = true;
            p.setColor(Color.parseColor("#FF1A44"));
            canvas.drawRect((int)(size/5), (int)(3*size/7), (int)(4*size/5), (int)(4*size/7), p);
            p.setColor(Color.WHITE);
            p.setTextSize((float)size/12);
            canvas.drawText("RESTART?",(int)(25*size/80), (int)(3*size/7+5*size/84+size/24), p);

            p.setColor(Color.WHITE);
            p.setStrokeWidth(1);
            p.setStyle(Paint.Style.STROKE);
            canvas.drawRect((int)(size/5), (int)(3*size/7), (int)(4*size/5), (int)(4*size/7), p);
            p.setStyle(Paint.Style.FILL);

        }
        else if (!paused){
            if (started) countdown--;
            else{
                p.setColor(Color.BLACK);
                canvas.drawRect(0, 0, (int)size, (int)size, p);
                game.drawScore(canvas, p);
                p.setColor(Color.WHITE);
                p.setTextSize((float)size/12);
                canvas.drawText("TAP SCREEN TO START",(int)(5*size/80), (int)(15*size/28), p);
                p.setTextSize((float)size/16);
                p.setColor(Color.parseColor("#AAFFAA"));
                canvas.drawText("Use ARROW BUTTONS to",(int)(9*size/80), (int)(15*size/28+size/12), p);
                p.setColor(Color.parseColor("#FFAAAA"));
                canvas.drawText("AVOID RED squares!!",(int)(15*size/80), (int)(15*size/28+size/6), p);
            }

            if (countdown == 0){ //create a new enemy
                game.newEnemy();
                countdown = (int)(Math.random()*50+75);
            }
            invalidate();
        }

    }
    public void moveRight(boolean r){
        if (!paused)moveRight = r;
    }
    public void moveLeft(boolean l){
        if (!paused)moveLeft = l;
    }
    private void restart(){
        countdown = 50;
        moveLeft = false;
        moveRight = false;
        over = false;
        game.restart();
        invalidate();
    }
    public boolean pause(){
        if (!started || over){
            end();
            return false;
        }
        moveRight = false;
        moveLeft = false;
        paused = true;
        return true;
    }
    public void unPause(){
        paused = false;
        started = true;
        invalidate();
    }
    public void start(){
        if (!paused) started = true;
    }
    private void end(){
        game.forceEnd();
        Log.i("DEBUG","END");
        started = false;
        moveLeft = false;
        moveRight = false;
        over = false;
        countdown = 10;
        paused = false;
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!paused){
            started = true;
            float x = event.getX();
            float y = event.getY();
            if (over && x >= size/5 && x <= 4*size/5 &&
                    y >= 3*size/7 && y <= 4*size/7) {
                restart();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    public double playerX(){
        return game.playerX();
    }
    public double[] enemyX(){
        return game.enemyX();
    }
    public double[] enemyY(){
        return game.enemyY();
    }
    public double[] enemyV(){
        return game.enemyV();
    }
    public double score(){
        return game.score();
    }

    public boolean isOver(){
        return (over || !started);
    }
}
