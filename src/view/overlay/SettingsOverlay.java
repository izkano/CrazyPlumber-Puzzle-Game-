package view.overlay;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import view.Button;

import javax.imageio.ImageIO;

public class SettingsOverlay extends Overlay {

    private BufferedImage settingsWindow;

    private Button backBtn;
    private Button musicButton;
    private Button sfxButton;

    int screenWidth;
    int screenHeight;
    int scale;

    public SettingsOverlay(int screenWidth, int screenHeight, int scale) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.scale = scale;
        loadAssets();
    }


    public Button getMusicButton() {
        return musicButton;
    }

    public Button getSfxButton() { return sfxButton; }

    public Button getBackButton() {
        return backBtn;
    }


    public void loadAssets() {
        try {
            settingsWindow = ImageIO.read(getClass().getResourceAsStream("/menu/setting/settingsWindow.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        backBtn = new Button("/menu/previous", 15,15, scale);
        musicButton= new Button("/menu/setting/sound", (screenWidth/2)-120, (screenHeight/2)-92, scale);
        sfxButton= new Button("/menu/setting/sound", (screenWidth/2)+50, (screenHeight/2)-92, scale);
    }


    public void resetButtons() {
        musicButton.setMouseOver(false);
        sfxButton.setMouseOver(false);
        backBtn.setMouseOver(false);
    }


    public void draw(Graphics2D g2){
        g2.drawImage(settingsWindow,0,0,null);
        musicButton.draw(g2);
        sfxButton.draw(g2);
        backBtn.draw(g2);
    }
}
