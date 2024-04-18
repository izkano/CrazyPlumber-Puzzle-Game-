package main;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.UIManager;

import view.GamePanel;

/**
 * Point d'entrée du programme
 */
public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        GamePanel gamePanel = new GamePanel();

        try {
            BufferedImage image = ImageIO.read(Main.class.getResourceAsStream("/images/plumber.png"));
            frame.setIconImage(image);
            frame.setTitle("CRAZY PLUMBER ED3A");
        } catch (Exception e) {
            // Handle any errors that occur during setting look and feel
            e.printStackTrace();
        }
        
        frame.setLocationRelativeTo(null);
        
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        frame.add(gamePanel);
        
        frame.pack();
        frame.setVisible(true);
        
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width/2 - frame.getWidth()/2, dim.height/2 - frame.getHeight()/2);
        
        gamePanel.startGameThread();
    }
}