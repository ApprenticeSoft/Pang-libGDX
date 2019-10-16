package com.pang.utils;

import com.badlogic.gdx.Gdx;

public class GameConstants {
	
	//World constants
	public static float MPP = 0.05f /*(float)50/Gdx.graphics.getHeight()*/;					//Meter/Pixel
	public static float PPM = 1/MPP;					//Pixel/Meter
	public static float BOX_STEP = 1/60f; 
	public static int BOX_VELOCITY_ITERATIONS = 6;
	public static int BOX_POSITION_ITERATIONS = 2;
	public static float GRAVITY = -20;
	public static float DENSITY = 2.0f;
	public static int LIGHT_RAY_MULTIPLICATOR = 1;

	//Constantes de la Tiled Map
	public static int PPT = 64;
	public static int NB_VERTICAL_TILE = 40;
	public static int NB_HORIZONTAL_TILE = 71;
	public static float SCREEN_RATIO;
	public static float LEVEL_RATIO;
	
	//Constantes du niveau
	public static float LEVEL_PIXEL_HEIGHT;
	public static float LEVEL_PIXEL_WIDTH;
	public static float LEVEL_HEIGHT;
	public static float LEVEL_WIDTH;
	
	
	//Hero constants
	public static float HERO_HEIGHT = GameConstants.MPP * 0.06f * Gdx.graphics.getHeight();	
	public static float HERO_WIDTH = HERO_HEIGHT/2;	
	public static boolean HERO_TOUCHÉ = false;
	
	//Ball constants
	//public static float BALL_RADIUS = GameConstants.MPP * 0.07f * Gdx.graphics.getHeight();
	public static float BALL_RADIUS = 2f * HERO_HEIGHT;
	public static float REBOND_BALLE_GRANDE = 38;
	public static float REBOND_BALLE_MOYENNE = 32;
	public static float REBOND_BALLE_PETITE = 27;
	public static float REBOND_BALLE_MINI = 22;
	
	//Weapon constants
	public static float VITESSE_CORDE = 16;
	
	//Game constants
	public static float GAME_TIME = 90;
	public static float HERO_ANIM_TIME = 0;
	public static float ANIM_TIME = 0;
	public static float POWERUP_COOLDOWN_LONG = 60;
	public static float POWERUP_COOLDOWN_COURT = 10;
	public static boolean pauseBalles;
	public static boolean dynamite;
	public static boolean activationBouclier;
	public static boolean bouclierOn;
	public static boolean bombeOn;
	public static int NB_TIR;
	public static int NB_VIE;

}
