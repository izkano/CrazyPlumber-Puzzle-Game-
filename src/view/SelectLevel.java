package view;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import model.State;

import java.util.LinkedList;

public class SelectLevel {
	private GamePanel gp;
	
	// WINDOW IMAGES
	private LinkedList<Button> level = new LinkedList<Button>();
	private BufferedImage background;
    private BufferedImage test;

	private int buttonWidth = 55; 
	private int buttonHeight = 55;

    private int pauseBackGroundXcoord;
	private int pauseBackGroundYcoord;

    private int buttonGlobalXoffset;
	private int buttonGlobalYoffset;


    private int pauseButtonXcoord =  105 + buttonGlobalXoffset;
	private int pauseButtonYcoord =  214 + buttonGlobalYoffset;

	private int relativeXoffset = 30 + buttonWidth;
	private int relativeYoffset = 31 + buttonHeight;

	public SelectLevel(GamePanel gp) {
		this.gp = gp;
		loadAssets();
	}
	
	public void loadAssets() {		
        try {
			background = ImageIO.read(getClass().getResourceAsStream("/menu/pause_window.png"));;
		} catch (IOException e) {
			e.printStackTrace();
		}
        this.pauseBackGroundXcoord = (gp.screenWidth / 2) - (background.getWidth() / 2);	
		this.pauseBackGroundYcoord = (gp.screenHeight / 2) - (background.getHeight() / 2);
		this.buttonGlobalXoffset = (gp.screenWidth - background.getWidth()) / 2;
		this.buttonGlobalYoffset = (gp.screenHeight - background.getHeight()) / 2;

        // SELECT LEVEL MENU
        int i = 1;
        int j = 0;
        while (true && i<8){
            if (j%5==0) j=0;
            try {
                test = ImageIO.read(getClass().getResourceAsStream("/menu/help_button_on.png"));;
                if (i>0 && i<=5){
                    level.add(new Button("/menu/help_button_",buttonGlobalXoffset+(relativeXoffset*j)+pauseButtonXcoord,buttonGlobalYoffset+pauseButtonYcoord));
                }
                else if (i>5 && i<=10){
                    level.add(new Button("/menu/help_button_",buttonGlobalXoffset+(relativeXoffset*j)+pauseButtonXcoord,buttonGlobalYoffset+relativeYoffset+pauseButtonYcoord));
                }
                else if (i>10 && i<=15){
                    level.add(new Button("/menu/help_button_",buttonGlobalXoffset+(relativeXoffset*j)+pauseButtonXcoord,buttonGlobalYoffset+2*relativeYoffset+pauseButtonYcoord));
                }
                else if (i>15 && i<=20){
                    level.add(new Button("/menu/help_button_",buttonGlobalXoffset+(relativeXoffset*j)+pauseButtonXcoord,buttonGlobalYoffset+3*relativeYoffset+pauseButtonYcoord));
                }
            } catch (Exception e) {
                break;
            }
            i++;
            j++;
        }
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
}
