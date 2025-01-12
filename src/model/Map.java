package model;

import view.Button;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import exception.MapException;

import javax.imageio.ImageIO;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.RandomAccessFile;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;


public class Map {

	private int level;
	private int minimumMoves = 0;
	private int playerMoves;

	private Cell[][] start;
	private final ArrayList<int[]> first;
	private final Button[] test = new Button[7];
	private Cell place;
	private int delete = -1;


	private int Time_start=0;
	private int Time_level;
	private GameMode gamemode;

	public boolean won;

	private final SoundManager soundManager;

	private BufferedImage gridBackground;
	private int screenWidth;
	private int screenHeight;
	private int gridX;
	private int gridY;
	private int newWidth;
	private int newHeight;
	private int scale;

	/**
	 * Permet de stocker les informations sur la partie en cours
	 * @param level : niveau représenter par un entier
	 * @throws MapException : si le fichier texte ne se charge pas
	 */
	public Map(GameMode gameMode, int level, SoundManager soundManager, int screenWidth, int screenHeight, int scale) throws MapException {
		this.level = level;
		this.playerMoves = 0;
		this.soundManager = soundManager;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		this.gamemode = gameMode;
		this.scale = scale;

		this.first = new ArrayList<>();

		if (gameMode != GameMode.RANDOM && gameMode != GameMode.BUILDER) {
			String modePath = switch (gameMode) {
				case CLASSIC, TIMER -> "classic/";
				case LIMITED -> "limited/";
				case PLAYBUILD -> "builder/";
				default -> throw new MapException();
			};

			modePath = "res/level/" + modePath + level + ".txt";

			try {
				this.start = readMatrixFromFile(modePath);
			} catch (IOException e) {
				throw new MapException("Unable to load map : " + e.getMessage());
			}
		} else if (gameMode == GameMode.RANDOM){
			generateRandom();
		}
		else if (gameMode == GameMode.BUILDER){
			createEmpty();
		}

		loadAssets();
		loadTest();
		parcoursProfondeurRec();
		resetCells();

		Time_level = 14 + level;
	}

	public void placePipe(int x, int y, int tileSize){
		if (place==null) {
			return;
		}
		if (gamemode == GameMode.BUILDER) {
			int row = x / tileSize;
			int col = y / tileSize;
			if (row >= 0 && row < start.length && col >= 0 && col < start[0].length && start[row][col] != null) {
				start[col][row] = place;
				place = null;
			}
		}
	}

	public void choseDeletePipe(int x, int y, int tileSize){
		int row = x / tileSize;
		int col = y / tileSize;
		if (row >= 0 && row < start.length && col >= 0 && col < start[0].length && start[col][row] != null && place==null) {
			if (start[col][row].getPipeType()!= 4) {
				delete = row+col*6;
			}
		}
	}
	public void setPlacePipe(int i){
		switch(i){
			case 0:
				place = new Cell(1,0,false);
				break;
			case 1:
				place = new Cell(2,0,false);
				break;
			case 2:
				place = new Cell(2,0,true);
				break;
			case 3:
				place = new Cell(3,0,false);
				break;
		}
	}
	public void deletePipe(){
		if (delete==-1) {
			return;}
		if (gamemode == GameMode.BUILDER) {
			start[delete/6][delete%6] = new Cell(4,0,false);
			delete = -1;
		}
	}

	public Button getBuildButton(int i){
		return test[i];
	}

	public void testBuild(){
		if (gamemode == GameMode.BUILDER) {
			for (int i = 0; i<start.length; i++) {
				for (int j = 0; j<start[0].length; j++) {
					if (start[i][j].getPipeType() == 4) {
						start[i][j] = new Cell(0,0,false);
					}
				}
			}
		}
		else if (gamemode == GameMode.TESTING) {
			for (int i = 0; i<start.length; i++) {
				for (int j = 0; j<start[0].length; j++) {
					if (start[i][j].getPipeType() == 0) {
						start[i][j] = new Cell(4,0,false);
					}
				}
			}
		}
	}

	public void loadTest() {
		test[0] = new Button("/menu/build/building/start",240*scale/3,800*scale/3,9*scale/3);
		test[1] = new Button("/menu/build/building/horizontal",384*scale/3,800*scale/3,9*scale/3);
		test[2] = new Button("/menu/build/building/curve",528*scale/3,800*scale/3,9*scale/3);
		test[3] = new Button("/menu/build/building/cross",672*scale/3,800*scale/3,9*scale/3);
		test[4] = new Button("/menu/build/building/save",850*scale/3,250*scale/3,3*scale/3);
		test[5] = new Button("/menu/build/building/delete",850*scale/3,370*scale/3,3*scale/3);
		test[6] = new Button("/menu/build/building/previous",850*scale/3,370*scale/3,3*scale/3);

	}

public void saveLevel() {
    String modePath = "builder/";
    String filePath = "res/level/" + modePath + level + ".txt";

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
        for (int i = 0; i <6; i++) {
            for (int j = 0; j <6; j++) {
                if(start[i][j].getPipeType()==2&&start[i][j].isCurve()) {writer.write("2+");}
                else {
                    writer.write(start[i][j].getPipeType()+" ");
                }
            }
            writer.write("\n");
        }
    } catch (IOException e) {
        e.printStackTrace(); 
    }
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
		return (this.getTime_now()-this.getTime_start()>=getTime_level());
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

		gridX = (screenWidth - gridBackground.getWidth()*scale/3) / 2 - 65*scale/3;
		gridY = (screenHeight - gridBackground.getHeight()*scale/3) / 2 - 50*scale/3;
		newWidth = screenWidth - 300*scale/3;
		newHeight = screenHeight - 300*scale/3;
	}
	
	public int calculateMinimumMoves(int level){
		return 100;
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
					this.minimumMoves++;
				} else if (c != '0') {
					matrix[i][k] = new Cell(Character.getNumericValue(c),r,false);
					this.minimumMoves++;
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
		this.minimumMoves = this.minimumMoves*4 + 5;
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
		if (gamemode == GameMode.BUILDER) return;
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
	/**
	 * Permet de sauvgarder les niveaux débloquables aprés les avoir réussi
	 * on modifiant le contenu du fichier sauvgarde.txt.
	 * @param fileName : le chemain d'accés vers le fichier sauvgarde.txt.
	 * @param gamemode : le mode de jeux.
	 * @param positionToChange : la postion du changement.
	 */
	public void sauvgarde(String fileName, String gamemode, int positionToChange) {
	    try (RandomAccessFile file = new RandomAccessFile(fileName, "rw")) {
	        String line;
	        boolean foundGamemode = false;
	        long currentPosition = file.getFilePointer();
	        while ((line = file.readLine()) != null) {
	            if (foundGamemode) {
	            	 currentPosition += positionToChange;
	                 file.seek(currentPosition);
	                char c = (char) file.read(); 
	                if (c == '0') {
	                	file.seek(currentPosition);
	                    file.write('1');
	                    return; 
	                } else {
	                    return; 
	                }
	            } else {
	                if (line.equals(gamemode)) {
	                    foundGamemode = true;
	                    currentPosition = file.getFilePointer();
	                }
	            }
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}


	public void createEmpty() {
		Cell[][] map = new Cell[6][6];
		for (int i = 0 ; i < 6 ; i++) {
			for (int j = 0 ; j < 6 ; j++) {
				map[i][j] = new Cell(4,0,false);
			}
		}
		this.start = map;
		resetCells();
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

	public int getMinimumMoves() {
        return minimumMoves;
    }
    public int getPlayerMoves() {
        return playerMoves;
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
		if (start==null) return;
		g2.drawImage(gridBackground, gridX, gridY,newWidth, newHeight-40*scale/3,  null);

		for (int i = 0 ; i < getHeight() ; i++)
			for (int j = 0 ; j < getWidth() ; j++)
				if (start[i][j] != null)
					start[i][j].draw(g2, (j+1)*tileSize+mapOffset, (i+1)*tileSize+mapOffset, tileSize);

		g2.setColor(Color.BLACK);
		g2.setFont(new Font("Retro Gaming", Font.PLAIN, 45*scale/3));
		g2.drawString("Niveau " + level, 370*scale/3, 50*scale/3);
		if (gamemode == GameMode.BUILDER) {
			drawBuild(g2);
		}
		if (gamemode == GameMode.TESTING) {
			drawTest(g2);
		}
		if (gamemode == GameMode.TIMER) {
			g2.drawString("Temps restant : " + getRemainnig_time(), 250*scale/3, 125*scale/3);
		}
		if (gamemode == GameMode.LIMITED) {
			g2.drawString("Coups restants : " + (getMinimumMoves()-getPlayerMoves()), 250*scale/3, 125*scale/3);
		}
	}

	public void drawBuild(Graphics2D g2) {
		for (int i = 0; i<6; i++) {
			test[i].draw(g2);
		}
	}

	public void drawTest(Graphics2D g2){
		test[4].draw(g2);
		test[6].draw(g2);
	}

	public void setGameMode(GameMode gameMode) {
		this.gamemode = gameMode;
	}

	public GameMode getGameMode() {
		return gamemode;
	}

	public int nbMIN(){
        int s=0;
        for (int i = 0; i <6; i++){
            for (int j = 0; j <6; j++){
                if(start[i][j]!=null&&start[i][j].getPipeType()!=0) {s++;}
            }
        }
        return s;
    }
}
