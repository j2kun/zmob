package game.info;

public class RotatingSpriteInfo extends SpriteInfo{

	public int horizDisp, vertDisp, halfD;

	public RotatingSpriteInfo(int sX, int sY, int width, int height)
	{
		super(sX, sY, width, height);

		halfD = (int)(Math.sqrt(height*height + width*width)/2);
		horizDisp = (int)(halfD);
		vertDisp = (int)(halfD - height/6.0);
	}

	public void setPosn(int sX, int sY)	{
		this.x = sX;
		this.y = sY;
	}

//	sX, sY: corner of the sprite image at no rotation
//	sX-, sYCorner: corner of the bounding box for the affine transformation
//	sX-, sYCenter: center of the sprite image relative to width and height
//	SpriteCenterX, -Y: sprite's center of gravity (his head)

	public int getCenterX()
	{return x+width/2;}

	public int getCenterY()
	{return y+height/2-3;}

	public int getCornerX()
	{return x-horizDisp;}

	public int getCornerY()
	{return y-vertDisp;}

	public int getSpriteCenterX()
	{return getCenterX()-10;	}

	public int getSpriteCenterY()
	{return getCenterY();	}
}
