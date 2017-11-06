package sv.navybattle.ntd5.replay;

import java.awt.Color;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import sv.navybattle.pkg1.Main;

public class BoatsColorField implements ReplayFieldType<Color> {

	@Override
	public void save(Object vo, DataOutput out) throws IOException {
		byte team = (byte)Main.teamColor.indexOf(vo);
		out.writeByte(team);
		if (team == -1) {
			Color v = (Color)vo;
			out.writeByte(v.getRed());
			out.writeByte(v.getGreen());
			out.writeByte(v.getBlue());
		}
	}

	@Override
	public Color load(DataInput in) throws IOException {
		int team = in.readUnsignedByte();
		if (team == 0xff) {
			int r = in.readUnsignedByte();
			int g = in.readUnsignedByte();
			int b = in.readUnsignedByte();
			return new Color(r, g, b);
		} else {
			return Main.teamColor.get(team);
		}
	}

	@Override
	public String getName() {
		return "Color";
	}

}
