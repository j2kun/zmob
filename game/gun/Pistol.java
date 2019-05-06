package game.gun;

import game.common.Constants;
import game.common.GameInfo;

import java.awt.image.BufferedImage;


public class Pistol extends Gun{

	private static final int INDEX = 1;
	private static final int MAX_AMMO = 500;
	private static final int RANGE = 300;
	
	@Override
	public String getName() {
		return Constants.PISTOL;
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
