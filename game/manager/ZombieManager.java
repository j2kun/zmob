package game.manager;

import game.collision.ZombieToZombieCollision;
import game.common.GameInfo;
import game.info.ZombieInfo;
import game.loader.SpriteLoader;
import game.sprite.Player;
import game.sprite.Zombie;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

public class ZombieManager extends Manager<Zombie>{

	private ArrayList<Zombie> zombies;
	private int numZombiesKilled;
	private Player player;
	
	public ZombieManager(Player player){
		this.player = player;
		zombies = new ArrayList<Zombie>();
		numZombiesKilled = 0;
	}
	
	public void add(Zombie z){
		zombies.add(z);
	}
	
	public void remove(int i){
		zombies.remove(i);
	}
	
	@Override
	public void updateIndividual(Zombie z) {
		z.checkPlayerCollision(player);
		
		for(Zombie right : zombies)
			if(z != right)
				new ZombieToZombieCollision(z, right).evaluate();
	}
	
	@Override
	public void update(){
		super.update();
		
		if(zombies.size() < GameInfo.currentDifficulty.getFactor() * 
				GameInfo.numWavesSurvived)
			spawn();
			
		Collection<Zombie> toRemove = new LinkedList<Zombie>();
		
		if(!toRemove.isEmpty())
			zombies.removeAll(toRemove);
	}
	
	public void spawn(){
		zombies.add(new Zombie(
				new ZombieInfo(0, 0, GameInfo.zombieW, GameInfo.zombieH)));
	}
	
	public void render(Graphics g, SpriteLoader sl){
		for(Zombie z : zombies){
			BufferedImage bi = (z.isPoisoned()) ? 
					GameInfo.poisonedZombie : GameInfo.zombie;
			z.draw(g, bi, GameInfo.translateOpZ, 
					sl.getZombieRotator(z.info.angle));
		}
	}

	public void newWave() {
		GameInfo.numWavesSurvived++;
		GameInfo.score += GameInfo.currentDifficulty.getFactor()*50;
	}

	public int size() {
		return zombies.size();
	}

	public void remove(Zombie z) {
		zombies.remove(z);
	}

	public int getNumZombiesKilled() {
		return numZombiesKilled;
	}

	public void setNumZombiesKilled(int numZombiesKilled) {
		this.numZombiesKilled = numZombiesKilled;
	}

	public ArrayList<Zombie> getZombies() {
		return zombies;
	}

	@Override
	public Collection<Zombie> getCollection() {
		return zombies;
	}
}
