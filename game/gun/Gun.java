package game.gun;

import java.awt.image.BufferedImage;

public abstract class Gun{
	
	public abstract String getName();
	public abstract int getMaxAmmo();
	public abstract int getRange();
	public abstract int getIndex();
	public abstract boolean isRocketType();
	public abstract boolean isAutomatic();
	public abstract BufferedImage getImage();
	
	protected int maxAmmo;
	protected int range;
	protected int currentAmmo;

	public Gun(){
		maxAmmo = getMaxAmmo();
		range = getRange();
		
		currentAmmo = maxAmmo;
	}
	
	public int getAmmo() {
		return currentAmmo;
	}

	public void setAmmo(int ammo) {
		currentAmmo = ammo;
	}
   
	public void addAmmo(int amount){
	   	currentAmmo += amount;
	   	
	   	if(currentAmmo > maxAmmo)
	   		currentAmmo = maxAmmo;
	}
	
	public int getCurrentAmmo() {
		return currentAmmo;
	}
	
	public boolean isFull(){
		return currentAmmo >= maxAmmo;
	}
	
	public int getPickupAmount(){
		return (int) .3 * maxAmmo;
	}
	
	public boolean isEmpty(){
		return currentAmmo <= 0;
	}
}