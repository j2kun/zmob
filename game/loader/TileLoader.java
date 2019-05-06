package game.loader;

import game.common.Constants;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.swing.ImageIcon;

public class TileLoader extends Loader{
	
	final int NUM_TILES = 5;
	BufferedImage[] tiles; //can be a0, b1, c2, d3, or e4
	//AffineTransform at;//for rotating images properly
	int tileW; //assume they are square tiles
	int numX, numY;//number of rows and cols to draw
	
	public TileLoader(Dimension d){
		super("Tiles");
		numX = (int)d.getWidth()/tileW;
		numY = (int)d.getHeight()/tileW;
		setFinished(true);
	}
	
	protected void load(){
		String base = Constants.IMAGE_FILENAME_PREFIX + "tile_";
		tiles = new BufferedImage[NUM_TILES];
		
		for(int i = 0; i<NUM_TILES; i++){
			String fnm = base + (char)(i+'a') + ".png";
			
			tiles[i] = getBI(createImage(fnm));
		}
	
		tileW = tiles[0].getHeight();
	}
	
	/** Returns an ImageIcon, or null if the path was invalid. */
	protected Image createImage(String path) {
	    URL imgURL = getClass().getResource(path);
	    if (imgURL != null) {
	        return new ImageIcon(imgURL).getImage();
	    } else {
	        System.err.println("Couldn't find file: " + path);
	        return null;
	    }
	}

	
	//either rotates or flips a tile with affinetransformop
	public BufferedImage arrangeTile(BufferedImage b, double rotate, 
			boolean horizFlip, boolean vertFlip){
		BufferedImage toReturn = b;
		
		//can only do one affine transformation on a single image
		if(vertFlip){
			AffineTransform reflectTransform = 
				new AffineTransform(1, 0, 0, -1, 0, b.getHeight());
			AffineTransformOp reflectOp = 
				new AffineTransformOp(reflectTransform, AffineTransformOp.TYPE_BILINEAR);
			
			toReturn = reflectOp.filter(b, null);
		}
		else if(horizFlip){
			AffineTransform reflectTransform = 
				new AffineTransform(-1, 0, 0, 1, b.getHeight(), 0);
			AffineTransformOp reflectOp = 
				new AffineTransformOp(reflectTransform, null);
			
			toReturn = reflectOp.filter(b, null);
		}
		
		if(rotate!=0){
			AffineTransform rotateTransform = 
				AffineTransform.getRotateInstance(rotate, b.getWidth()/2, b.getHeight()/2);
			AffineTransformOp rotateOp = new AffineTransformOp(rotateTransform, null);
			toReturn = rotateOp.filter(b, null);
		}
		
		return toReturn;
	}
	
	/**	Arrange the tiles according to the following algorithm
	 * if first row do a, b,..., b, a (applying transformations on a)
	 * if last row do a, b,..., b, a (applying transformations on a and b)
	 * else do c, d, ..., d, c (applying transformations on c)*/
	public void draw(Graphics graphics)	{
		Graphics2D g = (Graphics2D)graphics;
		
		//iterate over array of images (1280/128) = 10
		for(int i = 0; i<numY; i++)	{
			for(int j = 0; j<numX; j++)	{
				int curX = j*tileW;
				int curY = i*tileW;
				
				if(i==0){//if row 1
					if(curX==0)
						g.drawImage(tiles[0], curX, curY, null);//draw A tile on first col
					else if(curX == (numX-1)*tileW)
						g.drawImage(arrangeTile(tiles[0], 0, true, false), curX, curY, null);
						//draw A tile flipped on last col
					else
						g.drawImage(tiles[1], curX, curY, null);//draw B tile on cols between
				}else if(i==numY-1){ //if last row
					if(curX==0)
						g.drawImage(arrangeTile(tiles[0], 0, false, true), curX, curY, null);
						//draw A tile flipped on first col
					else if(curX == (numX-1)*tileW)
						g.drawImage(arrangeTile(tiles[0], Math.PI, false, false), curX, curY, null);
						//draw A tile flipped both ways on last col
					else
						g.drawImage(arrangeTile(tiles[1], 0, false, true), curX, curY, null);
						//draw B tile flipped on cols between
				}else{ //for all other rows
					if(curX==0)
						g.drawImage(arrangeTile(tiles[2], 0, false, false), curX, curY, null);
						//draw A tile flipped on first col
					else if(curX == (numX-1)*tileW)
						g.drawImage(arrangeTile(tiles[2], 0, true, false), curX, curY, null);
						//draw A tile flipped both ways on last col
					else
						g.drawImage(tiles[3], curX, curY, null);
					//draw a nothing tile
				}
			}
		}
	}
}
