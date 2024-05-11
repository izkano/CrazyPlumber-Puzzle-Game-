package view.overlay;

import view.GamePanel;

import java.awt.*;
import view.Button;

public class SettingsOverlay extends Overlay {

    private Button backBtn;

    private Button soundButton;
    private Button noSoundButton;

    int screenWidth;
    int screenHeight;


    public SettingsOverlay(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        loadAssets();
    }


    public Button getSoundButton() {
        return soundButton;
    }

    public Button getNoSoundButton(){
        return noSoundButton;
    }

    public Button getBackButton() {
        return backBtn;
    }


    public void loadAssets() {
        backBtn = new Button("/menu/previous", 15,15);
        soundButton= new Button("/menu/setting/sound_", (screenWidth/2)-72, (screenHeight/2)-72);
        noSoundButton = new Button("/menu/setting/nosound_", (screenWidth/2)-72, (screenHeight/2)-72);
    }


    public void resetButtons() {
        soundButton.setMouseOver(false);
        noSoundButton.setMouseOver(false);
        backBtn.setMouseOver(false);
    }


    public void draw(Graphics2D g2){
        if (GamePanel.sound){
            soundButton.draw(g2);
        }
        else {
            noSoundButton.draw(g2);
        }
        backBtn.draw(g2);
    }
}
