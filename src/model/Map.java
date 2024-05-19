package model;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import exception.MapException;

import javax.imageio.ImageIO;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;


public class Map {

	private int level;
	public int minimumMoves;
	public int playerMoves;

	private Cell[][] start;
	private final ArrayList<int[]> first;

	private int Time_start=0;
	private int Time_level;

	public boolean won;

	private final SoundManager soundManager;

	private BufferedImage gridBackground;
	private int screenWidth;
	private int screenHeight;
	private int gridX;
	private int gridY;
	private int newWidth;
	private int newHeight;


	/**
	 * Permet de stocker les informations sur la partie en cours
	 * @param level : niveau représenter par un entier
	 * @throws MapException : si le fichier texte ne se charge pas
	 */
	public Map(GameMode gameMode, int level, SoundManager soundManager, int screenWidth, int screenHeight) throws MapException {
		this.level = level;
		this.minimumMoves = calculateMinimumMoves(level);
		this.playerMoves = 0;
		this.soundManager = soundManager;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;

		this.first = new ArrayList<>();

		if (gameMode != GameMode.RANDOM) {
			String modePath = switch (gameMode) {
				case CLASSIC, TIMER -> "classic/";
				case LIMITED -> "limited/";
				case BUILDER -> "builder/";
				case ONLINE -> "online/";
				default -> throw new MapException();
			};

			modePath = "res/level/" + modePath + level + ".txt";

			try {
				this.start = readMatrixFromFile(modePath);
			} catch (IOException e) {
				throw new MapException("Unable to load map : " + e.getMessage());
			}
		} else {
			generateRandom();
		}

		loadAssets();

		parcoursProfondeurRec();
		resetCells();

		Time_level = 14 + level;
	}

	// Méthode pour calculer ou assigner le nombre minimum de coups
    private int calculateMinimumMoves(int level) {
        return 10; 
    }
	public int getMinimumMoves() {
		return minimumMoves;
	}
	public int getPlayerMoves() {
		return playerMoves;
	}


	public int getHeight() {
		return start.length;
	}

	public int getWidth() {
		return start[0].length;
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


	private void loadAssets() {
		try {
			gridBackground = ImageIO.read(getClass().getResourceAsStream("/menu/grilleMain.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		gridX = (screenWidth - gridBackground.getWidth()) / 2 - 65;
		gridY = (screenHeight - gridBackground.getHeight()) / 2 - 50;
		newWidth = screenWidth - 300;
		newHeight = screenHeight - 300;
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
		Random random = new Random();

		int rows = 6;
		int columns = 6;
	
	    Cell[][] matrix = new Cell[rows][columns];

		int k = 0;
	    for (int i = 0; i < rows; i++) {
	        line = reader.readLine();
	        for (int j = 0; j < line.length(); j+=2) {
	            char c = line.charAt(j);
				int r = random.nextInt(4);

				if (j != line.length()-1 && c == '2' && line.charAt(j+1) == '+') {
					matrix[i][k] = new Cell(2,r,true);
					matrix[i][k].setCurve();
				} else if (c != '0') {
					matrix[i][k] = new Cell(Character.getNumericValue(c),r,false);
				} else {
					matrix[i][k] = null;
				}

	            if (c == '1') {
	            	first.add(new int[]{i,k});
	            }
				k++;
	        }
			k=0;
	    }

	    reader.close();
	    return matrix;
	}
	
	
	/**
	 * Permet de determiner sur quelle cellule le joueur a cliqué, la fait tourner,
	 * et vérifie si les tuyaux sont bien orientés
	 * @param mouseX : coordonnées X du clic de la souris
	 * @param mouseY : coordonnées Y du clic de la souris
	 * @param tileSize : taille d'une cellule
	 */
	public void rotatePipe(int mouseX, int mouseY, int tileSize) {
        int row = mouseY / tileSize;
        int col = mouseX / tileSize;
		

        if (row >= 0 && row < start.length && col >= 0 && col < start[0].length && start[row][col] != null) {
			playerMoves++;

        	start[row][col].rotate(soundManager);

			boolean b = parcoursProfondeurRec();
			if (b)
				won = true;
			resetCells();
        }
    }
	
	
	public boolean parcoursProfondeurRec() {
		boolean valid = true;
		
		for (int[] t:first) {
			valid &= explorer(start[t[0]][t[1]],t[0],t[1]);
		}

		if (!valid)
			for (Cell[] c_row:start)
				for (Cell c:c_row)
					if (c != null && !c.isChecked())
						c.setConnected(false);

		return valid;
	}
	
	
	public boolean explorer(Cell s, int x, int y) {
		boolean[] con = s.getCon();
		boolean b = true;
		
		s.setChecked();

		if (s.getPipeType() != 1 && !s.isConnected()) {
			s.setConnected(true);
		}
			
		for (int i = 0 ; i < 4 ; i++) {
			if (con[i] ) {
				if (i == 0) {
						if (x <= 0 || start[x-1][y] == null || !start[x-1][y].getCon()[2])
							b = false;
						else if (start[x-1][y].getPipeType() == 1 && s.getPipeType()==1)
							return false;
						else if (!start[x-1][y].isChecked())
							b &= explorer(start[x-1][y], x-1,y);
				}
				else if (i == 1) {
						if (y >= start[x].length-1 || start[x][y+1] == null || !start[x][y+1].getCon()[3])
							b = false;
						else if (start[x][y+1].getPipeType() == 1 && s.getPipeType()==1)
							return false;
						else if (!start[x][y+1].isChecked())
							b &= explorer(start[x][y+1], x,y+1);
				}
				else if (i == 2) {
						if (x >= start.length-1 || start[x+1][y] == null || !start[x+1][y].getCon()[0]) 
							b = false;
						else if (start[x+1][y].getPipeType() == 1 && s.getPipeType()==1)
							return false;
						else if (!start[x+1][y].isChecked())
							b &= explorer(start[x+1][y], x+1,y);
				}
				else {
						if (y <= 0  || start[x][y-1] == null || !start[x][y-1].getCon()[1])
							b = false;
						else if (start[x][y-1].getPipeType() == 1 && s.getPipeType()==1)
							return false;
						else if (!start[x][y-1].isChecked()) 
							b &= explorer(start[x][y-1], x,y-1);
				}
			}
		}
		
		return b;
	}
	
	
	public void resetCells() {
        for (Cell[] c_row : start)
            for (Cell cell : c_row)
                if (cell != null)
                    cell.reset();
	}


	public void generateRandom() {
		Cell[][] maze = new Cell[6][6];
		for (int i = 0 ; i < 6 ; i++) {
			for (int j = 0 ; j < 6 ; j++) {
				maze[i][j] = new Cell(0,0,false);
			}
		}

		fillMaze(maze,0,0,0);

		Random random = new Random();
		for (int i = 0 ; i < 6 ; i++) {
			for (int j = 0 ; j < 6 ; j++) {
				maze[i][j] = new Cell(maze[i][j].getPipeType(),random.nextInt(4),maze[i][j].isCurve());
			}
		}

		this.start = maze;
		this.first.add(new int[]{0,0});
		resetCells();
	}


	public void fillMaze(Cell[][] maze, int i, int j, int d) {
		maze[i][j].setChecked();
		ArrayList<Integer> neighbours = getNeighbors(maze,i,j);

		if (neighbours.isEmpty()) {
			maze[i][j].setPipeType(1);
			first.add(new int[]{i,j});
		}

		for (Integer dir:neighbours) {
			if (dir == 1 && !maze[i-1][j].isChecked()) {
				maze[i][j].incrPipeType();
				maze[i-1][j].incrPipeType();
				if (d == 2 || d == 4) maze[i][j].setCurve();
				fillMaze(maze,i-1,j,1);
			}

			if (dir == 2 && !maze[i][j+1].isChecked()) {
				maze[i][j].incrPipeType();
				maze[i][j+1].incrPipeType();
				if (d == 1 || d == 3) maze[i][j].setCurve();
				fillMaze(maze,i,j+1,2);
			}

			if (dir == 3 && !maze[i+1][j].isChecked()) {
				maze[i][j].incrPipeType();
				maze[i+1][j].incrPipeType();
				if (d == 2 || d == 4) maze[i][j].setCurve();
				fillMaze(maze,i+1,j,3);
			}

			if (dir == 4 && !maze[i][j-1].isChecked()) {
				maze[i][j].incrPipeType();
				maze[i][j-1].incrPipeType();
				if (d == 1 || d == 3) maze[i][j].setCurve();
				fillMaze(maze,i,j-1,4);
			}
		}
	}


	public static ArrayList<Integer> getNeighbors(Cell[][] maze, int i, int j) {
		ArrayList<Integer> neighbors = new ArrayList<>();

		if (i != 0 && !maze[i-1][j].isChecked()) {
			neighbors.add(1);
		}
		if (j != maze.length - 1 && !maze[i][j+1].isChecked()) {
			neighbors.add(2);
		}
		if (i != maze.length - 1 && !maze[i+1][j].isChecked()) {
			neighbors.add(3);
		}
		if (j != 0 && !maze[i][j-1].isChecked()) {
			neighbors.add(4);
		}

		Collections.shuffle(neighbors);

		return neighbors;
	}


	public void draw(Graphics2D g2,int tileSize, int mapOffset)  {

		g2.drawImage(gridBackground, gridX, gridY,newWidth, newHeight,  null);

		for (int i = 0 ; i < getHeight() ; i++)
			for (int j = 0 ; j < getWidth() ; j++)
				if (start[i][j] != null)
					start[i][j].draw(g2, j*tileSize+mapOffset, i*tileSize+mapOffset, tileSize);

		g2.setColor(Color.BLACK);
		g2.setFont(new Font("Retro Gaming", Font.PLAIN, 45));
		g2.drawString("Niveau " + level, 345, 50);
	}
}
