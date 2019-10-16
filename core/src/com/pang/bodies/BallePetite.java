package com.pang.bodies;

import com.badlogic.gdx.physics.box2d.World;
import com.pang.MyGdxGame;
import com.pang.utils.GameConstants;

public class BallePetite extends Balle{

	public BallePetite(final MyGdxGame game){
		super(game);
		
		textureRegionString = "BallePetite";
		rayon = GameConstants.BALL_RADIUS/4;
		rebond = GameConstants.REBOND_BALLE_PETITE;
	}

	public BallePetite(MyGdxGame game, World world, float posX, float posY,	boolean left) {
		super(game, posX, posY, left);
		
		textureRegionString = "BallePetite";
		rayon = GameConstants.BALL_RADIUS/4;	
		createBall(/*posX, posY*/);
		//initialImpulse();
	}
	

	
	@Override
	public void contactRope(){
		BalleMini balle1 = (BalleMini) game.pools.obtain(BalleMini.class);
		balle1.init(body.getPosition().x - rayon/2, body.getPosition().y, true);
		game.balles.add(balle1);
		
		BalleMini balle2 = (BalleMini) game.pools.obtain(BalleMini.class);
		balle2.init(body.getPosition().x + rayon/2, body.getPosition().y, false);
		game.balles.add(balle2);
		destroy();
		
		System.out.println("Contact balle petite");
	}
}
