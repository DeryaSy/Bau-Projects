package Game;

import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.ImageIcon;

//created by Derya & Alper in 2014

public class Ball implements VisualObject{
    GamePanel panel;
    int x,y;
    double theta;
    static ImageIcon icon=new ImageIcon(Game.class.getResource("/fire.png"));
    static int ball_speed = 30;
    public Ball(GamePanel panel, double theta, int x, int y) {
        this.panel = panel;
        this.x = x;
        this.y = y;
        this.theta=theta;
        new BallAnimater(this).start();
    }
    
    public void draw(Graphics g) {
       g.drawImage(icon.getImage(), x, y, panel);
       //g.fillOval(x, y, 20, 20);
    }
    
}

class BallAnimater extends Thread{
    Ball ball;
    public BallAnimater(Ball ball) {
        this.ball = ball;
    }
    
    public void run() {
        while (true) {
            if (!ball.panel.game.isPaused()) {
                ball.x -= (int) (Ball.ball_speed * Math.cos(ball.theta));
                ball.y -= (int) (Ball.ball_speed * Math.sin(ball.theta));
                if (ball.y < 200 || ball.x < 0 || ball.x > ball.panel.getWidth()) {//check if the ball is out or not
                    ball.panel.objects.remove(ball);
                    break;
                }
                destroyCreatures();
            }
            try {
                sleep(100);
            } catch (Exception e) {
            }
        }
    }

    synchronized private void destroyCreatures() {
         ArrayList<VisualObject> v = new ArrayList(ball.panel.objects);
        for(VisualObject o:v){
            if(o instanceof Creature){
                Creature creature = (Creature)o;
                if(creature.hitBy(ball)){
                    creature.setKilled(); 
                    creature.theta=ball.theta;
                    if(creature.isDangereous()){
                        ball.panel.game.addScore(creature.getPoints());
                        ball.panel.game.addBullets(creature.getEarnedBullets());
                    }
                    ball.panel.repaint();
                }
            }
        }
    }
}
