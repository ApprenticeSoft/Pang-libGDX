package com.pang;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.pang.armes.Arme;
import com.pang.bodies.Balle;
import com.pang.bodies.Debris;
import com.pang.bodies.Obstacle;
import com.pang.screens.LoadingScreen;
import com.pang.upgrades.Upgrade;
import com.pang.utils.GameConstants;
import com.pang.utils.ObjectPools;

public class MyGdxGame extends Game {
	
	public SpriteBatch batch;
	public World world;
	public AssetManager assets;
	public Skin skin;
	public TextureAtlas textureAtlas;
	
	public Array<Arme> armes;
	public Array<Balle> balles;
	public Array<Upgrade> upgrades;
	public Array<Obstacle> obstacles;
	public Array<Debris> debris;
	
	public ObjectPools pools;
	private boolean loadingFinished = false;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
        world = new World(new Vector2(0, GameConstants.GRAVITY), true);
        
        assets = new AssetManager();

        armes = new Array<Arme>();
        balles = new Array<Balle>();
        upgrades = new Array<Upgrade>();
        obstacles = new Array<Obstacle>();
        debris = new Array<Debris>();
        
        pools = new ObjectPools(this);
        
        GameConstants.NB_VIE = 3;
        
		this.setScreen(new LoadingScreen(this));
	}

	@Override
	public void render () {
		super.render();

		if(!loadingFinished)
			if(assets.update()){
	    		loadingFinished = true;

	    		/*
	    		 * Fenêtre achat version complète
	    		 */
	    		skin = new Skin();
	    		
	    		textureAtlas = assets.get("Images/Images.pack", TextureAtlas.class);
	    		skin.addRegions(textureAtlas);
			}
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
