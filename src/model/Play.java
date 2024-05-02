package model;

import exception.MapException;
import view.GamePanel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Play {
	private final GamePanel gp;
	private static SoundManager soundManager = SoundManager.getInstance();
	private GameMode gameMode = GameMode.CLASSIC;
	private int lvl = 1;

	private int[] amountLevel = countLevel();
	private boolean[][] unlocked;
	
	
	public Play(GamePanel gp) {
		this.gp = gp;

		setLevel(1);

		unlocked = createUnlock();
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
			case ONLINE:
				online();
				soundManager.stopBackgroundMusic();
				soundManager.playLevelMusic();
				break;
		}
	}
	
	
	private void classic() {
		if (gp.map != null) {
	        if (gp.map.won) {
	        	gp.repaint();
	            unlockNextLvl(lvl);
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
	                unlockNextLvl(lvl);
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
	            unlockNextLvl(lvl);
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
	
	private void online() {
	
	}


	/**
	 * Charge le niveau dans l'attribut Map
	 */
	public void setLevel(int level) {
		this.lvl = level;

		try {
			gp.map = new Map(gameMode,level,soundManager,gp.getWidth(),gp.getHeight());
		} catch (MapException e) {
			System.out.println(e.getMessage());
		}
	}


	public void unlockNextLvl(int lvl) {
		if (lvl<unlocked[gameMode.getValue()].length){
			unlocked[gameMode.getValue()][lvl] = true;
		}

		updateSelectOverlay();
	}


	public boolean[][] createUnlock(){
		boolean[][] unlock = new boolean[3][];
		for (int i = 0 ; i<3 ; i++){
			unlock[i] = new boolean[amountLevel[i]];
			for (int j = 1; j<amountLevel[i];j++){
				unlock[i][j] = false;
			}
			unlock[i][0] = true;
		}
		return unlock;
	}


	public int[] countLevel(){
		int[] res = new int[4];
		for (int j =0;j<4;j++){
			int i = 1;
			while (true){
				try{
					switch(j){
						case 0:
							BufferedReader reader = new BufferedReader(new FileReader("res/level/classic/" + i + ".txt"));
							break;
						case 1:
							BufferedReader reader1 = new BufferedReader(new FileReader("res/level/timer/" + i + ".txt"));
							break;
						case 2:
							BufferedReader reader2 = new BufferedReader(new FileReader("res/level/limited/" + i + ".txt"));
							break;
						case 3:
							BufferedReader reader3 = new BufferedReader(new FileReader("res/level/builder/" + i + ".txt"));
							break;
						case 4:
							BufferedReader reader4 = new BufferedReader(new FileReader("res/level/online/" + i + ".txt"));
							break;
					}
				}
				catch (IOException e){
					break;
				}
				i++;
			}
			res[j] = i-1;
		}
		return res;
	}


	private void updateSelectOverlay() {
		gp.ui.selectOverlay.update(amountLevel,unlocked);
	}


	public int[] getAmountLevel(){
		return amountLevel;
	}

	public int getLevel() {
		return this.lvl;
	}

	public GameMode getGameMode() {
		return this.gameMode;
	}

	public void setGameMode(GameMode gm) {
		this.gameMode = gm;
	}

	public boolean[][] getUnlocked(){
		return unlocked;
	}
}
