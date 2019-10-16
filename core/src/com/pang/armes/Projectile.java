package com.pang.armes;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pools;
import com.pang.MyGdxGame;
import com.pang.utils.GameConstants;

public class Projectile extends Arme{

	public float angle;
	private Vector2 vSpeed;
	//private TextureAtlas projectileAtlas;
	private static Animation<TextureRegion> animationProjectileBombe, animationProjectile;
	
	public Projectile(MyGdxGame game) {
		super(game);

		vSpeed = Pools.obtain(Vector2.class).set(0,30);

    	//projectileAtlas = game.assets.get("Images/Projectiles/Projectile.pack", TextureAtlas.class);
        animationProjectileBombe = new Animation(1/30f,game.textureAtlas.findRegions("Projectile_Bombe"), Animation.PlayMode.LOOP_PINGPONG);
        animationProjectile = new Animation(1/15f, game.textureAtlas.findRegions("Projectile"), Animation.PlayMode.LOOP_PINGPONG);
	}
	
	public void init(float posX, float posY, float angle){
		super.init(posX, posY);
		this.angle = angle;
		vSpeed.set(0, 30);
		
		height = GameConstants.HERO_HEIGHT/3;
		width = height/2;
		polygonCenter.set(width/2, height/2);
		//polygonCenter.set(0, 0);
		
		create();
		body.setTransform(posX, posY, angle* MathUtils.degreesToRadians);
		vSpeed.rotate(angle);
	}
	
	public void init(float posX, float posY, float width, float height, float angle){
		super.init(posX, posY);
		this.angle = angle;
		this.height = height;
		this.width = width;
		vSpeed.set(0, 30);
		polygonCenter.set(width/2, height/2);
		//polygonCenter.set(0, 0);
		
		create();
		body.setTransform(posX, posY, angle* MathUtils.degreesToRadians);
		vSpeed.rotate(angle);
	}
	
	public void init(float posX, float posY, float width, float height, float speed, float angle){
		super.init(posX, posY);
		this.angle = angle;
		this.height = height;
		this.width = width;
		vSpeed.set(0, speed);
		polygonCenter.set(width/2, height/2);
		//polygonCenter.set(0, 0);
		
		create();
		body.setTransform(posX, posY, angle* MathUtils.degreesToRadians);
		vSpeed.rotate(angle);
	}

	@Override
	public void activity(){
		body.setLinearVelocity(vSpeed);
		if(contactWall || contactBall)
			destroy();	
	}
	
	@Override
	public void draw(SpriteBatch batch){
		if(width == height)
			batch.draw(	animationProjectileBombe.getKeyFrame(GameConstants.ANIM_TIME), 
						(body.getPosition().x - (width * MathUtils.cos(body.getAngle()) - GameConstants.HERO_HEIGHT*MathUtils.sin(body.getAngle())))/* * GameConstants.PPM*/, 
						(body.getPosition().y - (width * MathUtils.sin(body.getAngle()) + GameConstants.HERO_HEIGHT*MathUtils.cos(body.getAngle())))/* * GameConstants.PPM*/, 
						0,
						0,
						2*width/* * GameConstants.PPM*/, 
						2*height/* * GameConstants.PPM*/,
						1,
						1,
						body.getAngle()*MathUtils.radiansToDegrees);
		else
			batch.draw(	animationProjectile.getKeyFrame(GameConstants.ANIM_TIME), 
						(body.getPosition().x - (width * MathUtils.cos(body.getAngle()) - GameConstants.HERO_HEIGHT*MathUtils.sin(body.getAngle())))/* * GameConstants.PPM*/, 
						(body.getPosition().y - (width * MathUtils.sin(body.getAngle()) + GameConstants.HERO_HEIGHT*MathUtils.cos(body.getAngle())))/* * GameConstants.PPM*/, 
						0,
						0,
						2*width/* * GameConstants.PPM*/, 
						2*height/* * GameConstants.PPM*/,
						1,
						1,
						body.getAngle()*MathUtils.radiansToDegrees);
	}
}
