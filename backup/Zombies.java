package backup;
import java.awt.Container;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class Zombies extends JFrame implements IZombie{

	/**
	 * This program is a top-down zombie-shooting game. 
	 *
	 *Written By: Jeremy Kun
	 *current version: 1.2
	 */
	private static final long serialVersionUID = -4089174658070026670L;
	byte me;
	
	JTextField jtfBox,jtfTime;
	GamePanel gp;
	protected static GraphicsEnvironment gfxEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
	protected static GraphicsDevice[] screenDeviceList = gfxEnv.getScreenDevices();
	protected static GraphicsDevice defaultScreenDevice = gfxEnv.getDefaultScreenDevice();
	protected static DisplayMode[] displayModes = defaultScreenDevice.getDisplayModes();
	static int pWidth;
	static int pHeight;
	static boolean windowed = false;
	
	protected DisplayMode newDisplayMode = null;
	
	private static DisplayMode[] requestedDisplayModes = new DisplayMode[] 
    {
	       new DisplayMode(1280, 1024,32, 60)
	};	
	
	public Zombies() {
		setTitle("Zmob"); // title of JFrame
 	    pack();   // first one (the GUI doesn't include the JPanel yet) 
 	    Container c = getContentPane();
 	    // default layout of JFrame's contentPane is BorderLayout
 	    gp = new GamePanel(this, pWidth, pHeight); // create the playing area
 	    //pWidth = 100; pHeight = 100;
 	    //gp.setPreferredSize(new Dimension(pWidth, pHeight));
 	    c.add(gp, "Center");	// add playing area to center of JFrame 
 	    pack();  // second, after JPanel added - uses size of play are to control Frame size
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 	    setResizable(false);
 	    setLocation(100,100);
 	    setVisible(true);
	}
	
	public Zombies(String title, DisplayMode mode) throws HeadlessException
	{
		super(title);
		System.out.println("Entering the following video mode:");
		printDisplayMode(mode);
		
		newDisplayMode = mode;
		
		setUndecorated(true);
		setIgnoreRepaint(true);   	
		setupWindowListener();   
		setupKeyListener();   
  		
		Container c = getContentPane();
 	    gp = new GamePanel(this, pWidth, pHeight); // create the playing area
 	    c.add(gp, "Center");	
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 	    setResizable(false);
 	    setVisible(true);
	}

	public static void printDisplayModes()
	{
		// loop through each of the available modes
		for(int mIndex=0; mIndex < displayModes.length; mIndex++)
			printDisplayMode(displayModes[mIndex]);
	}
	
	public static void printDisplayMode(DisplayMode mode)
	{
		System.out.println(
			"" + mode.getWidth()+
			"x" + mode.getHeight()+
			"x" + mode.getBitDepth()+
			"@" + mode.getRefreshRate());
	}
	
	public static DisplayMode findRequestedMode()
	{
		DisplayMode best = null;

		// loop through each of our requested modes
		for(int rIndex=0; rIndex < requestedDisplayModes.length; rIndex++)
		{
			// loop through each of the available modes
			for(int mIndex=0; mIndex < displayModes.length; mIndex++)
			{
				if(displayModes[mIndex].getWidth() == requestedDisplayModes[rIndex].getWidth() &&
				   displayModes[mIndex].getHeight() == requestedDisplayModes[rIndex].getHeight() &&
				   displayModes[mIndex].getBitDepth() == requestedDisplayModes[rIndex].getBitDepth())
				{
					// We found resolution a match
					if(best==null)
					{
						// if the refresh rate was specified try to match that as well
						if(requestedDisplayModes[rIndex].getRefreshRate()!=DisplayMode.REFRESH_RATE_UNKNOWN)
						{
							if(displayModes[mIndex].getRefreshRate() >=
								requestedDisplayModes[rIndex].getRefreshRate())
							{
								best = displayModes[mIndex];
								return best;
							}
						}
						else
						{
							best = displayModes[mIndex];
							return best;
						}
					}
				}
			}
		}

		// no matching modes so we return null
		return best;
	}
	
	protected void exitForm(java.awt.event.WindowEvent evt) 
	{
		// make sure we restore the video mode before exiting
		initFromScreen();
		
		// force the program to exit
		System.exit(0);
	}
	
	public void initFromScreen() 
	{
			defaultScreenDevice.setFullScreenWindow(null);
	}
	
	public void initToScreen() 
	{
		pack();
			// set this Frame as a full screen window 
			defaultScreenDevice.setFullScreenWindow(this);
			// change the video mode to the one we wanted
			defaultScreenDevice.setDisplayMode(newDisplayMode);
	}
	
	private void setupWindowListener() 
	{
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent evt) {
				exitForm(evt);
			}
		});
        
	}
	public void setupKeyListener() 
	{
		addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyPressed(java.awt.event.KeyEvent evt) {keyPressedHandler(evt);}
		});
	}
	
	public void keyPressedHandler(java.awt.event.KeyEvent evt) {
		if(evt.getKeyCode()==java.awt.event.KeyEvent.VK_ESCAPE)
		{
			System.out.println("Exiting...");
			initFromScreen();
			System.exit(0);
		}
	}
	
	public void restart(IZombie x){
		x.dispose();
		Zombies myFrame = null;
		if (!windowed) {
			DisplayMode newMode = findRequestedMode();
			myFrame = new Zombies("Zombies Full Screen Mode", newMode);
			myFrame.initToScreen();
		}	
		else
			myFrame = new Zombies();
	}
	
	public static void main(String[] args) {
		
		//green out to toggle full screen
		//windowed = true;
		
		DisplayMode newMode = null;
		
		// see if the user wants to force windowed mode
		// even if full screen mode is available
		boolean forceWindowedMode = false;
		if(args.length >= 1)
			if(args[0].equalsIgnoreCase("windowed"))
				forceWindowedMode = true;
		
    	// we need to make sure the system defualt display can
    	// support full screen mode, if it can't we will run
    	// in windowed mode
    	boolean fullScreenMode = false;
		if(defaultScreenDevice.isFullScreenSupported())
		{
			System.out.println("full screen is supported");
			fullScreenMode = true;
			
			// try to get one of the modes we really want
			newMode = findRequestedMode();
		
			// if the mode doesn't exist then go into windowed mode
			// otherwise use full screen mode
			pWidth = newMode.getWidth();
			pHeight = newMode.getHeight();
			if(newMode==null)
				fullScreenMode = false;
			else
				System.out.print("Best full screen mode is : "); printDisplayMode(newMode);
		}
		else
			System.out.println("This system doesn't support full screen mode.");
			
		Zombies myFrame = null;
		
		//ungreen to enable full screen mode
		if(windowed)
		{
			pWidth = 1000;
			pHeight = 800;
			new Zombies();
		}
		else if(fullScreenMode && !forceWindowedMode)
		{
			myFrame = new Zombies("Zombies Full Screen Mode", newMode);
			myFrame.initToScreen();
		}
	}

}