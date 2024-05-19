package main;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.*;
import view.GamePanel;

/**
 * Main class of the game
 */
public class Main {
    public static void main(String[] args) {

        JFrame frame1 = new JFrame();
        Size size = new Size(frame1);
        frame1.setLocationRelativeTo(null);
        frame1.setResizable(false);
        frame1.add(size);
        frame1.pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame1.setLocation(dim.width/2 - frame1.getWidth()/2, dim.height/2 - frame1.getHeight()/2);
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame1.setVisible(true);

    }
}