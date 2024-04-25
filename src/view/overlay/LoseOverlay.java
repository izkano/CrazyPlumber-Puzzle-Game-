package view.overlay;

import view.Button;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class LoseOverlay extends Overlay{

    private BufferedImage loseWindow;

    private Button retryButton;
    private Button mainMenuButton;

    int screenWidth;
    int screenHeight;

    public LoseOverlay(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        loadAssets();
    }


    public void loadAssets() {
        try {
            loseWindow = ImageIO.read(getClass().getResourceAsStream("/menu/transition/lose_window.png"));;
        } catch (IOException e) {
            e.printStackTrace();
        }

        int buttonCenterXtransition = (screenWidth / 2) - 246;
        int startYtransition = screenHeight / 4;
        int gapYtransition = 200;

        retryButton = new view.Button("/menu/transition/buttons/replay", buttonCenterXtransition, startYtransition);
        mainMenuButton = new view.Button("/menu/transition/buttons/mainMenu", buttonCenterXtransition, startYtransition+gapYtransition);
    }


    public void draw(Graphics2D g2){
        g2.drawImage(loseWindow, screenWidth/2 -261, screenHeight/2-400, null);
        retryButton.draw(g2);
        mainMenuButton.draw(g2);
    }


    public void resetButtons() {
        retryButton.setMouseOver(false);
        mainMenuButton.setMouseOver(false);
    }


    public Button getRetryButton() {
        return retryButton;
    }

    public Button getMainMenuButton() {
        return mainMenuButton;
    }
}
