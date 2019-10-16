package com.pang.armes;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Pools;
import com.pang.MyGdxGame;
import com.pang.utils.GameConstants;

public class Arme {

	final MyGdxGame game;
	protected BodyDef bodyDef;
	public Body body;
	protected FixtureDef fixtureDef;
	protected PolygonShape polygonShape;
	protected Fixture fixture;
	protected float posX, posY, height, width;
	public boolean contactWall = false, contactBall = false;
	protected int index;
	protected Vector2 polygonCenter;
	
	public Arme(final MyGdxGame game){
		this.game = game;	
		polygonCenter = Pools.obtain(Vector2.class);
	}
	
	public void init(float posX, float posY){
		this.posX = posX;
		this.posY = posY;
		polygonCenter.set(0, height - GameConstants.HERO_HEIGHT);
		width = GameConstants.HERO_WIDTH/4;

		contactWall = false;
		contactBall = false;
	}
	
	public void create(){
		polygonShape = new PolygonShape();

		polygonCenter.set(0, height - GameConstants.HERO_HEIGHT);
		polygonShape.setAsBox(width, height, polygonCenter, 0);
		//Pools.free(polygonCenter);
		
		bodyDef = new BodyDef();
		bodyDef.position.set(posX,  posY);
		bodyDef.type = BodyType.DynamicBody; 
		body = game.world.createBody(bodyDef);

        fixture = body.createFixture(polygonShape, 0.0f);
        fixture.setSensor(true);
       	fixture.setUserData("Arme");
        body.setUserData("Arme");
        body.setFixedRotation(true);
        
        polygonShape.dispose();	
	}
	
	public void activity(){
		//Décrit l'activité de l'arme
	}

	
	public void contactWall(/*int indexRope*/){
		contactWall = true;
		//index = indexRope;
	}
	
	public void contactBall(int indexArme, int indexBalle){
		contactBall = true;
		index = indexArme;
		game.balles.get(indexBalle).setContactRope(true);
		game.balles.get(indexBalle).setIndex(indexBalle);

		System.out.println("indexRope : " + indexArme);
		System.out.println("indexBalle : " + indexBalle);
	}
	
	public void destroy(){
		body.setActive(false);
		body.setUserData(null);
		game.world.destroyBody(body);
		body = null;
		//body = null;
		//game.armes.removeIndex(index);
		game.armes.removeIndex(game.armes.indexOf(this, true)); 
		game.pools.free(this);
	}
	
	public void draw(SpriteBatch batch){
		
	}
	
	public void dispose(){
		destroy();
	}
}
