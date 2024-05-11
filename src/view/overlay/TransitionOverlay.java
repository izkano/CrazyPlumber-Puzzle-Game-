package view.overlay;

import view.Button;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class TransitionOverlay extends Overlay {

    private BufferedImage victoryWindow;

    private view.Button nextLevelBtn;
    private view.Button retryBtn;
    private view.Button mainMenuBtn;

    private int screenWidth;
    private int screenHeight;


    public TransitionOverlay(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        loadAssets();
    }


    public view.Button getNextLevelButton() {
        return nextLevelBtn;
    }

    public view.Button getRetryButton() {
        return retryBtn;
    }

    public Button getMainMenuButton() {
        return mainMenuBtn;
    }


    public void loadAssets() {
        try {
            victoryWindow = ImageIO.read(getClass().getResourceAsStream("/menu/transition/victory_window.png"));;
        } catch (IOException e) {
            e.printStackTrace();
        }

        int buttonCenterXtransition = (screenWidth / 2) - 246;
        int startYtransition = screenHeight / 2;
        int gapYtransition = 100;

        // initialisation avec les images pour la transition
        this.nextLevelBtn = new view.Button("/menu/transition/buttons/nextLevel", buttonCenterXtransition, startYtransition);
        this.retryBtn = new view.Button("/menu/transition/buttons/replay", buttonCenterXtransition,startYtransition + gapYtransition);
        this.mainMenuBtn = new view.Button("/menu/transition/buttons/mainMenu", buttonCenterXtransition, startYtransition + 2*gapYtransition);
    }


    public void resetButtons() {
        nextLevelBtn.setMouseOver(false);
        mainMenuBtn.setMouseOver(false);
        retryBtn.setMouseOver(false);
    }


    private int calculateStars() {
        int movesMade = 5;
        int minMoves = 4;

        if (movesMade <= minMoves) {
            return 3;
        } else if (movesMade < minMoves * 2) {
            return 2;
        } else {
            return 1;
        }
    }


    public void drawStars(Graphics2D g2, int numStarsGained, int totalStars) {
        try {
            BufferedImage starImage = ImageIO.read(getClass().getResourceAsStream("/menu/transition/starsss.png"));
            BufferedImage emptyStarImage = ImageIO.read(getClass().getResourceAsStream("/menu/transition/emptyStar.png"));

            starImage = resizeImage(starImage, 150, 150);
            emptyStarImage = resizeImage(emptyStarImage, 150, 150);

            int starWidth = starImage.getWidth();
            int starHeight = starImage.getHeight();

            int startX = (screenWidth - (totalStars * starWidth)) / 2;
            int startY = (screenHeight - starHeight) / 2 - 230;

            for (int i = 0; i < numStarsGained; i++) {
                g2.drawImage(starImage, startX + i * starWidth, startY, null);
            }

            for (int i = numStarsGained; i < totalStars; i++) {
                g2.drawImage(emptyStarImage, startX + i * starWidth, startY, null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private BufferedImage resizeImage(BufferedImage originalImage, int width, int height) {
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();
        return resizedImage;
    }


    private void drawMessage(Graphics2D g2, int numStars) {
        String message1;
        String message2;

        try {
            Font retroFont = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/menu/Retro_Gaming.ttf"));
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(retroFont);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }

        switch (numStars) {
            case 1:
                message1 = "Vous avez obtenu 1 étoile !";
                message2 = "Vous pouvez faire mieux !";
                break;
            case 2:
                message1 = "Vous avez obtenu 2 étoile !";
                message2 = "Pas mal du tout !";
                break;
            case 3:
                message1 = "Vous avez obtenu 3 étoile !";
                message2 = "Impressionant !";
                break;
            default:
                message1 = "Meilleure chance la prochaine fois !";
                message2 = "Meilleure chance la prochaine fois !";
                break;
        }

        Font font = new Font("Retro Gaming", Font.BOLD, 23);
        g2.setFont(font);
        g2.setColor(Color.BLACK);

        FontMetrics metrics = g2.getFontMetrics(font);
        int messageWidth = metrics.stringWidth(message1);
        int messageX = (screenWidth - messageWidth) / 2;
        int messageY = screenHeight / 2 - 100;
        int message2Width = metrics.stringWidth(message2);
        int message2X = (screenWidth - message2Width) / 2;
        g2.drawString(message1, messageX, messageY);
        g2.drawString(message2, message2X, messageY+50);
    }


    public void draw(Graphics2D g2) {
        g2.drawImage(victoryWindow, screenWidth/2 - 261, screenHeight/2-400, null);

        int stars = calculateStars();
        drawStars(g2, stars,3);

        nextLevelBtn.draw(g2);
        retryBtn.draw(g2);
        mainMenuBtn.draw(g2);
        drawMessage(g2, stars);
    }
}
