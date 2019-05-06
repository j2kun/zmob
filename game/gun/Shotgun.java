package game.gun;

import game.common.Constants;
import game.common.GameInfo;

import java.awt.image.BufferedImage;

public class Shotgun extends Gun{

	private static final int INDEX = 3;
	private static final int MAX_AMMO = 30;
	private static final int RANGE = 200;
	
	@Override
	public String getName() {
		return Constants.SHOTGUN;
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
		return false;
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
