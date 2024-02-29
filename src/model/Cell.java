package model;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;


public class Cell {
    private int pipeType;
    private int orientation;
    private BufferedImage image;
    
    public Cell (int pipeType,int orientation){
        this.pipeType = pipeType;
        this.orientation = orientation;
        loadImage(pipeType);
    }
    
    /** 
     * Charge dans l'attribut image, l'image correspondant au type de tuyau donné en argument
     * @param pipeType : 0-case vide, 1-tuyau horizontal, 2-tuyau courbé, 3-tuyau jonction, 4-tuyau de départ/arrivée
     */
    private void loadImage(int pipeType) {
    	String path = null;
        switch(pipeType){
        	case 0 :
        		path = "res/pipes/none.png";
        		break;
            case 1:
            	path = "res/pipes/horizontal.png";
            	break;
            case 2:
                path = "res/pipes/curve.png";
                break;
            case 3:
                path = "res/pipes/cross.png";
                break;
            case 4:
            	path = "res/pipes/start.png";
            default :
            	break;	
        }
        
        try {
            this.image = ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public int getOrientation() {
        return orientation;
    }
    

    public int getPipeType() {
        return pipeType;
    }

    
    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }
    

    public void setPipeType(int pipeType) {
        this.pipeType = pipeType;
    }
    

    public BufferedImage getImage(){
        return image;
    }


    public void setImage(BufferedImage image){
        this.image = image;
    }
    
    
    /**
     * Fait tourner un tuyau de 90 degré dans le sens horaire, et met a jour son attribut orientation.
     */
    public void rotate() {
        
    	if (pipeType == 0) return;
        orientation = (orientation + 1) % 4;
        playSound("res/pipes/pipe_rotate.wav");
        int width = image.getWidth();
	    int height = image.getHeight();

	    BufferedImage dest = new BufferedImage(height, width, image.getType());

	    Graphics2D graphics2D = dest.createGraphics();

	    graphics2D.translate((height - width) / 2, (height - width) / 2);
	    graphics2D.rotate(Math.PI / 2, height / 2, width / 2);
	    graphics2D.drawRenderedImage(image, null);

	    image = dest;
    }
    

    /**
     * Dessine la cellule sur la fenetre
     * @param g2
     * @param x
     * @param y
     * @param tileSize
     */
    public void drawCell(Graphics2D g2, int x, int y, int tileSize) {
    	if (pipeType != 0) {
    		g2.drawImage(image,x,y,tileSize,tileSize,null);
    	}
    }
     public static void playSound(String soundFilePath) {
        try {
            File soundFile = new File(soundFilePath);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
