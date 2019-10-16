package com.pang.bodies;

import com.badlogic.gdx.physics.box2d.World;
import com.pang.MyGdxGame;
import com.pang.utils.GameConstants;

public class BalleMini extends Balle{

	public BalleMini(MyGdxGame game) {
		super(game);
		
		textureRegionString = "BalleMini";
		rayon = GameConstants.BALL_RADIUS/8;
		rebond = GameConstants.REBOND_BALLE_MINI;
	}

	public BalleMini(MyGdxGame game, World world, float posX, float posY,	boolean left) {
		super(game, posX, posY, left);

		textureRegionString = "BalleMini";
		rayon = GameConstants.BALL_RADIUS/8;	
		createBall(/*posX, posY*/);
		//initialImpulse();
	}
	
	@Override
	public void contactRope(){
		destroy();
		System.out.println("Contact balle mini");
	}

}
