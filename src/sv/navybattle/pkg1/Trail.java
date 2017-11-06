package sv.navybattle.pkg1;

import java.awt.Color;
import java.awt.Graphics2D;

public class Trail implements Paintable {
	public Color trailColor = new Color(0xAA3300);
	public double x1,x2,y1,y2;
	public Ship e1,e2;
	public Trail(Ship entity1,Ship entity2) {
		commonConstruct(entity1,entity2);
		time = 5;
		trailColor = Main.teamColor.get(entity1.team);
		// TODO Auto-generated constructor stub
	}
	public Trail(Ship entity1,Ship entity2, Color color) {
		commonConstruct(entity1,entity2);
		trailColor = color;
		// TODO Auto-generated constructor stub
	}
	public void commonConstruct(Ship entity1,Ship entity2) {
		x1 = entity1.x;
		x2 = entity2.x;
		y1 = entity1.y;
		y2 = entity2.y;
		e1=entity1;
		e2=entity2;
		time = 5;
	}
	@Override
	public void paint(Graphics2D g2, double scale, double mapX, double mapY) {
		// TODO Auto-generated method stub
		g2.setColor(trailColor);
		g2.drawLine((int)((x1+mapX)*scale), (int)((mapY+y1)*scale), (int)((x2+mapX)*scale), (int)((mapY+y2)*scale));

	}
	public int time;
	public void doTick(){
		time--;
	}

}
