package Game;

import javax.swing.ImageIcon;

//created by Derya & Alper in 2014

public class Bushy extends Creature{

    public Bushy(GamePanel panel, int x, int y, double theta) {
        super(panel, x, y, new ImageIcon(Game.class.getResource("/bushy.png")), theta);
        speed = 8;
    }
    public int getPoints(){
        return 10;
    } 
    //1 bullet is earned when a dangereous bushy is killed.
    public int getEarnedBullets() {
        return 1;
    }
}
