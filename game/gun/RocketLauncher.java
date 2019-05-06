package game.gun;

import game.common.Constants;
import game.common.GameInfo;

import java.awt.image.BufferedImage;

public class RocketLauncher extends Gun{

	private static final int INDEX = 5;
	private static final int MAX_AMMO = 10;
	private static final int RANGE = 600;
	
	@Override
	public String getName() {
		return Constants.ROCKET_LAUNCHER;
	}
	
	@Override
	public int getMaxAmmo() {
		return MAX_AMMO;
	}

	@Override
	public int getRange() {
		return RANGE;
	}

	@Override
	public boolean isRocketType() {
		return true;
	}

	@Override
	public BufferedImage getImage() {
		return GameInfo.playerShotty;
	}

	@Override
	public boolean isAutomatic() {
		return false;
	}
	
	@Override
	public int getIndex() {
		return INDEX;
	}
}
