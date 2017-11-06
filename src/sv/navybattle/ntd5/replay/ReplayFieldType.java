package sv.navybattle.ntd5.replay;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public interface ReplayFieldType<V> {
	
	void save(Object vo, DataOutput out) throws IOException;
	
	V load(DataInput in) throws IOException;
	
	String getName();
	
}