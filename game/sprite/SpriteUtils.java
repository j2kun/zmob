package game.sprite;

import game.common.GameInfo;

import java.util.Random;

public class SpriteUtils {
	
	private static final Random rand = new Random();
	
   /**
    * 
    * @param sX - x position of the sprite
    * @param sY - y position of the sprite
    * @param x - x position of the target
    * @param y - y position of the target
    * @return double representing the angle of rotation from 0
    */
   public static double getAngle(int sX, int sY, int x, int y){
    //first, get the angle between (x,y) and (sX,sY)
      double angle = 0.0;
      double xDist, yDist;
      
      //set the xdist and ydist between the two points
      xDist = (double)(sX-x);
      yDist = (double)(sY-y);
      
      if(xDist == 0)
         angle = 0.0;
      else
         angle = Math.atan((double)yDist/xDist);
      
      //convert to degrees to make unit circle work
      angle = Math.toDegrees(angle);
      
      if(y<sY) //if in 1st or 2nd quadrant
         angle = (x>sX) ? -angle : 180-angle;
      else if(y>sY)//if in 3rd or 4th quadrant
         angle = (x>sX) ? 360-angle : 180-angle;
      
      //at this point, if the angle is 180 and the y value is not 0
      //then it is either 180 or 90
      if(angle == 180)
         angle = (y>sY) ? 270 : 90;
      
      if(angle == 0 || angle == 360)
         if(x<sX)
            angle = 180;
      
      angle = Math.toRadians(angle);
      return angle;
   }
   

	public static int getRandomX(){
		return rand.nextInt(GameInfo.pW);
	}
	
	public static int getRandomY(){
		return rand.nextInt(GameInfo.pH);
	}
}
