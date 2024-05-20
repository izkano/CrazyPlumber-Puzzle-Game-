package main;

import java.awt.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

import view.Button;
import view.GamePanel;

public class Size extends JPanel implements MouseListener{
    private BufferedImage playingBackground;
    private Button small;
    private Button medium;
    private Button large;

    public GraphicsEnvironment ge;
    public Font retro;

    private JFrame frame;

    public Size(JFrame frame){
    this.setPreferredSize(new Dimension(640,640));
    this.setBackground(Color.decode("#6735"));
    this.setFocusable(true);

    this.frame = frame;
    this.addMouseListener(this);
    loadBackgroundImages();
    setCursor();
    setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));

    int buttonCenterX = (640 / 2) - 275*2/3;
    int startY = 640 / 8;
    int offy = 150*2/3;
    
    small = new Button("/menu/size/buttons/small",buttonCenterX,startY+2*offy,2);
    medium = new Button("/menu/size/buttons/medium",buttonCenterX,startY+offy*3,2);
    large = new Button("/menu/size/buttons/large",buttonCenterX,startY+offy*4,2);

    try {
        ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        retro = Font.createFont(Font.TRUETYPE_FONT, Main.class.getResourceAsStream("/menu/Retro_Gaming.ttf"));
        ge.registerFont(retro);
    } catch (Exception e) {
        e.printStackTrace();
    }
}
    

    private void loadBackgroundImages() {
        try {
            playingBackground = ImageIO.read(getClass().getResourceAsStream("/menu/bgMain.jpg"));
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void setCursor() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image image = toolkit.getImage("res/images/cursor_shiny.png");
        Cursor c = toolkit.createCustomCursor(image , new Point(0,0), "c");
        setCursor (c);
    }

    @Override
    public void mouseClicked(MouseEvent e){
        if (isIn(e, small)) {
            frame.dispose();
            createGamePanel(1);
        } else if (isIn(e, medium)) {
            frame.dispose();
            createGamePanel(2);
        } else if (isIn(e, large)) {
            frame.dispose();
            createGamePanel(3);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (isIn(e, small)) {
            small.setMouseOver(true);
        } else if (isIn(e, medium)) {
            medium.setMouseOver(true);
        } else if (isIn(e, large)) {
            large.setMouseOver(true);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (isIn(e, small)) {
            small.setMouseOver(false);
        } else if (isIn(e, medium)) {
            medium.setMouseOver(false);
        } else if (isIn(e, large)) {
            large.setMouseOver(false);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    private boolean isIn(MouseEvent e, Button b) {
		return b.getBounds().contains(e.getX(), e.getY());
	}

    public void draw(Graphics2D g2) {
		small.draw(g2);
		medium.draw(g2);
		large.draw(g2);
	}

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(playingBackground, 0, 0, this.getWidth(), this.getHeight(), null);
        g.setColor(Color.BLACK);
		g.setFont(new Font("Retro Gaming", Font.PLAIN, 35));
        g.drawString("Welcome to Crazy Plumber", 40, 100);
        g.setFont(new Font("Retro Gaming", Font.PLAIN, 25));
        g.drawString("Choose the size of the grid :", 100, 250);
        Graphics2D g2 = (Graphics2D) g;
        draw(g2);
    }

    private void createGamePanel(int size) {
        JFrame frame = new JFrame();
        GamePanel gamePanel = new GamePanel(size);

        try {
            BufferedImage image = ImageIO.read(Main.class.getResourceAsStream("/images/plumber.png"));
            frame.setIconImage(image);
            frame.setTitle("CRAZY PLUMBER ED3A");
        } catch (Exception e) {
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
