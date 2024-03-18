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
		else if (gp.gameState == State.PAUSE) {
			if ( isIn(e, ui.getCloseBtnPause()) || isIn(e, ui.getContinueBtn()) ) {
				gp.gameState = State.PLAYING;
			} else if ( isIn(e, ui.getSelectBtn()) ) {
				gp.gameState = State.SELECT;
			} else if ( isIn(e, ui.getSettingsBtn()) ) {
				if(gp.sound) {gp.sound=false;}
				else {gp.sound=true;}
				gp.gameState = State.PLAYING;
			} else if ( isIn(e, ui.getMenuBtn()) ) {
				gp.gameState = State.MENU;
			}
		}

		// GAME STATE : SELECT
		else if (gp.gameState == State.SELECT) {
			for (int i = 0; i<sl.getLevelButton().size();i++){
				if ( isIn(e, sl.getLevelButton().get(i)) && gp.getUnlock().get(i)==true) { 
					gp.setLevel(i+1);
					gp.gameState = State.PLAYING;
				}
			}
		}
		
		// GAME STATE : TRANSITION
		else if (gp.gameState == State.TRANSITION) {
			if (isIn(e, ui.getNextLevelButton())) {
				gp.setLevel(gp.getLevel()+1);
			} else if (isIn(e, ui.getRetryButton())) {
				gp.setLevel(gp.getLevel());
			} else if (isIn(e, ui.getMainMenuButton())) {
				gp.gameState = State.MENU;
			}
		}
		
		
		// GAME STATE : MENU
		else if (gp.gameState == State.MENU) {
			if (isIn(e, ui.getStartGameBtn())) {
				gp.gameState = State.GAMEMODE;
			} else if (isIn(e, ui.getCreditsBtn())) {
				gp.gameState = State.MENU; 
			} else if (isIn(e, ui.getExitGameBtn())) {
				System.exit(0);
			}
		}

		// GAME STATE : GAMEMODE
		else if (gp.gameState == State.GAMEMODE) {
			if (isIn(e, sl.getClassicButton())) {
				gp.setGameMode(0);
				gp.gameState = State.SELECT;
			} else if (isIn(e, sl.getTimerButton())) {
				gp.setGameMode(1);
				gp.gameState = State.SELECT;
			} else if (isIn(e, sl.getLimitedButton())) {
				gp.setGameMode(2);
				gp.gameState = State.SELECT;
			} else if (isIn(e, sl.getBuilderButton())) {
				gp.setGameMode(3);
				gp.gameState = State.SELECT;
			}
		}
	}
	

	@Override
	public void mousePressed(MouseEvent e) {
		if (gp.gameState == State.PAUSE) {
			if ( isIn(e, ui.getCloseBtnPause()) ) {
				ui.getCloseBtnPause().setMouseOver(true);
			} else if ( isIn(e, ui.getContinueBtn()) ) {
				ui.getContinueBtn().setMouseOver(true);
			} else if ( isIn(e, ui.getSelectBtn()) ) {
				ui.getSelectBtn().setMouseOver(true);
			} else if ( isIn(e, ui.getSettingsBtn()) ) {
				ui.getSettingsBtn().setMouseOver(true);
			} else if ( isIn(e, ui.getMenuBtn()) ) {
				ui.getMenuBtn().setMouseOver(true);
			}
		}
		
		else if (gp.gameState == State.MENU) {
			if (isIn(e, ui.getStartGameBtn())) {
				ui.getStartGameBtn().setMouseOver(true);
			} else if (isIn(e, ui.getCreditsBtn())) {
				ui.getCreditsBtn().setMouseOver(true);
			} else if (isIn(e, ui.getExitGameBtn())) {
				ui.getExitGameBtn().setMouseOver(true);
			}
			gp.repaint(); 
		}
		
		else if (gp.gameState == State.TRANSITION) {
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
		
		else if (gp.gameState == State.MENU) {
			ui.getStartGameBtn().setMouseOver(false);
			ui.getCreditsBtn().setMouseOver(false);
			ui.getExitGameBtn().setMouseOver(false);
			gp.repaint(); 
		}
		
		else if (gp.gameState == State.TRANSITION) {
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