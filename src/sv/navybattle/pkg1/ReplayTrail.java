package sv.navybattle.pkg1;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.Serializable;

public class ReplayTrail implements Paintable, Serializable {
	private static final long serialVersionUID = 1L;
	public Color trailColor = new Color(0xAA3300);
	public double x1,x2,y1,y2;
	public ReplayTrail(Trail trail) {
		// TODO Auto-generated constructor stub
		x1 = trail.x1;
		x2 = trail.x2;
		y1 = trail.y1;
		y2 = trail.y2;
		trailColor = trail.trailColor;
	}
	public ReplayTrail() {
	}

	@Override
	public void paint(Graphics2D g2, double scale, double mapX, double mapY) {
		// TODO Auto-generated method stub
		g2.setColor(trailColor);
		g2.drawLine((int)((x1+mapX)*scale), (int)((mapY+y1)*scale), (int)((x2+mapX)*scale), (int)((mapY+y2)*scale));
	}

}
