package view;


import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import control.KeyHandler;
import control.MouseHandler;
import main.Main;
import model.*;


/**
 * Moteur principal du jeu
 */
public class GamePanel extends JPanel implements Runnable {
	
	// SIZES
	final int originalTileSize = 32;
	final int scale = 3;
	
	public final int tileSize = originalTileSize * scale;
	public final int maxScreenCol = 10;
	public final int maxScreenRow = 10;
	final int screenWidth = tileSize * maxScreenCol;
	final int screenHeight = tileSize * maxScreenRow; 
	public final int mapOffset = tileSize*2;


	// SYSTEM
	public Map map;
	public State gameState;
    public final Play play = new Play(this);
	public UserInterface ui = new UserInterface(this);
    private final MouseHandler mouseHandler = new MouseHandler(this);
    private final KeyHandler keyHandler = new KeyHandler(this);
    private final Thread gameThread = new Thread(this);
    public static boolean sound = true;

    public GraphicsEnvironment ge;
    public Font retro;

    private BufferedImage playingBackground;
    

    public GamePanel() {
        this.setPreferredSize(new Dimension(tileSize*maxScreenCol,tileSize*maxScreenRow));
        this.setBackground(Color.decode("#6735"));
		this.setDoubleBuffered(true);
		this.setFocusable(true);

        loadBackgroundImages();
        setCursor();
        setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
		
		this.addMouseListener(mouseHandler);
		this.addKeyListener(keyHandler);
		
		this.gameState = State.MENU;

        try {
            ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            retro = Font.createFont(Font.TRUETYPE_FONT, Main.class.getResourceAsStream("/menu/Retro_Gaming.ttf"));
            ge.registerFont(retro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setCursor() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image image = toolkit.getImage("res/images/cursor_shiny.png");
        Cursor c = toolkit.createCustomCursor(image , new Point(0,0), "c");
        setCursor (c);
    }

    
    private void loadBackgroundImages() {
        try {
            playingBackground = ImageIO.read(getClass().getResourceAsStream("/menu/bgMain.jpg"));
            
        } catch (IOException e) {
            e.printStackTrace();
        }
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
	    if (gameState == State.PLAYING) {
	    	play.play();
	    }
	}
    
    
    /**
     * Affichage des composants graphiques, appelée depuis repaint() à chaque tour de boucle du jeu
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.drawImage(playingBackground, 0, 0, this.getWidth(), this.getHeight(), null);
        
        if (gameState == State.PLAYING) {
            map.draw(g2,tileSize, mapOffset);
        }

        else {
            ui.draw(g2);
        }
    }
}