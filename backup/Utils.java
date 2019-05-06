package backup;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

public class Utils {
//	given an image get a buffered image
	public static BufferedImage getBI(Image im)
	{
	   int w = im.getWidth(null);
	   int h = im.getHeight(null);
	
	   BufferedImage bi = new BufferedImage (w, h, BufferedImage.TYPE_INT_ARGB);
	   //TYPE_INT_ARGB MAKES THE BG TRANSPARENT
	   Graphics2D g2 = bi.createGraphics ();
	   g2.drawImage (im, 0, 0, null);
	
	   return bi;
	}
	
	//get an Image given a BufferedImage
	public static Image getImage(BufferedImage bufferedImage) {
        return Toolkit.getDefaultToolkit().createImage(bufferedImage.getSource());
    }
}
