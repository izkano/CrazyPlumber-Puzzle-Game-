package view;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.awt.*;
import javax.imageio.ImageIO;

import model.State;

import java.util.LinkedList;

public class SelectLevel {
	private GamePanel gp;
	
	// WINDOW IMAGES
	private LinkedList<Button> level = new LinkedList<Button>();
	private BufferedImage background;
    private BufferedImage test;

    private Button classicButton;
    private Button timerButton;
    private Button limitedButton;
    private Button builderButton;

	public SelectLevel(GamePanel gp) {
		this.gp = gp;
		loadAssets();
	}
	
	public void loadAssets() {		
        try {
			background = ImageIO.read(getClass().getResourceAsStream("/menu/pause/pause_window.png"));;
		} catch (IOException e) {
			e.printStackTrace();
		}

		int startYSelectLevel = gp.screenHeight / 4; // Ajuste selon le besoin
		int gapYtransition = 125; // Ajuste l'espacement selon le besoin
        int gapXtransition = 100; // Ajuste l'espacement selon le besoin

        int startYGamemode = gp.screenHeight / 5;
        int buttonCenterXGamemode = (gp.screenWidth / 2) - 300;
        int gapYGamemode = 150;

        // SELECT LEVEL MENU
        int i = 1;
        int j = 0;
        while (true && i<=20){
            if (j%5==0) j=0;
            try {
                test = ImageIO.read(getClass().getResourceAsStream("/menu/help_button_on.png"));;
                if (i>0 && i<=5){
                    level.add(new Button("/menu/boxLevel",250+gapXtransition*j, startYSelectLevel));
                }
                else if (i>5 && i<=10){
                    level.add(new Button("/menu/boxLevel",250+gapXtransition*j,startYSelectLevel+gapYtransition));
                }
                else if (i>10 && i<=15){
                    level.add(new Button("/menu/boxLevel",250+gapXtransition*j,startYSelectLevel+2*gapYtransition));
                }
                else if (i>15 && i<=20){
                    level.add(new Button("/menu/boxLevel",250+gapXtransition*j,startYSelectLevel+3*gapYtransition));
                }
            } catch (Exception e) {
                break;
            }
            i++;
            j++;
        }

        classicButton = new Button("/menu/transition/buttons/nextLevel", buttonCenterXGamemode, startYGamemode);
        timerButton = new Button("/menu/transition/buttons/nextLevel", buttonCenterXGamemode, startYGamemode+gapYGamemode);
        limitedButton = new Button("/menu/transition/buttons/nextLevel", buttonCenterXGamemode, startYGamemode+2*gapYGamemode);
        builderButton = new Button("/menu/transition/buttons/nextLevel", buttonCenterXGamemode, startYGamemode+3*gapYGamemode);
    }
	
	public void resetButtons() {
		for (Button e: level){
            e.setMouseOver(false);
        }
	}
	
	public void draw(Graphics2D g2) {
		if (gp.gameState == State.SELECT) {
			drawSelectLevel(g2);
		}
        if(gp.gameState == State.GAMEMODE){
            drawSelectMode(g2);
        }
	}
	
    public LinkedList<Button> getLevelButton() {
        return level;
    }

	private void drawSelectLevel(Graphics2D g2) {	
        //g2.drawImage(background, pauseBackGroundXcoord, pauseBackGroundYcoord, null);
        for (Button e: level){
            e.draw(g2);
        }
	}

    private void drawSelectMode(Graphics2D g2){
        //g2.drawImage(background, 0, 0, null);
        classicButton.draw(g2);
        timerButton.draw(g2);
        limitedButton.draw(g2);
        builderButton.draw(g2);
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
}
