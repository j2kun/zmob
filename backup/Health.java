package backup;

public class Health {
	
	byte me;
	int currentHealth;
	final int MAX_HEALTH = 100;
	Player player;
	int pW, pH, barX, barY, barL, barW;
	
	public Health(Player bb, int w, int h)
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
	
	public boolean isFull()
	{
		return (currentHealth == MAX_HEALTH);
	}
}
