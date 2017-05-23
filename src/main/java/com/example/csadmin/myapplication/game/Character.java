package com.example.csadmin.myapplication.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by student on 5/15/17.
 */

class Character {
    private double screenSize;
    private double speed;
    private double maxSpeed;
    private double size;
    private double x;
    private double y;
    Character(double s, double x1, double y1, double size1) {
        screenSize = s;
        speed = s / 100;
        maxSpeed = speed * 1.1;
        x = x1;
        y = y1;
        size = size1;
    }
    void moveRight(){
        x += speed;
        if (x + size >= screenSize) x = screenSize - size;
    }
    void moveLeft(){
        x -= speed;
        if (x < 0) x = 0;
    }
    void moveDown(){
        y += speed;
    }
    void draw(Canvas canvas, Paint p, int c){
        p.setColor(c);
        canvas.drawRect((int)x, (int)y, (int)(x+size), (int)(y+size), p);
    }
    void draw(Canvas canvas, Paint p){
        draw(canvas,p,Color.WHITE);
    }
    void incrementSpeed(){
        if (speed < maxSpeed) speed += 0.005*speed*(maxSpeed-speed);
        else speed = maxSpeed;
    }
    double screenSize(){
        return screenSize;
    }
    double x(){
        return x;
    }
    double y(){
        return y;
    }
    double size(){
        return size;
    }
    void setSpeed(double s){
        speed = s;
        maxSpeed = speed * 1.1;
    }

}
