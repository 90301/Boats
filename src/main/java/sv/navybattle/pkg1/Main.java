/**
 * 
 */
package sv.navybattle.pkg1;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;

/**
 * @author tbsw
 * 
 */
public class Main {

	/**
	 * @param args
	 */
	static int simInterval = 20;
	static int teams = 4;
	static int initialShips = 1000;
	static boolean ddp = false;
	static Random rand = new Random();
	static ArrayList<Ship> ships = new ArrayList<Ship>();
	static int spawnSize = 2500;
	static double distroL = 0.5;
	static double distroU = 0.5;
	static ArrayList<Double> stats = new ArrayList<Double>();
	static boolean reStat = true;
	static ArrayList<Trail> trails = new ArrayList<Trail>();
	static ArrayList<Color> teamColor = new ArrayList<Color>();
	static double fps;

	public static void main(String[] args) {
		for (int i = 0; i < teams * 2; i++) {
			stats.add(i, (double) 0);
		}
		teamColor.add(Color.GREEN);
		teamColor.add(Color.CYAN);
		teamColor.add(Color.RED);
		teamColor.add(Color.YELLOW);
		teamColor.add(Color.MAGENTA);
		// TODO Auto-generated method stub
		JFrame f = new JFrame("Navy Sim");
		f.setBackground(Color.black);
		f.setForeground(Color.GREEN);
		f.setBounds(10, 10, 1100, 1100);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setContentPane(new UIDraw());
		f.setVisible(true);
		while (true) {
			for (int i = 0; i < initialShips; i++) {
				int team = rand.nextInt(teams);
				// even distros
				ships.add(new Ship(team, Math.random() * spawnSize + spawnSize
						* (team / (teams / 2)) * (distroL), Math.random() * spawnSize + spawnSize
						* (team % (teams / 2)) * (distroU)));
			}
			ddp = false;
			long tickStartTime = -1;
			while (ddp != true) {
				long curTime = System.currentTimeMillis();
				fps = 1000.0 / (curTime - tickStartTime);
				tickStartTime = curTime;
				if (reStat == true) {
					for (int i = 0; i < teams * 2; i++) {
						stats.set(i, 0.0);
					}
				}
				for (int i = ships.size() - 1; i >= 0; i--) {
					ships.get(i).targeted = 0;
				}
				for (int i = ships.size() - 1; i >= 0; i--) {
					ships.get(i).doTick();
				}
				for (int i = 0; i < trails.size(); i++) {
					Trail trail = trails.get(i);
					trail.doTick();
					if (trail.time <= 0) {
						trails.remove(i);
					}
				}
				reStat = false;
				for (int i = ships.size() - 1; i >= 0; i--) {
					if (ships.get(i).hp <= 0) {
						ships.remove(i);
						reStat = true;
					}
				}
				f.repaint();
				long sleepTime = simInterval + tickStartTime - System.currentTimeMillis();
				if (sleepTime > 0) {
					try {
						Thread.sleep(sleepTime);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				int teamsLeft = 0;
				for (int i = 0; i < teams; i++) {
					if (stats.get(i) != 0.0) {
						teamsLeft++;
					}
				}
				if (teamsLeft <= 1) {
					for (int i = ships.size() - 1; i >= 0; i--) {
						ships.remove(i);
					}
					reStat = true;
					ddp = true;
				}
			}
		}
	}

}