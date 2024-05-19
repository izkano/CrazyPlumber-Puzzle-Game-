package model;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


public class Cell {

    private int pipeType;
    private boolean isCurve;
    private int orientation;

    private boolean[] con;
    private boolean checked;
    private boolean connected;

    private BufferedImage image;
    private String imagePath;
    private String imageBluePath;
    

    public Cell (int pipeType, int orientation, boolean isCurve){
        this.pipeType = pipeType;
        this.orientation = orientation;
        this.isCurve = isCurve;

        this.connected = pipeType == 1;

        initCon();
        loadImage(pipeType);
    }


    public int getPipeType() {
        return pipeType;
    }

    public int getOrientation() {
        return orientation;
    }

    public boolean[] getCon() {
        return this.con;
    }

    public BufferedImage getImage(){
        return image;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public void setPipeType(int pipeType) {
        this.pipeType = pipeType;
    }

    public void setCurve() {
        this.isCurve = true;
    }

    public void setImage(BufferedImage image){
        this.image = image;
    }

    public void setChecked() {
        this.checked = true;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
        changeImageColor();
    }

    public boolean isChecked() {
        return checked;
    }

    public boolean isConnected() {
        return connected;
    }

    public boolean isCurve() { return isCurve; }

    public void reset() {
        this.checked = false;
    }

    public void incrPipeType() {
        this.pipeType++;
        loadImage(pipeType);
    }


    private void initCon() {
        this.con = new boolean[4];
        switch(pipeType) {
            case 1:
                con[0] = false;
                con[1] = false;
                con[2] = true;
                con[3] = false;
                break;
            case 2:
                if (isCurve) {
                    con[0] = false;
                    con[1] = false;
                    con[2] = true;
                    con[3] = true;
                } else {
                    con[0] = true;
                    con[1] = false;
                    con[2] = true;
                    con[3] = false;
                }
                break;
            case 3:
                con[0] = true;
                con[1] = false;
                con[2] = true;
                con[3] = true;
                break;
        }
    }
    
    
    /** 
     * Charge dans l'attribut image, l'image correspondant au type de tuyau donné en argument
     * @param pipeType : 0-case vide, 1-tuyau horizontal, 2-tuyau courbé, 3-tuyau jonction, 4-tuyau de départ/arrivée
     */
    public void loadImage(int pipeType) {
        if (pipeType==1) {
            imagePath = "res/pipes/start.png";
            imageBluePath = "res/pipes/start.png";
        } else if(pipeType == 2 && isCurve) {
            imagePath = "res/pipes/curve.png";
            imageBluePath = "res/pipes/curveBLUE.png";
        } else if (pipeType == 2) {
            imagePath = "res/pipes/horizontal.png";
            imageBluePath = "res/pipes/horizontalBLUE.png";
        } else if (pipeType == 3) {
            imagePath = "res/pipes/cross.png";
            imageBluePath = "res/pipes/crossBLUE.png";
        } else {
            imagePath = "res/pipes/none.png";
        }


        try {
            image = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        int originalOrientation = orientation;
        int i = orientation;
        while (i > 0) {
            rotate(null);
            i--;
        }
        this.orientation = originalOrientation;
    }


    private void changeImageColor() {
        String path = (connected) ? imageBluePath : imagePath;

        try {
            image = ImageIO.read(new File(path));
            int j = orientation;
            while (j > 0) {
                rotateImage();
                j--;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    /**
     * Fait tourner un tuyau de 90 degré dans le sens horaire, et met a jour son attribut orientation.
     */
    public void rotate(SoundManager soundManager) {
        orientation = (orientation + 1) % 4;
        rotateCon();
        rotateImage();
        if (soundManager != null) soundManager.playRotateSound();
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


    public void rotateImage() {
        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage dest = new BufferedImage(width, height, image.getType());

        Graphics2D graphics2D = dest.createGraphics();

        graphics2D.translate((height - width) / 2, (height - width) / 2);
        graphics2D.rotate(Math.PI / 2, width / 2, height / 2);
        graphics2D.drawRenderedImage(image, null);

        this.image = dest;
    }
    

    /**
     * Dessine la cellule sur la fenêtre
     * @param g2
     * @param x
     * @param y
     * @param tileSize
     */
    public void draw(Graphics2D g2, int x, int y, int tileSize) {
    	if (pipeType != 0) {
    		g2.drawImage(image,x,y,tileSize,tileSize,null);
    	}
    }
}
