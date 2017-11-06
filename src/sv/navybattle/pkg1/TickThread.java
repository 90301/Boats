package sv.navybattle.pkg1;

public class TickThread implements Runnable {

	int i,type;
	public TickThread(int i,int type) {
		// TODO Auto-generated constructor stub
		this.i=i;
		this.type=type;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		if (type==1) {
		if (i<Main.ships.size()&&i>=0) {
			Main.ships.get(i).doTick();
		}
		}
		if (type==2) {
			if (i<Main.ships.size()&&i>=0) {
				Main.ships.get(i).repairTick();
			}
		}
	}

}
