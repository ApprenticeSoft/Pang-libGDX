package com.pang.bodies;

import com.badlogic.gdx.physics.box2d.World;
import com.pang.MyGdxGame;
import com.pang.utils.GameConstants;

public class BalleGrande extends Balle{
	

	public BalleGrande(final MyGdxGame game){
		super(game);
		
		textureRegionString = "BalleGrande";
		rayon = GameConstants.BALL_RADIUS;
		rebond = GameConstants.REBOND_BALLE_GRANDE;
	}

	public BalleGrande(MyGdxGame game, World world, float posX, float posY,	boolean left) {
		super(game, posX, posY, left);

		textureRegionString = "BalleGrande";
		rayon = GameConstants.BALL_RADIUS;	
		createBall(/*posX, posY*/);
		//initialImpulse();
	}
	
	@Override
	public void contactRope(){
		BalleMoyenne balle1 = (BalleMoyenne) game.pools.obtain(BalleMoyenne.class); 
		balle1.init(body.getPosition().x - rayon/2, body.getPosition().y, true);
		game.balles.add(balle1);
		
		BalleMoyenne balle2 = (BalleMoyenne) game.pools.obtain(BalleMoyenne.class);
		balle2.init(body.getPosition().x + rayon/2, body.getPosition().y, false);
		game.balles.add(balle2);
		destroy();
		
		System.out.println("Contact balle grande");
	}
}
