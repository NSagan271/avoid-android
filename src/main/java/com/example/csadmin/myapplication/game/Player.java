package com.example.csadmin.myapplication.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;


/**
 * Created by student on 5/15/17.
 */

class Player extends Character {
    Player(double s) {
        super(s, 0, 19*s/20, s/20);
    }
    @Override
    void draw(Canvas canvas, Paint p){
        super.draw(canvas, p, Color.BLUE);
    }
    void die(Canvas canvas, Paint p){
        p.setColor(Color.BLACK);
        canvas.drawRect(0, 0, (int)super.screenSize(), (int)super.screenSize(), p);
        p.setColor(Color.BLUE);
        for (int i = 0; i < 100; i ++){
            double tempR = (Math.random()*super.size()*10 - super.size()*5);
            double tempX = super.x() + tempR*Math.cos(i*2*Math.PI/100);
            double tempY =  super.y() - tempR*Math.sin(i*2*Math.PI/100);
            double tempS = super.size()*(Math.random()*1.0/5+ 1.0/5);
            canvas.drawRect((int)tempX, (int)tempY, (int)(tempX+tempS), (int)(tempY+tempS), p);
        }

    }
}
