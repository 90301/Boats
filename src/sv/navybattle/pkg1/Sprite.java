package sv.navybattle.pkg1;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class Sprite implements Paintable {
	public BufferedImage rawImg,modImg,modImg2 = null;
	public Sprite(int imgNum) {
		// TODO Auto-generated constructor stub
		BufferedImage BI = null;
		//URL urlx = getClass().getResource("/Images/ExShip.jpg");
		URL urlx = getClass().getResource("/Images/Square Ship.jpg");
		
		switch (imgNum) {
		case 0: urlx = getClass().getResource("/Images/Square Ship.jpg");
		case 1: urlx = getClass().getResource("/Images/ExShip.jpg");
		case 2: urlx = getClass().getResource("/Images/Ship2.jpg");
		}
		try {
			BI = ImageIO.read(urlx);
			rawImg = BI;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	AffineTransformOp scaleOp,rotateOp = null;
	public double scaleS=1;
	
	public void scale(double scale) {
		//AffineTransform is what we need for this.
		AffineTransform AF = new AffineTransform();
		AF.setToScale(scale, scale);
		
		AffineTransformOp op = new AffineTransformOp(AF, AffineTransformOp.TYPE_BILINEAR);
		scaleOp = op;
		modImg = op.filter(rawImg, null);
		scaleS=scale;
		//sets the scale transform to the set scale item.
	}
	public void rotate(double radians) {
		AffineTransform AF = new AffineTransform();
		AF.rotate(radians,modImg.getWidth()/2,modImg.getHeight()/2);
		AffineTransformOp op = new AffineTransformOp(AF, AffineTransformOp.TYPE_BILINEAR);
		rotateOp = op;
		
	}
	@Override
	public void paint(Graphics2D g2, double scale, double mapX, double mapY) {
		// TODO Auto-generated method stub

	}

}
