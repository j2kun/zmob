package game.sprite;

import game.common.GameInfo;
import game.info.SpriteInfo;

import java.awt.Rectangle;

public abstract class BaseSprite<E extends SpriteInfo> {
	
	public E info;
	protected Rectangle rectContainingImage;
	
	public BaseSprite(){}
	
	public BaseSprite(E info){
		this.info = info;
	}
	
	public void checkBoundaries(){
		if(info.x <= 0)
			info.x = 0;
		else if (info.x >= GameInfo.pW - info.width)
			info.x = GameInfo.pW - info.width;
		if (info.y <= 0)
			info.y = 0;
		else if(info.y >= GameInfo.pH - info.height)
			info.y = GameInfo.pH - info.height;
	}
	
	public Rectangle getRectangle(){
		return rectContainingImage;
	}
	
	public void setRectangle(){
		rectContainingImage = new Rectangle(info.x, info.y, info.width, info.height);
	}
	
	public void setRectangle(int x, int y, int width, int height){
		rectContainingImage = new Rectangle(x, y, width, height);
	}
	
	public E getInfo(){
		return info;
	}
	
	
}
