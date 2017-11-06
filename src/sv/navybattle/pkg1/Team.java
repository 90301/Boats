package sv.navybattle.pkg1;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class Team implements Paintable{
	List<Ship> ships = Collections.synchronizedList(new LinkedList<Ship>());
	int gold;
	Color teamColor;
	int teamNumber;
	public int preferedShipClass=1;
	public int leaderCap = 1;//max command ships, really only here for performance issues.
	public int leaders = 0;
	public int differentation = 90;// how much varriation in spawning ship types 0-100 (100 = pure random, 0= all the same)
	//stats for the teams will be added later
	List<Integer> killedBy = new ArrayList<Integer>();//The numbers correspond to the weapon number
	//so killedBy[0] would be gun 0 and the int returned would be the number of times something has been
	//killed by that weapon
	int MEW = 0;
	List<Integer> shipClassDeaths = new ArrayList<Integer>();
	//same scheme as killedBy for weapons
	List<Sprite> sprites = new ArrayList<Sprite>();
	List<Ship> mostDamageDone = Collections.synchronizedList(new ArrayList<Ship>());
	public Team(Color teamColor) {
		// TODO Auto-generated constructor stub
		this.teamColor = teamColor;
		this.teamNumber = Main.teamColor.indexOf(teamColor);
		gold = 200000;
		for (int i=0;i<Main.totalGuns;i++) {
			killedBy.add(0);
		}
		for (int i=0;i<Main.totalShipClasses;i++) {
			shipClassDeaths.add(0);
		}
		for (int i = 0; i<Main.totalShipClasses;i++) {
			sprites.add(new Sprite(i));
		}
	}
	public void teamTick() {
		
	}
	public void addKilledBy(List<Guns> shotBy) {
		//the data on what the ships that were killed are sent to the team database
		//to find the most effective weapons.
		for (int i=0;i<shotBy.size();i++) {
			Guns gun =shotBy.get(i);
			int g = gun.gun;
			killedBy.set(g, killedBy.get(g)+1);
		}
		//System.out.println(killedBy);
		int mostKills=0;
		int MEWIndex=0;
		for (int i=0;i<Main.totalGuns;i++) {
			int kills = killedBy.get(i);
			if (kills>mostKills) {
				mostKills = kills;
				MEWIndex = i;
			}
		}
		MEW = MEWIndex;
	}
	int mostDamage = 0;
	public Ship mostDamageShip; 
	public void mostDamage() {
		mostDamage = 0;
		synchronized (mostDamageDone) {
		for (ListIterator<Ship> it = mostDamageDone.listIterator(); it.hasNext(); ) {
			Ship ship = it.next();
			if (ship.damageDone > mostDamage) {
				mostDamage = ship.damageDone;
				mostDamageShip = ship;
			}
		}
		}
	}
	public void addShipClass(int shipClass) {
		shipClassDeaths.set(shipClass,shipClassDeaths.get(shipClass)+1);
		//look for least deaths
		int lDeath = shipClassDeaths.get(0);
		preferedShipClass=0;
		for (int i=0;i<shipClassDeaths.size();i++) {
			if (shipClassDeaths.get(i)<lDeath) {
				lDeath = shipClassDeaths.get(i);
				preferedShipClass = i;
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void inStats() {
		String dataFile= "/Team" + teamNumber+".txt";
		ObjectInputStream in = null;
		boolean sucsess = true;
		try {
			in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(dataFile)));
			try {
				killedBy = (List<Integer>) in.readObject();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			sucsess = false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			sucsess=false;
		} finally {
			try {
				if (sucsess)
					in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void outStats() {
		String dataFile= "/Team" + teamNumber+".txt";
		ObjectOutputStream out = null;
		try {
			
			out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(dataFile)));
			out.writeObject(killedBy);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	@Override
	public void paint(Graphics2D g2, double scale, double mapX, double mapY) {
		// TODO Auto-generated method stub
		if (Main.drawTeamStats) {
		g2.setColor(teamColor);
		String mdtext;
		if (mostDamageShip == null) {
			mdtext = "none";
		} else {
			mdtext = mostDamageShip.shipClassName;
		}
		g2.drawString("Gold: " +gold, 20, 200+teamNumber*10);
		g2.drawString("Most Effective Weapon:" + Main.gunNames.get(MEW)+ "\t"+ " \t Deaths: " + killedBy.get(MEW) +"\t"+ " \t Most damage done: " + mostDamage + " \t Done By: " + mdtext, 80, 200+teamNumber*10);
		g2.drawString("ship Class Deaths:"+shipClassDeaths.toString() + " Prefered Ship Class:"+preferedShipClass, 20, 200+Main.teams*10+teamNumber*10);
		}
	}
	

}
