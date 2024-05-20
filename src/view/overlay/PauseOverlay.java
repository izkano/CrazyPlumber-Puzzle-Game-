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

	private int scale;

	public PauseOverlay(int scale) {
		this.scale = scale;
		loadAssets();
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


	public void loadAssets() {		
		try {
			pauseWindow = ImageIO.read(getClass().getResourceAsStream("/menu/pause/pause_window.png"));;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int offy = 104;
		closeBtnPause = new Button("/menu/pause/buttons/pause_menu_close_",673*scale/3,187*scale/3, scale);
		
		continueBtn = new Button("/menu/pause/buttons/continue_pause_",300*scale/3,320*scale/3, scale);
		selectBtn = new Button("/menu/pause/buttons/select_pause_",300*scale/3,(320+offy)*scale/3, scale);
		settingsBtn = new Button("/menu/pause/buttons/settings_pause_",300*scale/3,(320+offy*2)*scale/3, scale);
		menuBtn = new Button("/menu/pause/buttons/menu_pause_",300*scale/3,(320+offy*3)*scale/3, scale);
	}


	public void resetButtons() {
		closeBtnPause.setMouseOver(false);
		continueBtn.setMouseOver(false);
		selectBtn.setMouseOver(false);
		settingsBtn.setMouseOver(false);
		menuBtn.setMouseOver(false);
	}


	public void draw(Graphics2D g2) {
		g2.drawImage(pauseWindow,0,0,pauseWindow.getWidth()*scale/3, pauseWindow.getHeight()*scale/3, null);
		closeBtnPause.draw(g2);
		continueBtn.draw(g2);
		selectBtn.draw(g2);
		settingsBtn.draw(g2);
		menuBtn.draw(g2);
	}
}
