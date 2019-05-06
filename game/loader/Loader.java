package game.loader;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

public abstract class Loader {

	protected abstract void load();
	
	private String message;
	private boolean finished;
	
	public Loader(String type){
		this.message = "Loading " + type + "...";
		load();
	}
	
	public String getMessage(){
		return message;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}
	
	public static BufferedImage getBI(Image im) {
		int w = im.getWidth(null);
		int h = im.getHeight(null);

		BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		//TYPE_INT_ARGB MAKES THE BG TRANSPARENT
		Graphics2D g2 = bi.createGraphics();
		g2.drawImage(im, 0, 0, null);

		return bi;
	}
	
}
