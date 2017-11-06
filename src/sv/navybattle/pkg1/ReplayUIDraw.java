package sv.navybattle.pkg1;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;
import java.util.ListIterator;

import javax.swing.JComponent;

public class ReplayUIDraw extends JComponent {
	
	private static final long serialVersionUID = 1L;
	
	
	double scale = Main.iScale;
	double x,y=0;
	boolean ff=false;
	
	private static final boolean useChrisScaling = false;
	private static final double zoomUnit = .001;
	private static final double scaleSensitivity = 6.125;
	double rawScale = toRawScale(scale);
	
	
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
				if (useChrisScaling) {
					// calculate new raw scale
					double newRawScale = rawScale + e.getPreciseWheelRotation()
							* scaleSensitivity;
					// set the scale
					setRawScale(newRawScale, e.getPoint());
					
				} else {
					
				// calculate new raw scale
				double rotation = e.getPreciseWheelRotation();
				if (rotation >0) {
					scale -=zoomUnit;
					if (scale<=0) {
						scale=zoomUnit;
					}
				} else {
					scale +=zoomUnit;
					if (scale>1)
						scale = 1;
				}
				// set the scale
				//setRawScale(newRawScale, e.getPoint());
				}
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
		if (Main.startReplay) {
		for (int i=0;i<Main.currentReplay.ships.size();i++) {
			ReplayShip ship = Main.currentReplay.ships.get(i);
			
			g.setColor(Main.teamColor.get(ship.team));
			ship.paint(g2,scale,x,y);
		}
		for (ListIterator<ReplayTrail> i = Main.currentReplay.trails.listIterator(); i.hasNext();) {
			ReplayTrail trail = i.next();
			trail.paint(g2,scale,x,y);
		}
		/*if (ff) {
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
		}*/
		/*
		g.setColor(Color.white);
		g.drawString("Total Trails: " + Main.trails.size()+ " Total Ships: "+ Main.ships.size() + " mSPF: "+ Main.SPF + " Sims: " + Main.simsRun + " / " + Main.simsToRun, 20, 20);
		for (int i=0;i<Main.teamArray.size();i++) {
			Main.teamArray.get(i).paint(g2, scale, x, y);
		}
		*/
		}
	}
	
	
	
	
	private static final double
			minRawScale = toRawScale(.001),
			maxRawScale = toRawScale(1);
	
	public static double toRawScale(double scale) {
		return (scale < 1) ? (2 - (1 / scale)) : scale;
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
		x = factor * (x - cx) + cx;
		y = factor * (y - cy) + cy;
	}

}
