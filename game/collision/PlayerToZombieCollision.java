package game.collision;

import game.common.Constants;
import game.common.GameInfo;
import game.sprite.Player;
import game.sprite.Zombie;

public class PlayerToZombieCollision extends Collision<Player, Zombie>{

	public PlayerToZombieCollision(Player left, Zombie right) {
		super(left, right);
	}

	@Override
	public void executeOnCollision() {
		double x = left.info.getSpriteCenterX(), y = left.info.getSpriteCenterY();
		double x2 = right.info.getSpriteCenterX(), y2 = right.info.getSpriteCenterY();

		if(x2 > x){//if the zombie is on the player's right side, move it 1 pixel to the right
			right.info.x =  right.info.x - Constants.SPRITE_SPACING;
			if(y2 > y)//if it's above, move it 1 pixel up
				right.info.y = right.info.y - Constants.SPRITE_SPACING;
			else//if it's below, move it 1 pixel down
				right.info.y = right.info.y + Constants.SPRITE_SPACING;
		}else{ //if the zombie is on the player's left side, move it 1 pixel to the left
			right.info.x = right.info.x + Constants.SPRITE_SPACING;
			if(y2 > y)//if its above, move it 1 pixel up
				right.info.y = right.info.y - Constants.SPRITE_SPACING;
			else
				//if it's below, move it 1 pixel down
				right.info.y = right.info.y + Constants.SPRITE_SPACING;
		}
		
		if(!GameInfo.godMode)
			left.setHealth(left.getHealth()-Constants.DEFAULT_ZOMBIE_POWER);
	}
}
