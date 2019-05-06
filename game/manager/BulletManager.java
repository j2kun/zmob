package game.manager;

import game.bullet.Bullet;
import game.bullet.BulletFactory;
import game.bullet.Explodable;
import game.collision.BulletToZombieCollision;
import game.collision.ExplosionToPlayerCollision;
import game.common.Constants;
import game.common.GameInfo;
import game.sprite.Player;
import game.sprite.Zombie;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collection;

public class BulletManager extends Manager<Bullet>{
	
	private static final BulletFactory bulletFactory = new BulletFactory();
	private ArrayList<Bullet> bullets;
	private Player player;
	private ZombieManager zm;
	
	public BulletManager(Player player, ZombieManager zm){
		this.player = player;
		this.zm = zm;
		bullets = new ArrayList<Bullet>();
	}
	
	public void add(Bullet bullet){
		bullets.add(bullet);
		if(bullet.getType().equals(Constants.POISON_GUN))
			GameInfo.numPoisonBulletsShot++;
	}
	
	public void createAndAdd(String type){
		for(Bullet b : bulletFactory.create(type, player))
			add(b);
	}
	
	public void remove(Bullet bullet){
		bullets.remove(bullet);
	}
	
	public void drawAll(Graphics g){
		for(Bullet bullet : bullets)
			bullet.draw(g);
	}
	
	@Override
	public void updateIndividual(Bullet bullet) {
		bullet.update();
		
		for(Zombie z : zm.getZombies())
			new BulletToZombieCollision(bullet, z, this, zm).evaluate();
		
		if(bullet.isRocketType())
			new ExplosionToPlayerCollision(bullet, player).evaluate();
	}
	
	@Override
	public void update(){
		super.update();
	}
	
	public int size(){
		return bullets.size();
	}
	
	public void explodeAll(){
		for(Bullet b : bullets){
			if(b instanceof Explodable)
				((Explodable)b).setExplosion(true);
		}
	}
	
	public boolean isEmpty(){
		return bullets.isEmpty();
	}

	@Override
	public Collection<Bullet> getCollection() {
		return bullets;
	}
}