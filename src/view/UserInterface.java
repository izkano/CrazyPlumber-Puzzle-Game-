package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import java.awt.FontMetrics;

import model.State;


public class UserInterface {
	private GamePanel gp;
	
	// WINDOW IMAGES
	private BufferedImage pauseWindow;
	private BufferedImage victoryWindow;
	private BufferedImage mainBackground;
	private BufferedImage starImage;


	// BUTTONS : TRANSITION
	private Button nextLevelBtn;
	private Button retryBtn;
	private Button mainMenuBtn;
	


	// BUTTONS : MAIN MENU
	private Button startGameBtn;
	private Button creditsBtn;
	private Button exitGameBtn;
	
	// BUTTONS : PAUSE MENU
	private Button closeBtnPause;
	private Button continueBtn;
	private Button menuBtn;
	private Button selectBtn;
	private Button settingsBtn;
	
    public Object getNextLevelButton;
	
	
	public UserInterface(GamePanel gp) {
		this.gp = gp;
		
		loadAssets();
	}
	
	
	/**
	 * Charge toutes les images dans les attributs correspondants et création des boutons
	 */
	public void loadAssets() {		
		try {
			pauseWindow = ImageIO.read(getClass().getResourceAsStream("/menu/pause/pause_window.png"));;
			victoryWindow = ImageIO.read(getClass().getResourceAsStream("/menu/transition/victory_window.png"));;
			mainBackground = ImageIO.read(getClass().getResourceAsStream("/menu/bgMain.jpg"));;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	
		String basePath = "/menu/transition/buttons/"; // Chemin de base pour accéder au dossier des images des boutons

		int buttonCenterXtransition = (gp.screenWidth / 2) - 300;
		int startYtransition = gp.screenHeight / 2; // Ajuste selon le besoin
		int gapYtransition = 100; // Ajuste l'espacement selon le besoin
		
		// initialisation avec les images pour la transition
		this.nextLevelBtn = new Button(basePath + "nextLevel", buttonCenterXtransition, startYtransition);
		this.retryBtn = new Button(basePath + "replay", buttonCenterXtransition,startYtransition + gapYtransition);
		this.mainMenuBtn = new Button(basePath + "mainMenu", buttonCenterXtransition, startYtransition + 2*gapYtransition);
		



		// Calcul pour centrer les boutons horizontalement avec la correction
		int buttonCenterXmainMenu = (gp.screenWidth / 2) - 314; // Centre du bouton aligné avec le centre de l'écran

		// Ajustement des positions Y initiales et de l'espacement
		int startYmainMenu = gp.screenHeight / 4; // Ajuste selon le besoin
		int gapYmainMenu = 200; // Ajuste l'espacement selon le besoin
	
		// Initialisation des boutons du menu principal avec les nouvelles coordonnées
		startGameBtn = new Button("/menu/main/buttons/newGame", buttonCenterXmainMenu, startYmainMenu);
		creditsBtn = new Button("/menu/main/buttons/credits", buttonCenterXmainMenu, startYmainMenu + gapYmainMenu);
		exitGameBtn = new Button("/menu/main/buttons/exit", buttonCenterXmainMenu, startYmainMenu + 2 * gapYmainMenu);

		
		int offy = 104;
		closeBtnPause = new Button("/menu/pause/buttons/pause_menu_close_",673,187);
		
		continueBtn = new Button("/menu/pause/buttons/continue_pause_",300,320);
		selectBtn = new Button("/menu/pause/buttons/select_pause_",300,320+offy);
		settingsBtn = new Button("/menu/pause/buttons/settings_pause_",300,320+offy*2);
		menuBtn = new Button("/menu/pause/buttons/menu_pause_",300,320+offy*3);
	}
	
	
	/**
	 * Réinitialise l'état des boutons
	 */
	public void resetButtons() {
		closeBtnPause.setMouseOver(false);
		continueBtn.setMouseOver(false);
		selectBtn.setMouseOver(false);
		settingsBtn.setMouseOver(false);
		menuBtn.setMouseOver(false);
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
	
		g2.drawImage(pauseWindow,0,0,null);
		closeBtnPause.draw(g2);
		continueBtn.draw(g2);
		selectBtn.draw(g2);
		settingsBtn.draw(g2);
		menuBtn.draw(g2);
	}
	

	private void drawTransitionMenu(Graphics2D g2) {
		// Utiliser le même arrière-plan que le menu de pause
		g2.drawImage(victoryWindow, gp.screenWidth/2 -261, gp.screenHeight/2-400, null);


		int stars = calculateStars(); // Méthode à implémenter pour déterminer le nombre d'étoiles
    	drawStars(g2, stars,3);

		

		
		// Utiliser les mêmes coordonnées et logique pour dessiner les boutons spécifiques à la transition
		nextLevelBtn.draw(g2);
		retryBtn.draw(g2);
		mainMenuBtn.draw(g2);
		drawMessage(g2, stars);
	}

	private int calculateStars() {
		// Comparer le nombre de coups réalisés par le joueur avec le nombre de coups minimum
		int movesMade = gp.map.getMoveCount();
		int minMoves = gp.map.countMove();
	
		// Logique pour déterminer le nombre d'étoiles en fonction des performances du joueur
		if (movesMade <= minMoves) {
			return 3; // Le joueur a complété le niveau avec le nombre de coups minimum
		} else if (movesMade < minMoves * 2) {
			return 2; // Le joueur a utilisé moins de 50% de coups supplémentaires
		} else {
			return 1; // Le joueur a utilisé moins de 100% de coups supplémentaires
		}
	}
	
	public void drawStars(Graphics2D g2, int numStarsGained, int totalStars) {
		try {
			BufferedImage starImage = ImageIO.read(getClass().getResourceAsStream("/menu/transition/starsss.png"));
			BufferedImage emptyStarImage = ImageIO.read(getClass().getResourceAsStream("/menu/transition/emptyStar.png"));
			// Redimensionner les images si nécessaire
			starImage = resizeImage(starImage, 150, 150);
			emptyStarImage = resizeImage(emptyStarImage, 150, 150);
	
			// Taille de l'étoile redimensionnée
			int starWidth = starImage.getWidth();
			int starHeight = starImage.getHeight();
	
			// Coordonnées de départ pour dessiner les étoiles
			int startX = (gp.screenWidth - (totalStars * starWidth)) / 2; // Pour centrer les étoiles horizontalement
			int startY = (gp.screenHeight - starHeight) / 2 - 230; // Pour aligner les étoiles verticalement
	
			// Dessiner les étoiles gagnées avec l'image starsss.png
			for (int i = 0; i < numStarsGained; i++) {
				g2.drawImage(starImage, startX + i * starWidth, startY, null);
			}
	
			// Dessiner les étoiles manquantes avec l'image emptyStar.png
			for (int i = numStarsGained; i < totalStars; i++) {
				g2.drawImage(emptyStarImage, startX + i * starWidth, startY, null);
			}
		} catch (IOException e) {
			e.printStackTrace(); // ou tout autre traitement d'erreur approprié
		}
	}
	
	private BufferedImage resizeImage(BufferedImage originalImage, int width, int height) {
		BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, width, height, null);
		g.dispose();
		return resizedImage;
	}
	private void drawMessage(Graphics2D g2, int numStars) {
		String message1;
		String message2;
		try {
			Font retroFont = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/menu/Retro_Gaming.ttf"));
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(retroFont);
		} catch (IOException | FontFormatException e) {
			e.printStackTrace();
		}
		switch (numStars) {
			case 1:
				message1 = "Vous avez obtenu 1 étoile !";
				message2 = "Vous pouvez faire mieux !";
				break;
			case 2:
				message1 = "Vous avez obtenu 2 étoile !";
				message2 = "Pas mal du tout !";
				break;
			case 3:
				message1 = "Vous avez obtenu 3 étoile !";
				message2 = "Impressionant !";
				break;
			default:
				message1 = "Meilleure chance la prochaine fois !";
				message2 = "Meilleure chance la prochaine fois !";

				break;
		}
	
		// Définir la police et la couleur du message
		Font font = new Font("Retro Gaming", Font.BOLD, 23);
		g2.setFont(font);
		g2.setColor(Color.BLACK);
	
		// Dessiner le message centré en dessous des étoiles
		FontMetrics metrics = g2.getFontMetrics(font);
		int messageWidth = metrics.stringWidth(message1);
		int messageX = (gp.screenWidth - messageWidth) / 2;
		int messageY = gp.screenHeight / 2 - 100; // Adjust vertically to a position visible on the screen
		int message2Width = metrics.stringWidth(message2);
		int message2X = (gp.screenWidth - message2Width) / 2;
		g2.drawString(message1, messageX, messageY);
		g2.drawString(message2, message2X, messageY+50);
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

	public Button getCloseBtnPause() {
		return closeBtnPause;
	}
	
	public Button getContinueBtn() {
		return continueBtn;
	}
	
	public Button getSelectBtn() {
		return selectBtn;
	}
	
	public Button getSettingsBtn() {
		return settingsBtn;
	}
	
	public Button getMenuBtn() {
		return menuBtn;
	}
}
