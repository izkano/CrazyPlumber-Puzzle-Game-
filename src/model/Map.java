package model;

import java.util.Random;

import exception.MapException;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class Map {

	private Cell[][] start;
	private int[][] solution;
	
	public Map(int level) throws MapException {
		String path = "res/level/" + level + ".txt";		
		
		try {
			this.start = readMatrixFromFile(path);
		} catch (IOException e) {
			throw new MapException("Unable to load map : " + e.getMessage());
		}
	}
	
	public int getHeight() {
		return start.length;
	}
	
	public int getWidth() {
		return start[0].length;
	}
	
	public Cell[][] readMatrixFromFile(String filePath) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		String line;
		int rows = 0;
	    int columns = 0;
	    Random random = new Random();
	    int rdm;
	
	    // Compte le nombre de lignes et détermine le nombre maximum de colonnes
	    while ((line = reader.readLine()) != null) {
	        rows++;
	        columns = Math.max(columns, line.length());
	    }
	
	    // Réinitialise le lecteur pour recommencer la lecture depuis le début
	    reader.close();
	    reader = new BufferedReader(new FileReader(filePath));
	
	    Cell[][] matrix = new Cell[rows][columns];
	
	    // Remplit la matrice à partir du fichier
	    for (int i = 0; i < rows; i++) {
	        line = reader.readLine();
	        for (int j = 0; j < line.length(); j++) {
	            rdm = random.nextInt(4);
	            matrix[i][j] = new Cell(Character.getNumericValue(line.charAt(j)),rdm);
	        }
	    }
	
	    reader.close();
	    return matrix;
	}
	
	public void rotatePipe(int mouseX, int mouseY, int tileSize) {
        int row = mouseY / tileSize;
        int col = mouseX / tileSize;

        if (row >= 0 && row < start.length && col >= 0 && col < start[0].length) {
        	start[row][col].rotate();
        }
    }
	
	public void drawCell(int i, int j, Graphics2D g2, int x, int y, int tileSize) {
		start[i][j].drawCell(g2, x, y, tileSize);
	}
}
