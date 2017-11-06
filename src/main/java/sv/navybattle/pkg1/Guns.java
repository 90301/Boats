package sv.navybattle.pkg1;

import java.awt.Graphics2D;
import java.util.ArrayList;

public class Guns {
	public int dam;
	public int rof;
	public int reloadTime;
	public int magSize;
	public double accMax;
	public double accMin;
	public double rangeMin;
	public double rangeMax;
	public ArrayList<TargetPriority> targets = new ArrayList<TargetPriority>();

	public Guns(int i) {
		// TODO Auto-generated constructor stub
		if (i == 1) {
			dam = 10;
			rof = 10;
			reloadTime = 70;
			magSize = 5;
			accMax = 50;
			accMin = 100;
			rangeMin = 1;
			rangeMax = 400;
		}
		if (i == 2) {
			dam = 20;
			rof = 10;
			reloadTime = 70;
			magSize = 5;
			accMax = 50;
			accMin = 100;
			rangeMin = 1;
			rangeMax = 200;
		}
		if (i == 3) {
			dam = 100;
			rof = 60;
			reloadTime = 70;
			magSize = 5;
			accMax = 50;
			accMin = 100;
			rangeMin = 20;
			rangeMax = 1000;
		}
		if (i == 4) {
			dam = 10;
			rof = 2;
			reloadTime = 70;
			magSize = 5;
			accMax = 50;
			accMin = 100;
			rangeMin = 20;
			rangeMax = 100;
		}
		rof *= 0.75 + 0.5 * Math.random();
	}

	int delay = 0;

	public void coolDown() {
		delay--;
	}

	public void paint(Graphics2D g2, double scale) {

	}
}
