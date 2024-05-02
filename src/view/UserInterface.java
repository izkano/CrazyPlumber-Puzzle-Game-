package view;

import java.awt.Graphics2D;

import model.SoundManager;
import model.State;
import view.overlay.*;


public class UserInterface {
	private final GamePanel gp;

	public MainOverlay mainOverlay;
	public PauseOverlay pauseOverlay;
	public TransitionOverlay transitionOverlay;
	public SelectOverlay selectOverlay;
	public ModeOverlay modeOverlay;
	public LoseOverlay loseOverlay;
	public SettingsOverlay settingsOverlay;

	private static SoundManager soundManager = SoundManager.getInstance();


	public UserInterface(GamePanel gp) {
		this.gp = gp;
		this.pauseOverlay = new PauseOverlay();
		this.mainOverlay = new MainOverlay(gp.screenWidth, gp.screenHeight);
		this.transitionOverlay = new TransitionOverlay(gp.screenWidth, gp.screenHeight);
		this.modeOverlay = new ModeOverlay(gp.screenWidth, gp.screenHeight);
		this.selectOverlay = new SelectOverlay(gp.screenWidth, gp.screenHeight,gp.play.getAmountLevel(),gp.play.getUnlocked());
		this.loseOverlay = new LoseOverlay(gp.screenWidth, gp.screenHeight);
		this.settingsOverlay = new SettingsOverlay(gp.screenWidth, gp.screenHeight);
	}
	
	
	/**
	 * Méthode appelée depuis le GamePanel pour afficher les composants graphiques
	 * @param g2
	 */
	public void draw(Graphics2D g2) {
		if (gp.gameState == State.PAUSE) {
			pauseOverlay.draw(g2);
		}

		else if (gp.gameState == State.TRANSITION) {
			transitionOverlay.draw(g2);
		}

		else if (gp.gameState == State.MENU) {
			soundManager.stopLevelMusic();
			soundManager.playBackgroundMusic();
			mainOverlay.draw(g2);
		}

		else if (gp.gameState == State.GAMEMODE) {
			modeOverlay.draw(g2);
		}

		else if (gp.gameState == State.SELECT) {
			selectOverlay.draw(g2);
		}

		else if (gp.gameState == State.GAMEOVER) {
			loseOverlay.draw(g2);
		}

		else if (gp.gameState == State.SETTINGS) {
			settingsOverlay.draw(g2);
		}
	}
}
