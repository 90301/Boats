package sv.navybattle.pkg1;

import java.awt.Graphics2D;
import java.util.ArrayList;

public class Guns {
	public int dam;
	public int rof;
	public int reloadTime;
	public int magSize;
	public double acc;
	public int rangeMin;
	public int rangeMax;
	public int gun;
	public String gunName;
	public int ammo;
	public int expectedSize;
	public int aoe;//radius of blowing shit up
	public ArrayList<TargetPriority> targets = new ArrayList<TargetPriority>();
	public Guns(int i) {
		// TODO Auto-generated constructor stub
		gun = i;
		if (i==0) {
			gunName="ZeroStar";
			dam = 100;
			rof = 10;
			reloadTime=70;
			magSize=5;
			acc=50;
			rangeMin=20;
			rangeMax=800;
			expectedSize=1;
			aoe=1000;
		}
		if (i==1) {
			gunName="Light atx";
			dam = 50;
			rof = 13;
			reloadTime=70;
			magSize=5;
			acc=60;
			rangeMin=20;
			rangeMax=400;
			expectedSize=1;
			aoe=10;
		}
		if (i==2) {
			gunName="Blaster";
			dam = 100;
			rof = 10;
			reloadTime=70;
			magSize=25;
			acc=80;
			rangeMin=20;
			rangeMax=200;
			expectedSize=1;
			aoe=10;
		}
		if (i==3) {
			gunName="Sniper Max 5000";
			dam = 150;
			rof = 60;
			reloadTime=70;
			magSize=5;
			acc=100;
			rangeMin=20;
			rangeMax=1000;
			expectedSize=5;
			aoe=1;
		}
		if (i==4) {
			gunName="Lazer 2k6";
			dam = 15;
			rof = 1;
			reloadTime=700;
			magSize=500;
			acc=80;
			rangeMin=20;
			rangeMax=150;
			expectedSize=1;
			aoe=1;
		}
		if (i==5) {
			gunName="Small artilery";
			dam = 350;
			rof = 5;
			reloadTime=160;
			magSize=6;
			acc=60;
			rangeMin=20;
			rangeMax=1200;
			expectedSize=50;
			aoe=150;
		}
		if (i==6) {
			gunName="Master Blaster";
			dam = 100;
			rof = 5;
			reloadTime=70;
			magSize=50;
			acc=75;
			rangeMin=20;
			rangeMax=450;
			expectedSize=20;
			aoe=20;
		}
		//t2 size guns
		if (i==7) {
			gunName="BFG Missile";
			dam = 350;
			rof = 5;
			reloadTime=60;
			magSize=8;
			acc=70;
			rangeMin=20;
			rangeMax=1800;
			expectedSize=5;
			aoe=190;
		}
		if (i==8) {
			gunName="STtH Rifle";
			dam = 800;
			rof = 5;
			reloadTime=200;
			magSize=8;
			acc=150;
			rangeMin=20;
			rangeMax=2200;
			expectedSize=80;
			aoe=1;
		}
		if (i==9) {
			gunName="Major Pane";
			dam = 40;
			rof = 2;
			reloadTime=60;
			magSize=15;
			acc=90;
			rangeMin=20;
			rangeMax=1600;
			expectedSize=120;
			aoe=800;
		}
		ammo=magSize;
	}
	int delay = 0;
	
	public void coolDown(){
		delay--;
	}
	public void paint(Graphics2D g2, double scale) {
		
	}
}
