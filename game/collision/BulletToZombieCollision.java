package game.collision;

import game.bullet.Bullet;
import game.bullet.ChainBullet;
import game.bullet.Explodable;
import game.common.Constants;
import game.common.GameInfo;
import game.info.BulletInfo;
import game.manager.BulletManager;
import game.manager.ZombieManager;
import game.sprite.Zombie;

public class BulletToZombieCollision extends Collision<Bullet, Zombie>{

	private ZombieManager zombieManager;
	private BulletManager bulletManager;
	
	public BulletToZombieCollision(Bullet left, Zombie right, BulletManager bM, ZombieManager zM) {
		super(left, right);
		bulletManager = bM;
		zombieManager = zM;
	}

	@Override
	public void executeOnCollision() {
		right.subtractHealth(left.getStrength());
		// current zombie health minus strength of weapon

		//if explosion from chain gun hits a zombie, start another explosion
		if(left.getType() == Constants.CHAIN_GUN)
		{
			if(((Explodable)left).isExplosion())
			{
				int zX = right.info.getSpriteCenterX(); 
				int zY = right.info.getSpriteCenterY();
				
				ChainBullet newBullet = new ChainBullet(new BulletInfo(zX, zY, 0.0));
				newBullet.setExplosion(true);
				bulletManager.add(newBullet);
			}
		}

		if(left.getType() == Constants.POISON_GUN)
			right.setPoisoned(true);//poison zombies

		if (right.isDead()){ 
			zombieManager.remove(right);
			GameInfo.numZombiesKilled++;
			GameInfo.score += (Math.pow(GameInfo.currentDifficulty.getFactor(), 2)/10);
			
//			this algorithm makes smaller waves for harder difficulty
			if(GameInfo.numZombiesKilled % 
				(10.0 / GameInfo.currentDifficulty.getFactor() * 60) == 0) {
				zombieManager.newWave();
			}
		}

		if (left.getType() != Constants.POISON_GUN){ //collisions do not remove poison bullets
			if ((left instanceof Explodable) && !((Explodable)left).isExplosion())
				((Explodable)left).setExplosion(true);
			else//if a non-rocket, remove the bullet
				bulletManager.remove(left);
		}	
	}
}
