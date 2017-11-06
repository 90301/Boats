package sv.navybattle.ntd5.replay;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

import sv.navybattle.pkg1.Replay;

/* ## Usage ##

BoatsReplaySaver saver = new BoatsReplaySaver(...);
Replay frame1 = new Replay(...);
saver.writeReplayHeader(frame1);
saver.writeReplayFrame(frame1);
Replay frame2 = new Replay(...);
saver.writeReplayFrame(frame2);
Replay frame3 = new Replay(...);
saver.writeReplayFrame(frame3);

*/

public class BoatsReplaySaver extends ReplayFileSaver {
	
	private static BoatsReplayFileConfig config = new BoatsReplayFileConfig(); //changed this
	
	public BoatsReplaySaver(String filename) throws FileNotFoundException {
		super(filename, config);
	}
	
	public BoatsReplaySaver(File fileobj) throws FileNotFoundException {
		super(fileobj, config);
	}
	
	public BoatsReplaySaver(RandomAccessFile file) {
		super(file, config);
	}
	
	
	protected static Map<String, ?> toFrame(Replay replay) {
		Map<String, Object> frame = new HashMap<>();
		frame.put("ships", replay.ships);
		frame.put("trails", replay.trails);
		return frame;
	}
	
	public void writeReplayHeader(Replay replay) throws IOException {
		writeHeader(toFrame(replay));
	}
	
	public void writeReplayFrame(Replay replay) throws IOException {
		writeFrame(toFrame(replay));
	}
	
}