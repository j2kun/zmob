package game.collision;

import game.info.SpriteInfo;
import game.sprite.BaseSprite;

public abstract class Collision<L extends BaseSprite<? extends SpriteInfo>,
		R extends BaseSprite<? extends SpriteInfo>> {
	
	public abstract void executeOnCollision();
	
	protected L left;
	protected R right;
	
	public Collision(L left, R right){
		this.left = left;
		this.right = right;
	}
	
	public void evaluate(){
		if(left.getRectangle().intersects(right.getRectangle()))
			executeOnCollision();
	}
}
