package sv.navybattle.ntd5.replay;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

public abstract class ReplayStructField<V> implements ReplayFieldType<V> {

	private ReplayTypeContext typeContext;
	private final SortedMap<String, ReplayFieldType<?>> structFields = new TreeMap<String, ReplayFieldType<?>>();
	
	
	public ReplayStructField(ReplayTypeContext typeContext) {
		this.typeContext = typeContext;
	}
	
	
	public abstract Map<String, ?> getSaveableFields(Object vo);
	
	public abstract V createFromFields(Map<String, ?> fields);
	
	public abstract void setLoadableFields(Object vo, Map<String, ?> fields);
	
	
	@Override
	public void save(Object vo, DataOutput out) throws IOException {
		Map<String, ?> fields = getSaveableFields(vo);
		if (!fields.keySet().containsAll(structFields.keySet()))
			throw new RuntimeException("incomplete field key set"); // TODO actual exception type
		for (Entry<String, ReplayFieldType<?>> e : structFields.entrySet()) {
			e.getValue().save(fields.get(e.getKey()), out);
		}
	}

	@Override
	public V load(DataInput in) throws IOException {
		Map<String, Object> fields = new HashMap<String, Object>(structFields.size(), 1.25f);
		for (Entry<String, ReplayFieldType<?>> e : structFields.entrySet()) {
			fields.put(e.getKey(), e.getValue().load(in));
		}
		return createFromFields(fields);
	}
	
	
	public ReplayTypeContext getTypeContext() {
		return typeContext;
	}

	public void setTypeContext(ReplayTypeContext typeContext) {
		this.typeContext = typeContext;
	}
	
	protected Map<String, ReplayFieldType<?>> getFields() {
		return structFields;
	}

	public void put(String name, ReplayFieldType<?> type) {
		structFields.put(name, type);
	}
	
	public void put(String name, String ctxType) {
		put(name, typeContext.getType(ctxType));
	}
	
}
