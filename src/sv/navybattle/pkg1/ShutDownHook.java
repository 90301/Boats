package sv.navybattle.pkg1;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class ShutDownHook extends Thread {

	public ShutDownHook() {
		// TODO Auto-generated constructor stub
		
	}
	public void run() {
		
		if (Main.savePerformanceStats && (Main.simsRun >= Main.simsToRun || Main.saveprematrue)) {
			String statString = new String();
			for (int i=0;i<Main.performanceTicks.size();i++) {
				PerformanceTick pt = Main.performanceTicks.get(i);
				statString += pt.var1 + "," + pt.var2 + "\r";
			}
			System.out.println(statString);
			
			String dataFile= Main.saveDirectory + "PerformStats" + System.currentTimeMillis()+".csv";
			
			ObjectOutputStream out = null;
			try {
				
				out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(dataFile)));
				out.writeObject(statString);				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					out.close();
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		/*
		if (Main.saveReplay && Main.simsRun >= Main.simsToRun) {
		String replayFile = Main.saveDirectory+"replay" + System.currentTimeMillis()+".txt";
		
		ObjectOutputStream out = null;
		try {
			
			out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(replayFile)));
			out.writeObject(Main.replay);				
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				out.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		}
		*/
	}
	public ShutDownHook(Runnable target) {
		super(target);
		// TODO Auto-generated constructor stub
	}

	public ShutDownHook(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public ShutDownHook(ThreadGroup group, Runnable target) {
		super(group, target);
		// TODO Auto-generated constructor stub
	}

	public ShutDownHook(ThreadGroup group, String name) {
		super(group, name);
		// TODO Auto-generated constructor stub
	}

	public ShutDownHook(Runnable target, String name) {
		super(target, name);
		// TODO Auto-generated constructor stub
	}

	public ShutDownHook(ThreadGroup group, Runnable target, String name) {
		super(group, target, name);
		// TODO Auto-generated constructor stub
	}

	public ShutDownHook(ThreadGroup group, Runnable target, String name,
			long stackSize) {
		super(group, target, name, stackSize);
		// TODO Auto-generated constructor stub
	}

}
