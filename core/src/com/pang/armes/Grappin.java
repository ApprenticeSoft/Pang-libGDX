package com.pang.armes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.pang.MyGdxGame;
import com.pang.utils.GameConstants;
import com.pang.utils.MyTiledTexture;

public class Grappin extends Rope{

	private float cooldown = 5;
	
	public Grappin(MyGdxGame game) {
		super(game);

		textureCorde = new MyTiledTexture(game.assets.get("Images/Corde_Grappin.png", Texture.class));
	}
	
	@Override
	public void init(float posX, float posY){
		super.init(posX, posY);
		cooldown = 5;
	}
	
	@Override
	public void activity(){
		if(contactBall){
			destroy();
		}
		else if(!contactWall){
			grow();
		}
		else{
			body.setType(BodyType.StaticBody);
			cooldown -= Gdx.graphics.getDeltaTime();
			
			if(cooldown < 0)
				contactBall = true; 
			/*destroy();*/
		}
	}
	
	@Override
	public void draw(SpriteBatch batch){
		textureCorde.drawVertical(	batch, 
									(body.getPosition().x - width)/* * GameConstants.PPM*/, 
									(body.getPosition().y - GameConstants.HERO_HEIGHT)/* * GameConstants.PPM*/, 
									2*width/* * GameConstants.PPM*/, 
									(2*height - .2f*GameConstants.HERO_HEIGHT)/* * GameConstants.PPM*/);
		batch.draw(	game.textureAtlas.findRegion("Grappin"), 
				(body.getPosition().x - 2*width)/*  * GameConstants.PPM*/, 
				(body.getPosition().y + 2*height - 1.2f*GameConstants.HERO_HEIGHT)/* * GameConstants.PPM*/,
				4*width/* * GameConstants.PPM*/,
				4*width/* * GameConstants.PPM*/);
	}

}
