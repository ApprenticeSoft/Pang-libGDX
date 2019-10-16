package com.pang.armes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pang.MyGdxGame;
import com.pang.utils.GameConstants;
import com.pang.utils.MyTiledTexture;

public class Rope extends Arme{
	
	protected MyTiledTexture textureCorde;
	
	public Rope(final MyGdxGame game){
		super(game);
		
		textureCorde = new MyTiledTexture(game.assets.get("Images/Corde.png", Texture.class));
	}
	
	public void init(float posX, float posY){
		super.init(posX, posY);
		
		height = GameConstants.HERO_HEIGHT;
		
		create();
	}
	
	public Rope(final MyGdxGame game, float posX, float posY){
		super(game);
		this.posX = posX;
		this.posY = posY;
		
		height = GameConstants.HERO_HEIGHT;
		
		create();
	}
	
	@Override
	public void activity(){
		if(contactWall){
			destroy();
		}
		else if(contactBall){
			destroy();
		}
		else{
			grow();
		}
	}
	/*
	@Override
	public void contactWall(int indexRope){
		contactWall = true;
		index = indexRope;
	}

	public void contactBall(int indexArme, int indexBalle){
		contactBall = true;
		index = indexArme;
		game.balles.get(indexBalle).setContactRope(true);
		game.balles.get(indexBalle).setIndex(indexBalle);

		System.out.println("indexRope : " + indexArme);
		System.out.println("indexBalle : " + indexBalle);
	}
	*/

	public void destroy(){
		super.destroy();
		GameConstants.NB_TIR--;
	}
	
	public void grow(){
		//body.setLinearVelocity(0, 10);
		height += GameConstants.VITESSE_CORDE*Gdx.graphics.getDeltaTime();
		body.setActive(false);
		game.world.destroyBody(body);
		
		create(); 
	}
	
	@Override
	public void draw(SpriteBatch batch){
		textureCorde.drawVertical(	batch, 
									(body.getPosition().x - width)/* * GameConstants.PPM*/, 
									(body.getPosition().y - GameConstants.HERO_HEIGHT)/* * GameConstants.PPM*/, 
									2*width/* * GameConstants.PPM*/, 
									2*height/* * GameConstants.PPM*/);
		batch.draw(	game.textureAtlas.findRegion("Pointe"), 
					(body.getPosition().x - 2*width)/*  * GameConstants.PPM*/, 
					(body.getPosition().y + 2*height - 1.2f*GameConstants.HERO_HEIGHT)/* * GameConstants.PPM*/,
					4*width/* * GameConstants.PPM*/,
					4*width/* * GameConstants.PPM*/);
	}
}
