package game.sprite.pickup;

import game.common.GameInfo;
import game.gun.Gun;
import game.manager.PickupManager;
import game.manager.WeaponManager;
import game.sprite.Player;

import java.awt.image.BufferedImage;

public class Ammo extends Pickup {

	public Ammo(int w, int h, PickupManager pm) {
		super(w, h, pm);
	}

	@Override
	public PickupType getType() {
		return PickupType.AMMO;
	}

	@Override
	public void onPickup(Player player) {
		WeaponManager wm = owner.getWeaponManager();
		
		if(!wm.isAllFull()){
			Gun currentGun = wm.getCurrentGun();
			currentGun.addAmmo(currentGun.getPickupAmount());
			
			GameInfo.pickupDisplayString = "+" + currentGun.getPickupAmount() + 
				" " + currentGun.getName();
			owner.setDisplayFrame(0);
			owner.setDisplayInfo(true);//works with gameRender to display the String
			setRemove(true);
		}
	}

	@Override
	public BufferedImage getImage() {
		return GameInfo.ammo;
	}
}
