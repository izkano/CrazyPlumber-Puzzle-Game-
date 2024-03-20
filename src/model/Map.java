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
	
	private String mode;
	private int move;

	/**
	 * Permet de stocker les informations sur la partie en cours
	 * @param level : niveau représenter par un entier
	 * @throws MapException : si le fichier texte ne se charge pas
	 */
	public Map(int gamemode, int level) throws MapException {
		switch(gamemode){
			case 0:
				mode = "classic/";
				break;
			case 1:
				mode = "timer/";
				break;
			case 2:
				mode = "limited/";
				break;
			case 3:
				mode = "builder/";
				break;
		}
		String path = "res/level/" + mode + level + ".txt";		
		String sol = "res/solution/solution"+level+".txt";
		try {
			this.start = readMatrixFromFile(path);
			this.solution = readEndFile(sol);
		} catch (IOException e) {
			throw new MapException("Unable to load map : " + e.getMessage());
		}
		move = countMove();
	}
	
	public int getHeight() {
		return start.length;
	}
	
	public int getWidth() {
		return start[0].length;
	}
	
	
	/**
	 * Lis le fichier et construit une matrice afin de représenter les cellules du jeu
	 * @param filePath : chemin d'accès au fichier correspondant
	 * @return : tableau de tableau contenant les cellules du jeu
	 * @throws IOException : si n'arrive pas a lire le fichier à partir du filePath donné en argument
	 */
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
	
	
	/**
	 * Permet de determiner sur quelle cellule le joueur a cliqué, et appelle rotate sur celle ci
	 * @param mouseX : coordonnées X du clic de la souris
	 * @param mouseY : coordonnées Y du clic de la souris
	 * @param tileSize : taille d'une cellule
	 */
	public void rotatePipe(int mouseX, int mouseY, int tileSize) {
        int row = mouseY / tileSize;
        int col = mouseX / tileSize;

        if (row >= 0 && row < start.length && col >= 0 && col < start[0].length) {
        	start[row][col].rotate();
			move--;
        }
    }
	
	
	/**
	 * Permet de dessiner une cellule en appellant la méthode draw() de la classe Cellule
	 * @param i : indice de la ligne de la cellule dans le tableau start
	 * @param j : indice de la colonne de la cellule dans le tableau start
	 * @param g2 : composant graphique
	 * @param x : coordonée x 
	 * @param y : coordonnée y 
	 * @param tileSize : taille d'une cellule
	 */
	public void drawCell(int i, int j, Graphics2D g2, int x, int y, int tileSize) {
		start[i][j].drawCell(g2, x, y, tileSize);
	}
	

	/** 
	 * Verifie si le joueur a completé le niveau
	 * @return : true si le niveau est terminé, false sinon
	 */
	public boolean isWon(){
		for (int i = 0; i<start.length; i++){
			for (int j = 0; j<start[0].length; j++){
				if (this.start[i][j].getPipeType()==1){
					if(this.solution[i][j]!=(this.start[i][j].getOrientation()%2)){
						return false;
					}
				}
				else {
					if(this.solution[i][j]!=this.start[i][j].getOrientation()){
						return false;
					}
				}
			}
		}
		return true;
    }

	
	/**
	 * @debug Fonctions de deboguage
	 */
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

	public int getMove() {
		return move;
	}

	public int countMove(){
		int res = 0;
		for (int e[] : solution){
			for (int f : e){
				res += f;
			}
		}
		return res+20;
	}

}
