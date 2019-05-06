package backupnoimages;
import java.awt.*;
import java.util.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;
import java.awt.event.MouseEvent;

public class GamePanel extends JPanel implements Runnable {

	private static final long serialVersionUID = 2764728522400011394L;
	private int pW, pH, frame;   // dimensions of the panel
	// some booleans
	private volatile boolean gameRunning = false, isPaused = false, gameOver, godMode = false;   
	// used to stop the animation thread
	
	private boolean aKey = false, sKey = false, dKey = false, wKey = false, running = false;
	private boolean automatic = false, mousePressed = false;
	private int currentGun = 0;
	private final int NUM_OF_WEAPONS = 7;
	private final int PISTOL = 0, MACHINE_GUN = 1, SHOTGUN = 2, 
			WAVE_BEAM = 3, ROCKET_LAUNCHER = 4, CHAIN_GUN = 5, POISON_GUN = 6;
	//ranges for each gun correspond to the integers above
	int ranges[] = {300,400,200,500,660,720, 720};
	
	private final int EASY = 10, NORMAL = 20, HARD = 30;
	private final String[] difficultyStrings = new String[]{"Easy", "Normal", "Hard"};
	private int currentDifficulty = EASY;
	private boolean hasGameStarted;
	private Color bg = new Color(190,184,137);
	
	Cursor theCursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
	int mouseX, mouseY;
	ZombieSprites zombie;
	final int INITIAL_NUMZOMBIES = 1;
	
	ArrayList<ZombieSprites> zombieHorde;
	Zombies zombieClass;
	private int numZombiesKilled = 0, numWavesSurvived = 0, score = 0;
	public ArrayList<Bullet> bullets;
	
	//boolean and integer for the test gun
	boolean shootingPoisonGun = false;
	final int MAX_NUMBER_OF_TEST_BULLETS = 5;
	int numOfTestBullets = 0;

	ArrayList<Pickup> pickups;
	public final int HEALTH = 0, AMMO = 1;
	//string and boolean used to display info on each pickup
	String pickupInfo = "";
	boolean displayPickupInfo = false;
	private int pickupDisplayFrame = 0;
	private int numZombiesKilledForPickup = 0;
	private boolean bossExists = false;
	
	Random rand = new Random();
	
	BigBall bb = null;//sprite body
	LittleBall lb = null; //sprite weapon
	double newX, newY;//global vars to align the little ball
	Health life;
	Ammo ammo;
	HUD hud;
	Button pause;
	//ranges for different gun types; array index corresponds with currentGun value
	
	Container container;
	private Graphics dbg; 
	private Image dbImage = null;
	private int mouseDistX = 0, mouseDistY = 0;
	//dist between mouse and big ball	
	
	private double mouseAngle = 0;
	//angle between mouse and ball against the horizontal 0 line
	
	private final double CONVERT_TO_DEGREES = Math.PI/180;
	
	private Thread animator;           // the thread that performs the animation
	final int DELAY = 30;	// used in thread sleep to slow down game piece movement
	
	private final String[] instructions = new String[]{"Paused! Press ESC to quit.",
			"",
			"Welcome to Zmob!",
			"",
			"Instructions:",
			"Use w-s-a-d to move up, down, left, and right (respectively)", 
			"Click the mouse to shoot. Scroll the mouse wheel to switch weapons. (or use #1-5)",
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
	public GamePanel(Zombies aT, int width, int height) {
		
		zombieClass = aT;
		pW = width; pH = height;
		setBackground(Color.white);
		setPreferredSize(new Dimension(pW, pH));
		
		frame = 0;
		
		setCursor(theCursor);
		setFocusable(true);
		requestFocus();    // the JPanel now has focus, so receives key events
		readyForTermination();

		// create game components
		gameOver = false;
		setContainer(this);
		
		bb = new BigBall(pW, pH);
		lb = new LittleBall(bb);
		life = new Health(bb, pW, pH);
		ammo = new Ammo(bb, pW, pH);
		hud = new HUD(bb, ammo, life, pW, pH);
		pause = new Button();
		
		currentGun = PISTOL;//initialize default gun
		bb.setRange(ranges[currentGun]);//initialize range
		
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
    	//initialize bullets array list
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
			if (hasGameStarted) {
				if (pause.getRect().contains(new Point(mouseX, mouseY)))
					togglePause();
			}	
			
			if (!isPaused) 
			{//if game is not paused
				if (ammo.isAmmoLeft(currentGun)) 
				{
					//creates a new bullet to shoot

					if (!automatic)
						ammo.subtractAmmo(currentGun);//subtract an ammo
					//if automatic, ammo is subtracted in the gameUpdate method

					if (currentGun == SHOTGUN)
						newShotgunBullet();
					else if(currentGun == WAVE_BEAM)
						newWaveBeamBullet();
					else if(currentGun == POISON_GUN)
					{
						shootingPoisonGun = true;//set it up so that the poison gun shoots
						numOfTestBullets = 0;  //5 shots for one click, each shooting
						newPoisonGunBullet();	   //one frame after the next
					}
					else 
					{
						bullets.add(new Bullet((int)lb.getSX(), (int)lb.getSY(),
							mouseAngle, (int)(lb.getRadius()/2),
							currentGun, pW, pH));
					}

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
				{
					if (currentGun == NUM_OF_WEAPONS - 1)
						switchWeapons(0);
					else
						switchWeapons(++currentGun);
				} else//mouse wheel moves down
				{
					if (currentGun <= 0)
						switchWeapons(NUM_OF_WEAPONS - 1);
					else
						switchWeapons(--currentGun);
				}
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
		//System.out.println("x: " + e.getX() + " y: " + e.getY() + " ");
		
		//get the distance between the X variable and the X of the big ball
		mouseDistX = mouseX-bb.getSX();
		mouseDistY = mouseY-bb.getSY();
		
		//System.out.println(mouseDistX + " " + mouseDistY);
		
		//get the angle
		if(mouseDistX == 0)
			mouseAngle = 0;
		else
			mouseAngle = Math.atan((double)mouseDistY/mouseDistX);
			//division must be typecasted to get correct answer 
		
		//convert to degrees to make unit circle work
		mouseAngle/=CONVERT_TO_DEGREES;
		
		//arctan only gives values -pi/2 < x < pi/2
		//following code gets angles to work as a normal unit cicle
		if(mouseY<bb.getSY())
			//if in 1st or 2nd quadrant
		{
			if(mouseX>=bb.getSX())//if in 1st quadrant
				mouseAngle = -mouseAngle;
			else if(mouseDistX<=bb.getSX())//if in 2nd quadrant
			{
				mouseAngle += 180;
				mouseAngle = 360-mouseAngle;
			}
		}
		else if(mouseY>=bb.getSY())//if in 3rd or 4th quadrant
		{
			if(mouseX>=bb.getSX())//if in 4th quadrant
				mouseAngle = 360-mouseAngle;
			else if(mouseX<bb.getSX())//if in 3rd quadrant
			{
				mouseAngle += 180;
				mouseAngle = 360-mouseAngle;
			}
		}//end making unit circle
		
		//at this point, if the angle is 0 or 360, and the 
		//y value is not exactly 0, then the angle is 
		//either 90 or 270, following fixes this
		if(mouseAngle == 0 || mouseAngle == 360)
		{
			if(mouseY<bb.getSY())
				mouseAngle = 90;
			else if(mouseY>bb.getSY())
				mouseAngle = 270;
		}
		//System.out.println(mouseAngle);
		
		mouseAngle *= CONVERT_TO_DEGREES; //convert back to radians
		
		newX = bb.getSX()+Math.cos(mouseAngle)*lb.getLongRadius();
		newY = bb.getSY()-Math.sin(mouseAngle)*lb.getLongRadius();
		//add the sines and subtract the cosines to set the little ball
		//around the big ball
		newX -= lb.getRadius()/2;
		newY -= lb.getRadius()/2;
	}
	
	public void togglePause()
	{
		isPaused = !isPaused;
		if(isPaused)
		{
			bb.stopSprite();
			lb.up = false;
			lb.right = false;
			lb.down = false;
			lb.left = false;
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
		
		if (hasGameStarted) {
			if (keyCode == KeyEvent.VK_P)
				togglePause();
		}		
		
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
		//		Restart
		if(keyCode == KeyEvent.VK_F8){
			zombieClass.restart(zombieClass);
			}
		
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
				Bullet temp = (Bullet)bullets.get(i); // gets the current rocket
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
		if (keyCode == KeyEvent.VK_F4)
		{
			newCustomZombie(80, 50, 50, 6, 10, 10000);
			bossExists = true;
		}
		
		if(keyCode == KeyEvent.VK_Z)
			life.subtractHealth(1);
		if(keyCode == KeyEvent.VK_X)
			life.addHealth(1);
		
		//send out a zmob wave
		if (keyCode == KeyEvent.VK_F9)
		{
			for(int i = 0; i<100; i++)
				newZombie();
		}
		//******END DEVELOPERS' HACKS*******\\
		
		
		if (!isPaused) {
			if (wKey) {
				bb.up = true;
				lb.up = true;
			}
			if (sKey) {
				bb.down = true;
				lb.down = true;
			}
			if (aKey) {
				bb.left = true;
				lb.left = true;
			}
			if (dKey) {
				bb.right = true;
				lb.right = true;
			}
		}		
	}
	
	protected void processKeyRelease(KeyEvent e) {
		
		int keyCode = -99;
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
		{
			bb.up = false;
			lb.up = false;
		}
		if(!sKey)
		{
			bb.down = false;
			lb.down = false;
		}
		if(!aKey)
		{
			bb.left = false;
			lb.left = false;
		}
		if(!dKey)
		{
			bb.right = false;
			lb.right = false;
		}
		
	}
	
	//**************COLLISION ALGORITHMS*************//
	public void checkBulletCollision()
	{
		for (int x = 0; x < bullets.size(); x++) 
		{//for all bullets
			Bullet tempB = (Bullet)bullets.get(x);//create temp inst of bullet
			boolean hit = false;
			
			for (int y = 0; y < zombieHorde.size(); y++) 
			{//for all zombies
				ZombieSprites tempZ = (ZombieSprites)zombieHorde.get(y);//create temp inst of zombie
				
				if (tempB.getRectangle().intersects(tempZ.getRectangle()))//if they intersect 
				{
					//deal with the zombie
					hit = true;
					tempZ.subtractHealth(tempB.getStrength());
					// current zombie health minus strength of weapon
					
					//if explosion from chain gun hits a zombie, start another explosion
					if(tempB.currentGun == CHAIN_GUN && tempB.isExplosion())
					{
						double zX = tempZ.getPoint().getX(); 
						double zY = tempZ.getPoint().getY();
						
						Bullet newBullet = new Bullet((int)zX, (int)zY, 0, 0, CHAIN_GUN ,pW, pH);
						//create new explosion where the killed zombie was
						newBullet.setExplosion(true);
						bullets.add(newBullet);
					}
					
					if(tempB.getBulletType()==POISON_GUN && !tempZ.isBoss())
						tempZ.setPoisoned(true);//poison zombies
					
					if (tempZ.isDead()) 
					{ //if the health is <=0
						
						if(zombieHorde.get(y).isBoss())//if a boss is killed
						{
							bossExists = false;//change settings
							score += Math.pow(currentDifficulty, 2)/2;//add bonus score
						}
						zombieHorde.remove(y);//remove zombie
						numZombiesKilled++;//add count
						score += (Math.pow(currentDifficulty, 2)/10);//add score
						if(numZombiesKilled%(10.0/currentDifficulty*60) == 0)//makes smaller waves for harder difficulty 
							newWave();//check waves
					}
				}
			}//end zombie for
			
			if (hit) 
			{ //	now deal with the bullet
				if (tempB.currentGun != POISON_GUN)//do not remove poison bullets at all
				{
					if (tempB.currentGun != ROCKET_LAUNCHER
							&& tempB.currentGun != CHAIN_GUN)
						bullets.remove(x);//if a non-rocket, remove the bullet
					else if (!tempB.isExplosion()) //if a rocket, explode it if its not already exploding
						tempB.setExplosion(true);
				}				
			}
		}//end bullet for
	}
	
	public void checkPlayerCollision()
	{
		for (int y = 0; y < zombieHorde.size(); y++) 
		{
			ZombieSprites tempZ = (ZombieSprites)zombieHorde.get(y);
			
			if (bb.getRectangle().intersects(tempZ.getRectangle()))//if player intersects zombie 
				life.subtractHealth(tempZ.getPower());//minus a certain amt of health

			zombie.checkIntersectionWithBB(tempZ, bb);
		}
		/*if(life.getHealth()<=0)
			gameOver = true;*/
	}
	
	public void checkPickupCollision()
	{
		if (!pickups.isEmpty()) {
			for (int y = 0; y < pickups.size(); y++) 
			{
				Pickup tP = (Pickup) pickups.get(y);
				//create temp instance
				if (bb.getRectangle().intersects(tP.getRectangle()))
					//if player intersects pickup
					executePickup(tP.getType(), y);//minus 1 health
			}
		}		
	}
	
	public void checkZombieToZombieCollision(){
		for (int y = 0; y < zombieHorde.size(); y++){
			for (int z = 0; z < zombieHorde.size(); z++){
				zombie.checkOverlap((ZombieSprites)zombieHorde.get(z), (ZombieSprites)zombieHorde.get(y));
			}
		}
	}
	
	//check to dispose if a bullet goes out of range or off the screen, not for hitting zombies
	//checks to dispose a bullet if it has gone out of it's predetermined range
	public boolean checkBulletDispose(Bullet tB, int range)
	{
		boolean x = false;
		double x1 = tB.getStartingSX(), x2 = tB.getSX(), y1 = tB.getStartingSY(), y2 = tB.getSY();
		//pythag theorem
		double hypotenuse = Math.sqrt( Math.pow(x2-x1, 2) + Math.pow(y2-y1, 2) );
		
		if(hypotenuse>=range)//if outside allocated range
		{
			if((tB.currentGun == ROCKET_LAUNCHER || tB.currentGun == CHAIN_GUN) && !tB.isExplosion())
			{
				tB.setExplosion(true);//if it gets out of the range, then make it explode
				/*if(tB.checkRemove())//checks to see if explosion finished
					x = true;*/
			}
			else
				x = true; //remove all other bullets
		}
		
		return x;
	}
	
	
	//***************MISC ALGORITHS**********************\\
	//creates a new zombie randomly within the box 0,0,pW,pH
	public void newZombie()
	{
		zombieHorde.add(new ZombieSprites(pW-20, pH-20));
	}
	public void newCustomZombie(int r, int x, int y, int speed, int power, int health)
	{
		zombieHorde.add(new ZombieSprites(pW, pH, r, x, y, speed, power, health));
	}
	//initialize zombies for the first time
	
	public void newHealthPickup()
	{
		pickups.add(new Pickup(HEALTH, pW-30, pH-30));
	}
	
	public void newAmmoPickup()
	{
		pickups.add(new Pickup(AMMO, pW-30, pH-30));
	}
	
	private void setInitialZombieList() {
		for (int x = 0; x < INITIAL_NUMZOMBIES; x++) {
			zombie = new ZombieSprites(pW, pH);
			newZombie();
		}
	}
	
	public void switchWeapons(int type)
	{
		if (!isPaused) {
			currentGun = type;
			bb.setRange(ranges[currentGun]);
			ammo.changeGun(type);
			if (currentGun == MACHINE_GUN || currentGun == WAVE_BEAM)
				automatic = true;
			else
				automatic = false;
		}		
	}
	
	public void executePickup(int type, int index)
	{
		displayPickupInfo = true;//works with gameRender to display the String
		
		if(type == HEALTH)
		{
			Pickup tP = new Pickup();
			//create temp just to get recovery constant from class
			life.addHealth(tP.getRecovery());
			pickupInfo = "+" + String.valueOf(tP.getRecovery()) + " Health";//setup display
			pickups.remove(index);//remove pickup
		}
		else if(type == AMMO)
		{
			int tempInt = rand.nextInt(NUM_OF_WEAPONS-1)+1;//choose random weapon NOT PISTOL
			
			//deal with ammo "preference" (gives ammo to guns with 0 ammo first)
			while(ammo.getAmmo(tempInt) != 0 && ammo.isOneGunEmpty())
			{tempInt = rand.nextInt(NUM_OF_WEAPONS);}//get another random index
			
			int ammoToAdd = (int)(.3*ammo.getMaxAmmo(tempInt));			
			
			if(ammoToAdd==0)
				ammoToAdd = 1;//for the chain gun
			
			pickupInfo = "+" + ammoToAdd + " " + ammo.getWeaponString(tempInt);
			ammo.addAmmo(ammoToAdd, tempInt);
			//adds 30% of max ammo
			pickups.remove(index);//remove pickup
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
	
	public void newWaveBeamBullet()
	{
		boolean x = false;//boolean controls whether to make the big or small shot
		// in the for loop

		for (int i = 0; i < 2; i++) 
		{
			bullets.add(new Bullet((int)lb.getSX(), (int)lb.getSY(), mouseAngle, 
				(int)(lb.getRadius()/2), currentGun,
				pW, pH, x));
			//System.out.println(x);
			x = !x;
		}
	}
	
	public void newShotgunBullet()
	{
		for (int i = 0; i < 5; i++) {
			bullets.add(new Bullet((int)lb.getSX(), (int)lb.getSY(), mouseAngle, 
					(int)(lb.getRadius()/2), currentGun,
					pW, pH, i));
		}
	}
	
	public void newPoisonGunBullet()
	{
		currentGun = POISON_GUN;//make sure it's on poison gun
		
		bullets.add(new Bullet((int)lb.getSX(), (int)lb.getSY(), mouseAngle, 
				(int)(lb.getRadius()/2), currentGun, pW, pH));
		
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
		
		//pause button
		if(pause.getRect().contains(new Point(mouseX, mouseY)))//if mouse is over button
				pause.setMouseOver(true);
			else
				pause.setMouseOver(false);
		
		if (!isPaused && !gameOver && hasGameStarted)
		{			
			alignLittleBall();//call this method to position little ball every frame
			lb.setPosition(newX, newY);//and set the position
			
			//update automatic bullets and test bullets
			if(shootingPoisonGun)
				newPoisonGunBullet();
			
			int disp = (int)(lb.getRadius()/2);//disp for where the bullet starts
			if(mousePressed && automatic)//if machine gun and clicking
			{
				if(ammo.isAmmoLeft(currentGun))
					{
						if(currentGun==WAVE_BEAM)
							newWaveBeamBullet();
						else
						bullets.add(new Bullet((int)lb.getSX(), (int)lb.getSY(), mouseAngle,
								disp, currentGun, pW, pH));//add a new bullet each frame
						
						ammo.subtractAmmo(currentGun);//subtract ammo
						
						//while there is no ammo left in the current gun
						while (!ammo.isAmmoLeft(currentGun))
							switchWeapons(--currentGun);//switch weapons down
					}
			}
			
			//update running
			bb.setRunning(running);
			
			//update godMode
			if(godMode)
			{
				ammo.resetAmmo();
				life.setHealth(100);
				bb.addStamina(100);
			}
			
			//udpate zombies			
			if (zombieHorde.size() != 0) {
				for (int x = 0; x < zombieHorde.size(); x++) {
					ZombieSprites tempZ = (ZombieSprites)zombieHorde.get(x);
					tempZ.decideMove(bb.getSXCORNER(),bb.getSYCORNER());
					//if zombie is poisoned, remove health every frame
					if(tempZ.isPoisoned())
						tempZ.subtractHealth(1);
				}
				if(!bullets.isEmpty())
					{checkBulletCollision();}
				checkPlayerCollision();//checks player for any collisions with zombies
				checkZombieToZombieCollision();//checks for zombie to zombie collision
			}
			
			//if there is no boss zombie alive
			if (!bossExists) {
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
					Bullet temp = (Bullet)bullets.get(i); // gets the current bullet
					temp.move(); // move whether to be removed or not
					//temp.checkRemove();//checks if outside the panel
					
					if(checkBulletDispose(temp, temp.getRange()))//if the bullet should be removed
					{
						if (temp.currentGun != ROCKET_LAUNCHER && temp.currentGun != CHAIN_GUN)
						{
							bullets.remove(i);//if a non-rocket, remove the bullet
							removed = true;
						}
						else if(!temp.isExplosion()) //if a rocket, explode it if its not already exploding
						{
							temp.setExplosion(true);
							removed = false;
						}
					}
					
					if (temp.checkRemove() && removed == false) {
						bullets.remove(i);
					}
					
					//check to see if explosion hits player, which then causes damage
					if(temp.currentGun == ROCKET_LAUNCHER)
						if(bb.getRectangle().intersects(temp.getRectangle()))
							life.subtractHealth(1.0);
				}
			}
			
			checkPickupCollision();//checks for pickups
			
			if(life.getHealth()<=0)
				gameOver = true;
		}
			
	}  // end of gameUpdate

	private void gameRender()	  {
		if (dbImage == null){ // must create the Image and get its Graphics 
			dbImage = createImage(pW, pH); // image is size of panel
			dbg = dbImage.getGraphics(); // all drawing done to this graphics
	    }
		
	    // clear the background
	    dbg.setColor(bg);
	    dbg.fillRect(0, 0, pW, pH);
	    dbg.setFont(new Font("SansSerif", Font.BOLD, 15));
	    
		if (!pickups.isEmpty()) {
			for (int i = 0; i < pickups.size(); i++) {
				Pickup tP = (Pickup) pickups.get(i);
				tP.draw(dbg);
			}
		}	    
		dbg.setColor(Color.black);
		
	    // draw the player sprites
	    bb.draw(dbg, isPaused);
	    lb.draw(dbg, container);
	    hud.draw(dbg, numZombiesKilled, numWavesSurvived, score, 
	    		difficultyStrings[(currentDifficulty/10)-1]);
	    pause.draw(dbg);
	    
	    if(isPaused)
	    	displayInstructions(dbg);
	    
	    //draw zombies
	    for (int x = 0; x < zombieHorde.size(); x++) {
	    	ZombieSprites tempZ = (ZombieSprites)zombieHorde.get(x);
	    	tempZ.draw(dbg);
		}
	    
	    //draw bullets
	    if(!bullets.isEmpty()) // if there is a bullet
		{
			//System.out.print(lasers.size()+",");
			for(int i = 0; i<bullets.size(); i++) 
			{ // bullets.size is how many bullets 
				Bullet temp = (Bullet)bullets.get(i); // gets the current bullet
				temp.draw(dbg, container); // draws
			}
		}
	    
	    //display pickup information
	    if(displayPickupInfo)
	    {
	    	pickupDisplayFrame++;
	    	dbg.setColor(Color.BLACK);
	    	dbg.drawString(pickupInfo, pW/2, pH-15);
	    	if(pickupDisplayFrame == 50)
	    	{
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
	    }   catch (Exception e)  { }
	}
	
	public void displayInstructions(Graphics g)
	{
		g.setColor(Color.BLUE);
		
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