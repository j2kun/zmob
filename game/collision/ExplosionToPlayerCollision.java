package game.collision;

import game.bullet.Bullet;
import game.sprite.Player;

public class ExplosionToPlayerCollision extends Collision<Bullet, Player>{

	public ExplosionToPlayerCollision(Bullet left, Player right) {
		super(left, right);
	}

	@Override
	public void executeOnCollision() {
		if(!(left.isRocketType())){
			System.out.println("Checking explosion collision for a non-rocket.");
			throw new RuntimeException();
		}
			
		right.subtractHealth();
	}
	
}
