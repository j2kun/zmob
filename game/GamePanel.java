package game;

import static game.common.Constants.INSTRUCTIONS;
import game.common.Constants;
import game.common.GameInfo;
import game.info.PlayerInfo;
import game.listener.KeyListener;
import game.listener.MouseListener;
import game.loader.SpriteLoader;
import game.loader.TileLoader;
import game.manager.PickupManager;
import game.manager.WeaponManager;
import game.manager.ZombieManager;
import game.sprite.HUD;
import game.sprite.Player;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {

	private static final long serialVersionUID = 1L;
	Cursor theCursor = new Cursor(Cursor.CROSSHAIR_CURSOR);

	private Player player = null;//sprite body
	private HUD hud;

	private TileLoader tileLoader;
	private SpriteLoader spriteLoader;
	private WeaponManager weaponManager;
	private PickupManager pickupManager;
	private ZombieManager zombieManager;
	
	private KeyListener keyListener;
	private MouseListener mouseListener;
	
	private Graphics dbg; 
	private Image dbImage = null;

	private Thread animator;           // the thread that performs the animation
	
	public GamePanel(final IZombie aT, final int width, final int height) {
		GameInfo.pW = width; 
		GameInfo.pH = height;
		setBackground(Color.white);
		setPreferredSize(new Dimension(width, height));

		setCursor(theCursor);
		setFocusable(true);
		requestFocus();

		tileLoader = new TileLoader(new Dimension(width, height));
		spriteLoader = new SpriteLoader();
		player = new Player(new PlayerInfo(
				width/2, height/2, GameInfo.playerW, GameInfo.playerH));

		zombieManager = new ZombieManager(player);
		weaponManager = new WeaponManager(player, zombieManager);
		pickupManager = new PickupManager(zombieManager, weaponManager, player);
		hud = new HUD(player, weaponManager);

		player.setRange(weaponManager.getCurrentGun().getRange());//initialize range

		mouseListener = new MouseListener(weaponManager, player);
		addMouseListener(mouseListener);
		addMouseMotionListener(mouseListener);
		addMouseWheelListener(mouseListener);

		keyListener = new KeyListener(aT, player, 
				weaponManager, pickupManager, zombieManager);
		addKeyListener(keyListener);	

		GameInfo.paused = true;
		GameInfo.gameStarted = false;
	}

	public void run() {
		//long gameStartTime = System.currentTimeMillis();
		GameInfo.gameRunning = true;
		
//		SpriteLoader is a thread. asynchronously add and remove rotator ops to a map
		new Thread(spriteLoader).start();

		while(GameInfo.gameRunning) {
			gameUpdate(); 	// move the pieces 
			gameRender();   // render the game to a buffer
			paintScreen();  // draw the buffer on-screen
			GameInfo.frame++;
			try {
				Thread.sleep(Constants.FRAME_RATE);
			} 
			catch (final InterruptedException e) {
				//Do nothing
			}
		}
	}

	private void gameUpdate(){
		if (!GameInfo.paused && !GameInfo.gameOver && GameInfo.gameStarted)	{
			player.update();
			mouseListener.setMouseVariables(GameInfo.mouseX, GameInfo.mouseY);
//			zombieManager.update();
			pickupManager.update();
			weaponManager.update();
			
			//update posion gun bullets
			if(GameInfo.numPoisonBulletsShot > 0){
				weaponManager.getBulletManager().createAndAdd(Constants.POISON_GUN);
				if(GameInfo.numPoisonBulletsShot >= 5)
					GameInfo.numPoisonBulletsShot = 0;
			}
			
			//update godMode
			if(GameInfo.godMode){
				player.setHealth(100);
				weaponManager.resetAmmo();
				player.addStamina(100);
			}
		}
	}

	private void gameRender() {
		if (dbImage == null) { // must create the Image and get its Graphics 
			dbImage = createImage(GameInfo.pW, GameInfo.pH); // image is size of panel
			dbg = dbImage.getGraphics(); // all drawing done to this graphics
			dbg.setFont(new Font("SansSerif", Font.BOLD, 15));
		}

		tileLoader.draw(dbg);
		player.draw(dbg, weaponManager.getCurrentGun(), spriteLoader);
		hud.draw(dbg);

		if(GameInfo.paused)
			displayInstructions(dbg);

//		zombieManager.render(dbg, spriteLoader);
		weaponManager.drawBullets(dbg);

		pickupManager.renderPickups(dbg);
		pickupManager.renderDisplayInfo(dbg, GameInfo.pW/2, GameInfo.pH-15);

		if (GameInfo.gameOver)
			gameOver(dbg);
	} 

	private void paintScreen() {
		// use active rendering to put the buffered image on-screen
		try {
			final Graphics g = this.getGraphics(); // panel 
			if ((g != null) && (dbImage != null))
				g.drawImage(dbImage, 0, 0, null);
			g.dispose();
		}   catch (final Exception e)  { }//System.out.println("Error painting the screen!");}
	}

	public void displayInstructions(final Graphics g) {
		g.setColor(Color.WHITE);

		for(int i = 0; i<INSTRUCTIONS.length; i++)
			g.drawString(INSTRUCTIONS[i], 50, 65+i*17);
	}

	public void addNotify() {
		// only start the animation once the JPanel has been added to the JFrame
		super.addNotify();   // creates the peer
		startGame();    // start the thread
	}

	private void startGame() {
		// initialise and start the thread 
		if (animator == null || !GameInfo.gameRunning) {
			animator = new Thread(this);
			animator.start();
		}
	}

	private void gameOver(final Graphics g)	{
		String msg = "Game Over. ";
		g.setColor(Color.red);
		g.drawString(msg, GameInfo.pW/2-50, GameInfo.pH/2-50);

		msg = "Press esc to exit, F8 to restart.";
		g.drawString(msg, GameInfo.pW/2-50, GameInfo.pH/2-25);

		GameInfo.gameRunning = false;
	} 

}