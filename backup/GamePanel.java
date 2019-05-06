package backup;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

public class GamePanel extends JPanel implements Runnable {

	static final long serialVersionUID = 2764728522400011394L;
	IZombie zombieClass;
	
	int pW, pH, frame;   // dimensions of the panel
	int mouseX, mouseY;
	// some booleans
	volatile boolean gameRunning = false;
	volatile boolean isPaused = false;
	volatile boolean gameOver;
	volatile boolean godMode = false;   
	// used to stop the animation thread
	
	boolean aKey = false; 
	boolean sKey = false;
	boolean dKey = false;
	boolean wKey = false;
	boolean running = false;
	boolean automatic = false;
	boolean mousePressed = false;
	boolean hasGameStarted = false;
	
	final int INITIAL_NUMZOMBIES = 1;
	final int EASY = 10;
	final int NORMAL = 20;
	final int HARD = 30;
	final int HEALTH = 0; 
	final int AMMO = 1;
	final int NUM_OF_WEAPONS = 7;
	
	final int PISTOL = 0;
	final int MACHINE_GUN = 1;
	final int SHOTGUN = 2;
	final int WAVE_BEAM = 3;
	final int ROCKET_LAUNCHER = 4;
	final int CHAIN_GUN = 5;
	final int POISON_GUN = 6;
	//range indecies for each gun correspond to the integers above
	final int[] ranges = {300,400,200,500,660,720, 720};
	
	final String[] difficultyStrings = new String[]{"Easy", "Normal", "Hard"};
	
	int currentGun = 0; 
	int currentDifficulty = EASY;
	
	Cursor theCursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
	
	ArrayList<ZombieSprites> zombieHorde;
	ArrayList<Pickup> pickups;
	ArrayList<Bullet> bullets;
	
	int numZombiesKilled = 0, numWavesSurvived = 0, score = 0;
	
	//boolean and integer for the test gun
	boolean shootingPoisonGun = false;
	final int MAX_NUMBER_OF_TEST_BULLETS = 5;
	int numOfTestBullets = 0;

	
	//string and boolean used to display info on each pickup
	String pickupInfo = "";
	boolean displayPickupInfo = false;
	int pickupDisplayFrame = 0;
	int numZombiesKilledForPickup = 0;
	boolean bossExists = false;
	boolean stopZombies = false;
	
	Random rand = new Random();
	
	Player player = null;//sprite body
	Health life;
	Ammo ammo;
	HUD hud;
	TileLoader tl;
	SpriteLoader l1;
	//ranges for different gun types; array index corresponds with currentGun value
	
	Container container;
	Graphics dbg; 
	Image dbImage = null;
	int mouseDistX = 0, mouseDistY = 0;
	//dist between mouse and big ball	
	
	double newX, newY;
	double mouseAngle = 0;
	//angle between mouse and ball against the horizontal 0 line
	
	final double CONVERT_TO_DEGREES = Math.PI/180;
	
	volatile Thread animator;           // the thread that performs the animation
	final int DELAY = 30;	// used in thread sleep to slow down game piece movement
	
	final String[] instructions = new String[]{"Paused! Press ESC to quit.",
			"",
			"Welcome to Zmob!",
			"",
			"Instructions:",
			"Use w-s-a-d to move up, down, left, and right (respectively)", 
			"Click the mouse to shoot. Scroll the mouse wheel to switch weapons. (or use #1-7)",
			"",
			"1 = Pistol: unlimited ammo",
			"2 = Machine Gun: hold the mouse to fire automatic",
			"3 = Shotgun: sprays five shots",
			"4 = Wave Beam: hold the mouse to fire a stream",
			"5 = Rocket Launcher: press E to explode the rockets manually",
			"6 = Chain Gun: Slower than rockets, but creates a chain reaction!",
			"7 = Poison Gun: Does little damage, but poisons zombies",
			"",
			"The various weapons have different ranges (shown by the blue circle).",
			"Hold shift to run, or press ctrl to toggle run. (watch the green stamina bar!)",
			"",
			"The red crosses give you +25 health, and the yellow boxes are ammunition crates.",
			"Preference is given to weapons with no ammo left. Pickups are spawned as you ",
			"kill zombies, with harder difficulties requiring more frags to get pickups.",
			"",
			"Use F5-F7 to change difficulty levels. (F5 = easy, F6 = normal, F7 = hard)",
			"Zombies spawn faster and are more numerous on harder difficulties.",
			"Whatever you do, don't press F9 during the game.",
			"",
			"Choose a Difficulty Level to begin!",
			"Good luck!"};
	
	//constructor
	public GamePanel(IZombie aT, int width, int height) {
		pW = width;
		pH = height;
		zombieClass = aT;
		
		setBackground(Color.white);
		setPreferredSize(new Dimension(pW, pH));
		
		frame = 0;
		
		setCursor(theCursor);
		setFocusable(true);
		requestFocus();    // the JPanel now has focus, so receives key events
		readyForTermination();

		tl = new TileLoader(new Dimension(pW, pH));
		l1 = new SpriteLoader();
		
		// create game components
		gameOver = false;
		setContainer(this);
		
		player = new Player(pW, pH, l1.playerW, l1.playerH);
		life = new Health(player, pW, pH);
		ammo = new Ammo(player, pW, pH);
		hud = new HUD(player, ammo, life, pW, pH);
		
		currentGun = PISTOL;//initialize default gun
		player.setRange(ranges[currentGun]);//initialize range
		
		addMouseListener(new MyMouseListener());
    	addMouseMotionListener(new MyMouseListener());
    	addMouseWheelListener(new MyMouseListener());
    	
    	addKeyListener( new KeyAdapter() {
			public void keyPressed(KeyEvent e) 
			{ processKeyPress(e);}
			public void keyReleased(KeyEvent e)
			{ processKeyRelease(e);	}
		});	
		
    	bullets = new ArrayList<Bullet>();
    	pickups = new ArrayList<Pickup>();
    	zombieHorde = new ArrayList<ZombieSprites>();
    	setInitialZombieList(); // add zombies to list using int NUMZOMBIES
    	
    	isPaused = true;//start game paused so user can begin after pressing P
    	hasGameStarted = false;
	}
	
	
	
	//mouse moves
	class MyMouseListener extends MouseInputAdapter implements MouseWheelListener
	{
		public void mouseMoved(MouseEvent e) 
   		{   			
   			mouseX = e.getX();
   			mouseY = e.getY();
   			//set the mouse X and Y so if the mouse doesnt continue to move
   			//you still have the coordinates of the mouse to work with
   		}
		
		public void mouseDragged(MouseEvent e) 
   		{   			
   			mouseX = e.getX();
   			mouseY = e.getY();
   		}
		
		public void mousePressed(MouseEvent e)
		{			
			if (!isPaused) 
			{//if game is not paused
				if (ammo.isAmmoLeft(currentGun)) 
				{
					if (!automatic)
						ammo.subtractAmmo(currentGun);//subtract an ammo
					//if automatic, ammo is subtracted in the gameUpdate method
					
					//new bullet
					if (currentGun == SHOTGUN)
						newShotgunBullet();
					else if(currentGun == WAVE_BEAM)
						newWaveBeamBullet();
					else if(currentGun == POISON_GUN)
						newPoisonGunBullet();
					else 
						newBullet(currentGun);

					mousePressed = true;
					//while there is no ammo left in the current gun
					while (!ammo.isAmmoLeft(currentGun) && !shootingPoisonGun)
						switchWeapons(--currentGun);//switch weapons down
				}
			}						
		}

//		mouse wheel changes weapons
		public void mouseWheelMoved(MouseWheelEvent e)
		{
			if (!isPaused) 
			{
				int notches = e.getWheelRotation();
				
				if (notches < 0)//mouse wheel moves up
					if (currentGun == NUM_OF_WEAPONS - 1)
						switchWeapons(0);
					else
						switchWeapons(++currentGun);
				else//mouse wheel moves down
					if (currentGun <= 0)
						switchWeapons(NUM_OF_WEAPONS - 1);
					else
						switchWeapons(--currentGun);
				
				ammo.changeGun(currentGun);
			}			
		}
	
		public void mouseReleased(MouseEvent e)
		{mousePressed = false;}
   	}
	
	//method ailgns the little ball to the big ball in reference to the position
	//of the mouse. is called every frame, even if mouse is not moved
	public void alignLittleBall()
	{
		//get the distance between the X variable and the X of the big ball
		int playerX = player.si.getSpriteCenterX();
		int playerY = player.si.getSpriteCenterY();
		
		mouseDistX = mouseX-playerX;
		mouseDistY = mouseY-playerY;
		
		//get the angle
		mouseAngle = (mouseDistX == 0) ? 0 : Math.atan((double)mouseDistY/mouseDistX);
		
		//convert to degrees to make unit circle work
		mouseAngle/=CONVERT_TO_DEGREES;
		
		//arctan only gives values -pi/2 < x < pi/2
		//following code gets angles to work as a normal unit cicle
		if(mouseY<playerY) //if in 1st or 2nd quadrant
			mouseAngle = (mouseX>playerX) ? -mouseAngle : 180-mouseAngle;
		else if(mouseY>playerY)//if in 3rd or 4th quadrant
			mouseAngle = (mouseX>playerX) ? 360-mouseAngle : 180-mouseAngle;
		
		//at this point, if the angle is 180 and the y value is not 0
		//then it is either 180 or 90
		if(mouseAngle == 180)
			mouseAngle = (mouseY>playerY) ? 270 : 90;
		//after this, if the mouse angle is 0 and the mousex value is less than
		//the playerx value, then the angle is 180
		if(mouseAngle == 0 || mouseAngle == 360)
			if(mouseX<playerX)
				mouseAngle = 180;
		//end making unit circle
		
		//System.out.println(mouseY-player.sY + "   " + mouseAngle);
		mouseAngle *= CONVERT_TO_DEGREES; //convert back to radians
		
		int dx = player.getRadius()/2;
		int dy = dx;
		//add the cosines and subtract the sines to set the little ball
		//around the big ball
		newX = playerX+Math.cos(mouseAngle)*dx;
		newY = playerY-Math.sin(mouseAngle)*dy;
	}
	
	public void togglePause()
	{
		isPaused = !isPaused;
		if(isPaused)
		{
			player.stopSprite();
			wKey = false;
			sKey = false;
			aKey = false;
			dKey = false;
		}
	}
	//key listener events for moving the big ball
	//and moving the litle ball in unison with the big ball
	//changes variables within the classes to make moving smooth
	
	protected void processKeyPress(KeyEvent e) {
		
		int keyCode = e.getKeyCode();
		
		if (hasGameStarted)
			if (keyCode == KeyEvent.VK_P)
				togglePause();
		
		if (!hasGameStarted && isPaused) {
			if (keyCode == KeyEvent.VK_F5) 
			{
				changeDifficulty(EASY);
				hasGameStarted = true;
				isPaused = false;
			}
			if (keyCode == KeyEvent.VK_F6) 
			{
				changeDifficulty(NORMAL);
				hasGameStarted = true;
				isPaused = false;
			}
			if (keyCode == KeyEvent.VK_F7) 
			{
				changeDifficulty(HARD);
				hasGameStarted = true;
				isPaused = false;				
			}
		}
		
		//Restart
		if(keyCode == KeyEvent.VK_F8)
			restart();
		
		
		keyCode = -1;
		if (!isPaused) {
			keyCode = e.getKeyCode();
		}	
		
		//movement
		if (keyCode == KeyEvent.VK_W)
			{wKey = true;}
		if (keyCode == KeyEvent.VK_S)
			{sKey = true;}
		if (keyCode == KeyEvent.VK_A)
			{aKey = true;}
		if (keyCode == KeyEvent.VK_D)
			{dKey = true;}
		
		//weapon changing
		if (keyCode == KeyEvent.VK_1)
			switchWeapons(PISTOL);
		else if (keyCode == KeyEvent.VK_2)
			switchWeapons(MACHINE_GUN);
		else if (keyCode == KeyEvent.VK_4)
			switchWeapons(WAVE_BEAM);
		else if (keyCode == KeyEvent.VK_3)
			switchWeapons(SHOTGUN);
		else if (keyCode == KeyEvent.VK_5)
			switchWeapons(ROCKET_LAUNCHER);
		else if (keyCode == KeyEvent.VK_6)
			switchWeapons(CHAIN_GUN);
		else if (keyCode == KeyEvent.VK_7)
			switchWeapons(POISON_GUN);
		
		ammo.changeGun(currentGun);//change ammo for each weapon change
		
		if(keyCode == KeyEvent.VK_E)
		{
			for(int i = 0; i<bullets.size(); i++) 
			{ // bullets.size is how many bullets 
				Bullet temp = bullets.get(i); // gets the current rocket
				if(temp.getType() == ROCKET_LAUNCHER || temp.getType() == CHAIN_GUN)
					if(!temp.isExplosion())
						temp.setExplosion(true);
			}
		}
		
		//toggle run
		if(keyCode == KeyEvent.VK_CONTROL)
			running = !running;
		
		//hold run
		if(keyCode == KeyEvent.VK_SHIFT)
			running = true;
		
		//******BEGIN DEVELOPERS' HACKS********\\
		if (keyCode == KeyEvent.VK_F1)
			newZombie();
		if (keyCode == KeyEvent.VK_F2)
			{godMode  = !godMode;}
		if (keyCode == KeyEvent.VK_F3)
		{
			pickups.add(new Pickup(AMMO, pW, pH));
			pickups.add(new Pickup(HEALTH, pW, pH));			
		}
		//add a boss zombie
		/*if (keyCode == KeyEvent.VK_F4)
		{
			newCustomZombie(80, 50, 50, 6, 10, 10000);
			bossExists = true;
		}*/
		
		if(keyCode == KeyEvent.VK_Z)
			life.subtractHealth(1);
		if(keyCode == KeyEvent.VK_X)
			life.addHealth(1);
		
		if(keyCode == KeyEvent.VK_F10)
			stopZombies = !stopZombies;
		
		//send out a zmob wave
		if (keyCode == KeyEvent.VK_F9)
		{
			for(int i = 0; i<25; i++)
				newZombie();
		}
		//******END DEVELOPERS' HACKS*******\\
		
		
		if (!isPaused) {
			if (wKey)
				player.up = true;
			if (sKey) 
				player.down = true;
			if (aKey) 
				player.left = true;
			if (dKey) 
				player.right = true;
		}		
	}
	
	protected void processKeyRelease(KeyEvent e) {
		
		int keyCode = -1;
		if (!isPaused) {
			keyCode = e.getKeyCode();
		}
		
		//wsad for updownleftright movement
		if (keyCode == KeyEvent.VK_W)
			{wKey = false;}
		if (keyCode == KeyEvent.VK_S)
			{sKey = false;}
		if (keyCode == KeyEvent.VK_A)
			{aKey = false;}
		if (keyCode == KeyEvent.VK_D)
			{dKey = false;}
		
		//	hold run
		if(keyCode == KeyEvent.VK_SHIFT)
			running = false;
		
		if(!wKey)
			player.up = false;
		if(!sKey)
			player.down = false;
		if(!aKey)
			player.left = false;
		if(!dKey)
			player.right = false;
	}
	
	//**************COLLISION ALGORITHMS*************//
	public void checkBulletCollision(Bullet b, int index)
	{
		for (int x = 0; x < zombieHorde.size(); x++) 
		{//for all bullets
			ZombieSprites z = zombieHorde.get(x);//create temp inst of bullet
			boolean hit = false;
			if (b.getRectangle().intersects(z.getRectangle()))//if they intersect 
			{
				//deal with the zombie
				hit = true;
				z.subtractHealth(b.getStrength());
				// current zombie health minus strength of weapon
				
				//if explosion from chain gun hits a zombie, start another explosion
				if(b.getType() == CHAIN_GUN)
				{
					if(b.isExplosion())
					{
						double zX = z.si.getSpriteCenterX(); 
						double zY = z.si.getSpriteCenterY();
						
						Bullet newBullet = new ChainBullet((int)zX, (int)zY, 0,pW, pH);
						//create new explosion where the killed zombie was
						newBullet.setExplosion(true);
						bullets.add(newBullet);
					}
				}
				
				if(b.getType()==POISON_GUN && !z.isBoss())
					z.setPoisoned(true);//poison zombies
				
				if (z.isDead()) 
				{ //if the health is <=0
					zombieHorde.remove(z);//remove zombie
					if(z.isBoss())//if a boss is killed
					{
						bossExists = false;//change settings
						score += Math.pow(currentDifficulty, 2)/2;//add bonus score
					}
					numZombiesKilled++;//add count
					score += (Math.pow(currentDifficulty, 2)/10);//add score
					if(numZombiesKilled%(10.0/currentDifficulty*60) == 0)//makes smaller waves for harder difficulty 
						newWave();//check waves
				}
			}
			
			if (hit) 
			{ //	now deal with the bullet
				if (b.getType() != POISON_GUN)//do not remove poison bullets at all
				{
					if (b.getType() != ROCKET_LAUNCHER && b.getType() != CHAIN_GUN)
					{
						try{
							bullets.remove(index);//if a non-rocket, remove the bullet
						} catch(IndexOutOfBoundsException e){
							bullets.clear();
						}
					}
					else if (!b.isExplosion()) //if a rocket, explode it if its not already exploding
						b.setExplosion(true);
				}				
			}
		}//end bullet for	
	}
	
	public void checkPlayerCollision(ZombieSprites z)
	{
		if (player.getRectangle().intersects(z.getRectangle()) && !godMode)//if player intersects zombie 
			life.subtractHealth(z.getPower());//minus a certain amt of health

		z.checkIntersectionWithBB(player);
	}
	
	public void checkPickupCollision()
	{
		for (int y = 0; y < pickups.size(); y++) 
		{
			Pickup tP = pickups.get(y);
			//create temp instance
			if (player.getRectangle().intersects(tP.getRectangle()))
				//if player intersects pickup
				executePickup(tP.getType(), y);//minus 1 health
		}
	}
	
	public void checkZombieToZombieCollision(ZombieSprites z){
		for (int y = 0; y < zombieHorde.size(); y++){
				z.checkOverlap(zombieHorde.get(y));
		}
	}
	
	//check to dispose if a bullet goes out of range or off the screen, not for hitting zombies
	//checks to dispose a bullet if it has gone out of it's predetermined range
	public boolean checkBulletDispose(Bullet tB, int range)
	{
		double x1 = tB.getStartingSX(), x2 = tB.getSX(), y1 = tB.getStartingSY(), y2 = tB.getSY();
		double hypotenuse = Math.sqrt( Math.pow(x2-x1, 2) + Math.pow(y2-y1, 2) );
		
		if(hypotenuse>=range)//if outside allocated range
			if((tB.getType() == ROCKET_LAUNCHER || tB.getType() == CHAIN_GUN))
			{	
				if(!tB.isExplosion())
					tB.setExplosion(true);//if out of range, explode, will be removed later
			}
			else
				return true; //remove all other bullets
		
		return false;
	}
	
	
	//***************MISC ALGORITHS**********************\\
	//creates a new zombie randomly within the box 0,0,pW,pH
	public void newZombie()
	{
		zombieHorde.add(new ZombieSprites(pW, pH, l1.getZombieW(), l1.getZombieH()));
	}
	/*public void newCustomZombie(int r, int x, int y, int speed, int power, int health)
	{
		zombieHorde.add(new ZombieSprites(pW, pH, r, x, y, speed, power, health));
	}*/
	//initialize zombies for the first time
	
	public void newHealthPickup()
	{
		Pickup t = new Pickup(HEALTH, pW-30, pH-30);
		t.setRectangle(l1.getHealthImage());
		pickups.add(t);
	}
	
	public void newAmmoPickup()
	{
		Pickup t = new Pickup(AMMO, pW-30, pH-30);
		t.setRectangle(l1.getAmmoImage());
		pickups.add(t);
	}
	
	private void setInitialZombieList() {
		for (int x = 0; x < INITIAL_NUMZOMBIES; x++)
			newZombie();
	}
	
	public void switchWeapons(int type)
	{
		if (!isPaused) 
		{
			currentGun = type;
			player.setRange(ranges[currentGun]);
			
			/*if(currentGun == SHOTGUN || currentGun == ROCKET_LAUNCHER || currentGun == CHAIN_GUN)
				player.setCurrentImage(2);
			else if (currentGun == WAVE_BEAM)
				player.setCurrentImage(3);
			else
				player.setCurrentImage(1);*/
			
			ammo.changeGun(type);
			if (currentGun == MACHINE_GUN || currentGun == WAVE_BEAM)
				automatic = true;
			else
				automatic = false;
		}		
	}
	
	public void executePickup(int type, int index)
	{
		
		if(type == HEALTH)
		{
			if(!life.isFull())
			{
				int recovery = new Pickup().RECOVERY;
				//create temp just to get recovery constant from class
				life.addHealth(recovery);
				pickupInfo = "+" + String.valueOf(recovery) + " Health";//setup display
				displayPickupInfo = true;//works with gameRender to display the String
				pickups.remove(index);//remove pickup
			}
		}
		else if(type == AMMO)
		{
			if (!ammo.isAllFull()) //if one is less than full
			{
				int tempInt = rand.nextInt(NUM_OF_WEAPONS-1)+1;//choose random weapon NOT PISTOL
				
				//deal with ammo "preference" (gives ammo to guns with 0 ammo first)
				while((ammo.getAmmo(tempInt) != 0 && ammo.isOneGunEmpty()) || ammo.isFull(tempInt))
				{tempInt = rand.nextInt(NUM_OF_WEAPONS);}//get another random index
				
				int ammoToAdd = (int)(.3*ammo.getMaxAmmo(tempInt));
				
				if(ammoToAdd==0)
					ammoToAdd = 1;//for the chain gun
				
				pickupInfo = "+" + ammoToAdd + " " + ammo.getWeaponString(tempInt);
				displayPickupInfo = true;//works with gameRender to display the String
				ammo.addAmmo(ammoToAdd, tempInt);
				//adds 30% of max ammo
				pickups.remove(index);//remove pickup
			}			
		}
	}
	
	//algorithm to change difficulty and remove zombies until the num zombies
	//in play are no greater than the maximum number of zombies for that difficulty
	public void changeDifficulty(int level)
	{
		if(level == EASY)//if requested level is easy
		{				//and he current difficulty + # zombies > EASY (10)
			if(currentDifficulty > EASY && zombieHorde.size()>EASY)
			{
				int i = zombieHorde.size()-1;
				
				while(zombieHorde.size()>=EASY)
				{
					//then remove until the arraylist is the size it should be
					zombieHorde.remove(i);
					i--;
				}//end while
			}//end if
		}//end if
		else if(level == NORMAL)//do the same for the normal level
		{
			if(currentDifficulty > NORMAL && zombieHorde.size()>NORMAL)
			{
				int i = zombieHorde.size()-1;
				
				while(zombieHorde.size()>=NORMAL)
				{
					//then remove until the array
					zombieHorde.remove(i);
					i--;
				}
			}
		}
		//do nothing if hard
		
		//finally, change difficulty
		currentDifficulty = level;
		//reset the #zombs killed to get pickups
		numZombiesKilledForPickup = numZombiesKilled + currentDifficulty/2;
	}
	
	public void newWave()
	{
		numWavesSurvived++;
		score += currentDifficulty*50;
	}
	
	//***************************NEW BULLET METHODS**********************\\
	
	public void newBullet(int type) 
	{
		//process single-shot guns
		Bullet tempB;
		
		if(type == PISTOL)
			tempB = new PistolBullet((int)newX, (int)newY, mouseAngle, pW, pH);
		else if (type == MACHINE_GUN)
			tempB = new MGBullet((int)newX, (int)newY, mouseAngle, pW, pH);
		else if(type == ROCKET_LAUNCHER)
			tempB = new RocketBullet((int)newX, (int)newY,mouseAngle, pW, pH);
		else //(type == CHAIN_GUN)
			tempB = new ChainBullet((int)newX, (int)newY, mouseAngle, pW, pH);
				
		bullets.add(tempB);
		
	}
	
	public void newWaveBeamBullet()
	{
		boolean x = false;//boolean controls whether to make the big or small shot
		// in the for loop

		for (int i = 0; i < 2; i++) 
		{
			bullets.add(new WavebeamBullet((int)newX, (int)newY, mouseAngle, pW, pH, x));
			x = !x;
		}
	}
	
	public void newShotgunBullet()
	{
		for (int i = 0; i < 5; i++)
			bullets.add(new ShotgunBullet((int)newX, (int)newY, mouseAngle ,pW, pH, i));
	}
	
	public void newPoisonGunBullet()
	{
		shootingPoisonGun = true;
		bullets.add(new PoisonBullet((int)newX, (int)newY, mouseAngle, pW, pH));
		
		numOfTestBullets++;
		//if shooting is done reset everything
		if(numOfTestBullets>=MAX_NUMBER_OF_TEST_BULLETS)
		{
			shootingPoisonGun = false;
			numOfTestBullets = 0;
			//if out of ammo
			if(!ammo.isAmmoLeft(POISON_GUN))
				switchWeapons(--currentGun);//switch weapons down
		}
	}
	
	//****************************RUNTIME METHODS*************************\\	
			///////////////////////////////////////////////////////
	
	public void run() {
		//long gameStartTime = System.currentTimeMillis();
		gameRunning = true;	
		
		while(gameRunning) {
			gameUpdate(); 	// move the pieces 
			gameRender();   // render the game to a buffer
			paintScreen();  // draw the buffer on-screen
			frame++;
			try {	Thread.sleep(DELAY);} catch (InterruptedException e) {}
		}
	}
	
	private void gameUpdate(){ 
		
		if (!isPaused && !gameOver && hasGameStarted)
		{			
			alignLittleBall();//call this method to position little ball every frame
			
			//update automatic bullets and test bullets
			if(shootingPoisonGun)
				newPoisonGunBullet();
			
			if(mousePressed && automatic)//if automatic and shooting
			{
				if(ammo.isAmmoLeft(currentGun))
					{
						if(currentGun==WAVE_BEAM)
							newWaveBeamBullet();
						else
						bullets.add(new MGBullet((int)newX, (int)newY, mouseAngle, pW, pH));
						//add a new bullet each frame
						
						ammo.subtractAmmo(currentGun);//subtract ammo
						
						//while there is no ammo left in the current gun
						while (!ammo.isAmmoLeft(currentGun))
							switchWeapons(--currentGun);//switch weapons down
					}
			}
			
			//update running
			player.setRunning(running);
			
			//update godMode
			if(godMode)
			{
				life.setHealth(100);
				ammo.resetAmmo();
				player.addStamina(100);
			}
			
			//udpate zombies
			for (int x = 0; x < zombieHorde.size(); x++) 
			{
				ZombieSprites tempZ = zombieHorde.get(x);
				tempZ.decideMove(player.si.getSpriteCenterX(),player.si.getSpriteCenterY());
				//if zombie is poisoned, remove health every frame
				if(tempZ.isPoisoned() && frame%2==0)
					tempZ.subtractHealth(1);
				
				
				checkPlayerCollision(tempZ);//checks player for any collisions with zombies
				checkZombieToZombieCollision(tempZ);//checks for zombie to zombie collision
			}
			
			if (!bullets.isEmpty()) {
				for(int i = 0; i<bullets.size(); i++)
					checkBulletCollision(bullets.get(i), i);
			}				
			
			//if there is no boss zombie alive
			if (!bossExists && !stopZombies) {
				//spawn zombies until there are a certain number
				//10/x*30 gives  makes 10 into 30, and 30 into 10
				//maximum number of zombies increases with every wave you survive
				if (frame % ((double) 10 / currentDifficulty * 30) == 0
						&& zombieHorde.size() < currentDifficulty
								+ (5 * numWavesSurvived))
					newZombie();
			}			
			//systematically spawn ammo and health
			if(numZombiesKilled-numZombiesKilledForPickup >= 0 && pickups.size()<40)
			{
				newAmmoPickup();
				newHealthPickup();
				numZombiesKilledForPickup += currentDifficulty;
				//System.out.println("num needed: " + numZombiesKilledForPickup);
			}
			
			//	update bullets
			if(!bullets.isEmpty()) // if there is a bullet
			{
				for(int i = 0; i<bullets.size(); i++) 
				{ // bullets.size is how many bullets 
					boolean removed = false;
					Bullet temp = bullets.get(i); // gets the current bullet
					temp.move(); // move whether to be removed or not
					//temp.checkRemove();//checks if outside the panel
					
					if(checkBulletDispose(temp, temp.getRange()))//if the bullet should be removed
					{
						if (temp.getType() != ROCKET_LAUNCHER && temp.getType() != CHAIN_GUN)
						{
							bullets.remove(i);//if a non-rocket, remove the bullet
							removed = true;
						}
						else if(!temp.isExplosion()) //if a rocket, explode it if its not already exploding
							temp.setExplosion(true);
					}
					
					if (temp.checkRemove() && removed == false) 
						bullets.remove(i);
					
					//check to see if explosion hits player, which then causes damage
					if(temp.getType() == ROCKET_LAUNCHER)
						if(player.getRectangle().intersects(temp.getRectangle()))
							life.subtractHealth(1.0);
				}
			}
			
			if (!pickups.isEmpty()) {
				checkPickupCollision();//checks for pickups
			}
			
			if(life.getHealth()<=0)
				gameOver = true;
		}	
	}  // end of gameUpdate

	private void gameRender()	  {
		if (dbImage == null){ // must create the Image and get its Graphics 
			dbImage = createImage(pW, pH); // image is size of panel
			dbg = dbImage.getGraphics(); // all drawing done to this graphics
	    }
		
	    // redraw the background
		tl.draw(dbg);
	    dbg.setFont(new Font("SansSerif", Font.BOLD, 15));
	    
		if (!pickups.isEmpty())
		{
			for (int i = 0; i < pickups.size(); i++)
			{
				Pickup tempP = pickups.get(i);
				if(tempP.getType()==HEALTH)
					tempP.draw(dbg, l1.getHealthImage());
				else
					tempP.draw(dbg, l1.getAmmoImage());
			}
		}
		
	    // draw the player sprite
		BufferedImage bi;
		
		if(currentGun == SHOTGUN || currentGun == ROCKET_LAUNCHER || currentGun == CHAIN_GUN)
			bi = l1.getShottyImage();
		else if (currentGun == WAVE_BEAM)
			bi = l1.getWavebeamImage();
		else
			bi = l1.getPistolImage();
		
		player.draw(dbg, isPaused, bi, l1.getPlayerTranslator(), l1.getPlayerRotator(mouseAngle));
		
		
		//draw the HUD
	    hud.draw(dbg, numZombiesKilled, numWavesSurvived, score, 
	    		difficultyStrings[(currentDifficulty/10)-1]);//draw the HUD
	    
	    if(isPaused)
	    	displayInstructions(dbg);
	    
	    //draw zombies
	    for (int x = 0; x < zombieHorde.size(); x++)
	    {
	    	ZombieSprites temp = zombieHorde.get(x);
	    	BufferedImage ci;
	    	
	    	if(temp.isPoisoned)
	    		ci = l1.getPoisonedZombieImage();
	    	else
	    		ci = l1.getZombieImage();
	    	
	    	temp.draw(dbg, ci, l1.getZombieTranslator(), l1.getZombieRotator(temp.getAngle()));
	    }
	    
	    //draw bullets
	    if(!bullets.isEmpty()) // if there is a bullet
			for(int i = 0; i<bullets.size(); i++)
				bullets.get(i).draw(dbg, container); //draws each bullet
	    
	    //display pickup information
	    if(displayPickupInfo)
	    {
	    	pickupDisplayFrame++;
	    	dbg.setColor(Color.WHITE);
	    	dbg.drawString(pickupInfo, pW/2, pH-15);
	    	if(pickupDisplayFrame == 50)
	    	{//info goes away after a few seconds
	    		pickupDisplayFrame = 0;
	    		displayPickupInfo = false;
	    	}
	    }
	    
	    if (gameOver)
	      gameOver(dbg);
	}  // end of gameRender()
	
	private void paintScreen() {
		// use active rendering to put the buffered image on-screen
	    try {
	    	Graphics g = this.getGraphics(); // panel 
	    	if ((g != null) && (dbImage != null))
	    		g.drawImage(dbImage, 0, 0, null);
	    	g.dispose();
	    }   catch (Exception e)  { }//System.out.println("Error painting the screen!");}
	}
	
	public void displayInstructions(Graphics g)
	{
		g.setColor(Color.WHITE);
		
		for(int i = 0; i<instructions.length; i++)
			g.drawString(instructions[i], 50, 65+i*17);
	}
	
	public void addNotify() {
		// only start the animation once the JPanel has been added to the JFrame
	    super.addNotify();   // creates the peer
	    //System.out.print("yes add notify");
	    startGame();    // start the thread
	}

	private void startGame(){
		// initialise and start the thread 
		if (animator == null || !gameRunning) {
			animator = new Thread(this);
			animator.start();
	    }
	}
	
	private void gameOver(Graphics g)
	{
		String msg = "Game Over. ";
		g.setColor(Color.red);
		g.drawString(msg, pW/2-50, pH/2-50);
		
		msg = "Press esc to exit, F8 to restart.";
		g.drawString(msg, pW/2-50, pH/2-25);
		
		gameRunning = false;
	}  // end of gameOverMessage()

	public void restart(){
		animator = null;
		
		zombieHorde.clear();
		bullets.clear();
		pickups.clear();
		
		zombieClass.restart(zombieClass);
	}
	
	private void readyForTermination() {
		addKeyListener( new KeyAdapter() {
		// listen for esc, q, end, ctrl-c on the canvas to
		// allow a convenient exit from the full screen configuration
		public void keyPressed(KeyEvent e){ 
			int keyCode = e.getKeyCode();
			if (keyCode == KeyEvent.VK_ESCAPE) {
				gameRunning = false;
				zombieClass.dispose();
			}
		}});
	}// end of readyForTermination()
	
	private void setContainer(Container c)
	{container = c;}
	
}