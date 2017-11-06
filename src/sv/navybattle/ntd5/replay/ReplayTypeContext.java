package sv.navybattle.ntd5.replay;


public interface ReplayTypeContext {
	
	ReplayFieldType<?> getType(String name);
	
	String getName(ReplayFieldType<?> type);
	
}
