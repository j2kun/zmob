package game.gun;

import game.common.Constants;
import game.common.GameInfo;

import java.awt.image.BufferedImage;

public class ChainGun extends Gun{

	private static final int INDEX = 6;
	private static final int MAX_AMMO = 2;
	private static final int RANGE = 720;

	@Override
	public String getName() {
		return Constants.CHAIN_GUN;
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
	public int getPickupAmount(){
		return 1;
	}

	@Override
	public int getIndex() {
		return INDEX;
	}
}
