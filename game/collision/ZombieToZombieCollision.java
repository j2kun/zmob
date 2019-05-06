package game.collision;

import game.sprite.Zombie;

public class ZombieToZombieCollision extends Collision<Zombie, Zombie>{
	
	public ZombieToZombieCollision(Zombie left, Zombie right) {
		super(left, right);
	}

	@Override
	public void executeOnCollision() {
		double x = left.info.getSpriteCenterX();
		double y = left.info.getSpriteCenterY();
		double x2 = right.info.getSpriteCenterX();
		double y2 = right.info.getSpriteCenterY();

		if(x2 > x)
		{//if this zombie is on the right infode
			right.info.x += 1;//move the one on the right 1 pixel to the right
			left.info.x -= 1;//move the one on the left one pixel left

			if(y2 > y)
			{//if its above
				right.info.y += 1;//move the higher one 1 pixel higher
				left.info.y -= 1;//move the lower one a pixel lower
			}
			else{
				left.info.y += 1;//if its <= to other zombie, then do the
				right.info.y -= 1;//oppoinfote of the if statement
			}
		}

		// if either of the the zombies are poisoned
		if(right.isPoisoned()) //then poison both zombies
			left.setPoisoned(true);
		else if(left.isPoisoned())
			right.setPoisoned(true);
	}
}
