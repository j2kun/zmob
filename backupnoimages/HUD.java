package backupnoimages;
import java.awt.Color;
import java.awt.Graphics;


public class HUD {
	
	BigBall bb; Ammo ammo; Health life;
	private int pW, pH, sX, sY, healthX, healthY, ammoX, ammoY, stamX, stamY;
	private final int SX_SPACING = 150, SY_SPACING = 100, BAR_L = 100, BAR_W = 25,
						A_V_SPACING = 35, H_V_SPACING = 10, S_V_SPACING = BAR_W;
	
	
	public HUD(BigBall b, Ammo am, Health li, int w, int h)
	{
		pW = w; pH = h; bb = b; ammo = am; life = li;//pass instances
		sX = pW - SX_SPACING;
		sY = pH - SY_SPACING;
		
		ammoX = sX;
		ammoY = sY + A_V_SPACING;
		
		healthX = sX;
		healthY = ammoY + H_V_SPACING;
		
		stamX = sX;
		stamY = healthY + S_V_SPACING;
	}
	
	public void draw(Graphics g, int zombiesKilled, int wavesSurvived, int score, String diff)
	{
		g.setColor(Color.BLACK);
		//frag display
		g.drawString("Difficulty: " + diff, sX, sY-34);
		g.drawString("Zombies Killed: " + String.valueOf(zombiesKilled), sX, sY-17);
		g.drawString("Waves Survived: " + String.valueOf(wavesSurvived), sX, sY);
		g.drawString("Score: " + String.valueOf(score), sX, sY+17);
		
		//ammo display
		if(ammo.getWeaponString()!="Pistol")
			g.drawString(ammo.getWeaponString()+": "+ammo.getCurrentAmmo(), ammoX, ammoY);
		else
			g.drawString(ammo.getWeaponString()+": Unlim", ammoX, ammoY);
		
		//health bar
		g.drawRect(healthX-1, healthY-1, BAR_L+1, BAR_W+1);//draw ouside rectangle
		g.setColor(Color.RED);
		g.fillRect(healthX, healthY, life.getHealth(), BAR_W);//fill inside rectangle
		
		//stamina bar
		g.setColor(Color.BLACK);
		g.drawRect(stamX-1, stamY-1, BAR_L+1, BAR_W+1);//draw ouside rectangle
		g.setColor(Color.BLUE);
		g.fillRect(stamX, stamY, (int)bb.getStamina(), BAR_W);//fill inside rectangle
	}
}
