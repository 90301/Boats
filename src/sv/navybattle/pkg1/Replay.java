package sv.navybattle.pkg1;

import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class Replay implements Paintable, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public List<ReplayShip> ships = new ArrayList<ReplayShip>();
	public List<ReplayTrail> trails = new ArrayList<ReplayTrail>();
	public Replay(List<Ship> ships, List<Trail> trails) {
		// TODO Auto-generated constructor stub
		for (ListIterator<Ship> is = ships.listIterator();is.hasNext();) {
			Ship ship = is.next();
			this.ships.add(new ReplayShip(ship));
		}
		for (ListIterator<Trail> is = trails.listIterator();is.hasNext();) {
			Trail trail = is.next();
			this.trails.add(new ReplayTrail(trail));
		}
	}
	public Replay() {
	}

	@Override
	public void paint(Graphics2D g2, double scale, double mapX, double mapY) {
		// TODO Auto-generated method stub

	}

}
