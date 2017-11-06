package sv.navybattle.pkg1;

import java.util.ArrayList;

public class MoveChoice {
	ArrayList<String> types = new ArrayList<String>();
	
	String type;
	double locX,locY;
	public MoveChoice(double locX,double locY) {
		setup();
		// TODO Auto-generated constructor stub
	}
	public MoveChoice(Ship ship) {
		setup();
		// TODO Auto-generated constructor stub
	}
	public void setup() {
		types.add("Evade");
		types.add("Advance");
	}
	public void evade() {
		type = types.get(0);
	}
	public void advance() {
		type = types.get(1);
	}

}
