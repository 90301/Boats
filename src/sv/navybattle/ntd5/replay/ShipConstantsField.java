package sv.navybattle.ntd5.replay;

import java.util.HashMap;
import java.util.Map;

import sv.navybattle.pkg1.ReplayShip;

public class ShipConstantsField extends ReplayStructField<ReplayShip> {

	public ShipConstantsField(ReplayTypeContext typeContext) {
		super(typeContext);
		put("id", "3B");
		put("team", "B");
		put("shipSize", "H");
		put("maxHP", "h");
	}

	@Override
	public String getName() {
		return "ShipC";
	}

	@Override
	public Map<String, ?> getSaveableFields(Object vo) {
		ReplayShip v = (ReplayShip)vo;
		Map<String, Object> fields = new HashMap<String, Object>();
		fields.put("id", v.id);
		fields.put("team", v.team);
		fields.put("shipSize", v.shipSize);
		fields.put("maxHP", v.maxHP);
		return fields;
	}

	@Override
	public ReplayShip createFromFields(Map<String, ?> fields) {
		ReplayShip v = new ReplayShip();
		setLoadableFields(v, fields);
		return v;
	}

	@Override
	public void setLoadableFields(Object vo, Map<String, ?> fields) {
		ReplayShip v = (ReplayShip)vo;
		v.id = (Integer)fields.get("id");
		v.team = (Integer)fields.get("team");
		v.shipSize = (Integer)fields.get("shipSize");
		v.maxHP = (Short)fields.get("maxHP");
	}

}
