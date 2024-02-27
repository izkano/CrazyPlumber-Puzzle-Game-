package view;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Button {
	private BufferedImage[] src;
	private BufferedImage current;
	
	private int x;
	private int y;
	private int height;
	private int width;
	
	private Rectangle bounds;
	
	
	public Button(String path, int x, int y) {
		loadImage(path);
		
		this.current = src[0];
		
		this.x = x;
		this.y = y;
		this.height = src[0].getHeight();
		this.width = src[0].getWidth();
		
		createBounds();
	}
	
	private void loadImage(String path) {
		this.src = new BufferedImage[2];
		
		try {
			this.src[0] = ImageIO.read(getClass().getResourceAsStream(path + "off.png"));
			this.src[1] = ImageIO.read(getClass().getResourceAsStream(path + "on.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void createBounds() {
		this.bounds = new Rectangle(x,y,width,height);
	}
	
	public BufferedImage getImage() {
		return this.current;
	}
	
	public void setMouseOver(boolean b) {
		if (b) 
			this.current = src[1];
		else
			this.current = src[0];
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public Rectangle getBounds() {
		return this.bounds;
	}
	
	public void draw(Graphics2D g2) {
		g2.drawImage(current,x,y,null);
	}
}
