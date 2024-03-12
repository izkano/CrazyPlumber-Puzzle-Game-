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
		
		else if (gp.gameState == State.TRANSITION) {
			if (isIn(e, ui.getNextLevelButton())) {
				gp.setLevel(gp.getLevel()+1);
			} else if (isIn(e, ui.getRetryButton())) {
				gp.setLevel(gp.getLevel());
			} else if (isIn(e, ui.getMainMenuButton())) {
				gp.gameState = State.MENU;
			}
		}
		
		else if (gp.gameState == State.MENU) {
			if (isIn(e, ui.getStartGameBtn())) {
				gp.gameState = State.SELECT;
			} else if (isIn(e, ui.getCreditsBtn())) {
				gp.gameState = State.MENU; 
			} else if (isIn(e, ui.getExitGameBtn())) {
				System.exit(0);
			}
		}


	}
	

	@Override
	public void mousePressed(MouseEvent e) {
		if (gp.gameState == State.PAUSE) {
			
		}
		if (gp.gameState == State.MENU) {
			if (isIn(e, ui.getStartGameBtn())) {
				ui.getStartGameBtn().setMouseOver(true);
			} else if (isIn(e, ui.getCreditsBtn())) {
				ui.getCreditsBtn().setMouseOver(true);
			} else if (isIn(e, ui.getExitGameBtn())) {
				ui.getExitGameBtn().setMouseOver(true);
			}
			gp.repaint(); 
		}
		if (gp.gameState == State.TRANSITION) {
			if (isIn(e, ui.getNextLevelButton())) {
				ui.getNextLevelButton().setMouseOver(true);
			} else if (isIn(e, ui.getMainMenuButton())) {
				ui.getMainMenuButton().setMouseOver(true);
			} else if (isIn(e, ui.getRetryButton())) {
				ui.getRetryButton().setMouseOver(true);
			}
			gp.repaint(); 
		}

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (gp.gameState == State.PAUSE) {
			ui.resetButtons();
		}
		if (gp.gameState == State.MENU) {
			ui.getStartGameBtn().setMouseOver(false);
			ui.getCreditsBtn().setMouseOver(false);
			ui.getExitGameBtn().setMouseOver(false);
			gp.repaint(); 
		}
		if (gp.gameState == State.TRANSITION) {
			ui.getNextLevelButton().setMouseOver(false);
			ui.getMainMenuButton().setMouseOver(false);
			ui.getRetryButton().setMouseOver(false);
			gp.repaint(); 
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
