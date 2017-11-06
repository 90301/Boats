package sv.navybattle.ntd5.replay;

import java.util.SortedMap;
import java.util.TreeMap;
import java.util.zip.Deflater;

public class ReplayFileConfig {
	
	private boolean deflate = false;
	private int deflateLevel = Deflater.DEFAULT_COMPRESSION;
	private ReplayTypeContext typeContext;
	private int frameLocationBytes = 8;
	private long maxFrames;
	private SortedMap<String, ReplayFieldType<?>> constantFields = new TreeMap<String, ReplayFieldType<?>>();
	private SortedMap<String, ReplayFieldType<?>> dynamicFields = new TreeMap<String, ReplayFieldType<?>>();
	
	
	public ReplayFileConfig() {
		this(new StandardTypeContext());
	}
	
	public ReplayFileConfig(ReplayTypeContext ctx) {
		this(ctx, -1);
	}
	
	public ReplayFileConfig(ReplayTypeContext ctx, long maxFrames) {
		typeContext = ctx;
		this.maxFrames = maxFrames;
	}
	
	
	public boolean doesDeflate() {
		return deflate;
	}

	public void setDeflate(boolean deflate) {
		this.deflate = deflate;
	}
	
	public int getDeflateLevel() {
		return deflateLevel;
	}
	
	public void setDeflateLevel(int deflateLevel) {
		this.deflateLevel = deflateLevel;
	}

	public ReplayTypeContext getTypeContext() {
		return typeContext;
	}

	public void setTypeContext(ReplayTypeContext typeContext) {
		this.typeContext = typeContext;
	}

	public int getFrameLocationBytes() {
		return frameLocationBytes;
	}
	
	public void setFrameLocationBytes(int frameLocationBytes) {
		this.frameLocationBytes = frameLocationBytes;
	}
	
	public long getMaxFrames() {
		return maxFrames;
	}
	
	public void setMaxFrames(long maxFrames) {
		this.maxFrames = maxFrames;
	}
	
	protected SortedMap<String, ReplayFieldType<?>> getConstantFields() {
		return constantFields;
	}
	
	protected void setConstantFields(SortedMap<String, ReplayFieldType<?>> constantFields) {
		this.constantFields = constantFields;
	}
	
	protected SortedMap<String, ReplayFieldType<?>> getDynamicFields() {
		return dynamicFields;
	}

	protected void setDynamicFields(SortedMap<String, ReplayFieldType<?>> dynamicFields) {
		this.dynamicFields = dynamicFields;
	}

	public void putConstant(String name, ReplayFieldType<?> type) {
		constantFields.put(name, type);
	}
	
	public void putDynamic(String name, ReplayFieldType<?> type) {
		dynamicFields.put(name, type);
	}
	
	public void putConstant(String name, String ctxType) {
		putConstant(name, typeContext.getType(ctxType));
	}
	
	public void putDynamic(String name, String ctxType) {
		putDynamic(name, typeContext.getType(ctxType));
	}
	
}
