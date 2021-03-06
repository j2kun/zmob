package game.gun;

import game.common.Constants;
import game.common.GameInfo;

import java.awt.image.BufferedImage;

public class PoisonGun extends Gun{

	private static final int INDEX = 7;
	private static final int MAX_AMMO = 15;
	private static final int RANGE = 720;
	
	@Override
	public String getName() {
		return Constants.POISON_GUN;
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
		return GameInfo.playerPistol;
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
