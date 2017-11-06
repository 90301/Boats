package sv.navybattle.ntd5.replay;

// TODO cmd-shift-O
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import sv.navybattle.pkg1.ReplayTrail;
// I wrote this on my phone, so please fix any compiler errors that may have slipped in...
// Note that currently all trail colors are coerced to 24-bit sRGB (no alpha).

public class BoatsTrailField extends ReplayStructField<ReplayTrail> {

	public BoatsTrailField(ReplayTypeContext typeContext) {
		super(typeContext);
		put("x1", "f");
		put("y1", "f");
		put("x2", "f");
		put("y2", "f");
		put("color", "Color");
	}

	@Override
	public String getName() {
		return "Trail";
	}

	@Override
	public Map<String, ?> getSaveableFields(Object vo) {
		ReplayTrail v = (ReplayTrail)vo;
		Map<String, Object> fields = new HashMap<String, Object>();
		fields.put("x1", (float)v.x1);
		fields.put("y1", (float)v.y1);
		fields.put("x2", (float)v.x2);
		fields.put("y2", (float)v.y2);
		fields.put("color", v.trailColor);
		return fields;
	}

	@Override
	public ReplayTrail createFromFields(Map<String, ?> fields) {
		ReplayTrail v = new ReplayTrail();
		setLoadableFields(v, fields);
		return v;
	}

	@Override
	public void setLoadableFields(Object vo, Map<String, ?> fields) {
		ReplayTrail v = (ReplayTrail)vo;
		v.x1 = (Float)fields.get("x1");
		v.y1 = (Float)fields.get("y1");
		v.x2 = (Float)fields.get("x2");
		v.y2 = (Float)fields.get("y2");
		v.trailColor = (Color)fields.get("color");
	}

}
