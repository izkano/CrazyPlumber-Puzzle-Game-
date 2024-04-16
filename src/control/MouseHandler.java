package control;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import model.Cell;
import model.GameMode;
import model.SoundManager;
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
	private SoundManager soundManager;

	
	public MouseHandler(GamePanel gp) {
		this.gp = gp;
		this.ui = gp.getUserInterface();
		this.sl = gp.getSelectLevel();
		this.soundManager = new SoundManager();

	}


	@Override
	public void mouseClicked(MouseEvent e) {
		soundManager.playClickSound();
		
		// GAME STATE : PLAYING
		if (gp.gameState == State.PLAYING) {
			int mouseX = e.getX() - gp.mapOffset;
	        int mouseY = e.getY() - gp.mapOffset;
	        gp.map.rotatePipe(mouseX, mouseY,gp.tileSize);
		}
		
		
		// GAME STATE : PAUSE
		else if (gp.gameState == State.PAUSE) {
			if ( isIn(e, ui.getCloseBtnPause()) || isIn(e, ui.getContinueBtn()) ) {
				gp.map.setTimer();
				gp.gameState = State.PLAYING;
			} else if ( isIn(e, ui.getSelectBtn()) ) {
				gp.gameState = State.SELECT;
			} else if ( isIn(e, ui.getSettingsBtn()) ) {
				gp.gameState = State.SETTINGS;
			} else if ( isIn(e, ui.getMenuBtn()) ) {
				gp.gameState = State.MENU;
			}
		}

		// GAME STATE : SELECT
		else if (gp.gameState == State.SELECT) {
			for (int i = 0; i<sl.getLevelButton()[gp.getGamemode()].length;i++){
				if ( isIn(e, sl.getLevelButton()[gp.getGamemode()][i]) && gp.getUnlock()[gp.getGamemode()][i]==true) { 
					gp.setLevel(i+1);
					gp.gameState = State.PLAYING;
				}
			}
			if (isIn(e, sl.getBackButton())) {
				gp.gameState = State.GAMEMODE;
			}
		}
		
		// GAME STATE : TRANSITION
		else if (gp.gameState == State.TRANSITION) {
			if (isIn(e, ui.getNextLevelButton())) {
				gp.setLevel(gp.getLevel()+1);
				gp.gameState = State.PLAYING;
			} else if (isIn(e, ui.getRetryButton())) {
				gp.setLevel(gp.getLevel());
				gp.gameState = State.PLAYING;
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
				gp.play.setGameMode(GameMode.CLASSIC);
				gp.gameState = State.SELECT;
			} else if (isIn(e, sl.getTimerButton())) {
				gp.setGameMode(1);
				gp.play.setGameMode(GameMode.TIMER);
				gp.gameState = State.SELECT;
			} else if (isIn(e, sl.getLimitedButton())) {
				gp.setGameMode(2);
				gp.play.setGameMode(GameMode.LIMITED);
				gp.gameState = State.SELECT;
			} else if (isIn(e, sl.getBuilderButton())) {
				gp.setGameMode(3);
				gp.play.setGameMode(GameMode.BUILDER);
				gp.gameState = State.SELECT;
			}
			else if (isIn(e, sl.getBackButton())) {
				gp.gameState = State.MENU;
			}
		}

		else if (gp.gameState == State.GAMEOVER) {
			if (isIn(e, sl.getRetryButton2())) {
				gp.setLevel(gp.getLevel());
				gp.gameState = State.PLAYING;
			} else if (isIn(e, sl.getMainMenuButton2())) {
				gp.gameState = State.MENU;
			}
		}

		else if (gp.gameState == State.SETTINGS) {
			if (isIn(e, sl.getSoundButton()) || isIn(e, sl.getNoSoundButton())){
				GamePanel.sound = !GamePanel.sound;
			} else if (isIn(e, sl.getBackButton())) {
				gp.gameState = State.PAUSE;
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

		else if (gp.gameState == State.GAMEMODE){
			if (isIn(e, sl.getClassicButton())) {
				sl.getClassicButton().setMouseOver(true);
			} else if (isIn(e, sl.getTimerButton())) {
				sl.getTimerButton().setMouseOver(true);
			} else if (isIn(e, sl.getLimitedButton())) {
				sl.getLimitedButton().setMouseOver(true);
			} else if (isIn(e, sl.getBuilderButton())) {
				sl.getBuilderButton().setMouseOver(true);
			} else if (isIn(e, sl.getBackButton())) {
				sl.getBackButton().setMouseOver(true);
			}
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

		else if (gp.gameState == State.GAMEMODE){
			sl.resetButtons();
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
