package control;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import view.GamePanel;

public class MouseHandler implements MouseListener {
	
	private GamePanel gp;
	
	public MouseHandler(GamePanel gp) {
		this.gp = gp;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int mouseX = e.getX() - gp.mapOffset;
        int mouseY = e.getY() - gp.mapOffset;
        gp.map.rotatePipe(mouseX, mouseY,gp.tileSize);
	}

	@Override
	public void mousePressed(MouseEvent e) { }

	@Override
	public void mouseReleased(MouseEvent e) { }

	@Override
	public void mouseEntered(MouseEvent e) { }

	@Override
	public void mouseExited(MouseEvent e) { }
}
