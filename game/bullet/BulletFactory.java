package game.bullet;

import game.common.Constants;
import game.common.Utils;
import game.info.BulletInfo;
import game.sprite.Player;

import java.util.ArrayList;
import java.util.List;

public class BulletFactory {
	public List<Bullet> create(String type, Player player){
		return createBulletOrBullets(type, player);
	}

	private List<Bullet> createBulletOrBullets(String type, Player player) {
		if(Constants.MACHINE_GUN.equals(type))
			return createMGBullet(player);
		else if(Constants.SHOTGUN.equals(type))
			return createShotgunBullet(player);
		else if(Constants.WAVE_BEAM.equals(type))
			return createWavebeamBullet(player);
		else if(Constants.ROCKET_LAUNCHER.equals(type))
			return createRocketBullet(player);
		else if(Constants.CHAIN_GUN.equals(type))
			return createChainBullet(player);
		else if(Constants.POISON_GUN.equals(type))
			return createPoisonBullet(player);
		
		return createPistolBullet(player);
	}

	private List<Bullet> createWavebeamBullet(Player player) {
		ArrayList<Bullet> bullets = new ArrayList<Bullet>();
		
		BulletInfo info = Utils.createBulletInfo(player);
		
		bullets.add(new WavebeamBullet(info, true));
		bullets.add(new WavebeamBullet(info, false));
		
		return bullets;
	}

	private List<Bullet> createPoisonBullet(Player player) {
		BulletInfo info = Utils.createBulletInfo(player);
		return new PoisonBullet(info).toList();
	}
	
	private List<Bullet> createMGBullet(Player player) {
		BulletInfo info = Utils.createBulletInfo(player);
		return new MGBullet(info).toList();
	}

	private List<Bullet> createChainBullet(Player player) {
		BulletInfo info = Utils.createBulletInfo(player);
		return new ChainBullet(info).toList();
	}

	private List<Bullet> createRocketBullet(Player player) {
		BulletInfo info = Utils.createBulletInfo(player);
		return new RocketBullet(info).toList();
	}

	private List<Bullet> createShotgunBullet(Player player) {
		ArrayList<Bullet> bullets = new ArrayList<Bullet>();
		for(int i = 0; i<Constants.NUM_SHOTTY_BULLETS; i++){
			BulletInfo info = Utils.createBulletInfo(player);
			bullets.add(new ShotgunBullet(info));
		}
		
		return bullets;
	}

	private List<Bullet> createPistolBullet(Player player) {
		BulletInfo info = Utils.createBulletInfo(player);
		return new PistolBullet(info).toList();
	}
}
