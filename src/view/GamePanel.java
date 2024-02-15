package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import control.MouseHandler;
import exception.MapException;
import model.Map;
import model.State;

public class GamePanel extends JPanel implements Runnable{
	
	// SIZES
	final int originalTileSize = 32;
	final int scale = 3;
	
	public final int tileSize = originalTileSize * scale;
	public final int maxScreenCol = 10;
	public final int maxScreenRow = 10;
	final int screenWidth = tileSize * maxScreenCol;
	final int screenHeight = tileSize * maxScreenRow; 
	public final int mapOffset = tileSize;
	private  JButton aideButton;
	private JLabel aideLabel;
    
	
	// SYSTEM
	public Map map;
	private State gameState;
    private MouseHandler mouseHandler = new MouseHandler(this);
    private Thread gameThread = new Thread(this);
    

    public GamePanel(){
        this.setPreferredSize(new Dimension(tileSize*7,tileSize*7));
        this.setBackground(Color.decode("#67b835"));
		this.setDoubleBuffered(true);
		this.setFocusable(true);
		
		ImageIcon aideIcon = new ImageIcon("res/pipes/help_boutant.png");
        setLayout((LayoutManager) new FlowLayout(FlowLayout.RIGHT, 0, 0));
        aideButton = new JButton(aideIcon);
        aideButton.setContentAreaFilled(false); 
        aideButton.setBorderPainted(false);
        aideButton.setFocusPainted(false);
        aideButton.addActionListener(e -> ShowRulesGame());
        this.add(aideButton);
        aideButton.setVisible(false);
		
		this.addMouseListener(mouseHandler);
		
		this.gameState = State.MENU;
		
		setLevel(1);
    }
    private void ShowRulesGame() {   
        ImageIcon reglesIcon = new ImageIcon("res/pipes/pngegg1.png");
        JOptionPane.showMessageDialog(this, "", "Règles du jeu", JOptionPane.PLAIN_MESSAGE, reglesIcon);
    }
    
    public void startGameThread() {
        gameThread.start();
    }

    public void run() {
    	
        double drawInterval = 1000000000/60;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        
        while(gameThread != null) {
            
            currentTime = System.nanoTime();
            
            delta += (currentTime-lastTime) / drawInterval;
            
            lastTime = currentTime;
            
            if (delta>=1) {
                update();
                repaint();
                delta--;
            }
        }
    }
    
    public void update() {
		
    }
    
    public void setLevel(int level) {
    	gameState = State.PLAYING;
    	try {
    		map = new Map(level);
    	} catch (MapException e) {
    		System.out.println(e.getMessage());
    	}
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        if (gameState == State.MENU) {
        	// afficher le menu etc...
        }
        
        else if (gameState == State.PLAYING) {
        	if (map != null) {
                for (int i = 0 ; i < map.getHeight() ; i++)
                    for (int j = 0 ; j < map.getWidth() ; j++)
                    	map.drawCell(i, j, g2, j*tileSize+mapOffset, i*tileSize+mapOffset, tileSize);
                aideButton.setVisible(true);
            }
        }
        
        else if (gameState == State.PAUSE) {
        	aideButton.setVisible(true);
        	// afficher l'écran de pause...
        }
    }
}