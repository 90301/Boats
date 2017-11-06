package sv.navybattle.pkg1;

import java.awt.Graphics2D;

public class Command implements Paintable {
	public Ship targetShip;
	public int timeOut = 60;
	public double xloc,yloc;
	public Command(Ship targetShip) {
		//This is an attack query!
		this.targetShip = targetShip;
		timeOut = 58;
		xloc = targetShip.x;
		yloc = targetShip.y;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void paint(Graphics2D g2, double scale, double mapX, double mapY) {
		// TODO Auto-generated method stub

	}

}
