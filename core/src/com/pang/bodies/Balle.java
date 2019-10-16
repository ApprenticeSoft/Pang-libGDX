package com.pang.bodies;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Pools;
import com.pang.MyGdxGame;
import com.pang.enums.ArmeActive;
import com.pang.enums.UpgradeType;
import com.pang.upgrades.PowerUp;
import com.pang.upgrades.UpgradeArme;
import com.pang.utils.GameConstants;

public class Balle {

	final MyGdxGame game;
	public Body body;
	protected BodyDef bodyDef;
	protected FixtureDef fixtureDef;
	protected CircleShape circleShape;
	public float posX, posY, rayon;
	private int indexBall;
	protected boolean contactRope = false;
	private boolean left;
	private float vitesse = 10;
	protected float rebond = 50;
	protected String upgrade;
	public Vector2 vitesseVecteur;
	protected String textureRegionString = "BalleGrande";

	public Balle(final MyGdxGame game){
		this.game = game;
		vitesseVecteur = Pools.obtain(Vector2.class);
	}
	
	public void init(float posX, float posY, boolean left){
		this.posX = posX;
		this.posY = posY;
		this.left = left;
		
		upgrade = null;
		vitesse = 15;
		contactRope = false;	
		createBall();
	}

	public void init(MapObject mapObject){
		Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();

		posX = (rectangle.x + rectangle.width/2) * GameConstants.MPP /** GameConstants.HORIZONTAL_RATIO*/;
		posY = (rectangle.y + rectangle.height/2) * GameConstants.MPP /** GameConstants.VERTICAL_RATIO*/;
		
		if(mapObject.getProperties().get("Vitesse") != null)
			vitesse = Integer.parseInt(mapObject.getProperties().get("Vitesse").toString());
		if(mapObject.getProperties().get("Gauche") != null)
			left = Boolean.parseBoolean(mapObject.getProperties().get("Gauche").toString());
		
		if(mapObject.getProperties().get("Upgrade") != null){
			upgrade = (String)mapObject.getProperties().get("Upgrade");
		}
		else upgrade = null;

		contactRope = false;	
		createBall();
		
		System.out.println("*********************upgrade : " + upgrade);
	}
	
	public Balle(final MyGdxGame game, float posX, float posY, boolean left){
		this.game = game;
		this.posX = posX;
		this.posY = posY; 
		this.left = left;
	}
	
	public void createBall(){
		circleShape = new CircleShape();
		circleShape.setRadius(rayon);
		
		bodyDef = new BodyDef();
		bodyDef.position.set(posX, posY);
		bodyDef.type = BodyType.DynamicBody; 
		body = game.world.createBody(bodyDef);
        body.setFixedRotation(true);
		
		fixtureDef = new FixtureDef();
		fixtureDef.shape = circleShape;
        fixtureDef.density = (float)(.15f/(rayon * rayon * Math.PI)); 
        fixtureDef.friction = 0f;  
        fixtureDef.restitution = 1;
        fixtureDef.filter.groupIndex = (short) -1000;
        //fixtureDef.filter.categoryBits = categoryBits;
   
        body.createFixture(fixtureDef).setUserData("Balle");
        body.setUserData("Balle");
        body.setBullet(true);
        
        circleShape.dispose();
        
        initialImpulse();
	}
	
	public void activity(){
		if(contactRope){
			contactRope();
		}
		else {
			if(Math.abs(body.getLinearVelocity().x) != vitesse)
				body.setLinearVelocity(vitesse * Math.signum(body.getLinearVelocity().x), body.getLinearVelocity().y);
		}
		
	}
	
	public void contactRope(){
		Balle balle1 = new Balle(game, GameConstants.PPM * (body.getPosition().x - rayon), GameConstants.PPM * body.getPosition().y, true);
		Balle balle2 = new Balle(game, GameConstants.PPM * (body.getPosition().x + rayon), GameConstants.PPM * body.getPosition().y, false);
		game.balles.add(balle1);
		game.balles.add(balle2);
		destroy();
	}
	
	public void initialImpulse(){
		if(left)
			body.applyLinearImpulse(-vitesse * GameConstants.MPP, 0, 0, 0, true);
		else
			body.applyLinearImpulse(vitesse * GameConstants.MPP, 0, 0, 0, true);
		
		vitesseVecteur.set(body.getLinearVelocity());	
	}
	
	public void rebond(){
		body.setLinearVelocity(body.getLinearVelocity().x, rebond);
	}
	
	public Vector2 getSpeed(){
		return body.getLinearVelocity();
	}	
	
	public void setContactRope(boolean contact){
		contactRope = contact;
		System.out.println("contactRope = " + contactRope);
	}
	
	public boolean getContactArme(){
		return contactRope;
	}
	
	public void setIndex(int i){
		indexBall = i;
	}
	
	public int getIndex(){
		return indexBall;
	}
	
	public void destroy(){
		for(ArmeActive armeActive : ArmeActive.values()){
			if(armeActive.toString().equals(upgrade)){
				UpgradeArme upgradeArme = (UpgradeArme) game.pools.obtain(UpgradeArme.class);
        		upgradeArme.init(body.getPosition().x * GameConstants.PPM, body.getPosition().y * GameConstants.PPM, armeActive);
        		System.out.println("game.upgrades.size = " + game.upgrades.size);
        		game.upgrades.add(upgradeArme);
        		System.out.println("game.upgrades.size = " + game.upgrades.size);
			};
		}

		for(UpgradeType upgradeType : UpgradeType.values()){
			if(upgradeType.toString().equals(upgrade)){
				PowerUp powerUp = (PowerUp) game.pools.obtain(PowerUp.class);
				powerUp.init(body.getPosition().x * GameConstants.PPM, body.getPosition().y * GameConstants.PPM, upgradeType);
        		System.out.println("game.upgrades.size = " + game.upgrades.size);
        		game.upgrades.add(powerUp);
        		System.out.println("game.upgrades.size = " + game.upgrades.size);
			};
		}
		
		randomUpgrade();
		
		dispose();
	}
	
	public void randomUpgrade(){
		if(MathUtils.random(0, 100) < 5){
			UpgradeArme upgradeArme = (UpgradeArme) game.pools.obtain(UpgradeArme.class);
			upgradeArme.init(body.getPosition().x * GameConstants.PPM, body.getPosition().y * GameConstants.PPM, ArmeActive.getRandom());
			game.upgrades.add(upgradeArme);
		}
		else if(MathUtils.random(0, 100) < 5){
			PowerUp powerUp = (PowerUp) game.pools.obtain(PowerUp.class);
			powerUp.init(body.getPosition().x * GameConstants.PPM, body.getPosition().y * GameConstants.PPM, UpgradeType.getRandom());
    		game.upgrades.add(powerUp);
		}
	}
	
	public void draw(SpriteBatch batch){		
		batch.setColor(1, 1, 1, 1);
		batch.draw(	game.textureAtlas.findRegion(textureRegionString), 
					(body.getPosition().x - rayon)/*  * GameConstants.PPM*/, 
					(body.getPosition().y - rayon)/* * GameConstants.PPM*/,
					2*rayon/* * GameConstants.PPM*/,
					2*rayon/* * GameConstants.PPM*/);
	}
	
	public void dispose(){
		body.setActive(false);
		body.setUserData(null);
		game.world.destroyBody(body);
		body = null;
		//game.balles.removeIndex(indexBall);
		game.balles.removeIndex(game.balles.indexOf(this, true));
		game.pools.free(this);
	}
}
