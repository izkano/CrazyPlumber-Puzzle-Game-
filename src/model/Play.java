package model;

import javax.swing.Timer;
import javax.swing.event.SwingPropertyChangeSupport;

import view.GamePanel;

public class Play {
	private GamePanel gp;
	private GameMode gameMode = GameMode.CLASSIC;
	private boolean transitioning;
	
	public Play(GamePanel gp) {
		this.gp = gp;
		switch (gp.getGamemode()){
			case 0:
				this.gameMode = GameMode.CLASSIC;
				break;
			case 1:
				this.gameMode = GameMode.TIMER;
				break;
			case 2:
				this.gameMode = GameMode.LIMITED;
				break;
			case 3:
				this.gameMode = GameMode.BUILDER;
				break;
		}
	}
	
	public GameMode getGameMode() {
		return this.gameMode;
	}
	
	public void setGameMode(GameMode gm) {
		this.gameMode = gm;
	}
	
	public void play() {
		switch (gameMode) {
			case CLASSIC : 
				classic();
				break;
			case TIMER:
				timer();
				break;
			case LIMITED:
				limited();
				break;
			case BUILDER:
				builder();
				break;
		}
	}
	
	private void classic() {
		if (gp.map != null && !transitioning) { // Vérifier également que transitioning est false
	        if (gp.map.isWon()) {
	            transitioning = true; // Empêche l'exécution répétée
	
	            // Temporiser l'exécution du code de transition
	            Timer timer = new Timer(500, e -> {
	                gp.unlockNextLvl(gp.getLevel());
	                gp.setLevel(gp.getLevel());
	                Cell.playSound("res/pipes/win.wav");
	                gp.gameState = State.TRANSITION;
	                gp.repaint(); 
	                transitioning = false; // Réinitialise le drapeau pour permettre de nouvelles transitions
	            });
	            timer.setRepeats(false);
	            timer.start();
	        }
	    }
	}
	
	private void timer() {

	}
	
	private void limited() {
		if (gp.map != null && !transitioning) { // Vérifier également que transitioning est false
	        if (gp.map.isWon()) {
	            transitioning = true; // Empêche l'exécution répétée
	
	            // Temporiser l'exécution du code de transition
	            Timer timer = new Timer(500, e -> {
	                gp.unlockNextLvl(gp.getLevel());
	                gp.setLevel(gp.getLevel());
	                Cell.playSound("res/pipes/win.wav");
	                gp.gameState = State.TRANSITION;
	                gp.repaint(); 
	                transitioning = false; // Réinitialise le drapeau pour permettre de nouvelles transitions
	            });
	            timer.setRepeats(false);
	            timer.start();
	        }
			if (gp.map.getMove() <= 0) {
				gp.gameState = State.TRANSITION;
				gp.repaint(); 
				transitioning = false;
	    	}
		}
	}
	
	private void builder() {
	
	}

}
