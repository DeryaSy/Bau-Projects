package Game;

import java.awt.Graphics;
import javax.swing.ImageIcon;

//created by Derya & Alper in 2014

public abstract class Creature  implements VisualObject{
    static int num_creatures;
    GamePanel panel;
    int x,y;
    double theta;
    ImageIcon icon; 
    boolean killed = false;
    int die_counter = 10;
    int speed = 10;
    public Creature(GamePanel panel, int x, int y,ImageIcon icon, double theta) {
        this.panel = panel;
        this.x=x;
        this.y=y;
        this.icon = icon;
        this.theta=theta;
        Creature.inc();
        new CreatureAnimater(this).start();
    }
    //check if the creature target my house
    public boolean isDangereous(){
        int ph = panel.getHeight();
        int pw = panel.getWidth();
        double slope = Math.tan(theta);//slop of the creature's path
        double Xpoint = ((ph+slope*x-y)/slope);//the x point where it cross the border
        return Xpoint >=0 && Xpoint <= pw;
    }
    public void draw(Graphics g) {
        g.drawImage(icon.getImage(), x, y, panel);
        if(killed){
            die_counter--;
            if(die_counter <=0){
                panel.removeCreature(this);
            }
        }
    }
    public static void inc(){
        num_creatures++;
    }
    public static synchronized void dec(){
        num_creatures--;
    }

    public boolean hitBy(Ball ball) {
        int h=icon.getIconHeight();
        int w=icon.getIconWidth();
        int bw=ball.icon.getIconWidth();
        int bh=ball.icon.getIconHeight();
        
        boolean cond1=(ball.x>=x && ball.x<=x+w) || (ball.x+bw >=x && ball.x+bw<= x+w);
        boolean cond2=(ball.y>=y && ball.y<=y+h) || (ball.y+bh>=y && ball.y+bh<=y+h) ;
        
        return cond1&&cond2;
    }

    public abstract int getPoints();
    public int speed(){
        return speed;
    }
    
    //number of bullets that will be earned when a dangereous creature is killed.
    //by default no bullet is earned.
    public int getEarnedBullets() {
        return 0;
    }

    public void setKilled() {
        killed = true;
        speed = 5;
        icon = new ImageIcon(Game.class.getResource("/ghost.png"));
    }
}
class CreatureAnimater extends Thread{
    Creature creature;
    public CreatureAnimater(Creature c) {
        this.creature = c;
    }
    
    public void run(){
        while (true) {
            if (!creature.panel.game.isPaused()) {
                creature.x -= (int) (creature.speed() * Math.cos(creature.theta));
                creature.y -= (int) (creature.speed() * Math.sin(creature.theta));
                if (creature.y > creature.panel.getHeight() || creature.x < 0 || creature.x > creature.panel.getWidth()) {//check if the ball is out or not
                    creature.panel.removeCreature(creature);
                    if (creature.isDangereous() && creature.y > creature.panel.getHeight()) {
                        creature.panel.game.addScore(-creature.getPoints() / 2);//negative score if creature crosses the border
                    }
                    break;
                }
            }
            try {
                sleep(100);
            } catch (Exception e) {
            }
        }
    }
}
