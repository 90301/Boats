/**
 * 
 */
package sv.navybattle.pkg1;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;

/**
 * @author tbsw
 * 
 */
public class Ship implements Paintable {

	/**
	 * 
	 */
	public final int initHp;
	public int prevHp;
	public int showHp;
	
	public int hp;
	public double x, y;
	public int team;
	public int OTgt; // official target
						// (more for
						// debugging)
	public double CDist;
	public int targeted = 0;
	public double shipSize;
	public double speed;
	ArrayList<Guns> slots = new ArrayList<Guns>();
	Random rand = new Random();

	public Ship(int team, double x, double y) {
		// TODO Auto-generated constructor stub
		// hp = 100+(10*rand.nextInt(50)*(team+1));
		hp = 100 + (10 * rand.nextInt(100));
		this.x = x;
		this.y = y;
		this.team = team;

		this.speed = 3 + rand.nextInt(2);
		// this.shipSize=1+50-this.speed;

		int guns = 1 + rand.nextInt(10);
		// int guns =
		// 1*(1+Main.teams-team)+rand.nextInt(2)*(1+(Main.teams-team));
		// this.shipSize=1+hp/50;
		this.shipSize = 1 + 5 * guns;
		for (int e = 0; e < guns; e++) {
			slots.add(new Guns(1 + rand.nextInt(4)));
			// slots.add(new Guns((int)3));
		}
		hp *= 10;
		initHp = hp;
	}

	public Ship(int team, double x, double y, int extra) {
		// TODO Auto-generated constructor stub
		if (extra == 1) {
			hp = 10 * Main.spawnSize;
			this.x = x;
			this.y = y;
			this.team = team;
			this.shipSize = 40;
			this.speed = 1;
			int guns = 25;
			for (int e = 0; e < guns; e++) {
				slots.add(new Guns((4)));
			}
		}
		if (extra == 2) {
			hp = 100;
			this.x = x;
			this.y = y;
			this.team = team;
			this.shipSize = 10;
			this.speed = 10;
			int guns = 2;
			for (int e = 0; e < guns; e++) {
				slots.add(new Guns((1)));
			}
		}
		hp *= 10;
		initHp = hp;
	}

	public void doTick() {
		// hp=hp+slots.size();
		for (int i = 0; i < slots.size(); i++) {
			// give info to stats
			if (Main.reStat) {
				Main.stats.set(team, Main.stats.get(team) + 1);
			}
			slots.get(i).coolDown();
		}
		if (Main.reStat) {
			Main.stats.set(Main.teams + team, Main.stats.get(Main.teams + team)
					+ hp);
		}
		int moveTGT = -1;
		for (int i = 0; i < slots.size(); i++) {
			int tgt = -1;
			if (slots.get(i).delay <= 0) {
				tgt = target(i);
			}

			// OTgt = tgt;
			// System.out.println(tgt);
			if (tgt != -1) {
				if (CDist < slots.get(i).rangeMax) {
					shoot(Main.ships.get(tgt), slots.get(i));
				} else {
					moveTGT = tgt;

					// x -= (x - Main.ships.get(tgt).x)/100;
					// y -= (y - Main.ships.get(tgt).y)/100;
				}
			}
			// end of for loop
		}
		if (moveTGT != -1) {
			move(Main.ships.get(moveTGT).x, Main.ships.get(moveTGT).y);
		}

	}

	public int target(int slot) {
		double closestDist = -1;
		int entity = -1;
		for (int i = 0; i < Main.ships.size(); i++) {
			Ship ship = Main.ships.get(i);
			if (ship.team != team) {
				double dist = Math.sqrt(Math.pow((ship.x - x), 2)
						+ Math.pow((ship.y - y), 2));
				if (dist > slots.get(slot).rangeMin
						&& (closestDist == -1 || closestDist > dist)
						&& ship.hp > 0) {

					closestDist = dist;
					entity = i;
				}
				// Separate code in the event there are multiple targets
				if (dist <= slots.get(slot).rangeMax && ship.hp >= 0) {
					slots.get(slot).targets.add(new TargetPriority(ship,
							ship.slots.size(), dist, i));
				}
			}
		}
		CDist = closestDist;
		int priority = -1;
		// A value of -1 will be sent if there is nothing to target
		for (int i = (slots.get(slot).targets.size()) - 1; i >= 0; i--) {
			TargetPriority tp = slots.get(slot).targets.get(i);
			if (tp.priority > priority) {
				priority = tp.priority;
				entity = tp.e;
			}
			slots.get(slot).targets.remove(i);
		}

		if (entity != -1) {
			Main.ships.get(entity).targeted++;
		}
		return entity;

	}

	public void move(double xd, double yd) {
		// the idea is to take a position (xd/yd x and y destination) and try to
		// move towards it.
		double ix = xd - x;
		double iy = yd - y;
		double dist = Math.sqrt(ix * ix + iy * iy);
		if (dist >= Math.sqrt(shipSize * shipSize + shipSize * shipSize)) {
			int IOP = 0;
			if (ix == 0) {
				IOP = 1;
				iy = Math.signum(iy);
			}
			if (iy == 0) {
				IOP = 2;
				ix = Math.signum(ix);
			}

			if (Math.abs(ix) > Math.abs(iy) && ix != 0 && iy != 0) {
				IOP = 3;
				iy = iy / Math.abs(ix);
				ix = Math.signum(ix);

			} else {
				if (ix != 0 && iy != 0) {
					IOP = 4;
					ix = ix / Math.abs(iy);
					iy = Math.signum(iy);
				}
			}

			x += ix * speed;
			y += iy * speed;

			if (Math.abs(ix) > 1 || Math.abs(iy) > 1) {
				System.out.println(" xd: " + xd + " yd: " + yd + " ix: " + ix
						+ " iy: " + iy + " IOP: " + IOP);
			}
		}
		collision();
	}

	public void collision() {
		// todo make collision code
		for (int i = 0; i < Main.ships.size(); i++) {
			Ship ship = Main.ships.get(i);
			if (ship != this) {
				if (x + shipSize / 2 >= ship.x - ship.shipSize / 2
						&& x - shipSize / 2 <= ship.x + ship.shipSize / 2) {
					if (y + shipSize / 2 >= ship.y - ship.shipSize / 2
							&& y - shipSize / 2 <= ship.y + ship.shipSize / 2) {
						while ((y + shipSize / 2 >= ship.y - ship.shipSize / 2 && y
								- shipSize / 2 <= ship.y + ship.shipSize / 2)
								&& (x + shipSize / 2 >= ship.x - ship.shipSize
										/ 2 && x - shipSize / 2 <= ship.x
										+ ship.shipSize / 2)) {
							x += x - ship.x;
							y += y - ship.y;
						}
					}
				}
			}
		}
	}

	public void shoot(Ship target, Guns slot) {
		if (slot.delay <= 0) {
			slot.delay = slot.rof;
			target.hp -= slot.dam;
			Main.trails.add(new Trail(this, target));
		}
	}


	public void paint(Graphics2D g2, double scale) {
		// TODO Auto-generated method stub
		// g2.drawString(""+targeted, (int)(x*scale), (int)(y*scale));
		int rx = (int) ((x - shipSize / 2) * scale);
		int ry = (int) ((y - shipSize / 2) * scale);
		int rs = (int) (shipSize * scale);
		g2.drawRect(rx, ry, rs, rs);
		if (showHp > 0 || hp != prevHp) {
			int hpy = ry - rs / 4;
			int hph = rs / 8;
			int hpw = rs * hp / initHp;
			g2.setColor(Color.RED);
			g2.fillRect(rx, hpy, rs, hph);
			g2.setColor(Color.GREEN);
			g2.fillRect(rx, hpy, hpw, hph);
			if (hp != prevHp) {
				showHp = 6;
				g2.setColor(Color.WHITE);
				g2.fillRect(rx + hpw, hpy, rs * (prevHp - hp) / initHp, hph);
				prevHp = hp;
			} else {
				showHp--;
			}
		}
	}

}
