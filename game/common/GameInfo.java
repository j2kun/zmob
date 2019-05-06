package game.common;

import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import game.Difficulty;

public class GameInfo {

	public static int pW, pH;
	public static int frame = 0;
	
	public static boolean paused;
	public static boolean gameStarted;
	public static boolean wKey, sKey, aKey, dKey;
	public static boolean gameOver, gameRunning;
	
	public static int numZombiesKilled = 0;
	public static int numWavesSurvived = 0;
	public static int score = 0;
	public static Difficulty currentDifficulty = Difficulty.EASY;
	public static boolean godMode;
	public static boolean zombiesSpawning = true;
	
	//Affine Transformation and image variables
	public static int playerW, playerH, zombieW, zombieH;
	public static int horizDispP, vertDispP, halfDP;//player variables
	public static int horizDispZ, vertDispZ, halfDZ;//zombie variables
	public static AffineTransformOp translateOpP, translateOpZ, rotateOp;
	public static BufferedImage playerPistol, playerShotty, 
				playerWavebeam, zombie, poisonedZombie, ammo, health;

	//mouse listener variables
	public static boolean mousePressed;
	public static double mouseAngle;
	public static int mouseX, mouseY;
	
	//pickup variables
	public static String pickupDisplayString;
	
	//gun variables
	public static int numPoisonBulletsShot = 0;
}
