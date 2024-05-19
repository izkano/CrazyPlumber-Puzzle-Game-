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
	public BuildOverlay buildOverlay;
	public int scale;
	private static SoundManager soundManager = SoundManager.getInstance();


	public UserInterface(GamePanel gp) {
		this.gp = gp;
		this.scale = gp.scale;
		this.pauseOverlay = new PauseOverlay(scale);
		this.mainOverlay = new MainOverlay(gp.screenWidth, gp.screenHeight, scale);
		this.transitionOverlay = new TransitionOverlay(gp.screenWidth, gp.screenHeight, scale);
		this.modeOverlay = new ModeOverlay(gp.screenWidth, gp.screenHeight, scale);
		this.selectOverlay = new SelectOverlay(gp.screenWidth, gp.screenHeight,gp.play.getAmountLevel(),gp.play.getUnlocked(), scale);
		this.loseOverlay = new LoseOverlay(gp.screenWidth, gp.screenHeight, scale);
		this.settingsOverlay = new SettingsOverlay(gp.screenWidth, gp.screenHeight, scale);
		this.buildOverlay = new BuildOverlay(gp.screenWidth, gp.screenHeight, scale);
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
			transitionOverlay.draw(g2,gp.play.getLevel());
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

		else if (gp.gameState == State.CREDITS) {
            creditsOverlay.draw(g2);
        }
	}
}
