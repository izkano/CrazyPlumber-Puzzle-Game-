package main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;

public class Case {
    private int typeDeTuyau;
    private int orientation;
    private BufferedImage image;
    public Case (int typeDeTuyau,int orientation){
        this.typeDeTuyau = typeDeTuyau;
        this.orientation = orientation;
        switch(typeDeTuyau){
            case 0:
                try {
                    this.image = ImageIO.read(new File("res/pipes/horizontal.png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    this.image = ImageIO.read(new File("res/pipes/curve.png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    this.image = ImageIO.read(new File("res/pipes/cross.png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }
    public void rotate() {
        orientation = (orientation + 1) % 4;
    }

    public int getOrientation() {
        return orientation;
    }

    public int getTypeDeTuyau() {
        return typeDeTuyau;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public void setTypeDeTuyau(int typeDeTuyau) {
        this.typeDeTuyau = typeDeTuyau;
    }

    public BufferedImage getImage(){
        return image;
    }

    public void setImage(BufferedImage image){
        this.image = image;
    }



}
