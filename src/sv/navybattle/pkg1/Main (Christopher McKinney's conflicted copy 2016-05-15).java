
package sv.navybattle.pkg1;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import sv.navybattle.ntd5.replay.BoatsReplayLoader;
import sv.navybattle.ntd5.replay.BoatsReplaySaver;

/**
 * @author tbsw
 * 
 */
public class Main {
	/*
	 * To-Do List: squad and leader functionality more content for teams teams
	 * linked list working and content migrate stats to team object
	 */

	/**
	 * @param args
	 */
	static int simSpeed = 5;
	static int teams = 16;
	static int initialShips = 5000;
	static int simsToRun = 1;// set to -1 to be infinite?
	static int simsRun = 0;
	static int shipsPlus = 0;
	static boolean ddp = false;
	static Random rand = new Random(0544);
	static ArrayList<Ship> ships = new ArrayList<Ship>();
	static int spawnSize = 10000;

	static double iScale = .002;
	static double distroL = 1;
	static double distroU = 1;
	static int distroD = (int) Math.floor(Math.sqrt(teams));
	static ArrayList<Double> stats = new ArrayList<Double>();
	static boolean reStat = true;
	static List<Trail> trails = Collections
			.synchronizedList(new ArrayList<Trail>());
	public static ArrayList<Color> teamColor = new ArrayList<Color>();
	static ArrayList<Team> teamArray = new ArrayList<Team>();
	static int threads = Runtime.getRuntime().availableProcessors();
	static int totalGuns = 10;// number subject to change based on guns
	static int totalShipClasses = 8;
	static int frameSkip = 0;
	static int frame = 0;
	static double SPF = 0;
	static ArrayList<String> gunNames = new ArrayList<String>();
	// graphics settings
	static boolean vectorGraphics = true;
	static boolean rasterGraphics = false;
	static boolean vectorTrails = false;
	public static final boolean drawTeamStats = false;
	
	static boolean network = true;//network compute?
	static boolean slave = false;//if slave, set to true, this should be asked if network compute is on
	static boolean loadStats = false;
	static boolean saveStats = false;
	static boolean savePerformanceStats = true;
	static boolean saveReplay = true;
	static boolean playReplay = true;
	static boolean useChrisIO = true;
	public static int deflateIntensity = 2;
	static BoatsReplaySaver saver;
	static BoatsReplayLoader loader;
	static List<Replay> replay = Collections.synchronizedList(new ArrayList<Replay>());
	static List<Replay> replayPlayback = Collections.synchronizedList(new ArrayList<Replay>());
	static ArrayList<PerformanceTick> performanceTicks = new ArrayList<PerformanceTick>();
	public static final boolean saveprematrue = true;
	public static String saveDirectory = (System.getProperty("os.name").startsWith("Windows")) ?
			"E:\\" : (System.getProperty("user.home") + File.separator);
	public static void main(String[] args) throws InterruptedException,
			ExecutionException {
		//System.err.println("STDERR TEST");
		MainArgs.handle(args);
		
		// technically expandable
		teamColor.add(Color.GREEN);
		teamColor.add(Color.CYAN);
		teamColor.add(Color.RED);
		teamColor.add(Color.YELLOW);
		teamColor.add(new Color(0xAAAAFF));
		teamColor.add(new Color(0x0033DD));
		teamColor.add(new Color(0xAA4444));
		teamColor.add(new Color(0xAAAAAA));
		teamColor.add(new Color(0xFFFFFF));
		teamColor.add(new Color(0xFFAA00));
		teamColor.add(new Color(0x00FFAA));
		teamColor.add(new Color(0xAA00FF));
			for (int i=1;i<teams-11;i++) {
				teamColor.add(new Color((0x000001*i+0x000044*(i%4)+0x440000*((int)Math.floor((i/4^2)))+0x004400*(int)Math.floor((i/4)))));
				System.out.println(teamColor);
			}
		
		
		addGunNames();
		
		Thread hook = new ShutDownHook();
		Runtime.getRuntime().addShutdownHook(hook);

		for (int i = 0; i < teams; i++) {
			teamArray.add(new Team(teamColor.get(i)));
		}
		if (loadStats) {
			teamInStats();
		}
		//Replay Code ------ Replay Code
		if (playReplay) {
			playReplayCode();
		}
		if (savePerformanceStats || saveReplay) {
		int result = fc.showSaveDialog(null);
		if (result == JFileChooser.APPROVE_OPTION) {
			saveDirectory = fc.getSelectedFile().getPath();
		} else {
			savePerformanceStats = false;
			saveReplay = false;
		}
		}
		if (saveReplay && useChrisIO) {
			try {
				saver = new BoatsReplaySaver(saveDirectory+"replay" + time + ".replay");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// frame code
		JFrame f = new JFrame("Navy Sim");
		f.setBackground(Color.black);
		f.setForeground(Color.GREEN);
		f.setBounds(10, 10, (int) (800 * (1 + distroL)), 800);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setContentPane(new UIDraw());
		f.setVisible(true);
		
		if (network==true&&slave==true) {
			//network slave code here
		}
		if (network==true&&slave==false){
			//master code goes here
		}
		ExecutorService executor = Executors.newFixedThreadPool(threads);
		while (simsRun < simsToRun || simsToRun <= 0) {
			initialShips += shipsPlus;
			for (int i = 0; i < teamArray.size(); i++) {
				teamArray.get(i).leaders = 0;
			}
			for (int i = 0; i < initialShips; i++) {
				int team = rand.nextInt(teams);
				// even distros
				Ship shipn = new Ship(team, rand.nextDouble() * spawnSize
						+ spawnSize + spawnSize * distroL
						* (Math.floor((team) / distroD) - 1), spawnSize
						* (team % distroD) * distroU + rand.nextDouble()
						* spawnSize);
				ships.add(shipn);
				teamArray.get(team).ships.add(shipn);

			}
			ddp = false;
			while (ddp != true) {

				for (int i = ships.size() - 1; i >= 0; i--) {
					ships.get(i).targeted = 0;
				}
				frame++;// adds one to the frame value, when it reaches or is
						// above frameSkip trigger repaint
				ArrayList<Future<?>> futures = new ArrayList<Future<?>>();
				for (int i = ships.size() - 1; i >= 0; i--) {
					// ships.get(i).doTick();
					Runnable workerThread = new TickThread(i, 1);
					futures.add(executor.submit(workerThread));
				}
				for (int i = 0; i < futures.size(); i++) {
					futures.get(i).get();
				}
				futures.clear();
				/*
				 * for (int i=ships.size()-1; i>=0;i--) {
				 * //ships.get(i).repairTick(); Runnable workerThread = new
				 * TickThread(i,2); futures.add(executor.submit(workerThread));
				 * } for (int i=0;i<ships.size();i++) { futures.get(i).get(); }
				 */
				for (int i = trails.size() - 1; i >= 0; i--) {
					Trail trail = trails.get(i);
					try {
						trail.doTick();
						if (trail.time <= 0) {
							trails.remove(i);
						}
					} catch (NullPointerException e) {
						System.out.println("Null pointer: I:" + i + "/"
								+ trails.size() + "trail:" + trail + "ti:"
								+ trails.get(i));
						trails.remove(i);
					}

				}
				reStat = false;
				for (int i = ships.size() - 1; i >= 0; i--) {
					if (ships.get(i).hp <= 0) {
						// Ship Death
						Ship ship = ships.get(i);
						ship.onDeath();
						teamArray.get(ship.team).ships.remove(ship);
						ships.remove(i);
					}
				}
				if (frame >= frameSkip) {
					f.repaint();
					// replay code
					// -----------------------------------------------------------------------
					// replay code
					if (saveReplay && useChrisIO) {
						Replay nextReplay = new Replay(ships,trails);
						//replay.add(nextReplay);
						
						try {
							if (headerWritten!=true) {
							saver.writeReplayHeader(nextReplay);
							headerWritten=true;
							}
							saver.writeReplayFrame(nextReplay);
							
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					frame = 0;
					SPF = System.currentTimeMillis() - time;
					time = System.currentTimeMillis();
					if (ships.size() > 100 && SPF > 25 && savePerformanceStats)
						performanceTicks.add(new PerformanceTick(SPF, ships
								.size()));

					try {
						Thread.sleep(simSpeed);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				int teamsLeft = 0;
				for (int i = 0; i < teamArray.size(); i++) {
					if (teamArray.get(i).ships.size() >= 1) {
						teamsLeft++;
					}
				}

				if (teamsLeft <= 1) {
					if (saveStats)
						teamOutStats();
					ships.clear();
					for (int i = teamArray.size() - 1; i >= 0; i--) {
						teamArray.get(i).ships.clear();
					}

					ddp = true;
					simsRun++;
				}
			}
		}
	}
	public static boolean headerWritten = false;
	final static JFileChooser fc = new JFileChooser();

	
	
	private static void playReplayCode() {
		// TODO Auto-generated method stub
		int returnValue = fc.showOpenDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File file =  fc.getSelectedFile();
			String replayLocation = file.getAbsolutePath();
			
			ObjectInputStream in = null;
			boolean sucsess = true;
			try {
				//in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(replayLocation)));
				loader = new BoatsReplayLoader(replayLocation);
				loader.readHeader();
				//replayPlayback = (List<Replay>) in.readObject();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				sucsess = false;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				sucsess=false;
			} finally {
				try {
					if (sucsess && in != null) 
						in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			JFrame frame2 = new JFrame("Replay Sim");
			frame2.setBackground(Color.black);
			frame2.setForeground(Color.GREEN);
			frame2.setBounds(10, 10, (int) (800 * (1 + distroL)), 800);
			frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame2.setContentPane(new ReplayUIDraw());
			frame2.setVisible(true);
			while (true) {
			for (int i=2; i<loader.getTotalFrames();i++) {
				
				
				Replay r;
				try {
					r = loader.readReplayFrame(i);
					currentReplay = r;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
				frame2.repaint();
				startReplay = true;
				try {
					Thread.sleep(simSpeed);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			}
			
			
		}
	}
	static Replay currentReplay;

	// metrics
	static long time = System.currentTimeMillis();
	public static boolean startReplay;

	public static void teamOutStats() {
		for (int i = 0; i < teamArray.size(); i++) {
			Team team = teamArray.get(i);
			team.outStats();
		}
	}

	public static void teamInStats() {
		for (int i = 0; i < teamArray.size(); i++) {
			Team team = teamArray.get(i);
			team.inStats();
			System.out.println("" + team.killedBy);
		}
	}

	private static void addGunNames() {
		for (int i = 0; i < totalGuns; i++) {
			Guns g = new Guns(i);
			gunNames.add(g.gunName);
		}

	}

}