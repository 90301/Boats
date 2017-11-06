package sv.navybattle.ntd5.replay;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

public class ReplayFileSaver {

	protected RandomAccessFile file;
	protected ReplayFileConfig cfg;
	protected int nFrames = 0;
	
	public ReplayFileSaver(String filename, ReplayFileConfig cfg) throws FileNotFoundException {
		this(new RandomAccessFile(filename, "rw"), cfg);
	}
	
	public ReplayFileSaver(File fileobj, ReplayFileConfig cfg) throws FileNotFoundException {
		this(new RandomAccessFile(fileobj, "rw"), cfg);
	}
	
	public ReplayFileSaver(RandomAccessFile file, ReplayFileConfig cfg) {
		this.file = file;
		this.cfg = cfg;
	}
	
	protected DataOutputStream newDeflatedOutput(OutputStream os) {
		if (cfg.doesDeflate()) {
			Deflater deflater = new Deflater(cfg.getDeflateLevel());
			deflater.setStrategy(Deflater.FILTERED);
			os = new DeflaterOutputStream(os, deflater);
		}
		DataOutputStream out = new DataOutputStream(os);
		return out;
	}
	
	void writeSeekTable() throws IOException {
		int flb = cfg.getFrameLocationBytes();
		long maxFrames = cfg.getMaxFrames();
		if (maxFrames < 0)
			throw new RuntimeException((maxFrames == -1)
					? "max frames not set (or -1)"
					: ("negative max frames (" + maxFrames + ")"));
		long tableLength = maxFrames * flb;
		file.writeBoolean(cfg.doesDeflate());
		file.writeByte(flb);
		file.writeLong(tableLength);
		file.writeInt(0);
		long hTableLength = 14 + tableLength;
		file.setLength(hTableLength);
		file.seek(hTableLength);
	}
	
	protected void writeFormat(SortedMap<String, ReplayFieldType<?>> fields, DataOutput out) throws IOException {
		out.writeInt(fields.size());
		for (Entry<String, ReplayFieldType<?>> e : fields.entrySet()) {
			out.writeUTF(e.getKey());
			ReplayFieldType<?> value = e.getValue();
			String name = value.getName();
			out.writeUTF(name);
		}
	}
	
	void writeTableAndFormat() throws IOException {
		writeSeekTable();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream out = newDeflatedOutput(baos);
		writeFormat(cfg.getConstantFields(), out);
		writeFormat(cfg.getDynamicFields(), out);
		out.close();
		file.write(baos.toByteArray());
	}
	
	protected byte[] arrayOfNByteUint(int n, BigInteger v) {
		byte[] biba = v.abs().toByteArray();
		byte[] ba = new byte[n];
		System.arraycopy(biba, 0, ba, n - biba.length, biba.length);
		return ba;
	}
	
	protected void writeFormattedFrame(SortedMap<String, ReplayFieldType<?>> format, Map<String, ?> fields) throws IOException {
		long ptr = file.getFilePointer();
		int flb = cfg.getFrameLocationBytes();
		file.seek(14 + nFrames*flb);
		file.write(arrayOfNByteUint(flb, BigInteger.valueOf(ptr)));
		file.seek(10);
		file.writeInt(++nFrames);
		file.seek(ptr);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream out = newDeflatedOutput(baos);
		for (Entry<String, ReplayFieldType<?>> e : format.entrySet()) {
			e.getValue().save(fields.get(e.getKey()), out);
		}
		out.close();
		file.write(baos.toByteArray());
	}
	
	public void writeHeader(Map<String, ?> fields) throws IOException {
		writeTableAndFormat();
		writeFormattedFrame(cfg.getConstantFields(), fields);
	}
	
	public void writeFrame(Map<String, ?> fields) throws IOException {
		writeFormattedFrame(cfg.getDynamicFields(), fields);
	}
	
}
