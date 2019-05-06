package game.collision;

import game.sprite.Player;
import game.sprite.pickup.Pickup;

public class PlayerToPickupCollision extends Collision<Player, Pickup>{
	
	public PlayerToPickupCollision(Player left, Pickup right) {
		super(left, right);
	}

	@Override
	public void executeOnCollision() {
		right.onPickup(left);
	}
}
