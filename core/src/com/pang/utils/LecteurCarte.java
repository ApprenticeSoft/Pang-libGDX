package com.pang.utils;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.pang.MyGdxGame;
import com.pang.bodies.BalleGrande;
import com.pang.bodies.BalleMini;
import com.pang.bodies.BalleMoyenne;
import com.pang.bodies.BallePetite;
import com.pang.bodies.Obstacle;
import com.pang.upgrades.PowerUp;
import com.pang.upgrades.Upgrade;
import com.pang.upgrades.UpgradeArme;

public class LecteurCarte {

    MapObjects objects;
	//public Array<Obstacle> obstacles;
	OrthographicCamera camera;
	World world;
	public float eauPosX, eauPosY, cameraOrigineX, cameraOrigineY;
    
	public LecteurCarte(final MyGdxGame game, TiledMap tiledMap, World world, OrthographicCamera camera2){
		this.camera = camera2;
		this.world = world;

		cameraOrigineX = camera2.position.x;
		cameraOrigineY = camera2.position.y;
		
		System.out.println("Position camera : " + cameraOrigineX + ", " + cameraOrigineY);
		GameConstants.PPT =  Integer.parseInt(tiledMap.getProperties().get("tileheight").toString());
		System.out.println("Variables.PPT = " + GameConstants.PPT);
		
		objects = tiledMap.getLayers().get("Objects").getObjects();
		

        //obstacles = new Array<Obstacle>();
        //balles = new Array<Balle>();    
        
        for (MapObject mapObject : objects.getByType(RectangleMapObject.class)) {
            if(mapObject.getProperties().get("type") != null){
            	//Création de balles
            	if(mapObject.getProperties().get("type").equals("BalleGrande")){
            		BalleGrande balle = (BalleGrande) game.pools.obtain(BalleGrande.class);
            		balle.init(mapObject);
            		game.balles.add(balle);
            	}
            	else if(mapObject.getProperties().get("type").equals("BalleMoyenne")){
            		BalleMoyenne balle = (BalleMoyenne) game.pools.obtain(BalleMoyenne.class);
            		balle.init(mapObject);
            		game.balles.add(balle);
            	}
            	else if(mapObject.getProperties().get("type").equals("BallePetite")){
            		BallePetite balle = (BallePetite) game.pools.obtain(BallePetite.class);
            		balle.init(mapObject);
            		game.balles.add(balle);
            	}
            	else if(mapObject.getProperties().get("type").equals("BalleMini")){
            		BalleMini balle = (BalleMini) game.pools.obtain(BalleMini.class);
            		balle.init(mapObject);
            		game.balles.add(balle);
            	}
            	//Création des upgrades
            	else if(mapObject.getProperties().get("type").equals("Upgrade")){
            		Upgrade upgrade = (Upgrade) game.pools.obtain(Upgrade.class);
            		upgrade.init(mapObject);
            		game.upgrades.add(upgrade);
            	}
            	else if(mapObject.getProperties().get("type").equals("UpgradeArme")){
            		UpgradeArme upgradeArme = (UpgradeArme) game.pools.obtain(UpgradeArme.class);
            		upgradeArme.init(mapObject);
            		game.upgrades.add(upgradeArme);
            	}
            	else if(mapObject.getProperties().get("type").equals("PowerUp")){
            		PowerUp powerUp = (PowerUp) game.pools.obtain(PowerUp.class);
            		powerUp.init(mapObject);
            		game.upgrades.add(powerUp);
            	}
            	else if(mapObject.getProperties().get("type").equals("Obstacle")){
            		Obstacle obstacle = new Obstacle(/*world,*/game, camera2, mapObject);
                    game.obstacles.add(obstacle);
            	}
            }
            else{
            	Obstacle obstacle = new Obstacle(game, camera2, mapObject);
                game.obstacles.add(obstacle);
            }
        }
	}
	/*
	public Array<Obstacle> getObstacles(){
		return obstacles;
	}
	*/
	public void draw(SpriteBatch batch, TextureAtlas textureAtlas){

	}
	
	public void drawObstacle(SpriteBatch batch, TextureAtlas textureAtlas){
 
	}
	
	public void activity(){

	}
	
	public void restart(){

	}
	
	public void dispose(){
		
	}
}
