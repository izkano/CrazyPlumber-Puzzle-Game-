package model;

/**
 * Etats du jeu : MENU, le joueur se trouve dans le menu
 * 				 PLAYING, le joueur est en train de résoudre un niveau
 *               PAUSE, le jeu est en pause, le joueur a appuyé sur echap
 */
public enum State {
	MENU,
	SELECT,
	PLAYING,
	GAMEMODE,
	TRANSITION,
	GAMEOVER,
	PAUSE,
	SETTINGS,
	CREDITS
}
