package sv.navybattle.ntd5.replay;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class StandardTypeContext extends MapTypeContext {
	
	public StandardTypeContext() {
		putType(BOOLEAN);
		putType(BYTE);
		putType(CHAR);
		putType(DOUBLE);
		putType(FLOAT);
		putType(INT);
		putType(LONG);
		putType(SHORT);
		putType(UBYTE);
		putType(USHORT);
		putType(UTF);
	}


	public static final BooleanField BOOLEAN = new BooleanField();
	public static final ByteField BYTE = new ByteField();
	public static final CharField CHAR = new CharField();
	public static final DoubleField DOUBLE = new DoubleField();
	public static final FloatField FLOAT = new FloatField();
	public static final IntField INT = new IntField();
	public static final LongField LONG = new LongField();
	public static final ShortField SHORT = new ShortField();
	public static final UByteField UBYTE = new UByteField();
	public static final UShortField USHORT = new UShortField();
	public static final UTFField UTF = new UTFField();
	
	
	public static final class BooleanField implements ReplayFieldType<Boolean> {
		@Override
		public void save(Object vo, DataOutput out) throws IOException {
			out.writeBoolean((boolean)vo);
		}
		@Override
		public Boolean load(DataInput in) throws IOException {
			return in.readBoolean();
		}
		@Override
		public String getName() {
			return "?";
		}
	}
	
	public static final class ByteField implements ReplayFieldType<Byte> {
		@Override
		public void save(Object vo, DataOutput out) throws IOException {
			out.writeByte((int)vo);
		}
		@Override
		public Byte load(DataInput in) throws IOException {
			return in.readByte();
		}
		@Override
		public String getName() {
			return "b";
		}
	}
	
	public static final class CharField implements ReplayFieldType<Character> {
		@Override
		public void save(Object vo, DataOutput out) throws IOException {
			out.writeChar((int)vo);
		}
		@Override
		public Character load(DataInput in) throws IOException {
			return in.readChar();
		}
		@Override
		public String getName() {
			return "u";
		}
	}
	
	public static final class DoubleField implements ReplayFieldType<Double> {
		@Override
		public void save(Object vo, DataOutput out) throws IOException {
			out.writeDouble((double)vo);
		}
		@Override
		public Double load(DataInput in) throws IOException {
			return in.readDouble();
		}
		@Override
		public String getName() {
			return "d";
		}
	}
	
	public static final class FloatField implements ReplayFieldType<Float> {
		@Override
		public void save(Object vo, DataOutput out) throws IOException {
			out.writeFloat((float)vo);
		}
		@Override
		public Float load(DataInput in) throws IOException {
			return in.readFloat();
		}
		@Override
		public String getName() {
			return "f";
		}
	}
	
	public static final class IntField implements ReplayFieldType<Integer> {
		@Override
		public void save(Object vo, DataOutput out) throws IOException {
			out.writeInt((int)vo);
		}
		@Override
		public Integer load(DataInput in) throws IOException {
			return in.readInt();
		}
		@Override
		public String getName() {
			return "i";
		}
	}
	
	public static final class LongField implements ReplayFieldType<Long> {
		@Override
		public void save(Object vo, DataOutput out) throws IOException {
			out.writeLong((long)vo);
		}
		@Override
		public Long load(DataInput in) throws IOException {
			return in.readLong();
		}
		@Override
		public String getName() {
			return "l";
		}
	}
	
	public static final class ShortField implements ReplayFieldType<Short> {
		@Override
		public void save(Object vo, DataOutput out) throws IOException {
			out.writeShort((int)vo);
		}
		@Override
		public Short load(DataInput in) throws IOException {
			return in.readShort();
		}
		@Override
		public String getName() {
			return "h";
		}
	}
	
	public static final class UByteField implements ReplayFieldType<Integer> {
		@Override
		public void save(Object vo, DataOutput out) throws IOException {
			out.writeByte((int)vo);
		}
		@Override
		public Integer load(DataInput in) throws IOException {
			return in.readUnsignedByte();
		}
		@Override
		public String getName() {
			return "B";
		}
	}
	
	public static final class UShortField implements ReplayFieldType<Integer> {
		@Override
		public void save(Object vo, DataOutput out) throws IOException {
			out.writeShort((int)vo);
		}
		@Override
		public Integer load(DataInput in) throws IOException {
			return in.readUnsignedShort();
		}
		@Override
		public String getName() {
			return "H";
		}
	}
	
	public static final class UTFField implements ReplayFieldType<String> {
		@Override
		public void save(Object v, DataOutput out) throws IOException {
			out.writeUTF((String)v);
		}
		@Override
		public String load(DataInput in) throws IOException {
			return in.readUTF();
		}
		@Override
		public String getName() {
			return "U";
		}
	}
	
}
