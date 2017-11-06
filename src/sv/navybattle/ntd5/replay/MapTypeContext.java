package sv.navybattle.ntd5.replay;

import java.util.HashMap;
import java.util.Map;

public class MapTypeContext implements ReplayTypeContext {
	
	protected final Map<String, ReplayFieldType<?>> map = new HashMap<String, ReplayFieldType<?>>();
	
	protected void putType(ReplayFieldType<?> type) {
		map.put(type.getName(), type);
	}

	@Override
	public ReplayFieldType<?> getType(String name) {
		ReplayFieldType<?> type = map.get(name);
		if (type == null)
			throw new NullPointerException("null type: \"" + name + "\"");
		return type;
	}

	@Override
	public String getName(ReplayFieldType<?> type) {
		return type.getName();
	}
	
}
