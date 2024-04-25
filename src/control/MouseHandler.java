package control;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import model.GameMode;
import model.SoundManager;
import model.State;
import view.*;


/**
 * Gestion des actions de la souris de l'utilisateur
 */
public class MouseHandler extends MouseAdapter implements MouseListener {
	private final GamePanel gp;
	private final UserInterface ui;
	private final SoundManager soundManager;

	private final Cursor normalCursor;
    private final Cursor pressedCursor;

	
	public MouseHandler(GamePanel gp) {
		this.gp = gp;
		this.soundManager = gp.soundManager;
		this.ui = gp.ui;

        Image normalImage = Toolkit.getDefaultToolkit().getImage("res/images/cursor_shiny.png");
        this.normalCursor = Toolkit.getDefaultToolkit().createCustomCursor(normalImage, new Point(0, 0), "NormalCursor");

        Image pressedImage = Toolkit.getDefaultToolkit().getImage("res/images/cursor_shinyON.png");
        this.pressedCursor = Toolkit.getDefaultToolkit().createCustomCursor(pressedImage, new Point(0, 0), "PressedCursor");
	}


	@Override
	public void mouseClicked(MouseEvent e) {
		if (isInAnyButton(e)) {
            soundManager.playClickSound();
        }
		
		// GAME STATE : PLAYING
		if (gp.gameState == State.PLAYING) {
			int mouseX = e.getX() - gp.mapOffset;
	        int mouseY = e.getY() - gp.mapOffset;
	        gp.map.rotatePipe(mouseX, mouseY,gp.tileSize);
		}
		
		// GAME STATE : PAUSE
		else if (gp.gameState == State.PAUSE) {
			if ( isIn(e, ui.pauseOverlay.getCloseBtnPause()) || isIn(e, ui.pauseOverlay.getContinueBtn()) ) {
				gp.map.setTimer();
				gp.gameState = State.PLAYING;
			} else if ( isIn(e, ui.pauseOverlay.getSelectBtn()) ) {
				gp.gameState = State.SELECT;
			} else if ( isIn(e, ui.pauseOverlay.getSettingsBtn()) ) {
				gp.gameState = State.SETTINGS;
			} else if ( isIn(e, ui.pauseOverlay.getMenuBtn()) ) {
				gp.gameState = State.MENU;
			}
		}

		// GAME STATE : SELECT
		else if (gp.gameState == State.SELECT) {
			for (int i = 0; i < ui.selectOverlay.getLevelButton()[gp.play.getGameMode().getValue()].length ; i++){
				if ( isIn(e, ui.selectOverlay.getLevelButton()[gp.play.getGameMode().getValue()][i]) && gp.play.getUnlocked()[gp.play.getGameMode().getValue()][i]) {
					gp.play.setLevel(i+1);
					gp.gameState = State.PLAYING;
				}
			}
			if (isIn(e, ui.selectOverlay.getBackButton())) {
				gp.gameState = State.GAMEMODE;
			}
		}
		
		// GAME STATE : TRANSITION
		else if (gp.gameState == State.TRANSITION) {
			if (isIn(e, ui.transitionOverlay.getNextLevelButton())) {
				gp.play.setLevel(gp.play.getLevel()+1);
				gp.gameState = State.PLAYING;
			} else if (isIn(e, ui.transitionOverlay.getRetryButton())) {
				gp.play.setLevel(gp.play.getLevel());
				gp.gameState = State.PLAYING;
			} else if (isIn(e, ui.transitionOverlay.getMainMenuButton())) {
				gp.gameState = State.MENU;
			}
		}

		// GAME STATE : MENU
		else if (gp.gameState == State.MENU) {
			if (isIn(e, ui.mainOverlay.getStartBtn())) {
				gp.gameState = State.GAMEMODE;
			} else if (isIn(e, ui.mainOverlay.getCreditsBtn())) {
				gp.gameState = State.MENU; 
			} else if (isIn(e, ui.mainOverlay.getExitBtn())) {
				System.exit(0);
			}
		}

		// GAME STATE : GAMEMODE
		else if (gp.gameState == State.GAMEMODE) {
			if (isIn(e, ui.modeOverlay.getClassicButton())) {
				gp.play.setGameMode(GameMode.CLASSIC);
				gp.gameState = State.SELECT;
			} else if (isIn(e, ui.modeOverlay.getTimerButton())) {
				gp.play.setGameMode(GameMode.TIMER);
				gp.gameState = State.SELECT;
			} else if (isIn(e, ui.modeOverlay.getLimitedButton())) {
				gp.play.setGameMode(GameMode.LIMITED);
				gp.gameState = State.SELECT;
			} else if (isIn(e, ui.modeOverlay.getBuilderButton())) {
				gp.play.setGameMode(GameMode.BUILDER);
				gp.gameState = State.SELECT;
			} else if (isIn(e,ui.modeOverlay.getOnlineButton())){
				gp.play.setGameMode(GameMode.ONLINE);
				gp.gameState=State.SELECT;
			}
			else if (isIn(e, ui.modeOverlay.getBackButton())) {
				gp.gameState = State.MENU;
			}
		}

		else if (gp.gameState == State.GAMEOVER) {
			if (isIn(e, ui.loseOverlay.getRetryButton())) {
				gp.play.setLevel(gp.play.getLevel());
				gp.gameState = State.PLAYING;
			} else if (isIn(e, ui.loseOverlay.getMainMenuButton())) {
				gp.gameState = State.MENU;
			}
		}

		else if (gp.gameState == State.SETTINGS) {
			if (isIn(e, ui.settingsOverlay.getSoundButton()) || isIn(e, ui.settingsOverlay.getNoSoundButton())){
				GamePanel.sound = !GamePanel.sound;
			} else if (isIn(e, ui.settingsOverlay.getBackButton())) {
				gp.gameState = State.PAUSE;
			}
		}
	}
	

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
            gp.setCursor(pressedCursor);
        }

		if (gp.gameState == State.PAUSE) {
			if ( isIn(e, ui.pauseOverlay.getCloseBtnPause()) ) {
				ui.pauseOverlay.getCloseBtnPause().setMouseOver(true);
			} else if ( isIn(e, ui.pauseOverlay.getContinueBtn()) ) {
				ui.pauseOverlay.getContinueBtn().setMouseOver(true);
			} else if ( isIn(e, ui.pauseOverlay.getSelectBtn()) ) {
				ui.pauseOverlay.getSelectBtn().setMouseOver(true);
			} else if ( isIn(e, ui.pauseOverlay.getSettingsBtn()) ) {
				ui.pauseOverlay.getSettingsBtn().setMouseOver(true);
			} else if ( isIn(e, ui.pauseOverlay.getMenuBtn()) ) {
				ui.pauseOverlay.getMenuBtn().setMouseOver(true);
			}
		}
		
		else if (gp.gameState == State.MENU) {
			if (isIn(e, ui.mainOverlay.getStartBtn())) {
				ui.mainOverlay.getStartBtn().setMouseOver(true);
			} else if (isIn(e, ui.mainOverlay.getCreditsBtn())) {
				ui.mainOverlay.getCreditsBtn().setMouseOver(true);
			} else if (isIn(e, ui.mainOverlay.getExitBtn())) {
				ui.mainOverlay.getExitBtn().setMouseOver(true);
			}
			gp.repaint(); 
		}
		
		else if (gp.gameState == State.TRANSITION) {
			if (isIn(e, ui.transitionOverlay.getNextLevelButton())) {
				ui.transitionOverlay.getNextLevelButton().setMouseOver(true);
			} else if (isIn(e, ui.transitionOverlay.getMainMenuButton())) {
				ui.transitionOverlay.getMainMenuButton().setMouseOver(true);
			} else if (isIn(e, ui.transitionOverlay.getRetryButton())) {
				ui.transitionOverlay.getRetryButton().setMouseOver(true);
			}
			gp.repaint(); 
		}

		else if (gp.gameState == State.GAMEMODE){
			if (isIn(e, ui.modeOverlay.getClassicButton())) {
				ui.modeOverlay.getClassicButton().setMouseOver(true);
			} 
			else if (isIn(e, ui.modeOverlay.getTimerButton())) {
				ui.modeOverlay.getTimerButton().setMouseOver(true);
			} 
			else if (isIn(e, ui.modeOverlay.getLimitedButton())) {
				ui.modeOverlay.getLimitedButton().setMouseOver(true);
			} 
			else if (isIn(e, ui.modeOverlay.getBuilderButton())) {
				ui.modeOverlay.getBuilderButton().setMouseOver(true);
			} 
			else if (isIn(e, ui.modeOverlay.getOnlineButton())) {
				ui.modeOverlay.getOnlineButton().setMouseOver(true);
			} 
			else if (isIn(e, ui.modeOverlay.getBackButton())) {
				ui.modeOverlay.getBackButton().setMouseOver(true);
			}
		}
	}
	

	@Override
	public void mouseReleased(MouseEvent e) {

		if (e.getButton() == MouseEvent.BUTTON1) {
            gp.setCursor(normalCursor);
        }
		
		if (gp.gameState == State.PAUSE) {
			ui.pauseOverlay.resetButtons();
		}
		
		else if (gp.gameState == State.MENU) {
			ui.mainOverlay.resetButtons();
			gp.repaint(); 
		}
		
		else if (gp.gameState == State.TRANSITION) {
			ui.transitionOverlay.resetButtons();
			gp.repaint(); 
		}

		else if (gp.gameState == State.GAMEMODE) {
			ui.modeOverlay.resetButtons();
		}

		else if (gp.gameState == State.GAMEOVER) {
			ui.loseOverlay.resetButtons();
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) { }

	@Override
	public void mouseExited(MouseEvent e) { }
	
	
	private boolean isIn(MouseEvent e, Button b) {
		return b.getBounds().contains(e.getX(), e.getY());
	}

	private boolean isInAnyButton(MouseEvent e) {
		UserInterface ui = gp.ui;
	
		switch (gp.gameState) {
			case PAUSE:
				return isIn(e, ui.pauseOverlay.getCloseBtnPause()) || isIn(e, ui.pauseOverlay.getContinueBtn()) ||
					   isIn(e, ui.pauseOverlay.getSelectBtn()) || isIn(e, ui.pauseOverlay.getSettingsBtn()) ||
					   isIn(e, ui.pauseOverlay.getMenuBtn());
			case MENU:
				return isIn(e, ui.mainOverlay.getStartBtn()) || isIn(e, ui.mainOverlay.getCreditsBtn()) ||
					   isIn(e, ui.mainOverlay.getExitBtn());
			case TRANSITION:
				return isIn(e, ui.transitionOverlay.getNextLevelButton()) || isIn(e, ui.transitionOverlay.getRetryButton()) ||
					   isIn(e, ui.transitionOverlay.getMainMenuButton());
			case GAMEMODE:
				return isIn(e, ui.modeOverlay.getClassicButton()) || isIn(e, ui.modeOverlay.getTimerButton()) || isIn(e, ui.modeOverlay.getOnlineButton()) ||
					   isIn(e, ui.modeOverlay.getLimitedButton()) || isIn(e, ui.modeOverlay.getBuilderButton()) ||
					   isIn(e, ui.modeOverlay.getBackButton());
			default:
				return false;
		}
	}
}
