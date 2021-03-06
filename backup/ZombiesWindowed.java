package backup;

import java.awt.Container;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ZombiesWindowed extends JFrame implements IZombie{

	/**
	 * This program is a top-down zombie-shooting game. 
	 *
	 *Written By: Jeremy Kun, David Yuen, Ryan Aczon
	 *current version: 1.5
	 */
	private static final long serialVersionUID = -4089174658070026670L;
	byte me;
	GamePanel gp;
	static int pWidth;
	static int pHeight;
	
	public ZombiesWindowed() {
		setTitle("Zmob"); // title of JFrame
		makeGUI();	// create the non-playing area gui
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
 	    setLocation(0,0);
 	    setVisible(true);
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
			System.exit(0);
		}
	}
	
	public void restart(IZombie x){
		x.dispose();
		x = null;
		new ZombiesWindowed();
	}
	
	private void makeGUI() {
		// make gui for the display area, not the playing field
		Container c = getContentPane();    // default BorderLayout used
		
		JPanel ctrls = new JPanel();   // a row of textfields
		// put the control panels at the bottom of the JFrame
		c.add(ctrls);		
	}
	
	public static void main(String[] args) {
		
		pWidth = 128*9;
		pHeight = 128*7;
		new ZombiesWindowed();
	}
}
