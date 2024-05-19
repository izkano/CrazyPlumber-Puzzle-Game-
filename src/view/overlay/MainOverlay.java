package view.overlay;

import view.Button;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class MainOverlay extends Overlay {

    private BufferedImage mainBackground;
    private BufferedImage additionalImage; // Ajoutez cette ligne

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

    public Button getStartBtn() {
        return startBtn;
    }

    public Button getCreditsBtn() {
        return creditsBtn;
    }

    public Button getExitBtn() {
        return exitBtn;
    }

    public void loadAssets() {
        try {
            mainBackground = ImageIO.read(getClass().getResourceAsStream("/menu/bgMain.jpg"));
            BufferedImage originalImage = ImageIO.read(getClass().getResourceAsStream("/menu/gameLogo.png")); // Chargez l'image supplémentaire
            additionalImage = resizeImage(originalImage,900 , 470 ); // Redimensionnez l'image supplémentaire
        } catch (IOException e) {
            e.printStackTrace();
        }

        int buttonCenterXmainMenu = (screenWidth / 2) - 314;
        int startYmainMenu = (screenHeight / 4) + 250;
        int gapYmainMenu = 160;

        startBtn = new Button("/menu/main/buttons/newGame", buttonCenterXmainMenu, startYmainMenu);
        creditsBtn = new Button("/menu/main/buttons/credits", buttonCenterXmainMenu, startYmainMenu + gapYmainMenu);
        exitBtn = new Button("/menu/main/buttons/exit", buttonCenterXmainMenu, startYmainMenu + 2 * gapYmainMenu);
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(resultingImage, 0, 0, null);
        g2d.dispose();
        return outputImage;
    }

    public void resetButtons() {
        startBtn.setMouseOver(false);
        creditsBtn.setMouseOver(false);
        exitBtn.setMouseOver(false);
    }

    public void draw(Graphics2D g2) {
        g2.drawImage(mainBackground, 0, 0, null);

        // Dessinez l'image supplémentaire redimensionnée
        int imageX = (screenWidth - additionalImage.getWidth()) / 2;
        int imageY = 20; // Positionnez l'image en haut de l'écran
        g2.drawImage(additionalImage, imageX, imageY, additionalImage.getWidth(), additionalImage.getHeight(), null);

        startBtn.draw(g2);
        creditsBtn.draw(g2);
        exitBtn.draw(g2);
    }
}
