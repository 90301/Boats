package sv.navybattle.ntd5.replay;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class UShortishField implements ReplayFieldType<Integer> {
	
	@Override
	public void save(Object vo, DataOutput out) throws IOException {
		int v = (int)vo;
		out.writeByte(v>>16);
		out.writeShort(v);
	}

	@Override
	public Integer load(DataInput in) throws IOException {
		int b = in.readUnsignedByte();
		return (b<<16)|in.readUnsignedShort();
	}

	@Override
	public String getName() {
		return "3B";
	}

}
