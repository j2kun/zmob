package game.bullet;

import game.common.Constants;
import game.info.BulletInfo;



public class MGBullet extends Bullet{

	private static final int STRENGTH = 10;
	private static final int MOVE_DIST = 10;
	private static final int RANGE = 382;
	private static final int RADIUS = 3;
	
	public MGBullet(BulletInfo info){     
		super(info);
	}

	@Override
	public int getMoveDist() {
		return MOVE_DIST;
	}

	@Override
	public int getStrength() {
		return STRENGTH;
	}

	@Override
	public int getRange() {
		return RANGE;
	}

	@Override
	public int getRadius() {
		return RADIUS;
	}
	
	@Override
	public String getType() {
		return Constants.MACHINE_GUN;
	}

	@Override
	public boolean isRocketType() {
		return false;
	}
}