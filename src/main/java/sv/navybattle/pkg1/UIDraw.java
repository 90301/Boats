/**
 * 
 */
package sv.navybattle.pkg1;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;

import javax.swing.JComponent;

/**
 * @author tbsw
 * 
 */
public class UIDraw extends JComponent {

	// where the world's (0, 0) would be drawn in the component
	private Point2D origin = new Point2D.Double(0, 0);
	// rawScale is directly proportional to scroll wheel movement
	private double rawScale = -3;

	// scaling sensitivity; scroll wheel multiplier
	private double scaleSensitivity = 0.125;
	// space between grid lines, in pixels at scale 1
	private double minorGridSpacing = 12;
	private double majorGridSpacing = 120;
	// grid line colors
	private Color minorGridColor = new Color(16, 16, 16);
	private Color majorGridColor = new Color(32, 32, 32);
	// minimum scale (default: 12.5%)
	private double minRawScale = -6;
	// maximum scale (default: 1600%)
	private double maxRawScale = 16;

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
					int dy = pt.y - prevDragPoint.y;
					origin.setLocation(origin.getX() + dx, origin.getY() + dy);
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
				double newRawScale = rawScale + e.getPreciseWheelRotation()
						* scaleSensitivity;
				// set the scale
				setRawScale(newRawScale, e.getPoint());
			}
		};
		// MouseAdapter combines MouseListener, MouseMotionListener, and
		// MouseWheelListener, so set it up to receive events for all three
		addMouseListener(mouseAdapter);
		addMouseMotionListener(mouseAdapter);
		addMouseWheelListener(mouseAdapter);
	}

	public static double toScale(double rawScale) {
		return (rawScale < 1) ? (1 / (2 - rawScale)) : rawScale;
	}

	public void setRawScale(double newRawScale) {
		rawScale = newRawScale;
		// keep rawScale within its bounds
		if (rawScale < minRawScale) {
			rawScale = minRawScale;
		} else if (rawScale > maxRawScale) {
			rawScale = maxRawScale;
		}
		// set the scale based on rawScale
		scale = toScale(rawScale);
	}

	public void setRawScale(double newRawScale, Point2D center) {
		// the current scale
		double oldScale = scale;
		// set the scale
		setRawScale(newRawScale);
		// adjust origin so that zooming is centered on the mouse
		double factor = scale / oldScale;
		// center coordinates
		double cx = center.getX();
		double cy = center.getY();
		// calculate new origin
		double ox = factor * (origin.getX() - cx) + cx;
		double oy = factor * (origin.getY() - cy) + cy;
		// set origin to new location
		origin.setLocation(ox, oy);
	}

	protected void drawScaledGridLines(Graphics2D g2, double gridSpacing) {
		// adjust the spacing for scale
		gridSpacing *= scale;
		// draw the vertical lines
		for (double gx = origin.getX() % gridSpacing; gx < getWidth(); gx += gridSpacing) {
			g2.drawLine((int) gx, 0, (int) gx, getHeight());
		}
		// draw the horizontal lines
		for (double gy = origin.getY() % gridSpacing; gy < getHeight(); gy += gridSpacing) {
			g2.drawLine(0, (int) gy, getWidth(), (int) gy);
		}
	}

	public void drawGridLines(Graphics2D g2) {
		// draw minor grid lines
		if (scale < 1) {
			final Color c = minorGridColor;
			g2.setColor(new Color((int) (c.getRed() * scale), (int) (c
					.getGreen() * scale), (int) (c.getBlue() * scale)));
		} else {
			g2.setColor(minorGridColor);
		}
		if (scale >= 0.25) {
			drawScaledGridLines(g2, minorGridSpacing);
		}
		// draw major grid lines
		g2.setColor(majorGridColor);
		drawScaledGridLines(g2, majorGridSpacing);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	double scale = toScale(rawScale);// .1;
	double x, y;

	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		drawGridLines(g2);
		
		g.setColor(Color.white);
		//for (int i = 0; i < Main.stats.size(); i++)
		//{ g2.drawString("" + Main.stats.get(i), 200 + 100 * i, 800); }
		
		//g2.drawString("" + (int) Main.fps, 0, 12);
		
		g2.translate(origin.getX(), origin.getY());
		
		// g.drawRect(25,25,50,50);
		for (int i = 0; i < Main.ships.size(); i++) {
			Ship ship = Main.ships.get(i);
			/*
			 * switch (ship.team) { case 0:g.setColor(Color.GREEN); break; case
			 * 1:g.setColor(Color.BLUE); break; case 2:g.setColor(Color.red);
			 * break; case 3:g.setColor(Color.YELLOW); break; }
			 */
			g.setColor(Main.teamColor.get(ship.team));
			// g.drawRect((int)(ship.x*scale), (int)(ship.y*scale),
			// (int)(5*scale), (int)(5*scale));
			ship.paint(g2, scale);
			// g.setColor(Color.PINK);
			// g.drawLine((int)ship.x + 2, (int)ship.y + 2,
			// (int)Main.ships.get(ship.OTgt).x + 2,
			// (int)Main.ships.get(ship.OTgt).y + 2);
			/*
			 * for (int e=0; e<ship.slots.size(); e++) {
			 * ship.slots.get(e).paint(g2,scale); }
			 */
		}
		for (int i = 0; i < Main.trails.size(); i++) {
			Trail trail = Main.trails.get(i);
			trail.paint(g2, scale);
		}
	
	}

}
