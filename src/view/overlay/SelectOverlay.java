package view.overlay;

import java.awt.*;

import view.Button;

public class SelectOverlay extends Overlay {

    private Button backBtn;

	private Button[][] level = new Button[3][];
    private Button[][] locked = new Button[3][];

    private int screenWidth;
    private int screenHeight;

    private int[] amountLevel;
    private boolean[][] unlocked;


	public SelectOverlay(int screenWidth, int screenHeight, int[] amountLevel, boolean[][] unlocked) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.amountLevel = amountLevel;
        this.unlocked = unlocked;

		loadAssets();
	}


	public void loadAssets() {
		int startYSelectLevel = screenHeight / 4;
		int gapYLevel = 125; 
        int gapXtransition = 100;

        for (int k = 0; k < 3; k++) {
            int taille = amountLevel[k];
            level[k] = new Button[taille];
            locked[k] = new Button[taille];
            int j = 0;
            int l = 0;
            for (int i = 0; i < taille; i++) {
                if (j % 5 == 0) j = 0;
                if (i%5==0 && i!=0) l++;
                level[k][i] = new Button("/menu/boxLevel", 250 + gapXtransition * j, startYSelectLevel+l*gapYLevel);
                locked[k][i] = new Button("/menu/locked", 250 + gapXtransition * j, startYSelectLevel+l*gapYLevel);
                j++;
            }
        }

        backBtn = new Button("/menu/previous", 15,15);
    }
	
	
	public void resetButtons() {
        for (Button e: level[0]) {
            e.setMouseOver(false);
        }
	}

    
	public void draw(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Retro Gaming", Font.PLAIN, 50));
        for (int i = 0; i<level[0].length;i++){
            if (unlocked[0][i]==true){
                level[0][i].draw(g2);
                if (i==0){
                    g2.drawString(String.valueOf(i + 1), level[0][i].getX()+22, level[0][i].getY()+55);
                }
                else if (i<9){
                    g2.drawString(String.valueOf(i + 1), level[0][i].getX()+15, level[0][i].getY()+55);
                }
                else if (i==10){
                    g2.drawString(String.valueOf(i + 1), level[0][i].getX()+11, level[0][i].getY()+55);
                }
                else if (i>=9 && i<19){
                    g2.drawString(String.valueOf(i + 1), level[0][i].getX()+2, level[0][i].getY()+55);
                }
            }
            
            else {
                locked[0][i].draw(g2);
            }
        }
        
        backBtn.draw(g2);
	}


    public void update(int[] amountLevel, boolean[][] unlocked) {
        this.amountLevel = amountLevel;
        this.unlocked = unlocked;
    }


    public Button[][] getLevelButton() {
        return level;
    }

    public Button getBackButton() {
        return backBtn;
    }
}
