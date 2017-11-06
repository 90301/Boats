package sv.navybattle.ntd5.replay;

import sv.navybattle.pkg1.Main;

public class BoatsReplayFileConfig extends ReplayFileConfig {

	public BoatsReplayFileConfig() {
		this(64000);
	}
	
	public BoatsReplayFileConfig(long maxFrames) {
		super(new BoatsTypeContext(), maxFrames);
		setDeflate(false); // Deflating currently buggy
		setDeflateLevel(Main.deflateIntensity);
		setFrameLocationBytes(5);
		putConstant("ships", "L(ShipC)");
		putDynamic("ships", "L(ShipD)");
		putDynamic("trails", "L(Trail)");
	}
	
}
