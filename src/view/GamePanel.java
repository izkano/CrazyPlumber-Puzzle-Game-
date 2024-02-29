package view;

import java.util.LinkedList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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

import control.KeyHandler;
import control.MouseHandler;
import exception.MapException;
import model.*;


/**
 * Moteur principal du jeu
 */
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
	public State gameState;
	private UserInterface ui = new UserInterface(this);
    private SelectLevel sl = new SelectLevel(this);
    private MouseHandler mouseHandler = new MouseHandler(this);
    private KeyHandler keyHandler = new KeyHandler(this);
    private Thread gameThread = new Thread(this);
    
    private int lvl = 1;
    private LinkedList<Boolean> unlocked;

    public GamePanel(){
        this.setPreferredSize(new Dimension(tileSize*maxScreenCol,tileSize*maxScreenRow));
        this.setBackground(Color.decode("#67b835"));
		this.setDoubleBuffered(true);
		this.setFocusable(true);
		
		ImageIcon aideIcon = new ImageIcon("res/pipes/help_button.png");
        setLayout((LayoutManager) new FlowLayout(FlowLayout.RIGHT, 0, 0));
        aideButton = new JButton(aideIcon);
        aideButton.setContentAreaFilled(false); 
        aideButton.setBorderPainted(false);
        aideButton.setFocusPainted(false);
        aideButton.addActionListener(e -> ShowRulesGame());
        this.add(aideButton);
        aideButton.setVisible(false);
		
		this.addMouseListener(mouseHandler);
		this.addKeyListener(keyHandler);
		
		this.gameState = State.SELECT;
		
		setLevel(lvl);
        unlocked = createUnlock();
    }
    
    public SelectLevel getSelectLevel() {
    	return sl;
    }

    public UserInterface getUserInterface() {
    	return this.ui;
    }
    

    private void ShowRulesGame() {   
        ImageIcon reglesIcon = new ImageIcon("res/pipes/help_button.png");
        JOptionPane.showMessageDialog(this, "", "Règles du jeu", JOptionPane.PLAIN_MESSAGE, reglesIcon);
    }
    
    
    /**
     * Lancement du thread pour la boucle principale du jeu
     */
    public void startGameThread() {
        gameThread.start();
    }

    
    /**
     * Boucle principale 
     */
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
    
    
    /**
     * Méthode appelé tous les tours de boucles, met à jour la partie méchanique du jeu
     */
    public void update() {
    	if (map.isWon()){
            unlockNextLvl(lvl);
            lvl++;
            setLevel(lvl);
        }
    }
    
    
    /**
     * @param int level : numéro du niveau 
     * Charge le niveau dans l'attribut de type Map
     */
    public void setLevel(int level) {
    	//gameState = State.PLAYING;
        this.lvl = level;
    	try {
    		map = new Map(level);
    	} catch (MapException e) {
    		System.out.println(e.getMessage());
    	}
    }

    
    /**
     * Affichage des composants graphiques, appelée depuis repaint() l.106 à chaque tour de boucle du jeu
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        if (gameState == State.MENU) {

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

        	ui.draw(g2);
        }
        
        else if (gameState == State.SELECT) {
        	sl.draw(g2);
        }

        aideButton.setVisible(true);
        	// afficher l'écran de pause...
        
    }
    
    public void unlockNextLvl(int lvl){
        if (lvl<unlocked.size()){
            unlocked.set(lvl,true);
        }
    }

    public LinkedList<Boolean> getUnlock(){
        return unlocked;
    }

    public LinkedList<Boolean> createUnlock(){
        LinkedList<Boolean> unlock = new LinkedList<Boolean>();
        BufferedReader reader;
        int i = 1;
        while (true){
            try{
                reader = new BufferedReader(new FileReader("res/level/" + i + ".txt"));
                unlock.add(false);
            }
            catch (IOException e){
                break;
            }
            i++;
        }
        unlock.set(0,true);
        return unlock;
    }
}

