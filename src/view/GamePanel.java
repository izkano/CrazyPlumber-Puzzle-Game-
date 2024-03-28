package view;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import control.KeyHandler;
import control.MouseHandler;
import exception.MapException;
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
	public final int mapOffset = tileSize;
    public static boolean sound=true;
	private  JButton helpButton;
	private JLabel aideLabel;
    
    private int[] amountLevel = countLevel();
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
    private boolean[][] unlocked;

    public GraphicsEnvironment ge;
    public Font retro;

    private int difficulty;
    private int gamemode;
    private int move;

    private BufferedImage playingBackground;
    private BufferedImage gridBackground;

    

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
        
        setLevel(1);
        

        try {
            ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            retro = Font.createFont(Font.TRUETYPE_FONT, Main.class.getResourceAsStream("/menu/Retro_Gaming.ttf"));
            ge.registerFont(retro);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
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
    private void loadGridBackground() {
        try {
            gridBackground = ImageIO.read(getClass().getResourceAsStream("/menu/grilleMain.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public Thread getGameThread() {
    	return this.gameThread;
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
	public void update() {
	    if (gameState == State.PLAYING && map != null) { // Vérifier également que transitioning est false
	    	play.play();
	    }
	}
    
    
    
    /**
     * @param int level : numéro du niveau 
     * Charge le niveau dans l'attribut de type Map
     */
    public void setLevel(int level) {
        this.lvl = level;
    	try {
    		map = new Map(gamemode,level);
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
            loadGridBackground();
            int gridX = (this.getWidth() - gridBackground.getWidth()) / 2 -50;
            int gridY = (this.getHeight() - gridBackground.getHeight()) / 2 -50;
            int newWidth = this.getWidth() -300;
            int newHeight = this.getHeight() -300;
            Composite originalComposite = g2.getComposite();
            AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.85f);
            g2.setComposite(alphaComposite);
            g2.drawImage(gridBackground, gridX, gridY,newWidth, newHeight,  null);
            g2.setComposite(originalComposite);
            
        	if (map != null) {
                for (int i = 0 ; i < map.getHeight() ; i++)
                    for (int j = 0 ; j < map.getWidth() ; j++)
                    	map.drawCell(i, j, g2, j*tileSize+mapOffset, i*tileSize+mapOffset, tileSize);
                helpButton.setVisible(true);
            }
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Retro Gaming", Font.PLAIN, 50));
            g2.drawString("Niveau "+lvl, 340, 75);
            if (gamemode == 2){
                g2.drawString("Coups restants : "+map.getMove(), 250, 125);
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
        else if (gameState == State.GAMEOVER){
            sl.draw(g2);
        }
        else if (gameState == State.SETTINGS){
            sl.draw(g2);
        }
        helpButton.setVisible(true);
    }
    
    
    public void unlockNextLvl(int lvl){
        if (lvl<unlocked[gamemode].length){
            unlocked[gamemode][lvl] = true;
        }
    }
    

    public boolean[][] getUnlock(){
        return unlocked;
    }
    

    public boolean[][] createUnlock(){
        boolean[][] unlock = new boolean[3][];
        for (int i = 0; i<3;i++){
            unlock[i] = new boolean[amountLevel[i]];
            for (int j = 1; j<amountLevel[i];j++){
                unlock[i][j] = false;
            }
            unlock[i][0] = true;
        }
        return unlock;
    }
    

    public int[] countLevel(){
        int[] res = new int[4];
        for (int j =0;j<4;j++){
            int i = 1;
            while (true){
                try{
                    switch(j){
                        case 0:
                            BufferedReader reader = new BufferedReader(new FileReader("res/level/classic/" + i + ".txt"));
                            break;
                        case 1:
                            BufferedReader reader1 = new BufferedReader(new FileReader("res/level/timer/" + i + ".txt"));
                            break;
                        case 2:
                            BufferedReader reader2 = new BufferedReader(new FileReader("res/level/limited/" + i + ".txt"));
                            break;
                        case 3:
                            BufferedReader reader3 = new BufferedReader(new FileReader("res/level/builder/" + i + ".txt"));
                            break;
                    }
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

