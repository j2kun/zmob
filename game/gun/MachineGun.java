package game.gun;

import game.common.Constants;
import game.common.GameInfo;

import java.awt.image.BufferedImage;

public class MachineGun extends Gun{

	private static final int INDEX = 2;
	private static final int MAX_AMMO = 150;
	private static final int RANGE = 400;
	
	@Override
	public String getName() {
		return Constants.MACHINE_GUN;
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
		return true;
	}
	
	@Override
	public int getIndex() {
		return INDEX;
	}
}
