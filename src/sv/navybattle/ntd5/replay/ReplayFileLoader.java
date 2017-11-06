package sv.navybattle.ntd5.replay;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.nio.channels.Channels;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.zip.InflaterInputStream;

public class ReplayFileLoader {

	protected RandomAccessFile file;
	protected ReplayFileConfig cfg;
	protected int nFrames = -1;
	protected Map<String, ?> constantFields;
	
	public ReplayFileLoader(String filename, ReplayTypeContext ctx) throws FileNotFoundException {
		this(new RandomAccessFile(filename, "r"), ctx);
	}
	
	public ReplayFileLoader(File fileobj, ReplayTypeContext ctx) throws FileNotFoundException {
		this(new RandomAccessFile(fileobj, "r"), ctx);
	}
	
	public ReplayFileLoader(RandomAccessFile file, ReplayTypeContext ctx) {
		this.file = file;
		this.cfg = new ReplayFileConfig(ctx);
	}
	
	public int getTotalFrames() {
		return nFrames;
	}
	
	protected DataInputStream newInflatedInput() {
		InputStream is = Channels.newInputStream(file.getChannel());
		if (cfg.doesDeflate())
			is = new InflaterInputStream(is);
		DataInputStream in = new DataInputStream(is);
		return in;
	}
	
	private void readSeekTable() throws IOException {
		boolean deflate = file.readBoolean();
		int flb = file.readUnsignedByte();
		long tableLength = file.readLong();
		nFrames = file.readInt();
		long maxFrames = tableLength / flb;
		cfg.setDeflate(deflate);
		cfg.setFrameLocationBytes(flb);
		cfg.setMaxFrames(maxFrames);
		long hTableLength = 14 + tableLength;
		file.seek(hTableLength);
	}
	
	protected SortedMap<String, ReplayFieldType<?>> readFormat(DataInput in) throws IOException {
		SortedMap<String, ReplayFieldType<?>> fields = new TreeMap<>();
		int size = in.readInt();
		ReplayTypeContext ctx = cfg.getTypeContext();
		for (int i = 0; i < size; i++) {
			String key = in.readUTF();
			String name = in.readUTF();
			ReplayFieldType<?> value = ctx.getType(name);
			fields.put(key, value);
		}
		return fields;
	}
	
	void readTableAndFormat() throws IOException {
		readSeekTable();
		DataInput in = newInflatedInput();
		cfg.setConstantFields(readFormat(in));
		cfg.setDynamicFields(readFormat(in));
	}
	
	protected BigInteger uintOfNByteArray(byte[] ba) {
		return new BigInteger(1, ba);
	}
	
	protected Map<String, ?> readFormattedFrame(SortedMap<String, ReplayFieldType<?>> format) throws IOException {
		Map<String, Object> fields = new HashMap<>(format.size(), 1.25f);
		DataInput in = newInflatedInput();
		for (Entry<String, ReplayFieldType<?>> e : format.entrySet()) {
			Object value = e.getValue().load(in);
			fields.put(e.getKey(), value);
		}
		return fields;
	}
	
	public void readHeader() throws IOException {
		System.out.println("Reading replay header...");
		readTableAndFormat();
		System.out.format("Reading constant fields! Start byte 0x%x%n", file.getFilePointer());
		constantFields = readFormattedFrame(cfg.getConstantFields());
		System.out.println("Replay header read successfully.");
	}
	
	protected long getFramePosition(int frameNo) throws IOException {
		if (frameNo < 0 || frameNo >= nFrames)
			throw new IndexOutOfBoundsException("frame index out of bounds: " + frameNo);
		int flb = cfg.getFrameLocationBytes();
		file.seek(14 + frameNo*flb);
		byte[] ba = new byte[flb];
		file.readFully(ba);
		return uintOfNByteArray(ba).longValue();
	}
	
	protected void seekFrame(int frameNo) throws IOException {
		long pos = getFramePosition(frameNo);
		file.seek(pos);
	}
	
	public Map<String, ?> readFrame() throws IOException {
		return readFormattedFrame(cfg.getDynamicFields());
	}
	
	public Map<String, ?> readFrame(int frameNo) throws IOException {
		seekFrame(frameNo);
		return readFrame();
	}
	
}
