package sv.navybattle.ntd5.replay;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sv.navybattle.pkg1.Replay;
import sv.navybattle.pkg1.ReplayShip;
import sv.navybattle.pkg1.ReplayTrail;

/* ## Usage ##

BoatsReplayLoader loader = new BoatsReplayLoader(...);
loader.readHeader();
Replay frame1 = loader.readReplayFrame();
Replay frame2 = loader.readReplayFrame();
// Note that the frames are indexed starting from 1, NOT 0,
// so the seventh frame is accessed using frameNo=7
// Do NOT pass a value <= 0 to readReplayFrame.
Replay frame7 = loader.readReplayFrame(7);
Replay frame8 = loader.readReplayFrame();

*/

public class BoatsReplayLoader extends ReplayFileLoader {
	
	protected Map<Integer, ReplayShip> constShipMap = null;
	protected Map<Integer, SoftReference<Replay>> replayCache = new HashMap<Integer, SoftReference<Replay>>();
	
	
	private static BoatsTypeContext ctx = new BoatsTypeContext();
	
	public BoatsReplayLoader(String filename) throws FileNotFoundException {
		super(filename, ctx);
	}
	
	public BoatsReplayLoader(File fileobj) throws FileNotFoundException {
		super(fileobj, ctx);
	}
	
	public BoatsReplayLoader(RandomAccessFile file) {
		super(file, ctx);
	}
	
	
	protected void refreshConstShipMap() {
		List<?> constShips = (List<?>)constantFields.get("ships");
		constShipMap = new HashMap<Integer, ReplayShip>(constShips.size(), 1.25f);
		for (Object shipo : constShips) {
			ReplayShip ship = (ReplayShip)shipo;
			constShipMap.put(ship.id, ship);
		}
	}
	
	protected void copyInConstants(Map<String, ?> frame) {
		//can't understand this
		List<?> ships = (List<?>)frame.get("ships");
		if (constShipMap == null)
			refreshConstShipMap();
		for (Object shipo : ships) {
			ReplayShip ship = (ReplayShip)shipo;
			ReplayShip cShip = constShipMap.get(ship.id);
			ship.team = cShip.team;
			ship.shipSize = cShip.shipSize;
			int maxHP = cShip.maxHP;
			ship.maxHP = maxHP;
			if (!ship.isDamaged)
				ship.hp = maxHP;
		}
	}
	
	@SuppressWarnings("unchecked")
	protected Replay toReplay(Map<String, ?> frame) {
		Replay replay = new Replay();
		replay.ships = (List<ReplayShip>)frame.get("ships");
		replay.trails = (List<ReplayTrail>)frame.get("trails");
		return replay;
	}
	
	public Replay readReplayFrame() throws IOException {
		Map<String, ?> frame = readFrame();
		copyInConstants(frame);
		return toReplay(frame);
	}
	
	public Replay readReplayFrame(int frameNo) throws IOException {
		seekFrame(frameNo);
		return readReplayFrame();
	}
	
	public Replay retreiveReplayFrame(int frameNo) throws IOException {
		SoftReference<Replay> ref = replayCache.get(frameNo);
		if (ref != null) {
			Replay replay = ref.get();
			if (replay != null)
				return replay;
		}
		Replay newReplay = readReplayFrame(frameNo);
		replayCache.put(frameNo, new SoftReference<>(newReplay));
		return newReplay;
	}
	
}