package sv.navybattle.pkg1;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.Serializable;

public class ReplayShip implements Paintable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int hp,id,team,shipSize,maxHP;
	public double x,y;
	public boolean isDamaged = false;
	public ReplayShip(Ship ship) {
		// TODO Auto-generated constructor stub
		id = ship.id;
		hp = ship.hp;
		maxHP=ship.maxHP;
		team = ship.team;
		shipSize=ship.shipSize;
		x = ship.x;
		y = ship.y;
		isDamaged = hp != maxHP;
	}
	
	public ReplayShip() {
	}

	@Override
	public void paint(Graphics2D g2, double scale, double mapX, double mapY) {
		// TODO Auto-generated method stub
		if (Main.vectorGraphics) {
			g2.drawRect((int) ((x + mapX - shipSize / 2) * scale), (int) ((y
					+ mapY - shipSize / 2) * scale), (int) (shipSize * scale),
					(int) (shipSize * scale));
		}
		if (hp*100/maxHP>=50) {
			g2.setColor(Color.GREEN);
		} else {
			if (hp*100/maxHP>=25) {
				g2.setColor(Color.YELLOW);
			} else {
				g2.setColor(Color.red);
			}
		}
			
			g2.fillRect((int) ((mapX + x-shipSize/2)*scale), (int) ((y+ mapY - shipSize / 2-10) * scale-1),(int) ((hp*shipSize/maxHP)*scale+1),(int) (10*scale+1));
		
			/*g2.drawString("" + ((hp * 100)/maxHP),
				(int) ((x + mapX) * scale) - 8,
				(int) ((y + mapY - shipSize / 2) * scale - 8));
				*/
		
	}

}
