package view.overlay;

import view.Button;

import java.awt.*;

public class ModeOverlay extends Overlay {

    private Button backBtn;

    private Button classicButton;
    private Button randomButton;
    private Button timerButton;
    private Button limitedButton;
    private Button builderButton;

    int screenWidth;
    int screenHeight;
    int scale;

    public ModeOverlay(int screenWidth, int screenHeight, int scale) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.scale = scale;
        loadAssets();
    }


    public Button getClassicButton(){
        return classicButton;
    }

    public Button getRandomButton(){ return randomButton; }

    public Button getTimerButton(){
        return timerButton;
    }

    public Button getLimitedButton(){
        return limitedButton;
    }

    public Button getBuilderButton(){
        return builderButton;
    }

    public Button getBackButton() { return backBtn; }


    public void loadAssets() {
        int startYGamemode = screenHeight / 8;
        int buttonCenterXGamemode = (screenWidth / 2) - 275*scale/3;
        int gapYGamemode = 150*scale/3;

        classicButton = new Button("/menu/gamemode/buttons/classic_", buttonCenterXGamemode, startYGamemode, scale);
        randomButton = new Button("/menu/gamemode/buttons/random_",buttonCenterXGamemode, startYGamemode+gapYGamemode, scale);
        timerButton = new Button("/menu/gamemode/buttons/timer_", buttonCenterXGamemode, startYGamemode+2*gapYGamemode, scale);
        limitedButton = new Button("/menu/gamemode/buttons/limited_", buttonCenterXGamemode, startYGamemode+3*gapYGamemode, scale);
        builderButton = new Button("/menu/gamemode/buttons/builder_", buttonCenterXGamemode, startYGamemode+4*gapYGamemode, scale);
        backBtn = new Button("/menu/previous", 15,15, scale);
    }


    public void resetButtons() {
        classicButton.setMouseOver(false);
        randomButton.setMouseOver(false);
        timerButton.setMouseOver(false);
        limitedButton.setMouseOver(false);
        builderButton.setMouseOver(false);
        backBtn.setMouseOver(false);
    }


    public void draw(Graphics2D g2){
        classicButton.draw(g2);
        randomButton.draw(g2);
        timerButton.draw(g2);
        limitedButton.draw(g2);
        builderButton.draw(g2);

        backBtn.draw(g2);
    }
}
