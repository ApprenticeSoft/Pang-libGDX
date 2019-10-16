package com.pang.bodies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Pools;
import com.pang.MyGdxGame;
import com.pang.armes.Projectile;
import com.pang.armes.Rope;
import com.pang.armes.Grappin;
import com.pang.enums.ArmeActive;
import com.pang.utils.GameConstants;

public class Hero {
	
	final MyGdxGame game;
	public Body body;
	protected BodyDef bodyDef;
	protected FixtureDef fixtureDef;
	protected PolygonShape polygonShape;
	public float posX, posY, width, height, cooldown = 0, dureeAnimationTir = 0, spriteHeight, spriteWidth, sens;
	public int vitesse = 620;
	public boolean droite, contactSol;
	private Vector2 déplacementCharacter;
	public static ArmeActive arme;	

	//private TextureAtlas heroAtlas;
	private Animation<TextureRegion> animationCourse, animationTire, animationSautMonte, animationSautDescent, animationBouclier, animationBouclierExplosion;
	private float explosionTime = 10;
	
	//Mitraillet
	float angleTir = 0;
	
	public Hero(final MyGdxGame game){
		this.game = game;
		
		posX = GameConstants.MPP * Gdx.graphics.getWidth()/2;
		posY = GameConstants.MPP * 0.05f * Gdx.graphics.getHeight();
		
		create();      
	}
		
	public Hero(final MyGdxGame game, float X, float Y){
		this.game = game;
		
		posX = X;
		posY = Y;
		
		create();      
	}
	
	public void create(){
		height = GameConstants.HERO_HEIGHT;
		width = GameConstants.HERO_WIDTH;
		
		arme = ArmeActive.CORDE_SIMPLE;

		déplacementCharacter = Pools.obtain(Vector2.class).set(0,0);
		
		polygonShape = new PolygonShape();
		polygonShape.setAsBox(GameConstants.HERO_WIDTH, GameConstants.HERO_HEIGHT - GameConstants.HERO_WIDTH/2);

		bodyDef = new BodyDef();
		bodyDef.position.set(posX, posY);
		bodyDef.type = BodyType.DynamicBody; 
		body = game.world.createBody(bodyDef);
		
		fixtureDef = new FixtureDef();
		fixtureDef.shape = polygonShape;
        fixtureDef.density = .01f;  
        fixtureDef.friction = 0.1f;  
        fixtureDef.restitution = 0;
        //fixtureDef.filter.categoryBits = categoryBits;
   
        body.createFixture(fixtureDef).setUserData("Hero");
        body.setUserData("Hero");
        body.setFixedRotation(true);
        
        //Création du pied
        CircleShape pied = new CircleShape();
        pied.setRadius(1.05f * width);
        Vector2 positionPied = Pools.obtain(Vector2.class);
        positionPied.set(0, -height + width/2);
        pied.setPosition(positionPied);
        Pools.free(positionPied);
        fixtureDef.shape = pied;
        fixtureDef.density = 0.0f;  
		body.createFixture(fixtureDef).setUserData("Hero");
		body.setUserData("Hero");
		body.setFixedRotation(true);
        
        //Création du détecteur 
        PolygonShape detecteurShape = new PolygonShape();
        detecteurShape.setAsBox(0.20f*width,width/3, new Vector2(0,-height - width/2),0); //Taille et positionnement (new Vector2()) du detecteur par rapport au body 
		fixtureDef.shape = detecteurShape;
        fixtureDef.density = 0.0f;  
		fixtureDef.isSensor = true;			
		
		body.createFixture(fixtureDef).setUserData("PiedDetecteur");
		body.setUserData("Hero");
		body.setFixedRotation(true);
		
		//Création du bouclier
		CircleShape bouclierShape = new CircleShape();
		bouclierShape.setRadius(1.3f*height);
        Vector2 positionBouclier = Pools.obtain(Vector2.class);
        positionBouclier.set(0, -width/2);
        bouclierShape.setPosition(positionBouclier);
		fixtureDef.shape = bouclierShape;
        fixtureDef.density = 0.0f;  
		//fixtureDef.isSensor = false;			
		
		body.createFixture(fixtureDef).setUserData("BouclierOff");
		body.setUserData("Hero");
		
		bouclierShape.dispose();
		detecteurShape.dispose(); 
		pied.dispose();
        polygonShape.dispose();
        
        /*
         * Animations
         */
    	//heroAtlas = game.assets.get("Images/Hero/Hero.pack", TextureAtlas.class);
        animationCourse = new Animation(1/15f, game.textureAtlas.findRegions("Hero_Course"), Animation.PlayMode.LOOP);
        animationTire = new Animation(1/15f, game.textureAtlas.findRegions("Hero_Tir"), Animation.PlayMode.NORMAL);
        animationSautMonte = new Animation(1/15f, game.textureAtlas.findRegions("Hero_Saut_Montee"), Animation.PlayMode.NORMAL);
        animationSautDescent = new Animation(1/15f, game.textureAtlas.findRegions("Hero_Saut_Descente"), Animation.PlayMode.NORMAL);
        animationBouclier = new Animation(1/15f, game.textureAtlas.findRegions("Bouclier"), Animation.PlayMode.LOOP);
        animationBouclierExplosion = new Animation(1/45f, game.textureAtlas.findRegions("Bouclier_Explosion"), Animation.PlayMode.NORMAL);
        
        spriteHeight = 2.2f * height;
        spriteWidth = spriteHeight * animationCourse.getKeyFrame(0, true).getRegionWidth()  / animationCourse.getKeyFrame(0, true).getRegionHeight();  
        
        
	}
	
	public void control(){	
		GameConstants.HERO_ANIM_TIME += Gdx.graphics.getDeltaTime();
		
		//Déplacements
		if(Gdx.input.isKeyPressed(Keys.A)){
        	déplacementCharacter.x = - vitesse * GameConstants.MPP;
        	droite = false;
        }
	    else if(Gdx.input.isKeyPressed(Keys.D)){
	    	déplacementCharacter.x = vitesse * GameConstants.MPP;
	    	droite = true;
	    }
	    else {
	    	déplacementCharacter.x = 0;
	    }
		
		if(dureeAnimationTir > 0)
	    	déplacementCharacter.x = 0;
		
		if(Gdx.input.isKeyPressed(Keys.W)){
			if(contactSol){
				GameConstants.HERO_ANIM_TIME = 0;
				body.applyLinearImpulse(0, 4.8f, 0, 0, true);
			}
        	contactSol = false;
        }
		
		
		//Tirs
		if(Gdx.input.isKeyPressed(Keys.SPACE)){
			switch(arme){
				case CORDE_SIMPLE:
					if(GameConstants.NB_TIR < 2)
						cordeSimple();
					break;
				case CORDE_DOUBLE:
					if(GameConstants.NB_TIR < 2)
						cordeDouble();
					break;
				case GRAPPIN:
					if(GameConstants.NB_TIR < 2)
						grappin();
					break;
				case MITRAILLETTE:
					mitraillette();
					break;
				case TROMBLON:
					tromblon();
					break;
			}
		}

		//Bouclier
		if(GameConstants.activationBouclier){
			bouclierOn();
			GameConstants.activationBouclier = false;
		}
		//Bombe
		if(GameConstants.bombeOn){
			bombe();
			GameConstants.bombeOn = false;
		}
		
        
        déplacementCharacter.y = body.getLinearVelocity().y;   
        body.setLinearVelocity(déplacementCharacter);
        
        cooldown -= Gdx.graphics.getDeltaTime();
        dureeAnimationTir -= Gdx.graphics.getDeltaTime();
        
        body.applyForceToCenter(0, -15, true); 
	}
	
	public void cordeSimple(){
		if(cooldown < 0){
			GameConstants.HERO_ANIM_TIME = 0;
			Rope rope = (Rope)game.pools.obtain(Rope.class);
			rope.init(getPosition().x, getPosition().y);
			game.armes.add(rope);
			System.out.println("PEW !");
			cooldown = .15f;
			dureeAnimationTir = .25f;

			GameConstants.NB_TIR++;
		}
	}
	
	public void cordeDouble(){
		if(cooldown < 0){
			GameConstants.HERO_ANIM_TIME = 0;
			Rope rope1 = (Rope)game.pools.obtain(Rope.class);
			rope1.init(getPosition().x - 0.70f * width, getPosition().y);
			Rope rope2 = (Rope)game.pools.obtain(Rope.class);
			rope2.init(getPosition().x + 0.70f * width, getPosition().y);
			
			game.armes.add(rope1);
			game.armes.add(rope2);
			cooldown = .15f;
			dureeAnimationTir = .25f;
			
			GameConstants.NB_TIR++;

			System.out.println("PEW PEW !");
		}
	}

	public void tromblon(){
		if(cooldown < 0){
			GameConstants.HERO_ANIM_TIME = 0;
			Projectile projectile1 = (Projectile)game.pools.obtain(Projectile.class);
			projectile1.init(getPosition().x, getPosition().y + 0.5f*GameConstants.HERO_HEIGHT, 0);
			Projectile projectile2 = (Projectile)game.pools.obtain(Projectile.class);
			projectile2.init(getPosition().x - 0.8f*GameConstants.HERO_WIDTH, getPosition().y + 0.5f*GameConstants.HERO_HEIGHT, 45);
			Projectile projectile3 = (Projectile)game.pools.obtain(Projectile.class);
			projectile3.init(getPosition().x + 0.8f*GameConstants.HERO_WIDTH, getPosition().y + 0.5f*GameConstants.HERO_HEIGHT, -45);
			game.armes.add(projectile1);
			game.armes.add(projectile2);
			game.armes.add(projectile3);
			System.out.println("PEW !");
			cooldown = .5f;	
			dureeAnimationTir = .25f;
		}
	}

	public void mitraillette(){
		if(cooldown < 0){
			GameConstants.HERO_ANIM_TIME = 0;
			Projectile projectile1 = (Projectile)game.pools.obtain(Projectile.class);
			projectile1.init(getPosition().x, getPosition().y + 0.5f*GameConstants.HERO_HEIGHT, 70*MathUtils.cos(angleTir));
			game.armes.add(projectile1);
			System.out.println("PEW !");
			cooldown = .12f;	
			dureeAnimationTir = .25f;
			
			angleTir += 0.45f;
		}
	}
	
	public void grappin(){
		if(cooldown < 0){
			GameConstants.HERO_ANIM_TIME = 0;
			Grappin rope = (Grappin)game.pools.obtain(Grappin.class);
			rope.init(getPosition().x, getPosition().y);
			game.armes.add(rope);
			System.out.println("PEW !");
			cooldown = .25f;	
			dureeAnimationTir = .25f;

			GameConstants.NB_TIR++;
		}
	}
	
	public void bouclierOn(){
		GameConstants.bouclierOn = true;
		body.getFixtureList().get(3).setSensor(false);
		body.getFixtureList().get(3).setUserData("Bouclier");	
	}
	
	public void bouclierOff(){
		GameConstants.bouclierOn = false;
		body.getFixtureList().get(3).setSensor(true);
		body.getFixtureList().get(3).setUserData("BouclierOff");
		
		explosionTime = 0;
	}
	
	public void bombe(){
		for(int i = 0; i < 40; i++){
			Projectile projectile = (Projectile)game.pools.obtain(Projectile.class);
			projectile.init(	getPosition().x, 
								getPosition().y + 0.5f*GameConstants.HERO_HEIGHT,  
								0.1875f * GameConstants.HERO_HEIGHT, 
								0.1875f * GameConstants.HERO_HEIGHT,
								50,
								i*(360/40));
			game.armes.add(projectile);
			System.out.println("PEW !");
		}
	}
	
	public Vector2 getPosition(){
		return body.getPosition();
	}
	
	public void draw(SpriteBatch batch, float animTime){
		batch.setColor(1, 1, 1, 1);
		//if(Gdx.input.isKeyPressed(Keys.SPACE))
		if(contactSol){
			if(dureeAnimationTir > 0)
				batch.draw(	animationTire.getKeyFrame(animTime), 
							(body.getPosition().x - width - 0.25f*spriteWidth)/*  * GameConstants.PPM*/, 
							(body.getPosition().y - height - width/2)/* * GameConstants.PPM*/, 
							width,
							spriteHeight - height/2,
							spriteWidth/* * GameConstants.PPM*/, 
							spriteHeight/* * GameConstants.PPM*/,
							1,
							1,
							body.getAngle()*MathUtils.radiansToDegrees);
			else if(déplacementCharacter.x == 0)
				batch.draw(	animationTire.getKeyFrame(2), 
							(body.getPosition().x - width - 0.25f*spriteWidth)/*  * GameConstants.PPM*/, 
							(body.getPosition().y - height - width/2)/* * GameConstants.PPM*/, 
							width,
							spriteHeight - height/2,
							spriteWidth/* * GameConstants.PPM*/, 
							spriteHeight/* * GameConstants.PPM*/,
							1,
							1,
							body.getAngle()*MathUtils.radiansToDegrees);
			else if(déplacementCharacter.x > 0)
				batch.draw(	animationCourse.getKeyFrame(animTime), 
							(body.getPosition().x - width + (0.25f - Math.signum(déplacementCharacter.x ) * 0.5f)*spriteWidth)/* * GameConstants.PPM*/, 
							(body.getPosition().y - height - width/2)/* * GameConstants.PPM*/, 
							width,
							spriteHeight - height/2,
							Math.signum(déplacementCharacter.x ) * spriteWidth/* * GameConstants.PPM*/, 
							spriteHeight/* * GameConstants.PPM*/,
							1,
							1,
							body.getAngle()*MathUtils.radiansToDegrees);
			else if(déplacementCharacter.x < 0)
				batch.draw(	animationCourse.getKeyFrame(animTime), 
							(body.getPosition().x - width + (0.25f - Math.signum(déplacementCharacter.x ) * 0.5f)*spriteWidth)/*  * GameConstants.PPM*/, 
							(body.getPosition().y - height - width/2)/* * GameConstants.PPM*/, 
							width,
							spriteHeight - height/2,
							Math.signum(déplacementCharacter.x ) * spriteWidth/* * GameConstants.PPM*/, 
							spriteHeight/* * GameConstants.PPM*/,
							1,
							1,
							body.getAngle()*MathUtils.radiansToDegrees);
		}
		else{
			if(Math.signum(déplacementCharacter.x ) == 0)
				sens = 1;
			else
				sens = Math.signum(déplacementCharacter.x );
			
			if(body.getLinearVelocity().y > 0)
				batch.draw(	animationSautMonte.getKeyFrame(animTime), 
							(body.getPosition().x - width + (0.25f - sens * 0.5f)*spriteWidth)/* * GameConstants.PPM*/, 
							(body.getPosition().y - height - width/2)/* * GameConstants.PPM*/, 
							width,
							spriteHeight - height/2,
							sens * spriteWidth/* * GameConstants.PPM*/, 
							spriteHeight/* * GameConstants.PPM*/,
							1,
							1,
							body.getAngle()*MathUtils.radiansToDegrees);
			else /*if(body.getLinearVelocity().y < 0)*/
				batch.draw(	animationSautDescent.getKeyFrame(animTime), 
							(body.getPosition().x - width + (0.25f - sens * 0.5f)*spriteWidth)/* * GameConstants.PPM*/, 
							(body.getPosition().y - height - width/2)/* * GameConstants.PPM*/, 
							width,
							spriteHeight - height/2,
							sens * spriteWidth/* * GameConstants.PPM*/, 
							spriteHeight/* * GameConstants.PPM*/,
							1,
							1,
							body.getAngle()*MathUtils.radiansToDegrees);
		}
		
		/*
		 * Bouclier
		 */
		if(GameConstants.bouclierOn){
			batch.setColor(1, 1, 1, 0.8f);
			batch.draw(	animationBouclier.getKeyFrame(GameConstants.ANIM_TIME), 
						(body.getPosition().x - 1.3f*height)/*  * GameConstants.PPM*/, 
						(body.getPosition().y - 1.3f*height -width/2)/* * GameConstants.PPM*/,
						2.6f*height/* * GameConstants.PPM*/,
						2.6f*height/* * GameConstants.PPM*/);
			batch.setColor(1, 1, 1, 1);
		}
		else if(explosionTime < animationBouclierExplosion.getAnimationDuration()){
			batch.setColor(1, 1, 1, 0.8f);
			batch.draw(	animationBouclierExplosion.getKeyFrame(explosionTime), 
						(body.getPosition().x - 1.3f*height)/*  * GameConstants.PPM*/, 
						(body.getPosition().y - 1.3f*height)/* * GameConstants.PPM*/,
						2.6f*height/* * GameConstants.PPM*/,
						2.6f*height/* * GameConstants.PPM*/);
			batch.setColor(1, 1, 1, 1);
			explosionTime += Gdx.graphics.getDeltaTime();
		}

	}
	
	public void dispose(){
		Pools.free(déplacementCharacter);
		game.world.destroyBody(body);
	}
}
