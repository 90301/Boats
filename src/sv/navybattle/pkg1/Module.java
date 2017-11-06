package sv.navybattle.pkg1;

import java.awt.Graphics2D;

public class Module implements Paintable {
	public int mod;
	public int coolDown;
	public int time;
	public Module(int mod) {
		// TODO Auto-generated constructor stub
		this.mod = mod;
		coolDown=60;
		
	}
	public void modTick() {
		if (time>0) {
		time--;
		}
	}

	@Override
	public void paint(Graphics2D g2, double scale, double mapX, double mapY) {
		// TODO Auto-generated method stub

	}

}
