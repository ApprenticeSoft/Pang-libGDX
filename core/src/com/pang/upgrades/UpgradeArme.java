package com.pang.upgrades;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.pang.MyGdxGame;
import com.pang.bodies.Hero;
import com.pang.enums.ArmeActive;
import com.pang.utils.GameConstants;

public class UpgradeArme extends Upgrade{

	protected ArmeActive armeActive;
	//private static TextureAtlas armeAtlas;
	private static Animation<TextureRegion> animationMitraillette, animationTromblon, animationCordeDouble, animationGrappin;
	
	public UpgradeArme(final MyGdxGame game){
		super(game);
		
		armeActive = ArmeActive.CORDE_DOUBLE;

    	//armeAtlas = game.assets.get("Images/PowerUps/Armes.pack", TextureAtlas.class);
        animationMitraillette = new Animation(1/15f, game.textureAtlas.findRegions("Mitraillette"), Animation.PlayMode.LOOP);
        animationTromblon = new Animation(1/15f, game.textureAtlas.findRegions("Tromblon"), Animation.PlayMode.LOOP);
        animationCordeDouble = new Animation(1/15f, game.textureAtlas.findRegions("Corde_Double"), Animation.PlayMode.LOOP);
        animationGrappin = new Animation(1/15f, game.textureAtlas.findRegions("Grappin_PowerUp"), Animation.PlayMode.LOOP);
	}
	
	public void init(float posX, float posY, ArmeActive armeActive){
		this.armeActive = armeActive;
		
		this.posX = posX;
		this.posY = posY;

		contactHero = false;
		create = false;
		cooldown = GameConstants.POWERUP_COOLDOWN_COURT;
		delai = 0;
		
		switch(armeActive){
			case CORDE_SIMPLE:
				width = GameConstants.HERO_WIDTH/2;
				height = GameConstants.HERO_WIDTH/2;
				break;
			case CORDE_DOUBLE:
				width = GameConstants.HERO_WIDTH;
				height = width;
				break;
			case GRAPPIN:
				width = GameConstants.HERO_WIDTH;
				height = width;
				break;
			case MITRAILLETTE:
				height = GameConstants.HERO_WIDTH/2;
				width = 2 * height;
				break;
			case TROMBLON:
				height = GameConstants.HERO_WIDTH/2;
				width = 2 * height;
				break;
		}
		
		//createBody(posX, posY);
		
		blinker.init();
	}

	@Override
	public void init(MapObject mapObject){
		Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();
		

		if(mapObject.getProperties().get("Delai") != null)
			delai = Float.parseFloat(mapObject.getProperties().get("Delai").toString());
		else 
			delai = 0;
		
		if(mapObject.getProperties().get("Arme") != null)
			armeActive = ArmeActive.valueOf((String)mapObject.getProperties().get("Arme"));
		else System.out.println("**** Arme non définie ****");

		posX = (rectangle.x + rectangle.width/2) /** GameConstants.HORIZONTAL_RATIO*/;
		posY = (rectangle.y + rectangle.height/2) /** GameConstants.VERTICAL_RATIO*/;

		contactHero = false;	
		create = false;
		cooldown = GameConstants.POWERUP_COOLDOWN_LONG;
		
		switch(armeActive){
			case CORDE_SIMPLE:
				width = GameConstants.HERO_WIDTH/2;
				height = GameConstants.HERO_WIDTH/2;
				break;
			case CORDE_DOUBLE:
				width = GameConstants.HERO_WIDTH;
				height = width;
				break;
			case GRAPPIN:
				width = GameConstants.HERO_WIDTH;
				height = width;
				break;
			case MITRAILLETTE:
				height = GameConstants.HERO_WIDTH/2;
				width = 2 * height;
				break;
			case TROMBLON:
				height = GameConstants.HERO_WIDTH/2;
				width = 2 * height;
				break;
		}
		
		//createBody(posX, posY);

		blinker.init();
	}
	
	@Override
	public void contactHero(int indexUpgrade){
		contactHero = true;
		Hero.arme = armeActive;
		index = indexUpgrade;
	}
	
	@Override
	public void draw(SpriteBatch batch){
		if(delai < 0){
			if(cooldown > 2 || !blinker.shouldBlink()){
				switch(armeActive){
					case CORDE_SIMPLE:
			
						break;
					case CORDE_DOUBLE:
						batch.draw(	animationCordeDouble.getKeyFrame(GameConstants.ANIM_TIME), 
									(body.getPosition().x - width)/*  * GameConstants.PPM*/, 
									(body.getPosition().y - height)/* * GameConstants.PPM*/,
									2*width/* * GameConstants.PPM*/,
									2*height/* * GameConstants.PPM*/);
						break;
					case GRAPPIN:
						batch.draw(	animationGrappin.getKeyFrame(GameConstants.ANIM_TIME), 
									(body.getPosition().x - width)/*  * GameConstants.PPM*/, 
									(body.getPosition().y - height)/* * GameConstants.PPM*/,
									2*width/* * GameConstants.PPM*/,
									2*height/* * GameConstants.PPM*/);
						break;
					case MITRAILLETTE:
						batch.draw(	animationMitraillette.getKeyFrame(GameConstants.ANIM_TIME), 
									(body.getPosition().x - width)/*  * GameConstants.PPM*/, 
									(body.getPosition().y - height)/* * GameConstants.PPM*/,
									2*width/* * GameConstants.PPM*/,
									2*height/* * GameConstants.PPM*/);
						break;
					case TROMBLON:
						batch.draw(	animationTromblon.getKeyFrame(GameConstants.ANIM_TIME), 
									(body.getPosition().x - width)/*  * GameConstants.PPM*/, 
									(body.getPosition().y - height)/* * GameConstants.PPM*/,
									2*width/* * GameConstants.PPM*/,
									2*height/* * GameConstants.PPM*/);
						break;
				}	
			}
		}
	}
	
}
