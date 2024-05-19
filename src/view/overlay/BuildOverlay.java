package view.overlay;

import view.Button;

import java.awt.*;

public class BuildOverlay extends Overlay{

    private Button play;
    private Button create;
    private Button back;

    private int screenWidth;
    private int screenHeight;
    private int scale;

    public BuildOverlay(int screenWidth, int screenHeight, int scale) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.scale = scale;
        loadAssets();
    }

    public void loadAssets() {
        int buttonCenterXtransition = (screenWidth / 2) - 246;
        int startYtransition = screenHeight / 4;
        int gapYtransition = 200;

        play = new Button("/menu/build/buttons/play", buttonCenterXtransition, startYtransition, scale);
        create = new Button("/menu/build/buttons/create", buttonCenterXtransition, startYtransition+gapYtransition, scale);
        back = new Button("/menu/previous", 15*scale/3,15*scale/3, scale);
    }

    public Button getPlayBtn() {
        return play;
    }

    public Button getCreateBtn() {
        return create;
    }

    public Button getBackBtn() {
        return back;
    }

    public void draw(Graphics2D g2) {
        back.draw(g2);
        play.draw(g2);
        create.draw(g2);
    }

    public void resetButtons() {
        play.setMouseOver(false);
        create.setMouseOver(false);
    }

}
