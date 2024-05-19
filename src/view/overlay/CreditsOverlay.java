package view.overlay;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import model.State;
import view.GamePanel;

public class CreditsOverlay extends Overlay {

    private int screenWidth;
    private int screenHeight;
    private int yOffset;
    private float alpha = 0.0f;
    private boolean fadeIn = true;
    private boolean fadeOut = false;
    private BufferedImage background;
    private final String[] credits = {
            "Issac Semani",
            "Algorithm Design",
            "Gameplay Mechanics",
            "Pathfinding Algorithm",
            "Andrei Tachou",
            "Level Design",
            "Game Balancing",
            "User Experience",
            "Ludovic Zhou",
            "Character Design",
            "Environment Art",
            "Animation",
            "Abdessamed Khadir",
            "Music Composition",
            "Sound Effects",
            "Audio Integration"
    };
    private final int[] nameIndices = {0, 4, 8, 12}; // Indices of names in the credits array
    private GamePanel gamePanel;

    public CreditsOverlay(GamePanel gamePanel, int screenWidth, int screenHeight) {
        this.gamePanel = gamePanel;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.yOffset = screenHeight;
        loadAssets();
    }

    @Override
    void loadAssets() {
        try {
            background = ImageIO.read(getClass().getResourceAsStream("/menu/bgMain.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    void resetButtons() {
        yOffset = screenHeight;
        alpha = 0.0f;
        fadeIn = true;
        fadeOut = false;
    }

    @Override
    public void draw(Graphics2D g2) {
        // Draw the original background
        g2.drawImage(background, 0, 0, screenWidth, screenHeight, null);

        // Apply fade-in and fade-out effect
        if (fadeIn) {
            alpha += 0.01f;
            if (alpha >= 1.0f) {
                alpha = 1.0f;
                fadeIn = false;
            }
        } else if (fadeOut) {
            alpha -= 0.01f;
            if (alpha <= 0.0f) {
                alpha = 0.0f;
                gamePanel.gameState = State.MENU;
                return;
            }
        }

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

        int y = yOffset;
        for (int i = 0; i < credits.length; i++) {
            String credit = credits[i];
            int stringWidth = g2.getFontMetrics().stringWidth(credit);
            int x = (screenWidth - stringWidth) / 2;

            if (isNameIndex(i)) {
                // Draw name with larger font
                try {
                    Font font = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/menu/Retro_Gaming.ttf"));
                    font = font.deriveFont(Font.BOLD, 40);  // Larger font size for names
                    g2.setFont(font);
                } catch (FontFormatException | IOException e) {
                    e.printStackTrace();
                    g2.setFont(new Font("Arial", Font.BOLD, 40));  // Fallback font
                }

                // Draw name without shadow in black
                g2.setColor(Color.BLACK);
                g2.drawString(credit, x, y);
                y += 50; // Spacing between name and next line
            } else {
                // Draw task with smaller font
                try {
                    Font font = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/menu/Retro_Gaming.ttf"));
                    font = font.deriveFont(Font.PLAIN, 25);  // Smaller font size for tasks
                    g2.setFont(font);
                } catch (FontFormatException | IOException e) {
                    e.printStackTrace();
                    g2.setFont(new Font("Arial", Font.PLAIN, 25));  // Fallback font
                }

                // Draw shadow for task
                g2.setColor(Color.BLACK);
                g2.drawString(credit, x + 3, y + 3);

                // Draw main text for task
                g2.setColor(Color.WHITE);
                g2.drawString(credit, x, y);
                y += 35; // Spacing between tasks

                // Add extra spacing after last task before next name
                if (isLastTaskBeforeNextName(i)) {
                    y += 50; // Extra spacing before the next name
                }
            }
        }

        yOffset -= 4; // Speed of scrolling

        // Check if all credits have scrolled past the screen
        if (y + yOffset < 0) {
            fadeOut = true;
        }

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f)); // Reset alpha composite for other drawings
    }

    private boolean isNameIndex(int index) {
        for (int nameIndex : nameIndices) {
            if (index == nameIndex) {
                return true;
            }
        }
        return false;
    }

    private boolean isLastTaskBeforeNextName(int index) {
        for (int nameIndex : nameIndices) {
            if (index == nameIndex - 1) {
                return true;
            }
        }
        return false;
    }
}
