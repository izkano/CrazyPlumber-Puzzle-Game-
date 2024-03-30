package view;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.awt.*;
import javax.imageio.ImageIO;

import model.State;

public class SelectLevel {
	private GamePanel gp;
	
	// WINDOW IMAGES
	private Button[][] level = new Button[3][];
    private Button[][] locked = new Button[3][];
    private BufferedImage loseWindow;

    // BUTTONS : MODE DE JEU
    private Button classicButton;
    private Button timerButton;
    private Button limitedButton;
    private Button builderButton;

    // BUTTONS : GAME OVER
    private Button retryButton;
    private Button mainMenuButton;

    // BACK BUTTON
    private Button backBtn;


    // BUTTONS : SETTINGS
    private Button soundButton;
    private Button noSoundButton;

	public SelectLevel(GamePanel gp) {
		this.gp = gp;
		loadAssets();
	}
	
	public void loadAssets() {		
        try {
            loseWindow = ImageIO.read(getClass().getResourceAsStream("/menu/transition/lose_window.png"));;
		} catch (IOException e) {
			e.printStackTrace();
		}

		int startYSelectLevel = gp.screenHeight / 4; 
		int gapYLevel = 125; 
        int gapXtransition = 100; 

        int startYGamemode = gp.screenHeight / 6;
        int buttonCenterXGamemode = (gp.screenWidth / 2) - 275;
        int gapYGamemode = 150;

        // Initialisation des boutons de sélection de niveau


        for (int k = 0; k < 3; k++) {
            int taille = gp.getAmountLevel()[k];
            level[k] = new Button[taille];
            locked[k] = new Button[taille];
            int j = 0;
            int l = 0;
            for (int i = 0; i < taille; i++) {
                if (j % 5 == 0) j = 0;
                if (i%5==0 && i!=0) l++;
                level[k][i] = new Button("/menu/boxLevel", 250 + gapXtransition * j, startYSelectLevel+l*gapYLevel);
                locked[k][i] = new Button("/menu/locked", 250 + gapXtransition * j, startYSelectLevel+l*gapYLevel);
                j++;
            }
        }

        // Initialisation des boutons de sélection de mode de jeu
        classicButton = new Button("/menu/gamemode/buttons/classic_", buttonCenterXGamemode, startYGamemode);
        timerButton = new Button("/menu/gamemode/buttons/timer_", buttonCenterXGamemode, startYGamemode+gapYGamemode);
        limitedButton = new Button("/menu/gamemode/buttons/limited_", buttonCenterXGamemode, startYGamemode+2*gapYGamemode);
        builderButton = new Button("/menu/gamemode/buttons/builder_", buttonCenterXGamemode, startYGamemode+3*gapYGamemode);

        // Ajustement de la position des boutons : GAME OVER
        int buttonCenterXtransition = (gp.screenWidth / 2) - 246;
        int startYtransition = gp.screenHeight / 4;
        int gapYtransition = 200;

        // Initialisation des boutons lors d'une défaite
        retryButton = new Button("/menu/transition/buttons/replay", buttonCenterXtransition, startYtransition);
        mainMenuButton = new Button("/menu/transition/buttons/mainMenu", buttonCenterXtransition, startYtransition+gapYtransition);

        backBtn = new Button("/menu/previous", 15,15);

        soundButton= new Button("/menu/setting/sound_", (gp.screenWidth/2)-72, (gp.screenHeight/2)-72);
        noSoundButton = new Button("/menu/setting/nosound_", (gp.screenWidth/2)-72, (gp.screenHeight/2)-72);
    }
	
	public void resetButtons() {
        if (gp.gameState == State.SELECT) {
            for (Button e: level[gp.getGamemode()]){
                e.setMouseOver(false);
            }
        }
        if (gp.gameState == State.GAMEMODE){
            classicButton.setMouseOver(false);
            timerButton.setMouseOver(false);
            limitedButton.setMouseOver(false);
            builderButton.setMouseOver(false);
        }
	}
	
	public void draw(Graphics2D g2) {
		if (gp.gameState == State.SELECT) {
			drawSelectLevel(g2);
		}
        if(gp.gameState == State.GAMEMODE){
            drawSelectMode(g2);
        }
        if (gp.gameState == State.GAMEOVER){
            drawLoseWindow(g2);
        }
        if (gp.gameState == State.SETTINGS){
            drawSetting(g2);
        }
	}
	
    public Button[][] getLevelButton() {
        return level;
    }

	private void drawSelectLevel(Graphics2D g2) {	
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Retro Gaming", Font.PLAIN, 50));
        for (int i = 0; i<level[gp.getGamemode()].length;i++){
            if (gp.getUnlock()[gp.getGamemode()][i]==true){
                level[gp.getGamemode()][i].draw(g2);
                if (i==0){
                    g2.drawString(String.valueOf(i + 1), level[gp.getGamemode()][i].getX()+22, level[gp.getGamemode()][i].getY()+55);
                }
                else if (i<9){
                    g2.drawString(String.valueOf(i + 1), level[gp.getGamemode()][i].getX()+15, level[gp.getGamemode()][i].getY()+55);
                }
                else if (i==10){
                    g2.drawString(String.valueOf(i + 1), level[gp.getGamemode()][i].getX()+11, level[gp.getGamemode()][i].getY()+55);
                }
                else if (i>=9 && i<19){
                    g2.drawString(String.valueOf(i + 1), level[gp.getGamemode()][i].getX()+2, level[gp.getGamemode()][i].getY()+55);
                }
            }
            else {
                locked[gp.getGamemode()][i].draw(g2);
            }
        }
        backBtn.draw(g2);
	}

    private void drawSelectMode(Graphics2D g2){
        classicButton.draw(g2);
        timerButton.draw(g2);
        limitedButton.draw(g2);
        builderButton.draw(g2);
        backBtn.draw(g2);
    }

    private void drawLoseWindow(Graphics2D g2){
        g2.drawImage(loseWindow, gp.screenWidth/2 -261, gp.screenHeight/2-400, null);
        retryButton.draw(g2);
        mainMenuButton.draw(g2);
    }

    private void drawSetting(Graphics2D g2){
        if (GamePanel.sound){
            soundButton.draw(g2);
        }
        else {
            noSoundButton.draw(g2);
        }
        backBtn.draw(g2);
    }

    public Button getClassicButton(){
        return classicButton;
    }
    
    public Button getTimerButton(){
        return timerButton;
    }

    public Button getLimitedButton(){
        return limitedButton;
    }

    public Button getBuilderButton(){
        return builderButton;
    }

    public Button getRetryButton2() {
        return retryButton;
    }

    public Button getMainMenuButton2() {
        return mainMenuButton;
    }

    public Button getBackButton() {
        return backBtn;
    }

    public Button getSoundButton(){
        return soundButton;
    }

    public Button getNoSoundButton(){
        return noSoundButton;
    }
}
