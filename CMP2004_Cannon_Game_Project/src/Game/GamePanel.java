package Game;

import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

//created by Derya & Alper in 2014

public class GamePanel extends JPanel{
    static int max_num_creature_onscreen=3;
    ArrayList<VisualObject> objects = new ArrayList();
    Cannon cannon;
    Game game;
    public GamePanel(Game g) {
        game = g;
        cannon = new Cannon(this,250,400-26);
        objects.add(cannon);
        addKeyListener(new KeyHandler(this));
        new GameAnimater(this).start();
    }
    
    public void paint(Graphics g){
        super.paint(g);
        g.drawImage(new ImageIcon(Game.class.getResource("/bg.jpg")).getImage(), 0, 0, this);
        ArrayList<VisualObject> v = new ArrayList(objects);
        for(VisualObject o:v){
            if(o==null) continue;
            o.draw(g);
        } 
    }
    
    public void createCreature(){
        if(Math.random() < 0.1 && Creature.num_creatures < max_num_creature_onscreen){
            //create a creature with a random x coordinate between 150 and 350 px. and with random theta angle
            Creature c = null;
            double r = Math.random();
            if(r < 0.5){
                c=new Crow(this,150+(int)(200*Math.random()),200,Math.PI*2 - Math.random()*Math.PI);
            }else if( r <  0.8){
                c=new Bushy(this,150+(int)(200*Math.random()),200,Math.PI*2 - Math.random()*Math.PI);
            }else{
                c=new Devil(this,150+(int)(200*Math.random()),200,Math.PI*2 - Math.random()*Math.PI);
            }
            objects.add(c);
        }
    }

    public synchronized void removeCreature(Creature c) {
        if(objects.contains(c)){
            objects.remove(c);
            Creature.dec();
        }
    }

}

    //Move connon based on left/right keys and fire it if up key is pressed.
    class KeyHandler extends KeyAdapter {
        GamePanel panel;

        public KeyHandler(GamePanel panel) {
            this.panel = panel;
        }
        
    public void keyPressed(KeyEvent event) {
        if (!panel.game.isPaused() && !panel.game.gameOver) {
            if (event.getKeyCode() == KeyEvent.VK_LEFT) {
                panel.cannon.decTheta();
            } else if (event.getKeyCode() == KeyEvent.VK_RIGHT) {
                panel.cannon.incTheta();
            } else if (event.getKeyCode() == KeyEvent.VK_UP) {
                if (panel.game.getBullets() > 0) {// can shoot only if there is at least one bullet
                    panel.objects.add(new Ball(panel, panel.cannon.theta, panel.cannon.x-Ball.icon.getIconWidth()/2 , panel.cannon.y-(int)(Cannon.H*Math.sin(panel.cannon.theta))));
                    panel.game.addBullets(-1); // decrement the number of bullets
                }
            }
            panel.repaint();
        }else{
            JOptionPane.showMessageDialog(panel, "You may start the game!");
        }
    }
}

class GameAnimater extends Thread {
    GamePanel panel;
    public GameAnimater(GamePanel panel) {
        this.panel = panel;
    }

    public void run() {
        while (!panel.game.gameOver) {
            if(!panel.game.isPaused()){
                if(panel.game.noMoreBullet()){
                  panel.game.gameOver = true;
                    JOptionPane.showMessageDialog(panel, "Game is over: You have no bullet.");
                }
                panel.createCreature();
                panel.repaint();
            }
            try {
                sleep(100);
            } catch (Exception e) {
            }
        }
    }
}