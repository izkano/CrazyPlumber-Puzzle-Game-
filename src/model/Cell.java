package model;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


public class Cell {
    private int pipeType;
    private int orientation;
    private BufferedImage image;
    
    private boolean[] con;
    private boolean checked;

    private boolean connected;


    public Cell (int pipeType,int orientation){
        this.pipeType = pipeType;
        this.orientation = orientation;
        if (pipeType!=4) {
            this.connected = false;
        }
        else this.connected = true;
        loadImage(pipeType);
        initCon();
    }
    
    
    /** 
     * Charge dans l'attribut image, l'image correspondant au type de tuyau donné en argument
     * @param pipeType : 0-case vide, 1-tuyau horizontal, 2-tuyau courbé, 3-tuyau jonction, 4-tuyau de départ/arrivée
     */
    public void loadImage(int pipeType) {
        String path = null;
        switch (pipeType) {
            case 0 :
        		path = "res/pipes/none.png";
        		break;
            case 1 :
            	path = "res/pipes/horizontal.png";
                if (connected) {
                    path = "res/pipes/horizontalBlue.png";
                    }
            	break;
            case 2 :
                path = "res/pipes/curve.png";
                if (connected) {
                    path = "res/pipes/curveBLUE.png";
                path = "res/pipes/curveBlue.png";
                }
                break;
            case 3 :
                path = "res/pipes/cross.png";
                if (connected) {
                    path = "res/pipes/crossBlue.png";
                    }
                break;
            case 4:
            	path = "res/pipes/start.png";
            default:
                break;
            
        }
        
    
        try {
            BufferedImage originalImage = ImageIO.read(new File(path));
            for (int i = 0; i < orientation; i++) {
                originalImage = rotateImage(originalImage);
            }
            this.image = originalImage;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    private BufferedImage rotateImage(BufferedImage originalImage) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        BufferedImage rotatedImage = new BufferedImage(height, width, originalImage.getType());
    
        Graphics2D g2d = rotatedImage.createGraphics();
        g2d.rotate(Math.PI / 2, height / 2, width / 2);
        g2d.translate((height - width) / 2, (height - width) / 2);
        g2d.drawImage(originalImage, 0, 0, null);
        g2d.dispose();
    
        return rotatedImage;
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
    
    
    public boolean[] getCon() {
    	return this.con;
    }
    
    public boolean isChecked() {
    	return checked;
    }
    
    public void setChecked() {
    	this.checked = true;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }
    
    
    private void initCon() {
    	this.con = new boolean[4];
    	switch(pipeType) {
    		case 1:
    			con[0] = true;
    			con[1] = false;
    			con[2] = true;
    			con[3] = false;
    			break;
    		case 2:
    			con[0] = false;
    			con[1] = false;
    			con[2] = true;
    			con[3] = true;
    			break;
    		case 3:
    			con[0] = true;
    			con[1] = false;
    			con[2] = true;
    			con[3] = true;
    			break;
    		case 4:
    			con[0] = false;
    			con[1] = false;
    			con[2] = true;
    			con[3] = false;
    			break;
    	}
    }
    
    
    /**
     * Met à jour le tableau de diffusion de la cellule (décalage de toutes les valeurs à droite)
     */
    private void rotateCon() {
    	boolean last = con[3];
    	
    	for (int i = 3 ; i > 0 ; i--) {
    		con[i] = con[i-1];
    	}
    	
    	con[0] = last;
    }
    
    
    public void reset() {
    	this.checked = false;
    }
    
    
    /**
     * Fait tourner un tuyau de 90 degré dans le sens horaire, et met a jour son attribut orientation.
     */
    public void rotate(SoundManager soundManager) {
        
    	if (pipeType == 0) return;
        if (pipeType<=5){
            orientation = (orientation + 1) % 4;
        }
        
        rotateCon();

        soundManager.playRotateSound();

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
}
