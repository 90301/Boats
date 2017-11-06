package sv.navybattle.ntd5.replay;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ReplayListField<W> implements ReplayFieldType<List<? extends W>> {

	protected ReplayFieldType<W> elemType;
	
	public ReplayListField(ReplayFieldType<W> elemType) {
		this.elemType = elemType;
	}
	
	@Override
	public void save(Object vo, DataOutput out) throws IOException {
		Collection<?> v = (Collection<?>) vo;
		out.writeInt(v.size());
		for (Object wo : v) {
			elemType.save(wo, out);
		}
	}

	@Override
	public ArrayList<W> load(DataInput in) throws IOException {
		int size = in.readInt();
		ArrayList<W> v = new ArrayList<W>(size);
		for (int i = 0; i < size; i++) {
			v.add(elemType.load(in));
		}
		return v;
	}

	@Override
	public String getName() {
		return "L(" + elemType.getName() + ")";
	}

}
