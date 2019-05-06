package game.common;

public final class Constants {
	public static final int FRAME_RATE = 30;
	
	public static final int RECOVERY_AMT = 25;

	public static final String PISTOL = "Pistol";
	public static final String MACHINE_GUN = "Machine Gun";
	public static final String SHOTGUN = "Shotgun";
	public static final String WAVE_BEAM = "Wave Beam";
	public static final String ROCKET_LAUNCHER = "Rocket Launcher";
	public static final String CHAIN_GUN = "Chain Gun";
	public static final String POISON_GUN = "Poison Gun";
	public static final int NUM_OF_WEAPONS = 7;   

	public static final int NUM_SHOTTY_BULLETS = 5;

	public static final int MAX_HEALTH = 100;
	public static final int WALK = 4, RUN = 10;
	public static final double RUN_DEPLETION = 1.0, RUN_RECHARGE = .5;
	public static final int MAX_RUN = 100;
	
	public static final int DEFAULT_ZOMBIE_POWER = 2;
	public static final int POISONED = 1;
	public static final int HEALTHY = 2;
	public static final int SPRITE_SPACING = 5;

	public static final String IMAGE_FILENAME_PREFIX = "img/";

	public static final String[] INSTRUCTIONS = new String[]{"Paused! Press ESC to quit.",
		"",
		"Welcome to Zmob!",
		"",
		"Instructions:",
		"Use w-s-a-d to move up, down, left, and right (respectively)", 
		"Click the mouse to shoot. Scroll the mouse wheel to switch weapons. (or use #1-7)",
		"",
		"1 = Pistol: unlimited ammo",
		"2 = Machine Gun: hold the mouse to fire automatic",
		"3 = Shotgun: sprays five shots",
		"4 = Wave Beam: hold the mouse to fire a stream",
		"5 = Rocket Launcher: press E to explode the rockets manually",
		"6 = Chain Gun: Slower than rockets, but creates a chain reaction!",
		"7 = Poison Gun: Does little damage, but poisons zombies",
		"",
		"The various weapons have different ranges (shown by the blue circle).",
		"Hold shift to run, or press ctrl to toggle run. (watch the green stamina bar!)",
		"",
		"The red crosses give you +25 health, and the yellow boxes are ammunition crates.",
		"Preference is given to weapons with no ammo left. Pickups are spawned as you ",
		"kill zombies, with harder difficulties requiring more frags to get pickups.",
		"",
		"Use F5-F7 to change difficulty levels. (F5 = easy, F6 = normal, F7 = hard)",
		"Zombies spawn faster and are more numerous on harder difficulties.",
		"Whatever you do, don't press F9 during the game.",
		"",
		"Choose a Difficulty Level to begin!",
	"Good luck!"};

}
