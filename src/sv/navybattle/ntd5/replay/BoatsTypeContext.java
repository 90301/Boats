package sv.navybattle.ntd5.replay;

public class BoatsTypeContext extends StandardTypeContext {
	
	private static final boolean VERBOSE = false;

	public BoatsTypeContext() {
		putType(new UShortishField());
		putType(new BoatsColorField());
		
		putType(new ReplayMaybeField<>(SHORT));
		
		ShipConstantsField shipC = new ShipConstantsField(this);
		ShipDynamicsField shipD = new ShipDynamicsField(this);
		BoatsTrailField trail = new BoatsTrailField(this);
		putType(shipC);
		putType(shipD);
		putType(trail);
		
		putType(new ReplayListField<>(shipC));
		putType(new ReplayListField<>(shipD));
		putType(new ReplayListField<>(trail));
	}
	
	protected void putType(ReplayFieldType<?> type) {
		if (VERBOSE)
			System.out.println("BoatsTypeContext.putType: " + type.getName());
		super.putType(type);
	}
	
}
