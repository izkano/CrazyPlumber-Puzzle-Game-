package view.overlay;

import view.Button;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class LoseOverlay extends Overlay {

    private BufferedImage loseWindow;

    private Button retryButton;
    private Button mainMenuButton;

    int screenWidth;
    int screenHeight;
    int scale;

    public LoseOverlay(int screenWidth, int screenHeight, int scale) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.scale = scale;
        loadAssets();
    }


    public Button getRetryButton() {
        return retryButton;
    }

    public Button getMainMenuButton() {
        return mainMenuButton;
    }


    public void loadAssets() {
        try {
            loseWindow = ImageIO.read(getClass().getResourceAsStream("/menu/transition/lose_window.png"));;
        } catch (IOException e) {
            e.printStackTrace();
        }

        int buttonCenterXtransition = (screenWidth / 2) - 246*scale/3;
        int startYtransition = screenHeight / 4;
        int gapYtransition = 200*scale/3;

        retryButton = new view.Button("/menu/transition/buttons/replay", buttonCenterXtransition, startYtransition, scale);
        mainMenuButton = new view.Button("/menu/transition/buttons/mainMenu", buttonCenterXtransition, startYtransition+gapYtransition, scale);
    }


    public void resetButtons() {
        retryButton.setMouseOver(false);
        mainMenuButton.setMouseOver(false);
    }


    public void draw(Graphics2D g2){
        g2.drawImage(loseWindow, screenWidth/2 -261*scale/3, screenHeight/2-400*scale/3,loseWindow.getWidth()*scale/3, 
        loseWindow.getHeight()*scale/3, null);
        retryButton.draw(g2);
        mainMenuButton.draw(g2);
    }
}
