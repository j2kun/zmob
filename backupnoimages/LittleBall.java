package backupnoimages;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;

public class LittleBall {

	double sX = 0, sY = 0, radius = 0;
	BigBall bb;
	boolean up = false, down = false, left = false, right = false;//if the ball is moving
	final int MOVE_DIST = 3;
	double longRadius = 0; //the radius that the little ball revolves on
	
	public LittleBall(BigBall b)
	{
		bb = b;//set the big ball to the little ball
		radius = 10;
		longRadius = (bb.getRadius()/2)+radius/2-1;
		//add or subtract a constant to longRadius to change the size of the ball's orbit
		//to make the ball: orbit along the inside edge of the circle, subtract radius/2
		//					orbit around the outsdie edge of the circle, add (radius/2)-1
		sX = bb.getSX()-(radius/2)+longRadius;//set the position based on circle centers
		sY = bb.getSY()-radius/2;
	}
	
	public void draw(Graphics g, Container container){
		g.setColor(Color.GRAY);
		
		if(up)
			sY-=MOVE_DIST;
		if(down)
			sY+=MOVE_DIST;
		if(left)
			sX-=MOVE_DIST;
		if(right)
			sX+=MOVE_DIST;
		
		g.fillOval((int)sX, (int)sY, (int)radius, (int)radius);//draw the image
	}
	
	//returns middle of the circle
	public double getSX()
	{return sX+(radius/2);}
	
	public double getSY()
	{return sY+(radius/2);}
	
	public void setPosition(double x, double y)
	{
		sX = Math.round((float)x);
		sY = Math.round((float)y);
	}
	
	public double getLongRadius()
	{return longRadius;}
	
	public double getRadius()
	{return radius;}
	
}
