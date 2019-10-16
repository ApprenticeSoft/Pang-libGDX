package com.pang.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.pang.MyGdxGame;


public class HUD {

	final MyGdxGame game;
	private Label timeLabel;
	private LabelStyle timeLabelStyle;
	
	
	
	public HUD(final MyGdxGame game){
		this.game = game;
		
		timeLabelStyle = new LabelStyle(game.assets.get("fontMenu.ttf", BitmapFont.class), Color.WHITE);
		timeLabel = new Label("" + GameConstants.GAME_TIME, timeLabelStyle);
		timeLabel.setX(Gdx.graphics.getWidth()/2 - timeLabel.getPrefWidth()/2);
		timeLabel.setY(Gdx.graphics.getHeight() - timeLabel.getPrefHeight());
	}
	
	public void addToStage(Stage stage){
		stage.addActor(timeLabel);
	}
	
	public void act(){
		timeLabel.setText("" + (int)(GameConstants.GAME_TIME+1));
		timeLabel.setX(Gdx.graphics.getWidth()/2 - timeLabel.getPrefWidth()/2);
	}
	
	public void draw(SpriteBatch batch){
		
		for(int i = 0; i < GameConstants.NB_VIE; i++){
			batch.draw(	game.textureAtlas.findRegion("Casque"),
						.25f * GameConstants.HERO_HEIGHT + i* 1.25f * GameConstants.HERO_HEIGHT,
						GameConstants.LEVEL_HEIGHT - 1.25f * GameConstants.HERO_HEIGHT, 
						GameConstants.HERO_HEIGHT, 
						GameConstants.HERO_HEIGHT);
		}
		
	}
}
