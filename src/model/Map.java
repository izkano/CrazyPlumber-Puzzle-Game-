package model;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import exception.MapException;

import javax.imageio.ImageIO;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class Map {

	private int level;

	private final Cell[][] start;
	private final ArrayList<int[]> first;

	private int Time_start=0;
	private int Time_level;

	private int move;
	private int moveCount = 0;
	
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
		this.soundManager = soundManager;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;

		String modePath = switch (gameMode) {
            case CLASSIC -> "classic/";
            case TIMER -> "timer/";
            case LIMITED -> "limited/";
            case BUILDER -> "builder/";
            case ONLINE -> "online/";
        };

        modePath = "res/level/" + modePath + level + ".txt";

		try {
			this.first = new ArrayList<>();
			this.start = readMatrixFromFile(modePath);
		} catch (IOException e) {
			throw new MapException("Unable to load map : " + e.getMessage());
		}

		loadAssets();

		parcoursProfondeurRec();
		resetCells();
		Time_level = 14 + level;
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
	    
	    while ((line = reader.readLine()) != null) {
	        rows++;
	        columns = Math.max(columns, line.length());
	    }
	
	    reader.close();
	    reader = new BufferedReader(new FileReader(filePath));
	
	    Cell[][] matrix = new Cell[rows][columns];
	
	    for (int i = 0; i < rows; i++) {
	        line = reader.readLine();
	        for (int j = 0; j < line.length(); j++) {
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
        	start[row][col].rotate(soundManager);
			move--;
			moveCount++;
			
			boolean b = parcoursProfondeurRec();
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
		
		if (!res) {
            for (Cell[] cells : start) {
                for (Cell cell : cells) {
                    if (cell != null && !cell.isChecked()) {
                        cell.setConnected(false);
                        cell.loadImage(cell.getPipeType());
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
				else {
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
        for (Cell[] cells : start) {
            for (Cell cell : cells) {
                if (cell != null) {
                    cell.reset();
                }
            }
        }
	}
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
	                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!"+c);
	                if (c == '0') {
	                	file.seek(currentPosition);
	                    file.write('1');
	                    System.out.println("Le fichier a été modifié avec succès : '0' à la position " +   positionToChange + " modifié en '1'.");
	                    return; 
	                } else {
	                    System.out.println("Aucun changement effectué : La position " + positionToChange   + " contient déjà un '1'.");
	                    return; 
	                }
	            } else {
	                if (line.equals(gamemode)) {
	                    foundGamemode = true;
	                    currentPosition = file.getFilePointer();
	                }
	            }
	        }
	        System.out.println("Le fichier n'a pas été modifié : le mode de jeu recherché '" + gamemode +  "' n'a pas été trouvé.");
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}


	public void draw(Graphics2D g2,int tileSize, int mapOffset)  {

		g2.drawImage(gridBackground, gridX, gridY,newWidth, newHeight,  null);

		for (int i = 0 ; i < getHeight() ; i++)
			for (int j = 0 ; j < getWidth() ; j++)
				drawCell(i, j, g2, j*tileSize+mapOffset, i*tileSize+mapOffset, tileSize);

		g2.setColor(Color.BLACK);
		g2.setFont(new Font("Retro Gaming", Font.PLAIN, 45));
		g2.drawString("Niveau "+level, 345, 50);
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
