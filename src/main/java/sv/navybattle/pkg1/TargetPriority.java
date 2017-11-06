package sv.navybattle.pkg1;

public class TargetPriority {
	Ship target;
	int priority;
	double distance;
	int e;

	public TargetPriority(Ship ship, int priority, double dist, int e) {
		// TODO Auto-generated constructor stub
		this.target = ship;
		this.priority = priority;
		this.distance = dist;
		this.e = e;
	}

}
