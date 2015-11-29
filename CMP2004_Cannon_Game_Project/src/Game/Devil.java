package Game;

import javax.swing.ImageIcon;

//created by Derya & Alper in 2014

public class Devil extends Creature{

    public Devil(GamePanel panel, int x, int y, double theta) {
        super(panel, x, y, new ImageIcon(Game.class.getResource("/devil.png")), theta);
        speed = 12;
    }
    public int getPoints(){
        return 16;
    } 
    //2 bullet is earned when a dangereous bushy is killed.
    public int getEarnedBullets() {
        return 2;
    }
}
