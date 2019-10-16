package com.pang.screens;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.Array;
import com.pang.MyGdxGame;
import com.pang.armes.Arme;
import com.pang.bodies.Balle;
import com.pang.bodies.Debris;
import com.pang.bodies.Hero;
import com.pang.bodies.Obstacle;
import com.pang.enums.ArmeActive;
import com.pang.upgrades.Upgrade;
import com.pang.upgrades.UpgradeArme;
import com.pang.utils.GameConstants;
import com.pang.utils.HUD;
import com.pang.utils.LecteurCarte;
import com.pang.utils.OrthogonalTiledMapRendererWithSprites;

public class GameScreen implements Screen{

	final MyGdxGame game;
	protected OrthographicCamera camera;
	private Box2DDebugRenderer debugRenderer;
	private Hero hero;
	private Balle balleTest;

	private Texture textureFond, textureBrouillard;
	private Image imageFond;
	
	private Stage stage;
	//private LabelStyle timeLabelStyle;
	//private Label timeLabel;
	private HUD hud;
	
	TiledMap tiledMap;
	TiledMapRenderer tiledMapRenderer;
	protected LecteurCarte lecteurCarte;
	
	/*
	 * Gestion des upgrades
	 */
	private float pauseBallesCooldown = 4;
	private Array<Balle> ballesDynamite;
	//private float dynamiteCooldown = 0.1f;
	private int dynamiteConteur = 0;
	
	/*
	 * Animations
	 */
	private float animTime = 0;
	
	PolygonSpriteBatch polyBatch;
	
	/*
	 * Test graphismes
	 */
	private float brouillard = 0;
	
	public GameScreen(final MyGdxGame game){
		this.game = game;

		GameConstants.HERO_TOUCHÉ = false;
		GameConstants.pauseBalles = false;
		GameConstants.dynamite = false;
		GameConstants.activationBouclier = false;
		GameConstants.bouclierOn = false;
		GameConstants.bombeOn = false;
		GameConstants.NB_TIR = 0;
		GameConstants.ANIM_TIME = 0;
		GameConstants.GAME_TIME = 90;
		
        tiledMap = new TmxMapLoader().load("Niveaux/Niveau 1.tmx");	
        initiate();  
		
		camera = new OrthographicCamera();
		if(GameConstants.SCREEN_RATIO <= GameConstants.LEVEL_RATIO)
			camera.setToOrtho(false, GameConstants.LEVEL_WIDTH, GameConstants.LEVEL_WIDTH / GameConstants.SCREEN_RATIO);
		else
			camera.setToOrtho(false, GameConstants.LEVEL_HEIGHT * GameConstants.SCREEN_RATIO, GameConstants.LEVEL_HEIGHT - GameConstants.PPT * GameConstants.MPP/2);

		camera.position.x = GameConstants.LEVEL_WIDTH/2;
        camera.position.y =  GameConstants.LEVEL_HEIGHT-camera.viewportHeight/2;
		camera.update();
        
        debugRenderer = new Box2DDebugRenderer();

        //Tiles Map
        //TmxMapLoader.Parameters params = new TmxMapLoader.Parameters();
        //params.generateMipMaps = true;
        //params.textureMagFilter = Texture.TextureFilter.MipMapLinearNearest;
        //params.textureMinFilter = Texture.TextureFilter.MipMapLinearNearest; 

        tiledMapRenderer = new OrthogonalTiledMapRendererWithSprites(tiledMap, GameConstants.MPP, game.batch);
        
		
        
        createBorders();
        lecteurCarte = new LecteurCarte(game, tiledMap, game.world, camera);
        
        hero = new Hero(game, GameConstants.LEVEL_WIDTH/2, 1.5f*GameConstants.PPT * GameConstants.MPP);  
		
		Iterator iterator = tiledMap.getProperties().getKeys();
		while(iterator.hasNext()){
			System.out.println("Iterator, property : " + iterator.next());
		}

		ballesDynamite = new Array<Balle>();
		
		stage = new Stage();
		/*
		timeLabelStyle = new LabelStyle(game.assets.get("fontMenu.ttf", BitmapFont.class), Color.WHITE);
		timeLabel = new Label("" + GameConstants.GAME_TIME, timeLabelStyle);
		timeLabel.setX(Gdx.graphics.getWidth()/2 - timeLabel.getPrefWidth()/2);
		timeLabel.setY(Gdx.graphics.getHeight() - timeLabel.getPrefHeight());
		
		stage.addActor(timeLabel);
		*/
		
		hud = new HUD(game);
		hud.addToStage(stage);
		
		textureFond = new Texture(Gdx.files.internal("Images/Fonds/Fond_Test_5.png"), true);
		textureFond.setFilter(TextureFilter.MipMapNearestNearest, TextureFilter.MipMapNearestNearest);
		brouillard = 0f;

		
		System.out.println("tilewidth : " + tiledMap.getProperties().get("tilewidth"));
		System.out.println("tileheight : " + tiledMap.getProperties().get("tileheight"));
		System.out.println("width : " + tiledMap.getProperties().get("width"));
		System.out.println("height : " + tiledMap.getProperties().get("height"));
		
		polyBatch = new PolygonSpriteBatch();
		polyBatch.setProjectionMatrix(camera.combined);
	}

	@Override
	public void render(float delta) {
		//Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClearColor(128/256f, 128/256f, 128/256f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glEnable(GL20.GL_BLEND); 
		
		for(Obstacle obstacle : game.obstacles)
			obstacle.activity();
		
		camera.update();
		if(Gdx.input.isKeyPressed(Keys.E)){
			camera.viewportWidth *= 1.01f;
	        camera.viewportHeight *= 1.01f;
		}
		
		GameConstants.GAME_TIME -= Gdx.graphics.getDeltaTime();
		game.world.step(GameConstants.BOX_STEP, GameConstants.BOX_VELOCITY_ITERATIONS, GameConstants.BOX_POSITION_ITERATIONS);
		/*
		timeLabel.setText("" + (int)(GameConstants.GAME_TIME+1));
		timeLabel.setX(Gdx.graphics.getWidth()/2 - timeLabel.getPrefWidth()/2);
		*/
		hud.act();
		stage.act();

		if(GameConstants.NB_TIR < 0)
			GameConstants.NB_TIR = 0;
		
		hero.control();
		GameConstants.ANIM_TIME += Gdx.graphics.getDeltaTime();
		renderScene();
		
		/*
		 * Upgrade pause balle
		 */
		if(GameConstants.pauseBalles){
			for(Balle balle : game.balles)
				balle.body.setType(BodyType.StaticBody);	
			
			pauseBallesCooldown -= Gdx.graphics.getDeltaTime();
			
			if(pauseBallesCooldown < 0){
				for(Balle balle : game.balles){
					balle.body.setType(BodyType.DynamicBody);
					balle.body.setLinearVelocity(balle.vitesseVecteur);		
				}
				pauseBallesCooldown = 4;
				GameConstants.pauseBalles = false;
			}
		}
		/*
		 * Dynamite
		 */
		if(GameConstants.dynamite){
			ballesDynamite.addAll(game.balles);

			for(int i = 0; i < ballesDynamite.size; i++){
				
				if(!ballesDynamite.get(i).getClass().toString().equals("class com.pang.bodies.BalleMini"))
					ballesDynamite.get(i).contactRope();	
			}
			ballesDynamite.clear();
			ballesDynamite.shrink();
			
			if(dynamiteConteur > 2){
				GameConstants.dynamite = false;
				dynamiteConteur = 0;
			}
			dynamiteConteur++;
		}
		
		for(int i = game.armes.size - 1; i > -1; i--)
    		game.armes.get(i).activity();
		for(int i = game.balles.size - 1; i > -1; i--){
			if(i < game.balles.size)
				game.balles.get(i).activity();
		}
		for(int i = game.upgrades.size - 1; i > -1; i--)
    		game.upgrades.get(i).activity();
		for(int i = game.debris.size - 1; i > -1; i--)
    		game.debris.get(i).activity();
				
		//System.out.println("world.getBodyCount() = " + game.world.getBodyCount());
		//System.out.println("game.balles.size = " + game.balles.size);
		
		//Jeu perdu
		if(GameConstants.HERO_TOUCHÉ){
			if(GameConstants.NB_VIE > 0){
				System.out.println("Mort");
				
				dispose();
				game.setScreen(new GameScreen(game));		
			}
			else{
				System.out.println("jeu Perdu!");
			}
		}
		
		if(Gdx.input.isKeyPressed(Keys.Q)){
			dispose();
			game.setScreen(new GameScreen(game));
		}
		
		//System.out.println("game.armes.size = " + game.armes.size);
	}
	
	@Override
	public void show() {

		game.world.setContactListener(new ContactListener(){
			@Override
			public void beginContact(Contact contact) {
				/*Body*/Fixture a = contact.getFixtureA()/*.getBody()*/;
				/*Body*/Fixture b = contact.getFixtureB()/*.getBody()*/;
			    if(a.getUserData() != null && b.getUserData() != null) {
			    	//Contact avec les parois
			    	if(a.getUserData() == "Obstacle"){
			    		if(b.getUserData() == "Arme"){
			    			for(int i = 0; i < game.armes.size; i++){
					    		if(game.armes.get(i).body == b.getBody())
					    			game.armes.get(i).contactWall(/*i*/);
					    	}
					    	for(int i = 0; i < game.obstacles.size; i++){
					    		if(game.obstacles.get(i).body == a.getBody())
					    			if(game.obstacles.get(i).destructible)
					    				game.obstacles.get(i).impact();
					    	}
			    		}
			    		else if(b.getUserData() == "PiedDetecteur"){
						   hero.contactSol = true;
						   System.out.println("Contact !!");
					   }
				    }
			    	else if(b.getUserData() == "Obstacle"){
			    		if(a.getUserData() == "Arme"){
			    			for(int i = 0; i < game.armes.size; i++){
					    		if(game.armes.get(i).body == a.getBody())
					    			game.armes.get(i).contactWall(/*i*/);
					    	}
					    	for(int i = 0; i < game.obstacles.size; i++){
					    		if(game.obstacles.get(i).body == b.getBody())
					    			if(game.obstacles.get(i).destructible)
					    				game.obstacles.get(i).impact();
					    	}
			    		}
			    		else if(a.getUserData() == "PiedDetecteur"){
						   hero.contactSol = true;
						   System.out.println("Contact !!");
					   }
				    }
				   //Contact avec le sol
				   if(a.getUserData() == "Bas"){
					   if(b.getUserData() == "Balle"){
					    	for(int i = 0; i < game.balles.size; i++){
					    		if(game.balles.get(i).body == b.getBody())
					    			game.balles.get(i).rebond();
					    	}
					   }
					   else if(b.getUserData() == "PiedDetecteur"){
						   hero.contactSol = true;
						   System.out.println("Contact !!");
					   }
				    }
				   else if(b.getUserData() == "Bas"){
					   	if(a.getUserData() == "Balle"){
						   for(int i = 0; i < game.balles.size; i++){
							   if(game.balles.get(i).body == a.getBody())
								   game.balles.get(i).rebond();
						 }
					   }
					   	else if(a.getUserData() == "PiedDetecteur"){
					   		hero.contactSol = true;
					   		System.out.println("Contact !!");
					  	}
				   	} 
				    //Contact avec les balles
				    if(a.getUserData() == "Balle"){
				    	if(b.getUserData() == "Arme"){
					    	for(int i = 0; i < game.armes.size; i++){
					    		if(game.armes.get(i).body == b.getBody()){
					    			for(int j = 0; j < game.balles.size; j++){
					    				if(game.balles.get(j).body == a.getBody())
							    			game.armes.get(i).contactBall(i, j);
					    			}
					    		}
					    	}
				    	}
				    	else if(b.getUserData() == "Bouclier"){
				    		hero.bouclierOff();
				    		/*
				    		for(int i = 0; i < game.balles.size; i++){
			    				if(game.balles.get(i).body == a.getBody())
			    					game.balles.get(i).setContactRope(true);
			    			}
			    			*/
				    	}
				    	else if(b.getUserData() == "Hero"){
				    		perd();
				    	}
				    }
				    else if(b.getUserData() == "Balle"){
				    	if(a.getUserData() == "Arme"){
					    	for(int i = 0; i < game.armes.size; i++){
					    		if(game.armes.get(i).body == a.getBody()){
					    			for(int j = 0; j < game.balles.size; j++){
					    				if(game.balles.get(j).body == b.getBody() && !game.balles.get(j).getContactArme())
							    			game.armes.get(i).contactBall(i, j);
					    			}
					    		}
					    	}
				    	}
				    	else if(a.getUserData() == "Bouclier"){
				    		hero.bouclierOff();
				    		/*
				    		for(int i = 0; i < game.balles.size; i++){
			    				if(game.balles.get(i).body == b.getBody())
			    					game.balles.get(i).setContactRope(true);
			    			}
			    			*/
				    	}
				    	else if(a.getUserData() == "Hero"){
				    		perd();
				    	}
				    } 
				    //Upgrade
				    if(a.getUserData() == "Upgrade" && b.getUserData() == "Hero"){
				    	for(int i = 0; i < game.upgrades.size; i++){
				    		if(game.upgrades.get(i).body == a.getBody())
				    			game.upgrades.get(i).contactHero(i);
				    	}
				    } 
				    else if(b.getUserData() == "Upgrade" && a.getUserData() == "Hero"){
				    	for(int i = 0; i < game.upgrades.size; i++){
				    		if(game.upgrades.get(i).body == b.getBody())
				    			game.upgrades.get(i).contactHero(i);
				    	}
				    }
				    
				    //Débris
				    if(a.getUserData() == "Debris"){
				    	for(int i = 0; i < game.debris.size; i++){
				    		if(game.debris.get(i).body == a.getBody())
				    			game.debris.get(i).contact();
				    	}
				    } 
				    else if(b.getUserData() == "Debris"){
				    	for(int i = 0; i < game.debris.size; i++){
				    		if(game.debris.get(i).body == b.getBody())
				    			game.debris.get(i).contact();
				    	}
				    } 
				    
			    }
			}

			@Override
			public void endContact(Contact contact) {
				/*Body*/Fixture a = contact.getFixtureA()/*.getBody()*/;
				/*Body*/Fixture b = contact.getFixtureB()/*.getBody()*/;
				//Rebond des balles
			   if(a.getUserData() == "Obstacle"){
				   if(b.getUserData() == "PiedDetecteur"){
					   hero.contactSol = false;
					   System.out.println("Fin du Contact !!");
				   }
			    }
			   else if(b.getUserData() == "Obstacle"){
				   if(a.getUserData() == "PiedDetecteur"){
					   hero.contactSol = false;
					   System.out.println("Fin du Contact !!");
				   }
			    } 		
			}

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
				Fixture a = contact.getFixtureA();
				Fixture b = contact.getFixtureB();
				if(a.getUserData() == "Bouclier"){
					   if(b.getUserData() != "Balle"){
						   contact.setEnabled(false);
					   }
				    }
				   else if(b.getUserData() == "Bouclier"){
					   if(a.getUserData() != "Balle"){
						   contact.setEnabled(false);
					   }
				   } 
			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
				
			}
			
		});
		
	}

	@Override
	public void resize(int width, int height) {
		initiate();  
		
		if(GameConstants.SCREEN_RATIO <= GameConstants.LEVEL_RATIO)
			camera.setToOrtho(false, GameConstants.LEVEL_WIDTH, GameConstants.LEVEL_WIDTH / GameConstants.SCREEN_RATIO);
		else
			camera.setToOrtho(false, GameConstants.LEVEL_HEIGHT * GameConstants.SCREEN_RATIO, GameConstants.LEVEL_HEIGHT - GameConstants.PPT * GameConstants.MPP/2);

		camera.position.x = GameConstants.LEVEL_WIDTH/2;
        camera.position.y =  GameConstants.LEVEL_HEIGHT-camera.viewportHeight/2;
		camera.update();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		//game.world.dispose();
		
		for(Balle balle : game.balles)
			balle.dispose();	
		for(Arme arme : game.armes)
			arme.dispose();
		for(Upgrade upgrade : game.upgrades)
			upgrade.dispose();
		for(Debris debris : game.debris)
			debris.dispose();
		//for(Obstacle obstacle : game.obstacles)
		//	obstacle.dispose();
		
		hero.dispose();
		
		if(game.balles.size > 0)
			game.balles.removeRange(0, game.balles.size-1);
		if(game.armes.size > 0)
			game.armes.removeRange(0, game.armes.size-1);
		if(game.upgrades.size > 0)
			game.upgrades.removeRange(0, game.upgrades.size-1);
		if(game.obstacles.size > 0)
			game.obstacles.removeRange(0, game.obstacles.size-1);
		if(game.debris.size > 0)
			game.debris.removeRange(0, game.debris.size-1);
		
		Array<Body> bodies = new Array<Body>();
		game.world.getBodies(bodies);
		System.out.println("bodies.size = " + bodies.size);
		for(Body body : bodies)
			game.world.destroyBody(body);
	}

	public void createBorders(){
		PolygonShape polygonShape = new PolygonShape();
		BodyDef bodyDef = new BodyDef();
		FixtureDef fixtureDef = new FixtureDef();
		
        fixtureDef.density = .1f;  
        fixtureDef.friction = 0.0f;  
        fixtureDef.restitution = .1f;     
		bodyDef.type = BodyType.StaticBody; 
		
        //Bordure bas
		polygonShape.setAsBox(GameConstants.LEVEL_WIDTH/2, GameConstants.PPT*GameConstants.MPP/2);
		fixtureDef.shape = polygonShape;
		bodyDef.position.set(GameConstants.LEVEL_WIDTH/2, GameConstants.PPT*GameConstants.MPP/2);
		Body bodyBas = game.world.createBody(bodyDef);
        bodyBas.createFixture(fixtureDef).setUserData("Bas");
        bodyBas.setUserData("Bas");

        //Sous sol
		polygonShape.setAsBox(GameConstants.LEVEL_WIDTH/2, 0.01f*GameConstants.LEVEL_HEIGHT);
		bodyDef.position.set(GameConstants.LEVEL_WIDTH/2, -0.01f*GameConstants.LEVEL_HEIGHT);
		Body bodySousSol = game.world.createBody(bodyDef);
		bodySousSol.createFixture(fixtureDef).setUserData("Obstacle");
		bodySousSol.setUserData("Obstacle");
     
		//Bordure haut
		bodyDef.position.set(GameConstants.LEVEL_WIDTH/2, GameConstants.LEVEL_HEIGHT);
		Body bodyHaut = game.world.createBody(bodyDef);
        bodyHaut.createFixture(fixtureDef).setUserData("Obstacle");
        bodyHaut.setUserData("Obstacle");
        System.out.println(bodyHaut.getUserData());
           
        //Bordure gauche
		polygonShape.setAsBox(0.01f*GameConstants.LEVEL_HEIGHT, GameConstants.LEVEL_HEIGHT/2);
		fixtureDef.shape = polygonShape;
		bodyDef.position.set(0, GameConstants.LEVEL_HEIGHT/2);
		Body bodyGauche = game.world.createBody(bodyDef);
        bodyGauche.createFixture(fixtureDef).setUserData("Obstacle");
        bodyGauche.setUserData("Obstacle");
        
        //Bordure droite
		bodyDef.position.set(GameConstants.LEVEL_WIDTH, GameConstants.LEVEL_HEIGHT/2);
		Body bodyDroite = game.world.createBody(bodyDef);
        bodyDroite.createFixture(fixtureDef).setUserData("Obstacle");
        bodyDroite.setUserData("Obstacle");
        
        polygonShape.dispose();
	}
	
	public void initiate(){
		GameConstants.PPT = Integer.parseInt(tiledMap.getProperties().get("tileheight").toString());
        GameConstants.LEVEL_PIXEL_HEIGHT = GameConstants.PPT * Float.parseFloat(tiledMap.getProperties().get("height").toString());
        GameConstants.LEVEL_PIXEL_WIDTH = GameConstants.PPT * Float.parseFloat(tiledMap.getProperties().get("width").toString());
        
        GameConstants.LEVEL_HEIGHT = GameConstants.LEVEL_PIXEL_HEIGHT * GameConstants.MPP;
        GameConstants.LEVEL_WIDTH = GameConstants.LEVEL_PIXEL_WIDTH * GameConstants.MPP;
        
		GameConstants.NB_HORIZONTAL_TILE = (Integer) tiledMap.getProperties().get("width");
		GameConstants.NB_VERTICAL_TILE = (Integer) tiledMap.getProperties().get("height");
		GameConstants.SCREEN_RATIO = (float)Gdx.graphics.getWidth()/(float)Gdx.graphics.getHeight();
		GameConstants.LEVEL_RATIO = GameConstants.LEVEL_WIDTH/GameConstants.LEVEL_HEIGHT;
		GameConstants.LEVEL_RATIO = GameConstants.LEVEL_WIDTH/(GameConstants.LEVEL_HEIGHT - GameConstants.PPT * GameConstants.MPP/2);
		
		GameConstants.HERO_HEIGHT = 0.06f * GameConstants.LEVEL_HEIGHT;	
		GameConstants.HERO_WIDTH = GameConstants.HERO_HEIGHT/2;	
		GameConstants.BALL_RADIUS = 2f * GameConstants.HERO_HEIGHT;
		GameConstants.REBOND_BALLE_GRANDE = 28;
		GameConstants.REBOND_BALLE_MOYENNE = 27; 
		GameConstants.REBOND_BALLE_PETITE = 24;
		GameConstants.REBOND_BALLE_MINI = 19;
		GameConstants.VITESSE_CORDE = 0.15f * GameConstants.LEVEL_HEIGHT;
	}
	
	public void perd(){
		GameConstants.HERO_TOUCHÉ = true;
		GameConstants.NB_VIE--;
	}
	
	public void renderScene(){		
		game.batch.begin();
		game.batch.draw(textureFond, 
						0,
						0,
						GameConstants.LEVEL_WIDTH, 
						GameConstants.LEVEL_HEIGHT);
		game.batch.setColor(1f, 1f, 1f, brouillard);
		game.batch.draw(game.textureAtlas.findRegion("WhiteSquare"), 
						0,
						0,
						GameConstants.LEVEL_WIDTH, 
						GameConstants.LEVEL_HEIGHT);
		game.batch.setColor(1, 1, 1, 1);
		game.batch.end();
		
		tiledMapRenderer.setView(camera); 
		tiledMapRenderer.render();
		game.batch.begin();
		for(int i = 0; i < game.armes.size; i++)
			game.armes.get(i).draw(game.batch);
		for(int i = 0; i < game.upgrades.size; i++)
			game.upgrades.get(i).draw(game.batch);
		hero.draw(game.batch, GameConstants.HERO_ANIM_TIME);
		for(int i = 0; i < game.balles.size; i++)
			game.balles.get(i).draw(game.batch);
		for(int i = 0; i < game.obstacles.size; i++)
				game.obstacles.get(i).draw(game.batch);
		hud.draw(game.batch);
		game.batch.end();
		
		polyBatch.begin();
		for(int i = 0; i < game.debris.size; i++)
			game.debris.get(i).draw(polyBatch);
		polyBatch.end();

		stage.draw();
		//debugRenderer.render(game.world, camera.combined);
	}
	
}
