package game.sprite.pickup;
import game.Managed;
import game.collision.PlayerToPickupCollision;
import game.info.SpriteInfo;
import game.manager.PickupManager;
import game.sprite.BaseSprite;
import game.sprite.Player;
import game.sprite.SpriteUtils;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public abstract class Pickup extends BaseSprite<SpriteInfo> implements Managed{

	public abstract void onPickup(Player player);
	public abstract PickupType getType();
	public abstract BufferedImage getImage();

	protected PickupType type;
	protected boolean toRemove;
	protected PickupManager owner;

	public Pickup(int w, int h, PickupManager pm){
		super(
			new SpriteInfo(
					SpriteUtils.getRandomX(), 
					SpriteUtils.getRandomY(), w, h)
		);
		
		type = getType();
		toRemove = false;
		this.owner = pm;
	}

	public void checkCollision(Player p){
		new PlayerToPickupCollision(p, this).evaluate();
	}

	public String toString(){
		return getDisplayString();
	}

	public String getDisplayString(){
		return type.toString();
	}
	
	public void update(){
		setRectangle();
	}
	
	public void draw(Graphics g){
		setRectangle();
		g.drawImage(getImage(), info.x, info.y, null);
	}
	public boolean shouldRemove() {
		return toRemove;
	}
	public void setRemove(boolean toRemove) {
		this.toRemove = toRemove;
	}
}
