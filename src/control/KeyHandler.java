package control;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import model.State;
import view.GamePanel;

public class KeyHandler implements KeyListener {
	private GamePanel gp;
	
	public KeyHandler(GamePanel gp) {
		this.gp = gp;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		
		if (code == KeyEvent.VK_ESCAPE) {
			switch (gp.gameState) {
				case PLAYING:
					gp.gameState = State.PAUSE;
					break;
				case PAUSE:
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
