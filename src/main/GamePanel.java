package main;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.IOException;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.JFrame;

public class GamePanel extends JPanel implements Runnable{

    private int tileSize = 32;
    private Case[][] map;
    Thread gameThread = new Thread(this);


    public GamePanel(){
        this.setPreferredSize(new Dimension(640,640));
        this.setBackground(Color.decode("#67b835"));
		this.setDoubleBuffered(true);
		this.setFocusable(true);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int mouseX = e.getX();
                int mouseY = e.getY();
                rotatePipe(mouseX, mouseY);
            }
        });
    }
    private void rotatePipe(int mouseX, int mouseY) {
        int row = mouseY / tileSize;
        int col = mouseX / tileSize;

        if (row >= 0 && row < map.length && col >= 0 && col < map[0].length) {
            map[row][col].rotate();
            repaint();
        }
    }
    public void startGameThread() {
        gameThread.start();
    }

    public void run() {
			
        double drawInterval = 1000000000/60;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        this.setUp();
        while(gameThread != null) {
            
            currentTime = System.nanoTime();
            
            delta += (currentTime-lastTime) / drawInterval;
            
            lastTime = currentTime;
            
            if (delta>=1) {
                update();
                repaint();
                delta--;
            }
        }
    }

    public void setUp(){
 try {
            map = Map.readMatrixFromFile("res/niveau/1.txt");
        } catch (IOException e) {
            System.out.println("Erreur lors de la lecture du fichier : " + e.getMessage());
        }
    }

    public void update() {
			
    }

    public BufferedImage rotateClockwise90(BufferedImage src, int nb) {
	    int width = src.getWidth();
	    int height = src.getHeight();

	    BufferedImage dest = new BufferedImage(height, width, src.getType());

	    Graphics2D graphics2D = dest.createGraphics();
	    graphics2D.translate((height - width) / 2, (height - width) / 2);
	    graphics2D.rotate(nb*Math.PI / 2, height / 2, width / 2);
	    graphics2D.drawRenderedImage(src, null);

	    return dest;
	}

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        if (map != null) {
            int x = 0;
            int y = 0;
            for (int i = 0; i<map.length;i++) {
                for (int j = 0; j<map[0].length;j++) {
                        AffineTransform at = AffineTransform.getTranslateInstance(x, y);
                        at.rotate(Math.toRadians(90*map[i][j].getOrientation()),j*tileSize+tileSize/2, i*tileSize+tileSize/2);
                        //g2.drawImage(map[i][j].getImage(), at, null);
                        g2.drawImage(rotateClockwise90(map[i][j].getImage(),map[i][j].getOrientation()),j*tileSize,i*tileSize,null);
                }
            }
        }
    }
}