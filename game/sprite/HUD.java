package game.sprite;
import game.common.Constants;
import game.common.GameInfo;
import game.manager.WeaponManager;

import java.awt.Color;
import java.awt.Graphics;

public class HUD {
	
	Player player; 
	WeaponManager weaponManager;
	private int sX, sY, healthX, healthY, ammoX, ammoY, stamX, stamY;
	private final int SX_SPACING = 150, SY_SPACING = 100, BAR_L = 100, BAR_W = 25,
						A_V_SPACING = 35, H_V_SPACING = 10, S_V_SPACING = BAR_W;
	
	
	public HUD(Player b, WeaponManager wm)
	{
		player = b; weaponManager = wm;
		sX = GameInfo.pW - SX_SPACING;
		sY = GameInfo.pH - SY_SPACING;
		
		ammoX = sX;
		ammoY = sY + A_V_SPACING;
		
		healthX = sX;
		healthY = ammoY + H_V_SPACING;
		
		stamX = sX;
		stamY = healthY + S_V_SPACING;
	}
	
	public void draw(Graphics g){		
		g.setColor(Color.WHITE);
		//frag display
		g.drawString("Difficulty: " + GameInfo.currentDifficulty.getName(), sX, sY-34);
		g.drawString("Zombies Killed: " + String.valueOf(GameInfo.numZombiesKilled), sX, sY-17);
		g.drawString("Waves Survived: " + String.valueOf(GameInfo.numWavesSurvived), sX, sY);
		g.drawString("Score: " + String.valueOf(GameInfo.score), sX, sY+17);
		
		//ammo display
		if(!weaponManager.getCurrentGun().getName().equals(Constants.PISTOL))
			g.drawString(weaponManager.getCurrentGun().getName()+": "
			   +weaponManager.getCurrentGun().getAmmo(), ammoX, ammoY);
		else
			g.drawString(weaponManager.getCurrentGun().getName()+": Unlim", ammoX, ammoY);
		
		//health bar
		g.setColor(Color.BLACK);
		g.drawRect(healthX-1, healthY-1, BAR_L+1, BAR_W+1);//draw ouside rectangle
		g.setColor(Color.RED);
		g.fillRect(healthX, healthY, player.getHealth(), BAR_W);//fill inside rectangle
		
		//stamina bar
		g.setColor(Color.BLACK);
		g.drawRect(stamX-1, stamY-1, BAR_L+1, BAR_W+1);//draw ouside rectangle
		g.setColor(Color.BLUE);
		g.fillRect(stamX, stamY, (int)player.getStamina(), BAR_W);//fill inside rectangle
	}
}
