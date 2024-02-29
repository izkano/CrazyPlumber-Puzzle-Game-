package control;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import model.State;
import view.Button;
import view.GamePanel;
import view.SelectLevel;
import view.UserInterface;


/**
 * Gestion des actions de la souris de l'utilisateur
 */
public class MouseHandler extends MouseAdapter implements MouseListener {
	
	private GamePanel gp;
	private UserInterface ui;
	private SelectLevel sl;
	
	public MouseHandler(GamePanel gp) {
		this.gp = gp;
		this.ui = gp.getUserInterface();
		this.sl = gp.getSelectLevel();
	}


	@Override
	public void mouseClicked(MouseEvent e) {
		
		// GAME STATE : PLAYING
		if (gp.gameState == State.PLAYING) {
			int mouseX = e.getX() - gp.mapOffset;
	        int mouseY = e.getY() - gp.mapOffset;
	        gp.map.rotatePipe(mouseX, mouseY,gp.tileSize);
		}
		
		
		// GAME STATE : PAUSE
		if (gp.gameState == State.PAUSE) {
			
			if ( isIn(e, ui.getPlayButtonPause()) ) {
				gp.gameState = State.PLAYING;
			}
			
			else if ( isIn(e, ui.getSettingsButtonPause()) ) {
				gp.gameState = State.SETTINGS;
			}
			
			else if ( isIn(e, ui.getMenuButtonPause()) ) {
				gp.gameState = State.SELECT;
			}
		}

		// GAME STATE : SELECT

		if (gp.gameState == State.SELECT) {
			for (int i = 0; i<sl.getLevelButton().size();i++){
				if ( isIn(e, sl.getLevelButton().get(i)) && gp.getUnlock().get(i)==true) { 
					gp.setLevel(i+1);
					gp.gameState = State.PLAYING;
				}
			}
		}
		
	}
	

	@Override
	public void mousePressed(MouseEvent e) {
		if (gp.gameState == State.PAUSE) {
			if ( isIn(e, ui.getPlayButtonPause()) ) {
				ui.getPlayButtonPause().setMouseOver(true);
			}
			
			if ( isIn(e, ui.getSettingsButtonPause()) ) {
				ui.getSettingsButtonPause().setMouseOver(true);
			}
			
			if ( isIn(e, ui.getMenuButtonPause()) ) {
				ui.getMenuButtonPause().setMouseOver(true);
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (gp.gameState == State.PAUSE) {
			ui.resetButtons();
		}
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		System.out.println("x : " + e.getX() + "  y : " + e.getY());
		
		if ( isIn(e, ui.getPlayButtonPause()) ) {
			ui.getPlayButtonPause().setMouseOver(true);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) { }

	@Override
	public void mouseExited(MouseEvent e) { }
	
	
	private boolean isIn(MouseEvent e, Button b) {
		return b.getBounds().contains(e.getX(), e.getY());
	}
}
