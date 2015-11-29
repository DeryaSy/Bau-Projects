package Game;

import java.awt.Color;
import java.awt.Graphics;

//created by Derya & Alper in 2014

public class Cannon implements VisualObject{
    static int W=16, H=40;
    int x,y;
    double theta=Math.PI/2;
    GamePanel panel;
    public Cannon(GamePanel panel, int x, int y) {
        this.x = x;
        this.y = y;
        this.panel = panel;
    }
    
    public void draw(Graphics g) {
        int X=x-W/2;
        g.setColor(new Color(0,50,80)); 
        g.fillRect(x-30, y-1, 60, 2);
        while(X<x+W/2){
            g.drawLine(X, y, X-(int)(H*Math.cos(theta)), y-(int)(H*Math.sin(theta))) ;
            X++;
        }
        g.setColor(Color.BLACK);
    }
    public void incTheta(){
        if(theta < Math.PI*7/8){
            theta += Math.PI/16;
        }
    }
    public void decTheta(){
        if(theta  > Math.PI/8){
            theta -= Math.PI/16;
        }
    }
}
