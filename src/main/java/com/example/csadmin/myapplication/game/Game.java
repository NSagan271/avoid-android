package com.example.csadmin.myapplication.game;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by student on 5/15/17.
 */

public class Game {
    private double screenSize;
    private double fontSize;
    private Player player;
    private List<Enemy> enemies;
    private double score;
    private double highScore;
    private boolean dead;
    private SharedPreferences prefs;
    public Game(double s, SharedPreferences sp){
        dead = false;
        screenSize = s;
        fontSize = (s*3/50);
        player = new Player(s);
        enemies = new ArrayList<>();
        score = 0;
        prefs = sp;
        highScore = getHighScore();
    }
    public Game(double s, double playerX, double[] enemyX,
                double[] enemyY, double[] enemyV, double score, SharedPreferences sp){
        dead = false;
        screenSize = s;
        fontSize = (s*3/50);
        player = new Player(s, playerX);
        enemies = new ArrayList<>();
        for (int i = 0; i < enemyX.length; i++){
            enemies.add (new Enemy(screenSize, enemyX[i], enemyY[i], enemyV[i]));
        }
        this.score = score;
        prefs = sp;
        highScore = getHighScore();
    }
    private void draw(Canvas canvas, Paint p){
        p.setColor(Color.BLACK);
        canvas.drawRect(0, 0, (int)screenSize, (int)screenSize, p);
        if (!dead){
            player.draw(canvas, p);
            for (int i = 0; i < this.enemies.size(); i++)
                enemies.get(i).draw(canvas,p);
        }
        drawScore(canvas, p);
    }
    public boolean move(Canvas canvas, Paint p, boolean playerRight, boolean playerLeft){
        if (dead){
            draw(canvas, p);
            return false;
        }
        boolean cont = true;
        if(playerRight) player.moveRight();
        if(playerLeft) player.moveLeft();
        for (int i = enemies.size()-1; i >= 0; i--){
            enemies.get(i).moveDown();
            if (enemies.get(i).y() + enemies.get(i).size() >= player.y()){
                if (enemies.get(i).x() <= player.x() + player.size() &&
                        enemies.get(i).x() + enemies.get(i).size() >= player.x()){
                    cont = false;
                    break;
                }
                else if (enemies.get(i).y() > screenSize){
                    enemies.remove(i);
                    if (Math.random() < 0.95)this.newEnemy();
                }
            }
        }
        if (cont){
            draw(canvas, p);
            return true;
        }
        over(canvas, p);
        drawScore(canvas, p);
        return false;
    }
    public void drawScore(Canvas canvas, Paint p){
        p.setColor(Color.WHITE);
        p.setTextSize((float)fontSize);
        canvas.drawText("SCORE: "+ (int)Math.ceil(this.score) + " ~ HIGH SCORE: " +(int)Math.ceil(this.highScore),
                5,(int)this.fontSize, p);
    }
    public void restart(){
        player = new Player(screenSize);
        enemies = new ArrayList<>();
        score = 0;
        dead = false;
        newEnemy();
    }
    public void newEnemy(){
        enemies.add(new Enemy(screenSize));
        score +=(1.0/5);
        incrementSpeed();
    }
    private void incrementSpeed(){
        player.incrementSpeed();
        for (int i = 0; i < enemies.size(); i++){
            enemies.get(i).incrementSpeed();
        }
    }
    private void over(Canvas canvas, Paint p){
        dead = true;
        enemies = null;
        player.die(canvas, p);
        player = null;
        System.gc();
        if (score > highScore){
            saveHighScore((float)score);
            highScore = getHighScore();
        }
    }
    public void forceEnd(){
        enemies = null;

        dead = false;
        player = null;

        System.gc();
        score = 0;
        enemies = new ArrayList<>();
        player = new Player(screenSize);
    }
    private void saveHighScore(float s){
        prefs.edit().putFloat("com.example.app.score",s).apply();
    }
    private double getHighScore(){
        return prefs.getFloat("com.example.app.score",0);
    }

    public double playerX(){
        return player.x();
    }
    public double[] enemyX(){
        double[] result = new double[enemies.size()];
        for (int i = 0; i < result.length; i++){
            result[i] = enemies.get(i).x();
        }
        return result;
    }
    public double[] enemyY(){
        double[] result = new double[enemies.size()];
        for (int i = 0; i < result.length; i++){
            result[i] = enemies.get(i).y();
        }
        return result;
    }
    public double[] enemyV(){
        double[] result = new double[enemies.size()];
        for (int i = 0; i < result.length; i++){
            result[i] = enemies.get(i).speed();
        }
        return result;
    }
    public double score(){
        return score;
    }
}
