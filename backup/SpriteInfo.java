package backup;

public class SpriteInfo {
	
	int sX, sY, width, height, centerX, centerY, 
		cornerX, cornerY, spriteCenterX, spriteCenterY,
		horizDisp, vertDisp, halfD;
	
	public SpriteInfo(int sX, int sY, int width, int height)
	{
		this.sX = sX;
		this.sY = sY;
		this.width = width;
		this.height = height;
		
		halfD = (int)(Math.sqrt(height*height + width*width)/2);
		horizDisp = (int)(halfD);
		vertDisp = (int)(halfD - height/6.0);
	}
	
	public void setPosn(int sX, int sY)
	{
		this.sX = sX;
		this.sY = sY;
	}

	//sX, sY: corner of the sprite image at no rotation
	//sX, sY Corner: corner of the bounding box for the affine transformation
	//sX, sY Center: center of the sprite image relative to width and height
	//Sprite x, y center: sprite's center of gravity (his head)

	public int getSX()
	{return sX;}

	public int getSY()
	{return sY;}
	
	public int getSXCENTER()
	{return getSX()+width/2;}
	
	public int getSYCENTER()
	{return getSY()+height/2-3;}
	
	public int getSXCORNER()
	{return sX-horizDisp;}
	
	public int getSYCORNER()
	{return sY-vertDisp;}
	
	public int getSpriteCenterX()
	{return getSXCENTER()-10;	}
	
	public int getSpriteCenterY()
	{return getSYCENTER();	}
}
