/**
 * 
 */
package sv.navybattle.pkg1;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.JComponent;

/**
 * @author tbsw
 *
 */
public class UIDraw extends JComponent {

	
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	double scale = Main.iScale;
	double x,y=0;
	boolean ff=false;
	{
		// handles mouse input
		MouseAdapter mouseAdapter = new MouseAdapter() {
			// the location of the mouse at previous update within a drag
			Point prevDragPoint = null;

			@Override
			public void mousePressed(MouseEvent e) {
				// drag started
				prevDragPoint = e.getPoint();
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				// the location of the mouse in the component
				Point pt = e.getPoint();
				// just to be safe
				if (prevDragPoint != null) {
					// move the origin (and thus the view) based on mouse
					// dragging
					int dx = pt.x - prevDragPoint.x;
					int dy =  pt.y - prevDragPoint.y;
					x+=dx/scale;
					y+=dy/scale;
				}
				prevDragPoint = pt;
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// drag ended
				prevDragPoint = null;
			}

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				// calculate new raw scale
				double rotation = e.getPreciseWheelRotation();
				if (rotation >0) {
					scale = scale*.9;
					if (scale<=.0001) {
						scale=.0001;
					}
				} else {
					scale = scale*1.1;
					if (scale>1)
						scale = 1;
				}
				// set the scale
				//setRawScale(newRawScale, e.getPoint());
			}
		};
		// MouseAdapter combines MouseListener, MouseMotionListener, and
		// MouseWheelListener, so set it up to receive events for all three
		addMouseListener(mouseAdapter);
		addMouseMotionListener(mouseAdapter);
		addMouseWheelListener(mouseAdapter);
}
	public void paint (Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		Font f = new Font(Font.SERIF,6, 9);
		g2.setFont(f);
		//g.drawRect(25,25,50,50);
		for (int i=0;i<Main.ships.size();i++) {
			Ship ship = Main.ships.get(i);
			/*switch (ship.team) {
				case 0:g.setColor(Color.GREEN);
				break;
				case 1:g.setColor(Color.BLUE);
				break;
				case 2:g.setColor(Color.red);
				break;
				case 3:g.setColor(Color.YELLOW);
				break;
			}*/
			g.setColor(Main.teamColor.get(ship.team));
			ship.paint(g2,scale,x,y);
		}
		if (ff) {
		for (int i=0;i<Main.trails.size();i++) {
			try {
			Trail trail = Main.trails.get(i);
			trail.paint(g2, scale,x,y);
			} catch (Exception e) {
				System.out.println("The god damn trails glitched again!" + e.toString());
			}
		}
		} else {
			ff=true;
		}
		g.setColor(Color.white);
		//This might be a bit long
		g.drawString("Total Trails: " + Main.trails.size()+ " Total Ships: "+ Main.ships.size() + " mSPF: "+ Main.SPF + " Sims: " + Main.simsRun + " / " + Main.simsToRun + " Scale: " + scale + " Threads:" + Main.threads, 20, 20);
		for (int i=0;i<Main.teamArray.size();i++) {
			Main.teamArray.get(i).paint(g2, scale, x, y);
		}
	}
	

}
