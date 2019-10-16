package com.pang.upgrades;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;
import com.pang.MyGdxGame;
import com.pang.bodies.Balle;
import com.pang.enums.UpgradeType;
import com.pang.utils.Blinker;
import com.pang.utils.GameConstants;

public class Upgrade {

	final MyGdxGame game;
	protected BodyDef bodyDef;
	public Body body;
	protected PolygonShape polygonShape;
	protected Fixture fixture;
	protected float posX, posY, width, height;
	public boolean contactHero = false;
	protected int index;
	protected float cooldown, delai;
	protected boolean create = false;
	
	protected Blinker blinker;
	
	public Upgrade(final MyGdxGame game){
		this.game = game;
		    
        blinker = new Blinker();
        blinker.setBlinking(true);
	}
	
	public void init(float posX, float posY){
		this.posX = posX;
		this.posY = posY;

		contactHero = false;
		create = false;
		
		//createBody(posX, posY);
	}


	public void init(MapObject mapObject){
		Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();

		posX = (rectangle.x + rectangle.width/2) /** GameConstants.HORIZONTAL_RATIO*/;
		posY = (rectangle.y + rectangle.height/2) /** GameConstants.VERTICAL_RATIO*/;

		contactHero = false;
		create = false;
		
		width = GameConstants.HERO_WIDTH/2;
		height = GameConstants.HERO_WIDTH/2;
		
		//createBody(posX, posY);
	}
	
	public void createBody(float posX, float posY){
		polygonShape = new PolygonShape();
		
		Vector2 polygonCenter = Pools.obtain(Vector2.class).set(0, /*height*/ - GameConstants.HERO_HEIGHT);
		polygonShape.setAsBox(width, height/*, polygonCenter, 0*/);
		Pools.free(polygonCenter);
		
		bodyDef = new BodyDef();
		bodyDef.position.set(GameConstants.MPP * posX, GameConstants.MPP * posY);
		bodyDef.type = BodyType.DynamicBody; 
		body = game.world.createBody(bodyDef);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = polygonShape;
        fixtureDef.density = 1;  
        fixtureDef.friction = 0.0f;  
        fixtureDef.restitution = 0.5f;
        fixtureDef.filter.groupIndex = (short) -1000;
        
        //fixture = body.createFixture(polygonShape, 0.0f);
       	//fixture.setUserData("Upgrade");

        body.createFixture(fixtureDef).setUserData("Upgrade");
        body.setUserData("Upgrade");
        body.setFixedRotation(true);
        
        polygonShape.dispose();	

		System.out.println("Created an upgrade !!!!");
	}
	
	public void activity(){
		delai -= Gdx.graphics.getDeltaTime();
		if(delai < 0){
			if(!create){
				create = true;
				createBody(posX, posY);
			}
			
			cooldown -= Gdx.graphics.getDeltaTime();
			if(cooldown < 0)
				destroy();
			
			if(contactHero)
				destroy();
		}
	}
	
	public void contactHero(int indexUpgrade){
		contactHero = true;
		index = indexUpgrade;
	}
	
	public void draw(SpriteBatch batch){
		
	}
	
	public void destroy(){
		if(delai < 0){
			body.setActive(false);
			body.setUserData(null);
			game.world.destroyBody(body);
			body = null;
		}
		//game.upgrades.removeIndex(index);
		game.upgrades.removeIndex(game.upgrades.indexOf(this, true));
		game.pools.free(this);
	}
	
	public void dispose(){
		destroy();
	}
}
