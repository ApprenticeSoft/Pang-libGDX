package com.pang.bodies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Pools;
import com.pang.MyGdxGame;
import com.pang.utils.GameConstants;

public class Obstacle{
	
	final MyGdxGame game;
	public Body body;
	public BodyDef bodyDef;
	protected PolygonShape polygonShape;
	public float posX, posY, width, height, angle;
	public boolean destructible = false;
	public int duretée = 2;
	protected NinePatch ninePatch;
	
	//protected Vector2 initialPosition;
	protected FixtureDef fixtureDef;
	Camera camera;
	
    private Texture textureFissure;

	public Obstacle(/*World world,*/final MyGdxGame game, Camera camera, MapObject mapObject){	
		this.game = game;
		create(/*world,*/ camera, mapObject);
		
		//initialPosition = Pools.obtain(Vector2.class).set(body.getPosition());
		if(!destructible)
			ninePatch = new NinePatch(game.textureAtlas.findRegion("Obstacle"), 7,7,7,7);
		else
			ninePatch = new NinePatch(game.textureAtlas.findRegion("Obstacle_destructible"), 7,7,7,7);
		ninePatch.scale(2*GameConstants.MPP, 2*GameConstants.MPP);

      	textureFissure = new Texture(Gdx.files.internal("Images/Fissure.png")); 
      	textureFissure.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);   	
	}
	
	public float getWidth(){
		return width;
	}
	
	public float getHeight(){
		return height;
	}
	
	public float getX(){
		return posX;
	}
	
	public float getY(){
		return posY;
	}

	public void setX( float X){
		posX = X;
	}

	public void setY( float Y){
		posY = Y;
	}
	
	public BodyType getBodyType(){
		return BodyType.StaticBody;
	}
	
	public void create(/*World world,*/ Camera camera, MapObject rectangleObject){

		Rectangle rectangle = ((RectangleMapObject) rectangleObject).getRectangle();
		
		this.camera = camera;
		this.posX = (rectangle.x + rectangle.width/2)  * GameConstants.MPP /* * GameConstants.HORIZONTAL_RATIO*/;
		this.posY = (rectangle.y + rectangle.height/2) * GameConstants.MPP /* * GameConstants.VERTICAL_RATIO*/;
		this.width = (rectangle.width/2) * GameConstants.MPP /* * GameConstants.HORIZONTAL_RATIO*/;
		this.height = (rectangle.height/2) * GameConstants.MPP /* * GameConstants.VERTICAL_RATIO*/;
		
		if(rectangleObject.getProperties().get("rotation") != null)
			this.angle = -Float.parseFloat(rectangleObject.getProperties().get("rotation").toString())*MathUtils.degreesToRadians;
		if(rectangleObject.getProperties().get("Destructible") != null)
			destructible = Boolean.parseBoolean(rectangleObject.getProperties().get("Destructible").toString());
		else destructible = false;
		
		bodyDef = new BodyDef();
    	
		polygonShape = new PolygonShape();
		polygonShape.setAsBox(width, height);

		bodyDef.position.set(posX, posY);
    	bodyDef.type = getBodyType();
		body = game.world.createBody(bodyDef);
		
		fixtureDef = new FixtureDef();
		fixtureDef.shape = polygonShape;
        fixtureDef.density = (float)(1/(5 * width * height));  
        fixtureDef.friction = 0.0f;  
        fixtureDef.restitution = 0.0f;
   
        body.createFixture(fixtureDef).setUserData("Obstacle");
        body.setUserData("Haut");
        
        if(rectangleObject.getProperties().get("rotation") != null){
            /*
             * Positions x' et y', à partir des positions x et y après une rotation d'un angle A
             * x' = x*cos(A) - y*sin(A)
             * y' = x*sin(A) + y*cos(A)
             * 
             * Ceci est vrai si la rotation est autour de l'origine (0,0)
             */
        	float X = (float)(body.getPosition().x - width + width * Math.cos(angle) + height * Math.sin(angle));
        	float Y = (float)(width * Math.sin(angle) + body.getPosition().y + height - height * Math.cos(angle));
        	body.setTransform(X, Y, this.angle);
        }
        
        polygonShape.dispose();       
	}
	
	public void draw(SpriteBatch batch){
		/*
		batch.draw(	game.textureAtlas.findRegion("Obstacle"), 
					(body.getPosition().x - width) * GameConstants.PPM, 
					(body.getPosition().y - height) * GameConstants.PPM,
					width,
					height,
					2 * width * GameConstants.PPM,
					2 * height * GameConstants.PPM,
					1,
					1,
					body.getAngle()*MathUtils.radiansToDegrees);
		*/
			ninePatch.draw(	batch, 
							(body.getPosition().x - width)/* * GameConstants.PPM*/, 
							(body.getPosition().y - height)/* * GameConstants.PPM*/,
							2 * width/* * GameConstants.PPM*/, 
							2 * height/* * GameConstants.PPM*/);		
			if(duretée < 2){
				/*
				game.batch.draw(textureFissure, 
								(body.getPosition().x - width), 
								(body.getPosition().y - height), 
								2 * width, 
								2 * height,  
								0, 
								0, 
								(int)(GameConstants.LEVEL_PIXEL_WIDTH / 10), 
								(int)(GameConstants.LEVEL_PIXEL_WIDTH / 10), 
								false, 
								false);
				*/
				game.batch.draw(textureFissure, 
								(body.getPosition().x - width), 
								(body.getPosition().y - height), 
								2 * width, 
								2 * height,  
								0, 
								0, 
								(int) (2*textureFissure.getHeight() * GameConstants.NB_VERTICAL_TILE * width/GameConstants.PPT), 
								(int) (2*textureFissure.getHeight() * GameConstants.NB_VERTICAL_TILE * height/GameConstants.PPT), 
								false, 
								false);
			}		
	}
	
	public void impact(){
		if(duretée > 0)
			duretée--;
		
		System.out.println("Duretée = " + duretée);
	}
	
	public void actif(){
		
	}
	
	public void activity(){
		if(duretée < 1)
			destroy();
	}
	
	public void initiate(){
		
	}
	
	public void spawnDebris(){	 
      	int colonne = (int) (width/(10*GameConstants.MPP));
      	int ligne = (int) (height/(10*GameConstants.MPP));

      	for(int i = 0; i < colonne; i++){       
      		for(int j = 0; j < ligne; j++){
				//Debris debris = new Debris(game, width/colonne, height/ligne, posX - width + 2*i*width/colonne, posY - height + 2*j*height/ligne);
				
				Debris debris = game.pools.debrisPool.obtain();
				debris.init(width/colonne, height/ligne, posX - width + 2*i*width/colonne, posY - height + 2*j*height/ligne);
				game.debris.add(debris);
      		}         
      	}
	}

	public void destroy(){
		body.setActive(false);
		body.setUserData(null);
		game.world.destroyBody(body);
		body = null;
		game.obstacles.removeIndex(game.obstacles.indexOf(this, true)); 
		//game.pools.free(this);
		
		spawnDebris();
	}
}
