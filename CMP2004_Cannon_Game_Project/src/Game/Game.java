package Game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

//created by Derya & Alper in 2014
public class Game extends JFrame {

    JPanel left;
    JPanel center;
    JPanel center_top;
    GamePanel gamepanel;
    JTextField score_field;
    JTextField bullets_field;
    JLabel max_player_label;
    JLabel max_score_label;
    private int score = 100;
    private int bullets = 100;
    private static Color BG = Color.WHITE;//new Color(235,147,23);
    private String player_name;
    public boolean gameOver = false;
    public boolean paused = true;
    String max_player;
    int max_score;

    public Game() {
        setLayout(new BorderLayout());
        while (player_name == null || player_name.length() < 3 || player_name.length() > 18) {
            player_name = JOptionPane.showInputDialog("Enter Player Name (3-18 chars.)");
            if (player_name == null) {
                System.exit(0);
            }
        }
        setSize(625, 456);
        setResizable(false);
        left = new JPanel();
        left.setLayout(new BorderLayout());
        left.setBackground(BG);
        center = new JPanel();
        center_top = new JPanel();
        center_top.setBackground(BG);
        center_top.setSize(595, 20);
        gamepanel = new GamePanel(this);
        gamepanel.setSize(500, 397);
        center.setLayout(new BorderLayout());
        center.add(center_top, BorderLayout.NORTH);
        center.add(gamepanel);
        add(left, BorderLayout.WEST);
        add(center);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        populate_top();
        populate_left();
        left.setMinimumSize(new Dimension(200, 443));
    }
    public void updateMaxScore(){
        if(score >= max_score){
            max_score=score;
            max_player=player_name;
            saveMaxScore();
        }
        max_score_label.setText(max_score+"");
        max_player_label.setText(max_player);
    }
    public synchronized void addScore(int s) {
        score += s;
        score_field.setText(""+score);
        updateMaxScore();
        if (score <= 0) {
            gameOver = true;
            JOptionPane.showMessageDialog(this, "Game is over: Your score is <= 0.");
        }
    }

    public synchronized void addBullets(int b) {
        bullets += b;
        bullets_field.setText("" + bullets);
    }

    private void populate_left() {
        JPanel p = left;
        JLabel player = new JLabel(player_name, SwingConstants.CENTER);
        player.setPreferredSize(new Dimension(100, 100));
        player.setFont(new Font("Serif", Font.BOLD, 14));
        p = addComp(p, player, BorderLayout.NORTH);
        JButton start = new JButton("Start");
        start.addActionListener(new StartPauseHandler());
        p = addComp(p, start, BorderLayout.NORTH);
        
        score_field = new JTextField(""+score);
        score_field.setEditable(false);
        score_field.setColumns(4);
        JPanel score_panel = new JPanel();
        score_panel.setBackground(BG); 
        score_panel.add(new JLabel("Score:  "));
        score_panel.add(score_field);
        p = addComp(p, score_panel, BorderLayout.NORTH);
        
        
        bullets_field = new JTextField(""+bullets);
        bullets_field.setEditable(false);
        bullets_field.setColumns(4);
        JPanel bullets_panel = new JPanel();
        bullets_panel.setBackground(BG); 
        bullets_panel.add(new JLabel("Bullets:"));
        bullets_panel.add(bullets_field);
        p = addComp(p, bullets_panel, BorderLayout.NORTH);
        p = addComp(p, new JLabel(" "), BorderLayout.NORTH);
        JLabel label3 = new JLabel("Maximum Score", SwingConstants.CENTER);
        label3.setFont(new Font("Serif", Font.BOLD, 14));
        p = addComp(p,label3 , BorderLayout.NORTH);
        max_player_label = new JLabel(" ", SwingConstants.CENTER);
        max_score_label = new JLabel(" ", SwingConstants.CENTER);
        p = addComp(p, max_player_label, BorderLayout.NORTH);
        p = addComp(p, max_score_label, BorderLayout.NORTH); 
        loadMaxScore();
    }

    private JPanel addComp(JPanel p, JComponent c, String pos) {
        JPanel pp = new JPanel();
        pp.setBackground(BG);
        pp.setLayout(new BorderLayout());
        p.add(c, pos);
        p.add(pp);
        return pp;
    }
    private void saveMaxScore(){
        try {
            FileWriter fw = new FileWriter(new File("max.txt"));
            fw.write(max_player+"\n"+max_score);
            fw.close();
        } catch (IOException ex) {  }
    }
    private void loadMaxScore(){
        try {
            File file = new File("max.txt");
            if(file.exists()){ 
                Scanner scanner = new Scanner(new FileInputStream(file));
                max_player=scanner.nextLine();
                max_score = scanner.nextInt();
            }
            updateMaxScore();
        } catch (FileNotFoundException ex) { }
    }
    private void populate_top() {
        center_top.setLayout(new FlowLayout());
        center_top.add(getCreatureInfo("6 points", "/crow_icon.png", "0 bullet"));
        center_top.add(new JLabel("  "));
        center_top.add(getCreatureInfo("10 points", "/bushy_icon.png", "1 bullet"));
        center_top.add(new JLabel("  "));
        center_top.add(getCreatureInfo("16 points", "/devil_icon.png", "2 bullets"));
    }

    private JPanel getCreatureInfo(String score, String icon, String bullet) {
        JPanel p = new JPanel();
        p.setBackground(BG);
        p.setLayout(new BorderLayout());
        p.add(new JLabel(new ImageIcon(Game.class.getResource(icon))));
        JLabel label1 = new JLabel(score, SwingConstants.CENTER);
        p.add(label1, BorderLayout.NORTH);
        label1.setFont(new Font("Serif", Font.BOLD, 12));
        JLabel label2 = new JLabel(bullet, SwingConstants.CENTER);
        label2.setFont(new Font("Serif", Font.PLAIN, 12));
        p.add(label2, BorderLayout.SOUTH);
        return p;
    }

    public int getBullets() {
        return bullets;
    }

    public boolean isPaused() {
        return paused;
    }

    boolean noMoreBullet() {
        if(bullets <=0){
            int count=0;
            ArrayList<VisualObject> v = new ArrayList(gamepanel.objects);
            for(VisualObject o:v){
                if(o instanceof Ball) count++;
            }
            if(count==0) return true;
        }
        return false;
    }

    //Implemented as an inner class in order to access paused and gamepanel variables of the Game class
    class StartPauseHandler implements ActionListener {

        public void actionPerformed(ActionEvent event) {
            JButton b = (JButton) event.getSource();
            if (paused) {
                b.setText("Pause");
            } else {
                b.setText("Start");
            }
            paused = !paused;
            gamepanel.requestFocusInWindow();
        }

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Game game = new Game();
        game.setVisible(true);
    }

}
