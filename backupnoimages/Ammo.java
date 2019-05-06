package backupnoimages;
public class Ammo {
	
	//corresponds with currentGun number
	int currentGun, pW, pH, sX, sY;
	BigBall player;
	private final int PISTOL = 0, MACHINE_GUN = 1, SHOTGUN = 2, WAVE_GUN = 3, 
						ROCKET = 4, CHAIN_GUN = 5, TEST = 6;
	private final int NUMBER_OF_WEAPONS = 6;
	private String[] gunTypes = {"Pistol", "Machine Gun", "Shotty", "Wave Beam",
			"Rocket Launcher", "Chain Gun", "Poison Gun"}; 
	private int[] maxAmmo = new int[]{500,150,30,125,10,2, 15};
	private int[] currentAmmo = new int[]{500,150,30,125,10,2, 15};
	byte me;
	
	public Ammo(BigBall bb, int w, int h)
	{
		currentGun = PISTOL;
		player = bb;
		pW = w; pH = h;
		
		//i just put in these lines to get rid of the stupid 
		//warning eclipse gives about not reading these
		//variables locally
		me = MACHINE_GUN;
		me = SHOTGUN;
		me = WAVE_GUN;
		me = ROCKET;
		me = CHAIN_GUN;
		me = TEST;
	}
	
	public void resetAmmo()
	{
		for(int i = 0; i<NUMBER_OF_WEAPONS; i++)
			currentAmmo[currentGun] = maxAmmo[currentGun];
	}
	
	public void changeGun(int gunType)
	{currentGun = gunType;}
	
	public void subtractAmmo(int gunType)
	{
		if(gunType != PISTOL)
			currentAmmo[gunType]--;
	}
	
	public boolean isAmmoLeft(int gunType)
	{
		boolean x = false;
		
		if(currentAmmo[gunType] > 0)
			x = true;
		
		return x;
	}
	
	public boolean isFull(int gunType)
	{
		boolean x = false;
		
		if(currentAmmo[gunType] == maxAmmo[gunType])
			x = true;
		
		return x;
	}
	
	public boolean isOneGunEmpty()
	{
		boolean x = false;
		
		for(int i = 0; i<NUMBER_OF_WEAPONS; i++)//for all guns
			if(currentAmmo[i]==0)//if any is 0
				x = true;//this is true
		
		return x;
	}
	
	//user gives the amount to add and the gun type
	public void addAmmo(int amount, int gunType)
	{
		currentAmmo[gunType]+=amount;
		
		if(currentAmmo[gunType]>maxAmmo[gunType])
			currentAmmo[gunType]=maxAmmo[gunType];
	}
	
	//returns the maximum allowed ammo for the specified weapon type
	public int getMaxAmmo(int index)
	{return maxAmmo[index];}
	
	public int getAmmo(int gunType)
	{return currentAmmo[gunType];}
	
	public int getCurrentAmmo()
	{return currentAmmo[currentGun];}
	
	public String getWeaponString(int index)
	{return gunTypes[index];}
	
	public String getWeaponString()
	{return gunTypes[currentGun];}
}
