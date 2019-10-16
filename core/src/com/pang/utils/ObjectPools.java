package com.pang.utils;

import com.badlogic.gdx.utils.Pool;
import com.pang.MyGdxGame;
import com.pang.armes.Projectile;
import com.pang.armes.Rope;
import com.pang.armes.Grappin;
import com.pang.bodies.BalleGrande;
import com.pang.bodies.BalleMini;
import com.pang.bodies.BalleMoyenne;
import com.pang.bodies.BallePetite;
import com.pang.bodies.Debris;
import com.pang.upgrades.PowerUp;
import com.pang.upgrades.Upgrade;
import com.pang.upgrades.UpgradeArme;

public class ObjectPools {
		
	final MyGdxGame game;

	public Pool<Rope> ropePool = new Pool<Rope>(){
		@Override
		protected Rope newObject() {
			return new Rope(game);
		}	
	};

	public Pool<Grappin> grappinPool = new Pool<Grappin>(){
		@Override
		protected Grappin newObject() {
			return new Grappin(game);
		}	
	};

	public Pool<Projectile> projectilePool = new Pool<Projectile>(){
		@Override
		protected Projectile newObject() {
			return new Projectile(game);
		}	
	};

	public Pool<BalleMini> balleMiniPool = new Pool<BalleMini>(){
		@Override
		protected BalleMini newObject() {
			return new BalleMini(game);
		}	
	};

	public Pool<BallePetite> ballePetitePool = new Pool<BallePetite>(){
		@Override
		protected BallePetite newObject() {
			return new BallePetite(game);
		}	
	};

	public Pool<BalleMoyenne> balleMoyennePool = new Pool<BalleMoyenne>(){
		@Override
		protected BalleMoyenne newObject() {
			return new BalleMoyenne(game);
		}	
	};

	public Pool<BalleGrande> balleGrandePool = new Pool<BalleGrande>(){
		@Override
		protected BalleGrande newObject() {
			return new BalleGrande(game);
		}	
	};

	public Pool<Upgrade> upgradePool = new Pool<Upgrade>(){
		@Override
		protected Upgrade newObject() {
			return new Upgrade(game);
		}	
	};

	public Pool<UpgradeArme> upgradeArmePool = new Pool<UpgradeArme>(){
		@Override
		protected UpgradeArme newObject() {
			return new UpgradeArme(game);
		}	
	};

	public Pool<PowerUp> powerUpPool = new Pool<PowerUp>(){
		@Override
		protected PowerUp newObject() {
			return new PowerUp(game);
		}	
	};

	public Pool<Debris> debrisPool = new Pool<Debris>(){
		@Override
		protected Debris newObject() {
			return new Debris(game);
		}	
	};
	
	public ObjectPools(final MyGdxGame game){
		this.game = game;
	}
	
	public Object obtain(Class object){
		if(object.toString().equals("class com.pang.armes.Rope")){
			return this.ropePool.obtain();
		}
		else if(object.toString().equals("class com.pang.armes.Grappin")){
			return this.grappinPool.obtain();
		}
		else if(object.toString().equals("class com.pang.armes.Projectile")){
			return this.projectilePool.obtain();
		}
		else if(object.toString().equals("class com.pang.bodies.BalleGrande")){
			return this.balleGrandePool.obtain();
		}
		else if(object.toString().equals("class com.pang.bodies.BalleMoyenne")){
			return this.balleMoyennePool.obtain();
		}
		else if(object.toString().equals("class com.pang.bodies.BallePetite")){
			return this.ballePetitePool.obtain();
		}
		else if(object.toString().equals("class com.pang.bodies.BalleMini")){
			return this.balleMiniPool.obtain();
		}
		else if(object.toString().equals("class com.pang.upgrades.Upgrade")){
			return this.upgradePool.obtain();
		}
		else if(object.toString().equals("class com.pang.upgrades.UpgradeArme")){
			return this.upgradeArmePool.obtain();
		}
		else if(object.toString().equals("class com.pang.upgrades.PowerUp")){
			return this.powerUpPool.obtain();
		}
		else if(object.toString().equals("class com.pang.bodies.Debris")){
			return this.debrisPool.obtain();
		}
		else return null;
	}
	
	public void free(Object object){
		if(object.getClass().toString().equals("class com.pang.armes.Rope")){
			ropePool.free((Rope)object);
		}
		else if(object.toString().equals("class com.pang.armes.Grappin")){
			grappinPool.free((Grappin)object);
		}
		else if(object.toString().equals("class com.pang.armes.Projectile")){
			projectilePool.free((Projectile)object);
		}
		else if(object.getClass().toString().equals("class com.pang.bodies.BalleGrande")){
			balleGrandePool.free((BalleGrande)object);
		}
		else if(object.getClass().toString().equals("class com.pang.bodies.BalleMoyenne")){
			balleMoyennePool.free((BalleMoyenne)object);
		}
		else if(object.getClass().toString().equals("class com.pang.bodies.BallePetite")){
			ballePetitePool.free((BallePetite)object);
		}
		else if(object.getClass().toString().equals("class com.pang.bodies.BalleMini")){
			balleMiniPool.free((BalleMini)object);
		}
		else if(object.getClass().toString().equals("class com.pang.upgrades.Upgrade")){
			upgradePool.free((Upgrade)object);
		}
		else if(object.getClass().toString().equals("class com.pang.upgrades.UpgradeArme")){
			upgradeArmePool.free((UpgradeArme)object);
		}
		else if(object.getClass().toString().equals("class com.pang.upgrades.PowerUp")){
			powerUpPool.free((PowerUp)object);
		}
		else if(object.getClass().toString().equals("class com.pang.bodies.Debris")){
			debrisPool.free((Debris)object);
		}
	}
}
