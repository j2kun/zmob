package game.gun;

import game.common.Constants;
import game.common.GameInfo;

import java.awt.image.BufferedImage;


public class WaveBeam extends Gun {

	private static final int INDEX = 4;
	private static final int MAX_AMMO = 50;
	private static final int RANGE = 500;
	
	@Override
	public String getName() {
		return Constants.WAVE_BEAM;
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
		return GameInfo.playerWavebeam;
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
