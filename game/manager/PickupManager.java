package game.manager;

import game.common.GameInfo;
import game.sprite.Player;
import game.sprite.pickup.Ammo;
import game.sprite.pickup.Health;
import game.sprite.pickup.Pickup;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collection;

public class PickupManager extends Manager<Pickup>{

	private ArrayList<Pickup> pickups;
	private WeaponManager weaponManager;
	private Player player;
	private int displayFrame;
	private boolean displayInfo;

	public PickupManager(ZombieManager zm, WeaponManager wm, Player player) {
		pickups = new ArrayList<Pickup>();
		weaponManager = wm;
		this.player = player;
		displayFrame = 0;
		displayInfo = false;
	}

	public int getDisplayFrame() {
		return displayFrame;
	}

	public void setDisplayFrame(int displayFrame) {
		this.displayFrame = displayFrame;
	}

	public boolean isDisplayInfo() {
		return displayInfo;
	}

	public void setDisplayInfo(boolean displayInfo) {
		this.displayInfo = displayInfo;
	}

	public boolean noneLeft() {
		return pickups.isEmpty();
	}
	
	@Override
	public void updateIndividual(Pickup pickup){
		pickup.checkCollision(player);
	}
	
	@Override
	public void update(){
		super.update();
		
		if(GameInfo.numZombiesKilled == 
			GameInfo.currentDifficulty.getFactor()
				&& pickups.size()<20)
			newPickups();
	}

	public void newPickups() {
		pickups.add(new Health(GameInfo.health.getWidth(), GameInfo.health.getHeight(), this));
		pickups.add(new Ammo(GameInfo.ammo.getWidth(), GameInfo.health.getHeight(), this));
	}

	public void checkPickupCollision(Player player) {
		for (Pickup p : pickups)
			p.checkCollision(player);
	}

	public void renderPickups(Graphics g) {
		for(Pickup p : pickups)
			p.draw(g);
	}

	public void renderDisplayInfo(Graphics dbg, int x, int y) {
		if (displayInfo) {
			displayFrame++;
			dbg.setColor(Color.WHITE);
			dbg.drawString(GameInfo.pickupDisplayString, x, y);
			if (displayFrame == 50) {// info goes away after a few seconds
				displayFrame = 0;
				displayInfo = false;
			}
		}
	}

	public WeaponManager getWeaponManager() {
		return weaponManager;
	}

	@Override
	public Collection<Pickup> getCollection() {
		return pickups;
	}
}
