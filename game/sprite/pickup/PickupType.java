package game.sprite.pickup;

public enum PickupType {
	HEALTH("health"),
	AMMO("ammo");
	
	private String name;
	
	private PickupType(String name){
		this.name = name;
	}
	
	public String toString(){
		return name;
	}
}
