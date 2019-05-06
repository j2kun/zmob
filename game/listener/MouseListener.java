package game.listener;

import game.common.GameInfo;
import game.common.Utils;
import game.gun.Gun;
import game.manager.WeaponManager;
import game.sprite.Player;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.event.MouseInputAdapter;

public class MouseListener extends MouseInputAdapter implements MouseWheelListener {
	
	private WeaponManager wm;
	private Player player;
	
	public MouseListener(WeaponManager wm, Player player){
		this.wm = wm;
		this.player = player;
	}
	
	public void mouseMoved(final MouseEvent e) {
		setMouseVariables(e.getX(), e.getY());
	}

	public void mouseDragged(final MouseEvent e) {
		setMouseVariables(e.getX(), e.getY());
	}
	
	public void setMouseVariables(int mouseX, int mouseY){
		GameInfo.mouseX = mouseX;
		GameInfo.mouseY = mouseY;
		GameInfo.mouseAngle = 
			Utils.getAngle(player.info.getSpriteCenterX(), 
					player.info.getSpriteCenterY(), mouseX, mouseY);
	}

	public void mousePressed(final MouseEvent e) {
		if (!GameInfo.paused) {
			Gun currentGun = wm.getCurrentGun();
			String name = currentGun.getName();
			if (wm.isAmmoLeft(currentGun)) {
				if (!currentGun.isAutomatic())
					wm.subtractAmmo(currentGun);
				//if automatic, ammo is subtracted in the gameUpdate method

				wm.getBulletManager().createAndAdd(name);
				
				GameInfo.mousePressed = true;
				//while there is no ammo left in the current gun
				if (!wm.isAmmoLeft(currentGun) && GameInfo.numPoisonBulletsShot == 0)
					wm.switchWeaponsDown();
			}
		}
	}

	public void mouseWheelMoved(final MouseWheelEvent e) {
		if (!GameInfo.paused) {
			final int notches = e.getWheelRotation();

			if (notches < 0)//mouse wheel moves up
				wm.switchWeaponsUp();
			else
				wm.switchWeaponsDown();
		}
	}

	public void mouseReleased(final MouseEvent e) {
		GameInfo.mousePressed = false;
	}
}
