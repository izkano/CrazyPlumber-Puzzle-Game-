package view.overlay;

import view.Button;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;


public class MainOverlay extends Overlay {

    private BufferedImage mainBackground;

    private Button startBtn;
    private Button creditsBtn;
    private Button exitBtn;

    private final int screenWidth;
    private final int screenHeight;


    public MainOverlay(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        loadAssets();
    }


    public void loadAssets() {
        try {
            mainBackground = ImageIO.read(getClass().getResourceAsStream("/menu/bgMain.jpg"));;
        } catch (IOException e) {
            e.printStackTrace();
        }

        int buttonCenterXmainMenu = (screenWidth / 2) - 314;
        int startYmainMenu = screenHeight / 4;
        int gapYmainMenu = 200;

        startBtn = new Button("/menu/main/buttons/newGame", buttonCenterXmainMenu, startYmainMenu);
        creditsBtn = new Button("/menu/main/buttons/credits", buttonCenterXmainMenu, startYmainMenu + gapYmainMenu);
        exitBtn = new Button("/menu/main/buttons/exit", buttonCenterXmainMenu, startYmainMenu + 2 * gapYmainMenu);
    }


    public void draw(Graphics2D g2) {
        g2.drawImage(mainBackground, 0, 0, null);
        startBtn.draw(g2);
        creditsBtn.draw(g2);
        exitBtn.draw(g2);
    }


    public void resetButtons() {
        startBtn.setMouseOver(false);
        creditsBtn.setMouseOver(false);
        exitBtn.setMouseOver(false);
    }


    public Button getStartBtn() {
        return startBtn;
    }

    public Button getCreditsBtn() {
        return creditsBtn;
    }

    public Button getExitBtn() {
        return exitBtn;
    }
}
