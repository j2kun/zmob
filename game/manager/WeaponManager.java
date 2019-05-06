package game.manager;

import game.common.Constants;
import game.common.GameInfo;
import game.common.Utils;
import game.gun.ChainGun;
import game.gun.Gun;
import game.gun.MachineGun;
import game.gun.Pistol;
import game.gun.PoisonGun;
import game.gun.RocketLauncher;
import game.gun.Shotgun;
import game.gun.WaveBeam;
import game.sprite.Player;

import java.awt.Graphics;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class WeaponManager{
	private Gun currentGun;
	Map<String, Gun> guns;
	protected BulletManager bulletManager;

	public WeaponManager(Player player, ZombieManager zm){
		guns = new TreeMap<String, Gun>(new GunComparator());

		guns.put(Constants.PISTOL, new Pistol());
		guns.put(Constants.MACHINE_GUN, new MachineGun());
		guns.put(Constants.SHOTGUN, new Shotgun());
		guns.put(Constants.WAVE_BEAM, new WaveBeam());
		guns.put(Constants.ROCKET_LAUNCHER, new RocketLauncher());
		guns.put(Constants.CHAIN_GUN, new ChainGun());
		guns.put(Constants.POISON_GUN, new PoisonGun());

		currentGun = guns.get(Constants.PISTOL);
		bulletManager = new BulletManager(player, zm);
	}
	
	public void update(){
		if(!bulletManager.isEmpty())
			bulletManager.update();
		
		if(GameInfo.mousePressed && currentGun.isAutomatic()){//if automatic and shooting
			if(isAmmoLeft(currentGun))
				bulletManager.createAndAdd(currentGun.getName());

			if (!isAmmoLeft(currentGun))
				switchWeaponsDown();
		}
	}
	
	public void drawBullets(Graphics g){
		bulletManager.drawAll(g);
	}

	public void resetAmmo(){
		for(Gun g : guns.values())
			g.setAmmo(g.getMaxAmmo());
	}

	public void switchTo(String type){
		currentGun = guns.get(type);  
	}

	public void subtractAmmo(Gun g){
		if(!g.getName().equals(Constants.PISTOL))
			g.addAmmo(-1);
	}

	public boolean isAmmoLeft(String type){
		Gun g = guns.get(type);
		return g.getCurrentAmmo() > 0;
	}

	public boolean isAmmoLeft(Gun g){
		return g.getCurrentAmmo() > 0;
	}

	public boolean isFull(String type){
		Gun g = guns.get(type);
		return g.getCurrentAmmo() == g.getMaxAmmo();
	}

	private boolean isFull(Gun g){
		return g.getCurrentAmmo() == g.getMaxAmmo();
	}

	public boolean isAllFull(){
		for(Gun g : guns.values())
			if(!isFull(g))
				return false;
		return true;
	}

	public boolean isOneGunEmpty(){
		for(Gun g : guns.values())
			if(!isAmmoLeft(g))
				return true;
		return false;
	}

	public void switchWeaponsDown(){
		if(currentGun == guns.get(Constants.PISTOL)){
			currentGun = guns.get(Constants.POISON_GUN);
			return;
		} else {
			for(Gun gun : (new Utils()).reverse(getNotEmptyGuns()))
				if(gun.getIndex() < currentGun.getIndex()){
					currentGun = gun;
					return;
				}
		}
		currentGun = guns.get(Constants.PISTOL);
	}

	public void switchWeaponsUp(){		
		if(currentGun == guns.get(Constants.POISON_GUN)){
			currentGun = guns.get(Constants.PISTOL);
			return;
		} else {
			for(Gun gun : getNotEmptyGuns())
				if(gun.getIndex() > currentGun.getIndex()){
					currentGun = gun;
					return;
				}
		}
		
		currentGun = guns.get(Constants.PISTOL);
	}

	public int getMaxAmmo(String type){return guns.get(type).getMaxAmmo();}

	public Gun getCurrentGun(){return currentGun;}
	
	public void explodeRockets(){
		bulletManager.explodeAll();
	}
	
	public BulletManager getBulletManager() {
		return bulletManager;
	}
	
	public Collection<Gun> getNotFullGuns(){
		Collection<Gun> notFullGuns = new LinkedList<Gun>();
		
		for(Gun gun : guns.values())
			if(!gun.isFull())
				notFullGuns.add(gun);
		
		return notFullGuns;
	}
	
	public Collection<Gun> getNotEmptyGuns(){
		Collection<Gun> notEmptyGuns = new LinkedList<Gun>();
		
		for(Gun gun : guns.values())
			if(!gun.isEmpty())
				notEmptyGuns.add(gun);
		
		return notEmptyGuns;
	}
	
	public Gun chooseNotFullGun(){
		Collection<Gun> notFullGuns = getNotFullGuns();
		
		int target = new Random().nextInt(notFullGuns.size());
		int index = 0;
		for(Gun gun : notFullGuns){
			if(index == target)
				return gun;
			index++;
		}
		
		return guns.get(Constants.PISTOL);
	}
}
