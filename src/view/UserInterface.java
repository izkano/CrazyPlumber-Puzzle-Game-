package view;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import model.State;


public class UserInterface {
	private GamePanel gp;
	
	// WINDOW IMAGES
	private BufferedImage pauseWindow;
	private BufferedImage victoryWindow;
	private BufferedImage mainBackground;

	
	// BUTTONS : PAUSE
	private Button playBtnPause;
	private Button settingsBtnPause;
	private Button menuBtnPause;


	// BUTTONS : TRANSITION
	private Button nextLevelBtn;
	private Button retryBtn;
	private Button mainMenuBtn;


	// BUTTONS : MAIN MENU
	private Button startGameBtn;
	private Button creditsBtn;
	private Button exitGameBtn;

	
	// CONSTANT GRAPHIC COORDS
	private int pauseWindowXcoord;
	private int pauseWindowYcoord;	
	
	private int buttonGlobalXoffset;
	private int buttonGlobalYoffset;
	
	private int buttonWidth = 206; 
	private int buttonHeight = 243;
	
	private int pauseButtonXcoord = 105 + buttonGlobalXoffset;
	private int pauseButtonYcoord = 214 + buttonGlobalYoffset;

	private int relativeXoffset = 94 + buttonWidth;
	private int relativeYoffset = 31 + buttonHeight;
	
	
	public UserInterface(GamePanel gp) {
		this.gp = gp;
		
		loadAssets();
	}
	
	
	/**
	 * Charge toutes les images dans les attributs correspondants et création des boutons
	 */
	public void loadAssets() {		
		try {
			pauseWindow = ImageIO.read(getClass().getResourceAsStream("/menu/pause_window.png"));;
			victoryWindow = ImageIO.read(getClass().getResourceAsStream("/menu/victory_window.png"));;
			mainBackground = ImageIO.read(getClass().getResourceAsStream("/menu/bgMain.jpg"));;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String playBtnPausePath = "/menu/play_btn_pause_";
		String settingsBtnPausePath = "/menu/settings_btn_pause_";
		String menuBtnPausePath = "/menu/menu_btn_pause_";

		String nextLevelBtnPath = "/menu/next_level_btn_";
		String retryBtnPath = "/menu/retry_btn_";
		String mainMenuBtnPath = "/menu/main_menu_btn_";

		
		// PAUSE MENU
		this.pauseWindowXcoord = (gp.screenWidth / 2) - (pauseWindow.getWidth() / 2);	
		this.pauseWindowYcoord = (gp.screenHeight / 2) - (pauseWindow.getHeight() / 2);
		this.buttonGlobalXoffset = (gp.screenWidth - pauseWindow.getWidth()) / 2;
		this.buttonGlobalYoffset = (gp.screenHeight - pauseWindow.getHeight()) / 2;
		
		this.playBtnPause = new Button(playBtnPausePath, pauseButtonXcoord + buttonGlobalXoffset, pauseButtonYcoord + buttonGlobalYoffset);
		this.settingsBtnPause = new Button(settingsBtnPausePath, pauseButtonXcoord + buttonGlobalXoffset + relativeXoffset, pauseButtonYcoord + buttonGlobalYoffset);
		this.menuBtnPause = new Button(menuBtnPausePath, pauseButtonXcoord + buttonGlobalXoffset, pauseButtonYcoord + buttonGlobalYoffset + relativeYoffset);
	
		String basePath = "/menu/"; // Chemin de base pour accéder au dossier des images des boutons

		//nitialisation avec les images pour la transition
		this.nextLevelBtn = new Button(basePath + "nextLevelButton", pauseButtonXcoord + buttonGlobalXoffset, pauseButtonYcoord + buttonGlobalYoffset);
		this.retryBtn = new Button(basePath + "restartButton", pauseButtonXcoord + buttonGlobalXoffset + relativeXoffset, pauseButtonYcoord + buttonGlobalYoffset);
		this.mainMenuBtn = new Button(basePath + "mainMenuButton", pauseButtonXcoord + buttonGlobalXoffset, pauseButtonYcoord + buttonGlobalYoffset + relativeYoffset);


		// Calcul pour centrer les boutons horizontalement avec la correction
		int buttonCenterX = (gp.screenWidth / 2) - 314; // Centre du bouton aligné avec le centre de l'écran

		// Ajustement des positions Y initiales et de l'espacement
		int startY = gp.screenHeight / 4; // Ajuste selon le besoin
		int gapY = 200; // Ajuste l'espacement selon le besoin
	
		// Initialisation des boutons du menu principal avec les nouvelles coordonnées
		startGameBtn = new Button("/menu/newGame", buttonCenterX, startY);
		creditsBtn = new Button("/menu/credits", buttonCenterX, startY + gapY);
		exitGameBtn = new Button("/menu/exit", buttonCenterX, startY + 2 * gapY);


	}
	
	public Button getPlayButtonPause() {
		return this.playBtnPause;
	}
	
	public Button getSettingsButtonPause() {
		return this.settingsBtnPause;
	}
	
	public Button getMenuButtonPause() {
		return this.menuBtnPause;
	}
	
	
	/**
	 * Réinitialise l'état des boutons
	 */
	public void resetButtons() {
		playBtnPause.setMouseOver(false);
		settingsBtnPause.setMouseOver(false);
		menuBtnPause.setMouseOver(false);
	}
	
	
	/**
	 * Méthode appelée depuis le GamePanel pour afficher les composants graphiques
	 * @param g2
	 */
	public void draw(Graphics2D g2) {
		if (gp.gameState == State.PAUSE) {
			drawPauseMenu(g2);
		}
		if (gp.gameState == State.TRANSITION) {
			drawTransitionMenu(g2);
		}
		if (gp.gameState == State.MENU) {
			drawMainMenu(g2);
		}

	}
	
	
	/**
	 * Dessine le menu de pause
	 * @param g2
	 */
	private void drawPauseMenu(Graphics2D g2) {	
	
		g2.drawImage(pauseWindow, pauseWindowXcoord, pauseWindowYcoord, null);
		
		playBtnPause.draw(g2);
		settingsBtnPause.draw(g2);
		menuBtnPause.draw(g2);
	}

	private void drawTransitionMenu(Graphics2D g2) {
		// Utiliser le même arrière-plan que le menu de pause
		g2.drawImage(victoryWindow, pauseWindowXcoord, pauseWindowYcoord, null);
		
		// Utiliser les mêmes coordonnées et logique pour dessiner les boutons spécifiques à la transition
		nextLevelBtn.draw(g2);
		retryBtn.draw(g2);
		mainMenuBtn.draw(g2);
	}

	public void drawMainMenu(Graphics2D g2) {
		g2.drawImage(mainBackground, 0, 0, null);		
		startGameBtn.draw(g2);
		creditsBtn.draw(g2);
		exitGameBtn.draw(g2);
	}

	
	public Button getNextLevelButton() {
        return nextLevelBtn;
    }

    public Button getRetryButton() {
        return retryBtn;
    }

    public Button getMainMenuButton() {
        return mainMenuBtn;
    }
	public Button getExitGameBtn() {
		return exitGameBtn;
	}
	public Button getStartGameBtn() {
		return startGameBtn;
	}
	public Button getCreditsBtn() {
		return creditsBtn;
	}


}
