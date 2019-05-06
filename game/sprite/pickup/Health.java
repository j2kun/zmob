package game.sprite.pickup;

import game.common.Constants;
import game.common.GameInfo;
import game.manager.PickupManager;
import game.sprite.Player;

import java.awt.image.BufferedImage;

public class Health extends Pickup{
	
	public Health(int w, int h, PickupManager pm) {
		super(w, h, pm);
	}

	@Override
	public PickupType getType() {
		return PickupType.HEALTH;
	}

	@Override
	public void onPickup(Player player) {
		if(!player.fullHealth())
		{
			player.addHealth(Constants.RECOVERY_AMT);
			GameInfo.pickupDisplayString = "+" + Constants.RECOVERY_AMT + " Health";//setup display
			owner.setDisplayFrame(0);
			owner.setDisplayInfo(true);//works with gameRender to display the String
			setRemove(true);
		}
	}

	@Override
	public BufferedImage getImage() {
		return GameInfo.health;
	}

}
