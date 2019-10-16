package com.pang.bodies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.ShortArray;
import com.pang.MyGdxGame;
import com.pang.utils.GameConstants;

public class Debris {

	final MyGdxGame game;
	private PolygonShape debrisShape;
	private BodyDef bodyDef;
	private FixtureDef fixtureDef;
	public Body body;
	
	private Texture textureSolid;
	private PolygonSprite polySprite, polySpriteOutline;
	float coordPoly[];
	
	public Debris (final MyGdxGame game){
		this.game = game;
	}
	
	public Debris(final MyGdxGame game, float width, float height, float x, float y){
		this.game = game;
		
		create(width, height, x, y);
	}
	
	public void init(float width, float height, float x, float y){
		create(width, height, x, y);
	}
	
	public void create(float width, float height, float x, float y){
		int nbCoté = MathUtils.random(4, 6);
		
		Vector2 debrisCoordinates [] = new Vector2 [nbCoté];
		
		if(nbCoté == 6){
			debrisCoordinates[0] = Pools.obtain(Vector2.class).set(-width * MathUtils.random(0.9f, 1.1f), -height * MathUtils.random(0.75f, 1.1f));
			debrisCoordinates[1] = Pools.obtain(Vector2.class).set(0, -height * MathUtils.random(1.2f, 1.5f));
			debrisCoordinates[2] = Pools.obtain(Vector2.class).set(width * MathUtils.random(0.8f, 1.1f), -height * MathUtils.random(0.9f, 1.1f));
			debrisCoordinates[3] = Pools.obtain(Vector2.class).set(width * MathUtils.random(1.25f, 1.6f), 0.12f * height);
			debrisCoordinates[4] = Pools.obtain(Vector2.class).set(width * MathUtils.random(0.9f, 1.3f), height * MathUtils.random(0.9f, 1.2f));
			debrisCoordinates[5] = Pools.obtain(Vector2.class).set(-width * MathUtils.random(0.9f, 1.1f), height * MathUtils.random(0.9f, 1.1f));
		}
		else if(nbCoté == 5){
			debrisCoordinates[0] = Pools.obtain(Vector2.class).set(-width * MathUtils.random(0.9f, 1.1f), -height * MathUtils.random(0.75f, 1.1f));
			debrisCoordinates[1] = Pools.obtain(Vector2.class).set(0, -height * MathUtils.random(1.2f, 1.5f));
			debrisCoordinates[2] = Pools.obtain(Vector2.class).set(width * MathUtils.random(0.8f, 1.1f), -height * MathUtils.random(0.9f, 1.1f));
			debrisCoordinates[3] = Pools.obtain(Vector2.class).set(width * MathUtils.random(0.9f, 1.6f), height * MathUtils.random(0.9f, 1.2f));
			debrisCoordinates[4] = Pools.obtain(Vector2.class).set(-width * MathUtils.random(0.9f, 1.1f), height * MathUtils.random(0.9f, 1.1f));
		}
		else{
			debrisCoordinates[0] = Pools.obtain(Vector2.class).set(-width * MathUtils.random(0.5f, 1.2f), -height * MathUtils.random(0.75f, 1.1f));
			debrisCoordinates[1] = Pools.obtain(Vector2.class).set(width * MathUtils.random(0.8f, 1.1f), -height * MathUtils.random(0.9f, 1.1f));
			debrisCoordinates[2] = Pools.obtain(Vector2.class).set(width * MathUtils.random(0.9f, 1.3f), height * MathUtils.random(0.9f, 1.2f));
			debrisCoordinates[3] = Pools.obtain(Vector2.class).set(-width * MathUtils.random(0.9f, 1.1f), height * MathUtils.random(0.9f, 1.1f));
		}
		
		debrisShape = new PolygonShape();
		debrisShape.set(debrisCoordinates);
				
		bodyDef = new BodyDef();
		bodyDef.position.set(x, y);
		bodyDef.type = BodyType.DynamicBody;
		body = game.world.createBody(bodyDef);
		
		fixtureDef = new FixtureDef();
		fixtureDef.shape = debrisShape;
		fixtureDef.density = (float)(1/(5 * width * height));  
		fixtureDef.friction = 0.0f;  
		fixtureDef.restitution = 0.5f;
        fixtureDef.filter.groupIndex = (short) -1000;
		
		body.createFixture(fixtureDef).setUserData("Debris");
		body.setUserData("Debris");
		
		Vector2 impulseForce = Pools.obtain(Vector2.class).set(	MathUtils.random(-15, 15) * body.getFixtureList().get(0).getDensity(), 
																MathUtils.random(-15, 15) * body.getFixtureList().get(0).getDensity());
		Vector2 impulseCenter = Pools.obtain(Vector2.class).set(body.getPosition().x + MathUtils.random(-0.9f * width, 0.9f * height), 
																body.getPosition().y + MathUtils.random(-0.9f * width, 0.9f * height));
		
		body.applyLinearImpulse(impulseForce, impulseCenter, true);
		
		/****************DESSIN*****************/
		coordPoly  = new float [debrisCoordinates.length*2];	
		for(int i = 0; i < debrisCoordinates.length; i++){
			coordPoly[2*i] = debrisCoordinates[i].x;
			coordPoly[2*i + 1] = debrisCoordinates[i].y;
		}
		
        // Creating the color filling (but textures would work the same way)
        Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pix.setColor(91/256f, 110/256f, 225/256f, 1); 
        pix.fill();
        textureSolid = new Texture(pix);
        TextureRegion textureRegion = new TextureRegion(textureSolid);

        EarClippingTriangulator triangulator = new EarClippingTriangulator();
        ShortArray triangleIndices = triangulator.computeTriangles(coordPoly);

        PolygonRegion polyReg = new PolygonRegion(textureRegion, coordPoly, triangleIndices.toArray());

        polySprite = new PolygonSprite(polyReg);
        polySprite.setOrigin(body.getLocalCenter().x, body.getLocalCenter().y);
        polySprite.scale(-0.2f);
		
        polySpriteOutline = new PolygonSprite(polyReg);
        polySpriteOutline.setOrigin(body.getLocalCenter().x, body.getLocalCenter().y);
        //polySpriteOutline.scale(0.15f);
        polySpriteOutline.setColor(34/256f, 32/256f, 52/256f, 1);
        /***************************************/
        
		debrisShape.dispose();
		
		for(int i = 0; i < debrisCoordinates.length; i++)
			Pools.free(debrisCoordinates[i]);
		Pools.free(impulseForce);
		Pools.free(impulseCenter);
	}
	
	public void contact(){
		body.getFixtureList().get(0).setSensor(true);
		System.out.println("Debris Contact !");
	}
	
	public void draw(PolygonSpriteBatch polyBatch){
        //polySprite.rotate(debrisBody.getAngle()/* * MathUtils.radiansToDegrees*/);
        polySpriteOutline.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
        polySpriteOutline.setX(body.getPosition().x);
        polySpriteOutline.setY(body.getPosition().y);
	    polySpriteOutline.draw(polyBatch);
		
        polySprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
		setPos(body.getPosition().x, body.getPosition().y);
	    polySprite.draw(polyBatch);

        //System.out.println("PolySprite X = "  + polySprite.getX());
        //System.out.println("PolySprite Y = "  + polySprite.getY());
        //System.out.println("PolySprite originX = "  + polySprite.getOriginX());
        //System.out.println("PolySprite originY = "  + polySprite.getOriginY());

        //System.out.println("body.getPosition().x = "  + body.getPosition().x);
        //System.out.println("body.getPosition().y = "  + body.getPosition().y);
        //System.out.println("***********************************************");
	}
	
	public void activity(){
		if(body.getPosition().y < -0.01f*GameConstants.LEVEL_HEIGHT){
			destroy();
		}
	}
	
	public void setPos(float X, float Y){
        polySprite.setX(X);
        polySprite.setY(Y);
	}
	
	public void destroy(){
		body.setActive(false);
		body.setUserData(null);
		game.world.destroyBody(body);
		body = null;
		//game.upgrades.removeIndex(index);
		game.debris.removeIndex(game.debris.indexOf(this, true));
		game.pools.free(this);
	}
	
	public void dispose(){
		destroy();
	}

}
