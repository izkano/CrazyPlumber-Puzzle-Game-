package view.overlay;

import view.Button;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;


public class PauseOverlay extends Overlay {
	private BufferedImage pauseWindow;
	
	private Button closeBtnPause;
	private Button continueBtn;
	private Button menuBtn;
	private Button selectBtn;
	private Button settingsBtn;


	public PauseOverlay() {
		loadAssets();
	}


	/**
	 * Charge toutes les images dans les attributs correspondants et création des boutons
	 */
	public void loadAssets() {		
		try {
			pauseWindow = ImageIO.read(getClass().getResourceAsStream("/menu/pause/pause_window.png"));;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int offy = 104;
		closeBtnPause = new Button("/menu/pause/buttons/pause_menu_close_",673,187);
		
		continueBtn = new Button("/menu/pause/buttons/continue_pause_",300,320);
		selectBtn = new Button("/menu/pause/buttons/select_pause_",300,320+offy);
		settingsBtn = new Button("/menu/pause/buttons/settings_pause_",300,320+offy*2);
		menuBtn = new Button("/menu/pause/buttons/menu_pause_",300,320+offy*3);
	}


	/**
	 * Dessine le menu de pause
	 * @param g2
	 */
	public void draw(Graphics2D g2) {
		g2.drawImage(pauseWindow,0,0,null);
		closeBtnPause.draw(g2);
		continueBtn.draw(g2);
		selectBtn.draw(g2);
		settingsBtn.draw(g2);
		menuBtn.draw(g2);
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
