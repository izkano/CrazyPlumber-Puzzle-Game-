package model;

import java.util.ArrayList;
import java.util.Random;

import exception.MapException;
import view.GamePanel;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class Map {

	private Cell[][] start;
	
	private ArrayList<int[]> first;

	private int Time_start=0;
	private int Time_level;
	
	private String mode;
	private int move;
	private int moveCount = 0;
	
	public boolean won;

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
		try {
			this.first = new ArrayList<int[]>();
			this.start = readMatrixFromFile(path);
		} catch (IOException e) {
			throw new MapException("Unable to load map : " + e.getMessage());
		}
		parcoursProfondeurRec();
		resetCells();
		Time_level = 14 + level;
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
	            int c = Character.getNumericValue(line.charAt(j));
	            
	            if (c == 4) {
	            	first.add(new int[]{i,j});
	            }
	            
	            if (c != 0 ) {
	            	matrix[i][j] = new Cell(c,0);
	            } else {
	            	matrix[i][j] = null;
	            }
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

        if (row >= 0 && row < start.length && col >= 0 && col < start[0].length && start[row][col] != null) {
        	start[row][col].rotate();
			move--;
			moveCount++;
			
			boolean b = parcoursProfondeurRec();
			//start[row][col].loadImage(start[row][col].getPipeType());
			if (b)
				won = true;			
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
		if (start[i][j] != null) {
			start[i][j].drawCell(g2, x, y, tileSize);
		}
	}
	
	
	public boolean parcoursProfondeurRec() {
		boolean res = true;
		
		for (int[] t:first) {
			start[t[0]][t[1]].loadImage(start[t[0]][t[1]].getPipeType());
			res &= explorer(start[t[0]][t[1]],t[0],t[1]);
		}
		
		if (res == false) {
			for (int i = 0 ; i < start.length ; i++) {
				for (int j = 0 ; j < start[i].length ; j++) {
					if (start[i][j] != null && !start[i][j].isChecked()) {
						start[i][j].setConnected(false);
						start[i][j].loadImage(start[i][j].getPipeType());
					}
				}
			}
		}
		
		return res;
	}
	
	
	public boolean explorer(Cell s, int x, int y) {
		boolean[] con = s.getCon();
		boolean b = true;
		
		s.setChecked();
		
		if (s.getPipeType()!=4) {
			s.setConnected(true);
			s.loadImage(s.getPipeType());
		}
			
		for (int i = 0 ; i < 4 ; i++) {
			if (con[i] ) {
				if (i == 0) {
						if (x <= 0 || start[x-1][y] == null || !start[x-1][y].getCon()[2])
							b &= false;
						else if (!start[x-1][y].isChecked())
							b &= explorer(start[x-1][y], x-1,y);
				}
				else if (i == 1) {
						if (y >= start[x].length-1 || start[x][y+1] == null || !start[x][y+1].getCon()[3])
							b &= false;
						else if (!start[x][y+1].isChecked())
							b &= explorer(start[x][y+1], x,y+1);
				}
				else if (i == 2) {
						if (x >= start.length-1 || start[x+1][y] == null || !start[x+1][y].getCon()[0]) 
							b &= false;
						else if (!start[x+1][y].isChecked())
							b &= explorer(start[x+1][y], x+1,y);
				}
				else if (i == 3) {
						if (y <= 0  || start[x][y-1] == null || !start[x][y-1].getCon()[1])
							b &= false;
						else if (!start[x][y-1].isChecked()) 
							b &= explorer(start[x][y-1], x,y-1);
				}
			}
		}
		
		return b;
	}
	
	
	public void resetCells() {
		for (int i = 0 ; i < start.length ; i++) {
			for (int j = 0 ; j < start[i].length ; j++) {
				if (start[i][j] != null) {
					start[i][j].reset();
				}
			}
		}
	}


	public int getMove() {
		return move;
	}


	public int getMoveCount() {
		return moveCount;
	}
	
	
	public void setTimer_fiel() {
		Time_start=0;
	}
	
	public void setTimer() {
		this.Time_start=(int) (System.currentTimeMillis()/1000);
	}
	
	public int getTime_start() {
		return this.Time_start;
	}
	
	public int getTime_now() {
		return (int) (System.currentTimeMillis()/1000);
	}
	
	public void  setTime_level(int time) {
		 this.Time_level = time;
	}
	public int  getTime_level() {
		 return Time_level;
	}
	
	public boolean level_Fail() {
		return this.getTime_now()-this.getTime_start()>=getTime_level();
	}
	
	public int getRemainnig_time() {
		return getTime_level()-(this.getTime_now()-this.getTime_start());
	}

	public int getStartTime() {
		return Time_start;
	}
}
