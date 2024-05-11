package control;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import model.SoundManager;
import model.State;
import view.GamePanel;

/**
 * Gestion des entrées au clavier de l'utilisateur
 */
public class KeyHandler implements KeyListener {

	private final GamePanel gp;
	private static SoundManager soundManager = SoundManager.getInstance();

	
	public KeyHandler(GamePanel gp) {
		this.gp = gp;
		
	}


	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		
		if (code == KeyEvent.VK_ESCAPE) {
			if (gp.gameState == State.PLAYING) {
				soundManager.stopLevelMusic();
				soundManager.playPauseSound();
				
			}
			
			switch (gp.gameState) {
				case PLAYING:
					gp.map.setTime_level(gp.map.getRemainnig_time());
					gp.gameState = State.PAUSE;
					break;
				case PAUSE:
					gp.map.setTimer();
					gp.gameState = State.PLAYING;
					break;
				default:
					break;
			}
		}
		if (code == KeyEvent.VK_ENTER) {
			switch (gp.gameState) {
				case PLAYING:
					gp.gameState = State.GAMEMODE;
					break;
				case GAMEMODE:
					gp.gameState = State.PLAYING;
					break;
				default:
					break;
			}
		}
	}


	@Override
	public void keyTyped(KeyEvent e) { }


	@Override
	public void keyReleased(KeyEvent e) { }
}
