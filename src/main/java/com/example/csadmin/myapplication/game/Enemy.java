package com.example.csadmin.myapplication.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

/**
 * Created by student on 5/15/17.
 */

class Enemy extends Character {
    Enemy(double s) {
        super(s, Math.random()*s, 0, s/50);
        super.setSpeed((Math.random()*1+1)*s/100);
    }
    void draw(Canvas canvas, Paint p){
        super.draw(canvas, p, Color.RED);
    }
}
