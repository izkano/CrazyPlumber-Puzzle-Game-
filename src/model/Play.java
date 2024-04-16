package model;

import view.GamePanel;

public class Play {
	private GamePanel gp;
	private GameMode gameMode = GameMode.CLASSIC;
	private static SoundManager soundManager = SoundManager.getInstance();

	
	
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
		soundManager.stopBackgroundMusic();
		soundManager.playLevelMusic();
		soundManager.stopBackgroundMusic();
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
		if (gp.map != null) {
	        if (gp.map.won) {
	        	gp.repaint();
	            gp.unlockNextLvl(gp.getLevel());
	            try {
					gp.getGameThread().sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	            
				if(GamePanel.sound) Cell.playSound("res/pipes/win.wav");	
				
	            gp.gameState = State.TRANSITION;
	        }
	        gp.map.resetCells();
	    }
	}
	
	
	private void timer() {
		if (gp.map != null) {
			if(gp.map.getTime_start()==0) {
				gp.map.setTimer();
			}
			if(gp.map.level_Fail()) {
				gp.gameState=State.GAMEOVER;
				gp.map.setTimer_fiel();
			}
	        if (gp.map.won) {
					gp.repaint();
	                gp.unlockNextLvl(gp.getLevel());
	                if(GamePanel.sound) Cell.playSound("res/pipes/win.wav");
	                try {
                       gp.getGameThread().sleep(300);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
	                gp.gameState = State.TRANSITION;
	        }
			gp.map.resetCells();
	    }

	}
	
	
	private void limited() {
		if (gp.map != null) {
	        if (gp.map.won) {
                gp.repaint();
	            gp.unlockNextLvl(gp.getLevel());
	            try {
					gp.getGameThread().sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(GamePanel.sound) Cell.playSound("res/pipes/win.wav");
	            gp.gameState = State.TRANSITION;
	        }
			
	        else if (gp.map.getMove() <= 0) {
				gp.gameState = State.GAMEOVER;
				gp.repaint(); 
	    	}
			gp.map.resetCells();
		}
	}
	
	
	private void builder() {
	
	}

}
