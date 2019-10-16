package com.pang.upgrades;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.pang.MyGdxGame;
import com.pang.bodies.Balle;
import com.pang.enums.UpgradeType;
import com.pang.utils.GameConstants;

public class PowerUp extends Upgrade{

	private UpgradeType upgradeType;
	private static Animation<TextureRegion> animationBallePause, animationDynamite, animationBombe, animationBouclier;
	
	public PowerUp(final MyGdxGame game){
		super(game);
		
		upgradeType = UpgradeType.BOUCLIER;
		
        animationBallePause = new Animation(1/15f, game.textureAtlas.findRegions("Horloge"), Animation.PlayMode.LOOP);
        animationDynamite = new Animation(1/15f, game.textureAtlas.findRegions("Dynamite"), Animation.PlayMode.LOOP);
        animationBombe = new Animation(1/15f, game.textureAtlas.findRegions("Bombe"), Animation.PlayMode.LOOP);
        animationBouclier = new Animation(1/15f, game.textureAtlas.findRegions("Bouclier_PowerUp"), Animation.PlayMode.LOOP);
	}
	
	public void init(float posX, float posY, UpgradeType upgradeType){
		this.upgradeType = upgradeType;
		
		this.posX = posX;
		this.posY = posY;

		contactHero = false;
		create = false;
		cooldown = GameConstants.POWERUP_COOLDOWN_COURT;
		delai = 0;
		
		switch(upgradeType){
			case BALLE_PAUSE:
				width = GameConstants.HERO_WIDTH;
				height = width;
				break;
			case DYNAMITE:
				height = GameConstants.HERO_WIDTH/2;
				width = 2 * height;
				break;
			case BOMBE:
				width = GameConstants.HERO_WIDTH;
				height = width;
				break;
			case BOUCLIER:
				width = GameConstants.HERO_WIDTH;
				height = width;
				break;
		}
		
		//createBody(posX, posY);
		
		blinker.init();
	}

	public void init(MapObject mapObject){
		Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();

		posX = (rectangle.x + rectangle.width/2) /** GameConstants.HORIZONTAL_RATIO*/;
		posY = (rectangle.y + rectangle.height/2) /** GameConstants.VERTICAL_RATIO*/;

		contactHero = false;
		create = false;
		cooldown = GameConstants.POWERUP_COOLDOWN_LONG;
		
		if(mapObject.getProperties().get("Delai") != null)
			delai = Float.parseFloat(mapObject.getProperties().get("Delai").toString());
		else delai = 0;
		
		if(mapObject.getProperties().get("PowerUp") != null)
			upgradeType = UpgradeType.valueOf((String)mapObject.getProperties().get("PowerUp"));	
		else System.out.println("**** Power-up non définie ****");
		
		switch(upgradeType){
		case BALLE_PAUSE:
			width = GameConstants.HERO_WIDTH;
			height = width;
			break;
		case DYNAMITE:
			height = GameConstants.HERO_WIDTH/2;
			width = 2 * height;
			break;
		case BOMBE:
			width = GameConstants.HERO_WIDTH;
			height = width;
			break;
		case BOUCLIER:
			width = GameConstants.HERO_WIDTH;
			height = width;
			break;
		}
		
		//createBody(posX, posY);
		
		blinker.init();
	}
	
	@Override
	public void contactHero(int indexUpgrade){
		contactHero = true;
		index = indexUpgrade;
		
		switch(upgradeType){
			case BALLE_PAUSE:
				ballePause();
				break;
			case DYNAMITE:
				dynamite();
				break;
			case BOMBE:
				bombe();
				break;
			case BOUCLIER:
				bouclier();
				break;
		}
		
	}
	
	public void ballePause(){
		GameConstants.pauseBalles = true;
		for(Balle balle : game.balles){
			balle.vitesseVecteur.set(balle.body.getLinearVelocity());			
		}
	}
	
	public void dynamite(){
		GameConstants.dynamite = true;
	}
	
	public void bouclier(){
		GameConstants.activationBouclier = true;
	}
	
	public void bombe(){
		GameConstants.bombeOn = true;
	}
	
	@Override
	public void draw(SpriteBatch batch){
		if(delai < 0){
			if(cooldown > 2 || !blinker.shouldBlink()){
				switch(upgradeType){
					case BALLE_PAUSE:
						batch.draw(	animationBallePause.getKeyFrame(GameConstants.ANIM_TIME), 
									(body.getPosition().x - width)/*  * GameConstants.PPM*/, 
									(body.getPosition().y - height)/* * GameConstants.PPM*/,
									2*width/* * GameConstants.PPM*/,
									2*height/* * GameConstants.PPM*/);
						break;
					case DYNAMITE:
						batch.draw(	animationDynamite.getKeyFrame(GameConstants.ANIM_TIME), 
									(body.getPosition().x - width)/*  * GameConstants.PPM*/, 
									(body.getPosition().y - height)/* * GameConstants.PPM*/,
									2*width/* * GameConstants.PPM*/,
									2*height/* * GameConstants.PPM*/);
						break;
					case BOMBE:
						batch.draw(	animationBombe.getKeyFrame(GameConstants.ANIM_TIME), 
									(body.getPosition().x - width)/*  * GameConstants.PPM*/, 
									(body.getPosition().y - height)/* * GameConstants.PPM*/,
									2*width/* * GameConstants.PPM*/,
									2*height/* * GameConstants.PPM*/);
						break;
					case BOUCLIER:
						batch.draw(	animationBouclier.getKeyFrame(GameConstants.ANIM_TIME), 
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
