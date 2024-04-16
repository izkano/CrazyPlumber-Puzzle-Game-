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
		
		
		switch (gameMode) {
			case CLASSIC : 
				classic();
				soundManager.stopBackgroundMusic();
				soundManager.playLevelMusic();
				break;
			case TIMER:
				timer();
				soundManager.stopBackgroundMusic();
				
				if (gp.gameState == State.PLAYING) {
					soundManager.playTimerMusic();
				}
				break;
			case LIMITED:
				limited();
				soundManager.stopBackgroundMusic();
				soundManager.playLevelMusic();
				break;
			case BUILDER:
				builder();
				soundManager.stopBackgroundMusic();
				soundManager.playLevelMusic();
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
	            
				soundManager.playWinSound();	
				
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
				soundManager.stopTimerMusic();
				soundManager.playLostBoom();
				gp.gameState=State.GAMEOVER;
				gp.map.setTimer_fiel();
			}
	        if (gp.map.won) {
					soundManager.stopTimerMusic();
					gp.repaint();
	                gp.unlockNextLvl(gp.getLevel());
	                soundManager.playWinSound();
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
				soundManager.playWinSound();
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
