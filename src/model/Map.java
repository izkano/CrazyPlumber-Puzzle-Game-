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
		String sol = "res/solution/solution"+level+".txt";
		try {
			this.start = readMatrixFromFile(path);
			this.solution = readEndFile(sol);
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
	            matrix[i][j] = new Cell(Character.getNumericValue(line.charAt(j)),0);
	        }
	    }
	
	    reader.close();
	    return matrix;
	}
	public static int[][] readEndFile(String filePath) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		String line;
		int rows = 0;
		int columns = 0;
	
		while ((line = reader.readLine()) != null) {
			rows++;
			columns = Math.max(columns, line.length());
		}
	
		reader.close();
		reader = new BufferedReader(new FileReader(filePath));
	
		int[][] matrix = new int[rows][columns];
	
		for (int i = 0; i < rows; i++) {
			line = reader.readLine();
			for (int j = 0; j < line.length(); j++) {
				matrix[i][j] = Character.getNumericValue(line.charAt(j));
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

	public boolean isWon(){
		for (int i = 0; i<start.length; i++){
			for (int j = 0; j<start[0].length; j++){
				if (this.start[i][j].getPipeType()==1){
					if(this.solution[i][j]!=(this.start[i][j].getOrientation()%2)){
						return false;
					}
				}
				else{
					if(this.solution[i][j]!=this.start[i][j].getOrientation()){
						return false;
					}
				}
			}
		}
		return true;
    }

	public void printOrientation(){
		System.out.println("Orientation:");
		for (int i = 0; i<start.length;i++){
			for (Cell j : start[i]){
				System.out.print(j.getOrientation());
			}
			System.out.println("");
		}
		System.out.println("");
	}

	public void printSolution(){
		System.out.println("Solution:");
		for (int i = 0; i<solution.length;i++){
			for (int j : solution[i]){
				System.out.print(j);
			}
			System.out.println("");
		}
		System.out.println("");
	}
}
