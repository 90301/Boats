/**
 * 
 */
package sv.navybattle.pkg1;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

/**
 * @author tbsw
 * 
 */
public class Ship implements Paintable {

	/**
	 * 
	 */
	public int hp;
	public double x, y;
	public int team;
	public int OTgt;// official target (more for debugging)
	public double CDist;
	public int targeted = 0;
	public int shipSize;
	public double speed;
	public double maxSpeed;
	public int maxHP;
	public int kills = 0;
	ArrayList<Guns> slots = new ArrayList<Guns>();
	Random rand = Main.rand;// need to switch over to seeded random numbers
							// later
	public int shipClass;
	public String shipClassName;
	public int gunClass;
	int enginesHP;// determines speed
	int hullHP;// causes DOT
	int engineerHP;// reduces repair effectiveness as this decreases
	int maxEnginesHP;
	int maxHullHP;
	int maxEngineerHP;
	Team teamA;
	int repairForce;// max repair amount per component
	double rotation = 0;
	public int longestRange;
	public List<Guns> shotBy = Collections
			.synchronizedList(new ArrayList<Guns>());
	public ArrayList<Module> mods = new ArrayList<Module>();
	ArrayList<Ship> nEnemies = new ArrayList<Ship>();
	ArrayList<Ship> sEnemies = new ArrayList<Ship>();
	public int courage = 5;// number between 5 and 100, determines the HP to
							// retreat at through 100-courage
	// at 100 the entity will not retreat.
	public int leadership = 100;// scale from 0-100, boosts command range, 100
								// is full map range
	public int damageDone = 0;
	public int lastDamageDone = 0;
	Sprite localSprite;

	static int ID_CTR = 0;
	public final int id = ID_CTR++;
	public boolean dead = false;

	public Ship(int team, double x, double y) {
		this.x = x;
		this.y = y;
		this.team = team;
		teamA = Main.teamArray.get(team);
		int guns = 1 + rand.nextInt(10);
		int chance = teamA.differentation;
		if (rand.nextInt(100) + 1 < chance) {
			shipClass = 1 + rand.nextInt(7);
		} else {
			shipClass = teamA.preferedShipClass;
		}
		gunClass = 0;
		// ship classes
		if (shipClass == 7) {
			if (teamA.leaders < teamA.leaderCap) {
				teamA.leaders++;
			} else {
				shipClass--;
			}
		}
		while (Main.teamArray.get(team).gold < 10 * (shipClass - 1)) {
			shipClass--;
		}
		Main.teamArray.get(team).gold -= 10 * (shipClass - 1);
		courage = 5 + rand.nextInt(95);

		switch (shipClass) {
		case 0:
			shipClassName = "Zero Star";
			guns = 10;
			this.speed = 25;
			this.hp = 120;
			this.shipSize = 10;
			gunClass = 0;
			enginesHP = 250;
			hullHP = 500;
			engineerHP = 100;
			repairForce = 5;
			break;
		case 1:
			shipClassName = "HS Dingy";
			guns = 2;
			this.speed = 30;
			this.hp = 800;
			this.shipSize = 20;
			gunClass = 0;
			enginesHP = 450;
			hullHP = 50;
			engineerHP = 100;
			repairForce = 5;
			break;
		case 2:
			shipClassName = "Small Standard";
			guns = 4;
			this.speed = 15;
			this.hp = 2000;
			this.shipSize = 30;
			gunClass = 0;
			enginesHP = 1000;
			hullHP = 500;
			engineerHP = 200;
			repairForce = 10;
			break;
		case 3:
			shipClassName = "Seige Swarm";
			guns = 1;
			this.speed = 13;
			this.hp = 2000;
			this.shipSize = 45;
			gunClass = 2;
			enginesHP = 500;
			hullHP = 250;
			engineerHP = 100;
			repairForce = 10;
			break;
		case 4:
			shipClassName = "Mid Class XJ9";
			guns = 8;
			this.speed = 15;
			this.hp = 6000;
			this.shipSize = 80;
			gunClass = 0;
			enginesHP = 1600;
			hullHP = 800;
			engineerHP = 100;
			repairForce = 10;
			break;
		case 5:
			shipClassName = "Mammoth Class";
			guns = 4;
			this.speed = 10;
			this.hp = 12000;
			this.shipSize = 150;
			gunClass = 0;
			enginesHP = 4000;
			hullHP = 2000;
			engineerHP = 900;
			repairForce = 10;
			break;
		case 6:
			shipClassName = "Indy Class";
			guns = 2;
			this.speed = 10;
			this.hp = 24000;
			this.shipSize = 200;
			gunClass = 3;
			enginesHP = 6500;
			hullHP = 3000;
			engineerHP = 500;
			repairForce = 5;
			break;
		case 7:
			shipClassName = "flagship Class";
			guns = 6;
			this.speed = 15;
			this.hp = 100000;
			this.shipSize = 300;
			gunClass = 3;
			enginesHP = 12900;
			hullHP = 7000;
			engineerHP = 5000;
			repairForce = 50;
			mods.add(new Module(0));// add command module
			break;
		}
		maxSpeed = speed;
		maxEnginesHP = enginesHP;
		maxEngineerHP = engineerHP;
		maxHullHP = hullHP;
		localSprite = teamA.sprites.get(shipClass);

		for (int e = 0; e < guns; e++) {
			if (gunClass == 0) {
				// all guns are possible if 0
				slots.add(new Guns((int) (1 + rand.nextInt(6))));
			}
			if (gunClass == 1) {
				// this is a long range gun
				slots.add(new Guns((int) (3)));
			}
			if (gunClass == 2) {
				// small arty
				slots.add(new Guns((int) (5)));
			}
			if (gunClass == 3) {
				// BFG missiles (soon to be T2 size guns)
				slots.add(new Guns((int) (7 + rand.nextInt(3))));
			}
		}
		for (int i = 0; i < slots.size(); i++) {
			Guns gun = slots.get(i);
			if (gun.rangeMax > longestRange)
				longestRange = (int) gun.rangeMax;
		}
		maxHP = hp;
	}

	public void moveChoices() {
		// to do, make this function have the movement AI to decide between
		// movement choices in objects
		if (commandsReceived.size() >= 1) {
			Ship ship = commandsReceived.get(0).targetShip;
			if (ship == null || ship.dead) {
				
			}
			// need to make this a thing at the top of the function
			move(ship.x, ship.y);
			return;
		}
		if (moveTGT != null && hp >= maxHP * (100 - courage) * .01) {
			move(moveTGT.x, moveTGT.y);
			return;
		} else {
			if (hp < maxHP * (100 - courage) * .01) {
				dRetreatAngle();
				return;
			}
		}

	}

	public List<Command> commandsReceived = Collections
			.synchronizedList(new ArrayList<Command>());

	public void commandLink() {
		// decide what to do
		// later add the feature to lock 10 or more targets and prioritize them,
		// should make things fun.
		
		
		
		Command com = null;
		if (cmdTGT != null) {
			com = new Command(cmdTGT);
			if (createTrail(this, cmdTGT)) {
				Main.trails.add(new Trail(this, cmdTGT, Color.MAGENTA));
			}
		} else {

		}
		// send command
		if (leadership >= 1 && com != null) {
			// Whole Map coverage!
			for (int i = 0; i < teamA.ships.size(); i++) {
				Ship ship = teamA.ships.get(i);
				if (ship.team == team) {
					if (determineDistance(this, ship) < leadership * 100
							|| leadership >= 100) {
						ship.receiveCommand(com);

					}
				}
			}
		}
	}
	public Ship cmdTGT;
	public void receiveCommand(Command com) {
		commandsReceived.add(new Command(com.targetShip));
		// will add other things in the future.
	}

	int updateCache = 0;
	int cacheDelay = 120;
	boolean firstUpdate = false;
	public void doTick() {
		for (int i = 0; i < slots.size(); i++) {
			// give info to stats
			slots.get(i).coolDown();
		}
		repairTick();
		if (updateCache <= 0) {
			if (firstUpdate==true) {
				updateCache = cacheDelay;
			} else {
				updateCache = cacheDelay+team;
				firstUpdate = true;
			}
			cacheShipArray(Main.ships, sEnemies, false, true);

		} else {
			cacheShipArray(Main.ships, sEnemies, false, false);
			updateCache--;
		}
		synchronized (commandsReceived) {
		for (ListIterator<Command> it = commandsReceived.listIterator(); it
				.hasNext();) {
			Command com = it.next();
			if (com.timeOut == 0) {
				it.remove();
			} else {
				com.timeOut--;
			}
		}
		}
			moveTGT = null;
		

		for (int i = 0; i < slots.size(); i++) {
			Ship tgt = null;
			if (slots.get(i).delay <= 0) {
				tgt = target(slots.get(i));
			}

			if (tgt != null) {
				if (CDist < slots.get(i).rangeMax) {
					shoot(tgt, slots.get(i));
				} else {
					moveTGT = tgt;
					// NEED to fix this to a ship.
				}
				cmdTGT = tgt;
			}
			
			
			// end of for loop
		}

		// move code goes here
		moveChoices();

		if (mods.size() >= 1) {
			for (int i = 0; i < mods.size(); i++) {
				Module mod = mods.get(i);
				if (mod.mod == 0) {
					if (mod.time <= 0) {
						mod.time = mod.coolDown;
						commandLink();
					} else {
						mod.time--;
					}
				}
				if (mod.mod == 1) {
					// put code here.
				}
			}
		}

		/*
		 * for (int i=commandsReceived.size()-1;i>=0;i--) { Command com =
		 * commandsReceived.get(i); if (com.timeOut==0) { debugText +=
		 * ""+com.timeOut; commandsReceived.remove(i); } else { debugText +=
		 * " "+com.timeOut+" / "; com.timeOut-=1; debugText += " "+com.timeOut;
		 * } }
		 */
	}

	String debugText;
	Ship moveTGT = null;

	public void repairTick() {
		if (engineerHP > 0) {
			int rf = (engineerHP / maxEngineerHP) * repairForce + 1;
			if (engineerHP < maxEngineerHP) {
				engineerHP += repairForce / 10 + 1;
				if (engineerHP > maxEngineerHP) {
					engineerHP = maxEngineerHP;
				}
			}
			if (hullHP < maxHullHP) {
				hullHP += rf;
				if (hullHP > maxHullHP) {
					hullHP = maxHullHP;
				}
			}

			if (enginesHP < maxEnginesHP) {
				enginesHP += rf / 2;
				if (enginesHP > maxEnginesHP) {
					enginesHP = maxEnginesHP;
				}
			}
			if (hp < maxHP - rf && enginesHP == maxEnginesHP
					&& hullHP == maxHullHP) {
				hp += rf;
			}
		} else {
			// if the engineers die
			engineerHP = 1;
			// hp-=repairForce*5;
		}
		if (hullHP < maxHullHP) {
			hp -= (hullHP / maxHullHP) * maxHP / 100;
		}
		if (enginesHP < maxEnginesHP) {
			speed = maxSpeed * (enginesHP / maxEnginesHP);
		} else {
			speed = maxSpeed;
		}

	}

	public void cacheShipArray(ArrayList<Ship> ships,
			ArrayList<Ship> cacheShips, boolean clear, boolean rescan) {
		if (clear) {
			cacheShips.clear();
		}
		// adding new ships based on distance
		if (clear || cacheShips.size() <= 0) {
			Ship closestShip = null;
			double closestDistance = -1;
			for (ListIterator<Ship> ir = ships.listIterator(); ir.hasNext();) {
				Ship ship = ir.next();
				
				if (ship.hp > 0 && ship.team != team) {
					double dist = determineDistance(ship, this);
					if (dist < longestRange * 2) {
						cacheShips.add(ship);
					} else {
						if (dist < closestDistance || closestDistance <= 0) {
							closestDistance = dist;
							closestShip = ship;
						}
					}
				}
			}
			if (cacheShips.size() <= 0) {
				cacheShips.add(closestShip);
			}
		} else {
			if (rescan) {
			for (ListIterator<Ship> ir = ships.listIterator(); ir.hasNext();) {
				Ship ship = ir.next();
				
				if (ship.hp > 0 && ship.team != team) {
					double dist = determineDistance(ship, this);
					if (dist < longestRange * 2) {
						if (cacheShips.contains(ship)!=true) {
							cacheShips.add(ship);
						}
					}
				}
			}
			}
		}

		// purging Ships
		for (ListIterator<Ship> is = cacheShips.listIterator(); is.hasNext();) {
			Ship ship = is.next();
			if (ship.dead || ship.hp <= 0) {
				is.remove();
			}
		}
	}

	public Ship lastTarget = null;

	public Ship target(Guns slot) {
		double closestDist = -1;
		Ship entity = null;
		
		if (lastTarget!=null) {
			//System.out.println(lastTarget.id);
			double dist = determineDistance(this,lastTarget);
			if (dist <= slot.rangeMax && lastTarget.hp>0 && lastTarget.dead != true) {
				entity = lastTarget;
				return entity;
			}
			
		}
		
		closestDist = -1;
		debugText = "xd: "+closestDist;
		for (int i = 0; i < sEnemies.size(); i++) {
			Ship ship = sEnemies.get(i);
			if (ship.team != team) {
				double dist = determineDistance(this,ship);
				if (dist > slot.rangeMin && ((closestDist <= -1 || closestDist > dist))	&& ship.hp > 0) {
					closestDist = dist;
					
					entity = ship;
				}
				// Separate code in the event there are multiple targets
				if (dist <= slot.rangeMax && ship.hp > 0) {
					int tp = (int) (Math.pow(ship.shipSize, 2) / Math.pow(
							slot.expectedSize, 2));
					if (tp > 100)
						tp = 100;
					tp += 10 - (ship.hp * 10) / ship.maxHP;
					slot.targets.add(new TargetPriority(ship, tp, dist, i));
				}
			}
		}
		CDist = closestDist;
		int priority = -1;
		// A value of -1 will be sent if there is nothing to target
		for (int i = (slot.targets.size()) - 1; i >= 0; i--) {
			TargetPriority tp = slot.targets.get(i);
			if (tp.priority > priority) {
				priority = tp.priority;
				entity = tp.target;
				lastTarget = entity;
			}
			slot.targets.remove(i);
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
			rotation = Math.atan2(iy, ix);
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
							ship.x += (ship.x - x) / 2;
							ship.y += (ship.y - y) / 2;
							x += x - ship.x;
							y += y - ship.y;
						}
					}
				}
			}
		}
	}

	public boolean createTrail(Ship e1, Ship e2) {
		for (int i = 0; i < Main.trails.size(); i++) {
			Trail trail = Main.trails.get(i);
			if (e1.equals(trail.e1) && e2.equals(trail.e2)) {
				return false;
			}
		}
		return true;
	}

	public void shoot(Ship target, Guns slot) {
		// time to make this far more complex -Dec 18th 2013
		if (slot.delay <= 0) {
			slot.delay = slot.rof;
			slot.ammo--;
			if (slot.ammo <= 0) {
				slot.ammo = slot.magSize;
				slot.delay = slot.reloadTime + slot.rof;
			}

			double hitChance = slot.acc
					* (Math.pow(target.shipSize, 2) / Math.pow(
							slot.expectedSize, 2));
			if (rand.nextDouble() * 100 > 100 - hitChance) {
				if (slot.aoe == 1) {
					target.takeDamage(slot.dam);
					addShotBy(slot, target);
					damageDone += slot.dam;
				} else {
					aoeDam(target, slot);
				}
				if (Main.vectorTrails) {
					if (createTrail(this, target))
						Main.trails.add(new Trail(this, target));
				}

			} else {
				if (Main.vectorTrails) {
					if (createTrail(this, target))
						Main.trails.add(new Trail(this, target, Color.GRAY));
				}
			}
		}
		if (damageDone > 1000 && damageDone > lastDamageDone) {
			damageUpdate();
		}
	}

	private void damageUpdate() {
		// TODO Auto-generated method stub
		lastDamageDone = damageDone;

		for (int i = 0; i < teamA.mostDamageDone.size(); i++) {
			Ship ship = teamA.mostDamageDone.get(i);
			if (ship == this) {
				//teamA.mostDamage();
				return;
			}
		}
		teamA.mostDamageDone.add(this);
		teamA.mostDamage();

	}

	public void aoeDam(Ship target, Guns slot) {
		for (int i = Main.ships.size() - 1; i >= 0; i--) {
			Ship ship = Main.ships.get(i);
			if (ship.team != team && ship.hp > 0) {
				// checking distance
				double dist = determineDistance(ship.x, target.x, ship.y,
						target.y) - target.shipSize - ship.shipSize;
				if (dist < slot.aoe) {
					// Actual AOE damage done here
					double dam = slot.dam * ((slot.aoe - dist) / slot.aoe);
					ship.takeDamage((int) dam + 1);
					damageDone += dam + 1;
					if (Main.vectorTrails) {
						if (createTrail(target, ship))
							Main.trails.add(new Trail(target, ship, new Color(
									0xaa0000)));
					}
					addShotBy(slot, ship);
				}
			}
		}
	}

	public void addShotBy(Guns gun, Ship ship) {
		Boolean redundent = false;
		for (int i = 0; i < ship.shotBy.size(); i++) {
			Guns nShotBy = ship.shotBy.get(i);
			if (nShotBy == gun) {
				redundent = true;
			}
		}
		if (redundent != true) {
			ship.shotBy.add(gun);
		}

	}

	public double determineDistance(Ship ship1, Ship ship2) {

		return determineDistance(ship1.x, ship2.x, ship1.y, ship2.y);
	}

	public double determineDistance(double x1, double x2, double y1, double y2) {
		double dist = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));

		return dist;
	}

	public void dNearbyEnemies() {
		// find enemies that are close, right now it is going to be based on a
		// static distance.
		double nearDist = 3000+longestRange*3;
		nEnemies.clear();
		for (int i = Main.ships.size() - 1; i >= 0; i--) {
			Ship ship = Main.ships.get(i);
			if (ship.team != team) {
				double dist = Math.sqrt(Math.pow((ship.x - x), 2)
						+ Math.pow((ship.y - y), 2))
						- ship.shipSize;
				if (dist <= nearDist) {
					nEnemies.add(ship);
				}

			}
		}
	}

	public void dRetreatAngle() {
		dNearbyEnemies();
		if (nEnemies.size() > 0) {
			double avgAngle = 0;
			for (int i = 0; i < nEnemies.size(); i++) {
				Ship ship = nEnemies.get(i);
				double nX = x - ship.x;
				double nY = y - ship.y;
				// double hypot=Math.sqrt(nX*nX+nY*nY);
				double angle = Math.atan2(nY, nX);
				if (nX > 0 && nY > 0) {
					angle = Math.atan2(nY, nX);
				}
				avgAngle = avgAngle + angle;
				// Main.trails.add(new Trail(this,ship,new Color(0xff0000)));

			}
			avgAngle /= nEnemies.size();
			// System.out.println(avgAngle/Math.PI+ " x: " + Math.cos(avgAngle)
			// + " y: " + Math.sin(avgAngle));
			move(x + Math.cos(avgAngle) * 200, y + Math.sin(avgAngle) * 200);
		}
	}

	public void takeDamage(int damageTaken) {
		hp -= damageTaken;
		if (rand.nextInt(100) > 75) {
			if (enginesHP > 0) {
				enginesHP -= damageTaken;
			}
			if (enginesHP < 0) {
				enginesHP = 0;
			}
		}
		if (hullHP > 0) {
			hullHP -= damageTaken;
		}
		if (hullHP < 0) {
			hullHP = 0;
		}
		if (rand.nextInt(100) > 50) {
			if (engineerHP > 0) {
				engineerHP -= damageTaken;
			}
			if (engineerHP < 0) {
				engineerHP = 0;
			}
		}

	}

	public void onDeath() {
		dead = true;
		Main.teamArray.get(team).addKilledBy(shotBy);
		Main.teamArray.get(team).addShipClass(shipClass);
	}

	@Override
	public void paint(Graphics2D g2, double scale, double mapX, double mapY) {
		// TODO Auto-generated method stub
		// g2.drawString(""+targeted, (int)(x*scale), (int)(y*scale));

		if (Main.vectorGraphics) {
			g2.drawRect((int) ((x + mapX - shipSize / 2) * scale), (int) ((y
					+ mapY - shipSize / 2) * scale), (int) (shipSize * scale),
					(int) (shipSize * scale));
			if (sEnemies.size()>1) {
			g2.drawOval((int) ((x + mapX - longestRange / 2) * scale), (int) ((y
					+ mapY - longestRange / 2) * scale), (int) (longestRange * scale),
					(int) (longestRange * scale));
			}
			if (hp * 100 / maxHP >= 50) {
				g2.setColor(Color.GREEN);
			} else {
				if (hp * 100 / maxHP >= 25) {
					g2.setColor(Color.YELLOW);
				} else {
					g2.setColor(Color.red);
				}
			}

			g2.fillRect((int) ((mapX + x - shipSize / 2) * scale), (int) ((y
					+ mapY - shipSize / 2 - 10)
					* scale - 1), (int) ((hp * shipSize / maxHP) * scale + 1),
					(int) (10 * scale + 1));
			
			
		}
		/*
		 * g2.drawString("" + ((hp * 100) / maxHP), (int) ((x + mapX) * scale) -
		 * 8, (int) ((y + mapY - shipSize / 2) * scale - 8)); g2.drawString("" +
		 * damageDone, (int) ((x + mapX) * scale) - 8, (int) ((y + mapY -
		 * shipSize / 2) * scale - 16));
		 */
		if (Main.rasterGraphics) {
			debugText = "";
			// Sprite sprite = Main.teamArray.get(team).sprites.get(shipClass);
			if (localSprite.scaleS != scale * shipSize / 50) {
				localSprite.scale(scale * shipSize / 50);

			}
			//g2.drawString(": "+debugText,(int)((x+mapX)*scale)-8,(int)((y+mapY-shipSize)*scale)-8);
			localSprite.rotate(rotation);

			g2.drawImage(localSprite.modImg, localSprite.rotateOp, (int) ((x
					+ mapX - shipSize / 2) * scale),
					(int) ((y + mapY - shipSize / 2) * scale));
		}
		/*
		 * g2.drawString("eg:"+((engineerHP*100)/maxEngineerHP),(int)((x+mapX)*scale
		 * )-8,(int)((y+mapY-shipSize)*scale)-8);
		 * g2.drawString("h:"+((hullHP*100
		 * )/maxHullHP),(int)((x+mapX)*scale)-8,(int
		 * )((y+mapY-shipSize)*scale)-16);
		 * g2.drawString("en:"+((enginesHP*100)/maxEnginesHP
		 * ),(int)((x+mapX)*scale)-8,(int)((y+mapY-shipSize)*scale)-24);
		 */
	}

}
