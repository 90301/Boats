package sv.navybattle.ntd5.replay;

import java.util.HashMap;
import java.util.Map;

import sv.navybattle.pkg1.ReplayShip;

public class ShipDynamicsField extends ReplayStructField<ReplayShip> {

	public ShipDynamicsField(ReplayTypeContext typeContext) {
		super(typeContext);
		put("id", "3B");
		put("hp", "(h)?");
		put("x", "f");
		put("y", "f");
	}

	@Override
	public String getName() {
		return "ShipD";
	}

	@Override
	public Map<String, ?> getSaveableFields(Object vo) {
		ReplayShip v = (ReplayShip)vo;
		Map<String, Object> fields = new HashMap<String, Object>();
		fields.put("id", v.id);
		fields.put("hp", (v.hp == v.maxHP) ? null : v.hp);
		fields.put("x", (float)v.x);
		fields.put("y", (float)v.y);
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
		Short hp = (Short)fields.get("hp");
		boolean isDamaged = (hp != null);
		v.isDamaged = isDamaged;
		v.hp = isDamaged ? hp : 0;
		v.x = (Float)fields.get("x");
		v.y = (Float)fields.get("y");
	}

}
