package sv.navybattle.pkg1;

import java.awt.Color;
import java.awt.Graphics2D;

public class Trail implements Paintable {
	public Color trailColor = new Color(0xAA3300);
	public double x1, x2, y1, y2;
	public double size;

	public Trail(Ship entity1, Ship entity2) {
		this(entity1, entity2, Main.teamColor.get(entity1.team));
		// TODO Auto-generated constructor stub
	}

	public Trail(Ship entity1, Ship entity2, Color color) {
		x1 = entity1.x;
		x2 = entity2.x;
		y1 = entity1.y;
		y2 = entity2.y;
		time = 6;
		trailColor = color;
		size = entity1.shipSize;
		// TODO Auto-generated constructor stub
	}

	public void paint(Graphics2D g2, double scale) {
		// TODO Auto-generated method stub
		g2.setColor(trailColor);
		g2.drawLine((int) (x1 * scale), (int) (y1 * scale), (int) (x2 * scale),
				(int) (y2 * scale));

		trailColor = nextColor();
		g2.setColor(trailColor);
		g2.fillOval((int) (scale * (x2 - size / 8)), (int) (scale * (y2 - size / 8)),
				(int) (scale * size / 4), (int) (scale * size / 4));

	}
	
	protected Color nextColor() {
		return new Color(trailColor.getRed(), trailColor.getGreen(), trailColor.getBlue(),
				Math.min(255, (int) (32 + size) * time / 2));
	}

	public int time;

	public void doTick() {
		time--;
	}

}
