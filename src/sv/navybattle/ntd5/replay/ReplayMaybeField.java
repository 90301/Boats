package sv.navybattle.ntd5.replay;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class ReplayMaybeField<V> implements ReplayFieldType<V> {

	protected ReplayFieldType<V> valueType;
	
	public ReplayMaybeField(ReplayFieldType<V> valueType) {
		this.valueType = valueType;
	}
	
	@Override
	public void save(Object vo, DataOutput out) throws IOException {
		if (vo == null) {
			out.writeBoolean(false);
		} else {
			out.writeBoolean(true);
			valueType.save(vo, out);
		}
	}

	@Override
	public V load(DataInput in) throws IOException {
		if (in.readBoolean()) {
			return valueType.load(in);
		} else {
			return null;
		}
	}

	@Override
	public String getName() {
		return "(" + valueType.getName() + ")?";
	}

}
