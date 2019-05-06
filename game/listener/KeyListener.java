package game.listener;

import game.Difficulty;
import game.IZombie;
import game.common.Constants;
import game.common.GameInfo;
import game.manager.PickupManager;
import game.manager.WeaponManager;
import game.manager.ZombieManager;
import game.sprite.Player;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyListener extends KeyAdapter{
	
	private Player player;
	private IZombie owner;
	private WeaponManager weaponManager;
	private PickupManager pickupManager;
	private ZombieManager zombieManager;
	
	public KeyListener(IZombie owner, Player player, 
			WeaponManager wm, PickupManager pm, ZombieManager zm){
		this.player = player;
		this.owner = owner;
		this.weaponManager = wm;
		this.pickupManager = pm;
		this.zombieManager = zm;
	}
	
	@Override
	public void keyPressed(KeyEvent e){
		processKeyPress(e);
	}
	
	@Override
	public void keyReleased(KeyEvent e){
		processKeyRelease(e);
	}
	
	protected void processKeyPress(KeyEvent e) {

		int keyCode = e.getKeyCode();

		if(keyCode == KeyEvent.VK_ESCAPE){
			GameInfo.gameRunning = false;
			owner.dispose();
		}
		
		if (GameInfo.gameStarted)
			if (keyCode == KeyEvent.VK_P)
				togglePause();

		if (!GameInfo.gameStarted) {
			if (keyCode == KeyEvent.VK_F5) {
				changeDifficulty(Difficulty.EASY);
				GameInfo.gameStarted = true;
				GameInfo.paused = false;
			}
			if (keyCode == KeyEvent.VK_F6) {
				changeDifficulty(Difficulty.NORMAL);
				GameInfo.gameStarted = true;
				GameInfo.paused = false;
			}
			if (keyCode == KeyEvent.VK_F7) {
				changeDifficulty(Difficulty.HARD);
				GameInfo.gameStarted = true;
				GameInfo.paused = false;				
			}
		}

		//Restart
		if(keyCode == KeyEvent.VK_F8)
			owner.restart(owner);

		keyCode = -1;
		if (!GameInfo.paused)
			keyCode = e.getKeyCode();

		//movement
		if (keyCode == KeyEvent.VK_W)
			GameInfo.wKey = true;
		if (keyCode == KeyEvent.VK_S)
			GameInfo.sKey = true;
		if (keyCode == KeyEvent.VK_A)
			GameInfo.aKey = true;
		if (keyCode == KeyEvent.VK_D)
			GameInfo.dKey = true;

		//weapon changing
		if (keyCode == KeyEvent.VK_1)
			weaponManager.switchTo(Constants.PISTOL);
		else if (keyCode == KeyEvent.VK_2)
			weaponManager.switchTo(Constants.MACHINE_GUN);
		else if (keyCode == KeyEvent.VK_4)
			weaponManager.switchTo(Constants.WAVE_BEAM);
		else if (keyCode == KeyEvent.VK_3)
			weaponManager.switchTo(Constants.SHOTGUN);
		else if (keyCode == KeyEvent.VK_5)
			weaponManager.switchTo(Constants.ROCKET_LAUNCHER);
		else if (keyCode == KeyEvent.VK_6)
			weaponManager.switchTo(Constants.CHAIN_GUN);
		else if (keyCode == KeyEvent.VK_7)
			weaponManager.switchTo(Constants.POISON_GUN);

		if(keyCode == KeyEvent.VK_E)
			weaponManager.explodeRockets();

		//toggle run
		if(keyCode == KeyEvent.VK_CONTROL)
			player.info.running = !player.info.running;

		//hold run
		if(keyCode == KeyEvent.VK_SHIFT)
			player.info.running = true;

		//******BEGIN DEVELOPERS' HACKS********\\
		if (keyCode == KeyEvent.VK_F1)
			zombieManager.spawn();
		if (keyCode == KeyEvent.VK_F2)
			GameInfo.godMode  = !GameInfo.godMode;
		if (keyCode == KeyEvent.VK_F3)
			pickupManager.newPickups();

		if(keyCode == KeyEvent.VK_Z)
			player.subtractHealth();
		if(keyCode == KeyEvent.VK_X)
			player.addHealth();

		if(keyCode == KeyEvent.VK_F10)
			GameInfo.zombiesSpawning = !GameInfo.zombiesSpawning;

		//send out a zmob wave
		if (keyCode == KeyEvent.VK_F9)
			for(int i = 0; i<25; i++)
				zombieManager.spawn();
		//******END DEVELOPERS' HACKS*******\\


		if (!GameInfo.paused) {
			if (GameInfo.wKey)
				player.info.up = true;
			if (GameInfo.sKey) 
				player.info.down = true;
			if (GameInfo.aKey) 
				player.info.left = true;
			if (GameInfo.dKey) 
				player.info.right = true;
		}		
	}

	private void togglePause() {
		GameInfo.paused = !GameInfo.paused;
		if(GameInfo.paused){
			player.stopSprite();
			GameInfo.wKey = false;
			GameInfo.sKey = false;
			GameInfo.aKey = false;
			GameInfo.dKey = false;
		}
	}

//	algorithm to change difficulty and remove zombies until the num zombies
	//in play are no greater than the maximum number of zombies for that difficulty
	public void changeDifficulty(Difficulty level){
		if(level == Difficulty.EASY)//if requested level is easy
		{				//and he current difficulty + # zombies > EASY (10)
			if(GameInfo.currentDifficulty.compareTo(Difficulty.EASY) > 0 
					&& zombieManager.size() > Difficulty.EASY.getFactor()){
				
				int i = zombieManager.size()-1;
				while(zombieManager.size() >= Difficulty.EASY.getFactor()){
					//then remove until the arraylist is the size it should be
					zombieManager.remove(i);
					i--;
				}//end while
			}//end if
		}//end if
		else if(level == Difficulty.NORMAL){//do the same for the normal level
			if(GameInfo.currentDifficulty.compareTo(Difficulty.NORMAL) > 0 && 
					zombieManager.size() > Difficulty.NORMAL.getFactor()){
				
				int i = zombieManager.size()-1;
				while(zombieManager.size() >= Difficulty.NORMAL.getFactor()){
					//then remove until the array
					zombieManager.remove(i);
					i--;
				}//end while
			}//end if
		}//end if/else
		
		//do nothing if hard
		GameInfo.currentDifficulty = level;
		zombieManager.setNumZombiesKilled(0);
	}

	protected void processKeyRelease(KeyEvent e) {

		int keyCode = -1;
		if (!GameInfo.paused) {
			keyCode = e.getKeyCode();
		}

		//wsad for updownleftright movement
		if (keyCode == KeyEvent.VK_W)
			GameInfo.wKey = false;
		if (keyCode == KeyEvent.VK_S)
			GameInfo.sKey = false;
		if (keyCode == KeyEvent.VK_A)
			GameInfo.aKey = false;
		if (keyCode == KeyEvent.VK_D)
			GameInfo.dKey = false;

		if(keyCode == KeyEvent.VK_SHIFT)
			player.info.running = false;

		if(!GameInfo.wKey)
			player.info.up = false;
		if(!GameInfo.sKey)
			player.info.down = false;
		if(!GameInfo.aKey)
			player.info.left = false;
		if(!GameInfo.dKey)
			player.info.right = false;
	}
}
