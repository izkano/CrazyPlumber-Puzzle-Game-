package main;

import java.util.Random;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class Map {

 private int [][] start;
 private int [][] solution;

 public static Case[][] readMatrixFromFile(String filePath) throws IOException {
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

    Case[][] matrix = new Case[rows][columns];

    // Remplit la matrice à partir du fichier
    for (int i = 0; i < rows; i++) {
        line = reader.readLine();
        for (int j = 0; j < line.length(); j++) {
            rdm = random.nextInt(4);
            matrix[i][j] = new Case(Character.getNumericValue(line.charAt(j)),rdm);
        }
    }

    reader.close();
    return matrix;
}
}
