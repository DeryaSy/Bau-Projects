package Game;

import javax.swing.ImageIcon;

//created by Derya & Alper in 2014

public class Crow extends Creature{

    public Crow(GamePanel panel, int x, int y, double theta) {
        super(panel, x, y, new ImageIcon(Game.class.getResource("/crow.png")), theta);
        speed=5;
    }
    
    public int getPoints(){
        return 6;
    }
}
