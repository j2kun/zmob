package backupnoimages;

public class Health {
	
	byte me;
	int currentHealth;
	BigBall player;
	int pW, pH, barX, barY, barL, barW;
	
	public Health(BigBall bb, int w, int h)
	{
		currentHealth = 100;
		player = bb;
		pW = w; pH = h;
	}
	
	//add and subtract and set health
	
	public void setHealth(int h)
	{
		currentHealth = h;
	}
	
	public void addHealth(double amt)
	{
		currentHealth+=amt;
		
		if(currentHealth > 100)
			currentHealth = 100;
	}
	
	public void subtractHealth(double amt)
	{
		currentHealth-=amt;
		
		if(currentHealth < 0)
			currentHealth = 0;
	}
	
	public int getHealth()
	{
		return currentHealth;
	}
}
