package view;

import java.util.LinkedList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.imageio.ImageIO;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import control.KeyHandler;
import control.MouseHandler;
import exception.MapException;
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
	public final int mapOffset = tileSize;
	private  JButton helpButton;
	private JLabel aideLabel;
    

	// SYSTEM
	public Map map;
	public Play play;
	public State gameState;
	private UserInterface ui = new UserInterface(this);
    private SelectLevel sl = new SelectLevel(this);
    private MouseHandler mouseHandler = new MouseHandler(this);
    private KeyHandler keyHandler = new KeyHandler(this);
    private Thread gameThread = new Thread(this);
    
    private int lvl = 1;
    private LinkedList<Boolean> unlocked;
    private int[] amountLevel = countLevel();
    private int difficulty;
    private int gamemode;
    private int move;

    private BufferedImage playingBackground;
    

    public GamePanel() {
        this.setPreferredSize(new Dimension(tileSize*maxScreenCol,tileSize*maxScreenRow));
        this.setBackground(Color.decode("#6735"));
		this.setDoubleBuffered(true);
		this.setFocusable(true);
        loadBackgroundImages();
        setCursor();
		
		ImageIcon aideIcon = new ImageIcon("res/pipes/help_button.png");
        setLayout((LayoutManager) new FlowLayout(FlowLayout.RIGHT, 0, 0));
        helpButton = new JButton(aideIcon);
        helpButton.setContentAreaFilled(false); 
        helpButton.setBorderPainted(false);
        helpButton.setFocusPainted(false);
        helpButton.addActionListener(e -> showRulesGame());
        this.add(helpButton);
        helpButton.setVisible(false);
		
		this.addMouseListener(mouseHandler);
		this.addKeyListener(keyHandler);
		
		this.gameState = State.MENU;
        unlocked = createUnlock();
        
        this.play = new Play(this);
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
    
    public SelectLevel getSelectLevel() {
    	return sl;
    }

    public UserInterface getUserInterface() {
    	return this.ui;
    }
    
    public int getLevel() {
    	return this.lvl;
    }

    private void showRulesGame() {   
        ImageIcon reglesIcon = new ImageIcon("res/pipes/help_button.png");
        JOptionPane.showMessageDialog(this, "Pour gagner rien de plus simple : Il suffit de relier les tuyaux afin de former un circuit fermé reliant la base à la fin. Bonne chance !", "Règles du jeu", JOptionPane.PLAIN_MESSAGE, reglesIcon);
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
    private boolean transitioning = false; // Ajouter comme attribut de classe

	public void update() {
	    if (gameState == State.PLAYING && map != null) { // Vérifier également que transitioning est false
	    	
	    	play.play();
	    	
	        /*if (map.isWon()) {
	            transitioning = true; // Empêche l'exécution répétée
	
	            // Temporiser l'exécution du code de transition
	            Timer timer = new Timer(500, e -> {
	                unlockNextLvl(lvl);
	                setLevel(lvl);
	                Cell.playSound("res/pipes/win.wav");
	                gameState = State.TRANSITION;
	                repaint(); // Pour s'assurer que l'UI est mis à jour
	                transitioning = false; // Réinitialise le drapeau pour permettre de nouvelles transitions
	            });
	            timer.setRepeats(false); // S'assurer que le Timer ne se répète pas
	            timer.start();
	        }*/
	    }
	}
    

    
    
    /**
     * @param int level : numéro du niveau 
     * Charge le niveau dans l'attribut de type Map
     */
    public void setLevel(int level) {
    	gameState = State.PLAYING;
        this.lvl = level;
    	try {
    		map = new Map(level);
            move = map.countMove();
    	} catch (MapException e) {
    		System.out.println(e.getMessage());
    	}
    }

    public void setDifficulty(int difficulty){
        this.difficulty = difficulty;
        gameState = State.SELECT;
    }
    
    /**
     * Affichage des composants graphiques, appelée depuis repaint() l.106 à chaque tour de boucle du jeu
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.drawImage(playingBackground, 0, 0, this.getWidth(), this.getHeight(), null);
        
        if (gameState == State.MENU) {
            
            ui.drawMainMenu(g2);
        }
        
        else if (gameState == State.PLAYING) {
        	if (map != null) {
                for (int i = 0 ; i < map.getHeight() ; i++)
                    for (int j = 0 ; j < map.getWidth() ; j++)
                    	map.drawCell(i, j, g2, j*tileSize+mapOffset, i*tileSize+mapOffset, tileSize);
                helpButton.setVisible(true);
            }
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.BOLD, 50));
            g2.drawString("Niveau "+lvl, 375, 75);
            if (gamemode == 2){
                g2.drawString("Coups restants : "+map.getMove(), 375, 125);
            }
        }
        
        else if (gameState == State.PAUSE) {

        	ui.draw(g2);
        }
        
        else if (gameState == State.SELECT) {
        	sl.draw(g2);
        }
        else if (gameState == State.TRANSITION) {
        	ui.draw(g2);
        }
        else if (gameState == State.GAMEMODE){
            sl.draw(g2);
        }
        helpButton.setVisible(true);
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
    

    public int[] countLevel(){
        int[] res = new int[4];
        for (int j =0;j<4;j++){
            int i = 1;
            while (true){
                try{
                    BufferedReader reader = new BufferedReader(new FileReader("res/level/"+j+"/" + i + ".txt"));
                }
                catch (IOException e){
                    break;
                }
                i++;
            }
            res[j] = i-1;
        }
        return res;
    }

    public int[] getAmountLevel(){
        return amountLevel;
    }

    public void setGameMode(int gamemode){
        this.gamemode = gamemode;
    }

    public int getGamemode(){
        return gamemode;
    }
}

