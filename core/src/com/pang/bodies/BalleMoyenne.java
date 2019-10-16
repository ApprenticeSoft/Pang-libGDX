package com.pang.bodies;

import com.badlogic.gdx.physics.box2d.World;
import com.pang.MyGdxGame;
import com.pang.utils.GameConstants;

public class BalleMoyenne extends Balle{

	public BalleMoyenne(final MyGdxGame game){
		super(game);
		
		textureRegionString = "BalleMoyenne";
		rayon = GameConstants.BALL_RADIUS/2;
		rebond = GameConstants.REBOND_BALLE_MOYENNE;
	}

	public BalleMoyenne(MyGdxGame game, World world, float posX, float posY, boolean left) {
		super(game, posX, posY, left);

		textureRegionString = "BalleMoyenne";
		rayon = GameConstants.BALL_RADIUS/2;	
		createBall(/*posX, posY*/);
		//initialImpulse();
	}
	
	@Override
	public void contactRope(){
		BallePetite balle1 = (BallePetite) game.pools.obtain(BallePetite.class);
		balle1.init(body.getPosition().x - rayon/2, body.getPosition().y, true);
		game.balles.add(balle1);
		
		BallePetite balle2 = (BallePetite) game.pools.obtain(BallePetite.class);
		balle2.init(body.getPosition().x + rayon/2, body.getPosition().y, false);
		game.balles.add(balle2);
		destroy();
		
		System.out.println("Contact balle moyenne");
	}
}
