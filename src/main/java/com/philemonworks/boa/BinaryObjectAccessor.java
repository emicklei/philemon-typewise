/*
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004 - All rights reserved.
 * Use, duplication, renaming or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 * 
 * VERSION HISTORY
 * 	2004-01-01: created
 * 	2004-03-18: version 2 : TColor(99) and TZero(39)
 *     2004-04-13: Added Byte support by emm, readNext for Numbers return Integer instances
 *     2004-05-02: readObject throws exception
 * 	2004-06-21: Color null test
 **/
package com.philemonworks.boa;
import com.philemonworks.util.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import org.apache.log4j.Logger;
/**
 * @author emicklei
 * @author mvhulsentop
 *
 * Purpose:
 *
 */
public class BinaryObjectAccessor {
	protected static final byte TNull = 1;
	protected static final byte TTrue = 2;
	protected static final byte TFalse = 3;
	protected static final byte TByte = 4;
	protected static final byte TShort = 5;
	protected static final byte TInt = 6;
	protected static final byte TLong = 7;
	protected static final byte TFloat = 8;
	protected static final byte TDouble = 9;
	protected static final byte TChar = 10;
	protected static final byte TByteString = 11;
	protected static final byte TUTF8String = 12;
	protected static final byte TSymbol = 13;
	protected static final byte TArray = 14;
	protected static final byte TSet = 15;
	protected static final byte TBehaviour = 16;
	protected static final byte TObject = 17;
	protected static final byte TTime = 18;
	protected static final byte TDate = 19;
	protected static final byte TMessage = 20;
	protected static final byte TDirectedMessage = 21;
	protected static final byte TOrderedCollection = 27;
	protected static final byte TKeyedCollection = 28;
	protected static final byte TFraction = 29;
	protected static final byte TByteArray = 31;
	protected static final byte TDictionary = 32;
	protected static final byte TTwoByteString = 33;
	protected static final byte TSortedCollection = 34;
	protected static final byte TPoint = 35;
	protected static final byte TObjectByReference = 36;
	// protected static final byte ?? = 37;
	protected static final byte TException = 38;
	protected static final byte TZero = 39;
	protected static final byte TColor = 99;
	private static final Number ZERO = new Integer(0);
	private static final int BOA_VERSION = 3;
	private DataOutputStream writer;
	private ByteArrayOutputStream out;
	private DataInputStream reader;

	public BinaryObjectAccessor() throws BinaryObjectReadException {
		super();
		this.setOutputStream(new ByteArrayOutputStream());
	}
	public BinaryObjectAccessor(byte[] byteArray)
		throws BinaryObjectReadException {
		super();
		this.setInputStream(new ByteArrayInputStream(byteArray));
	}
	public BinaryObjectAccessor(ByteArrayInputStream inputStream)
		throws BinaryObjectReadException {
		super();
		this.setInputStream(inputStream);
	}
	public BinaryObjectAccessor(ByteArrayOutputStream outputStream)
		throws BinaryObjectReadException {
		super();
		this.setOutputStream(outputStream);
	}
	protected int getVersion() {
		return BOA_VERSION;
	}
	public void close() throws IOException {
		if (this.reader != null)
			this.reader.close();
		if (this.writer != null)
			this.writer.close();
	}
	protected Object dispatch(byte tag)
		throws BinaryObjectReadException, IOException {
		Object result = null;
		switch (tag) {
			case TChar :
				result = readCharacter();
				break;
			case TNull :
				result = readNull();
				break;
			case TTrue :
				result = Boolean.TRUE;
				break;
			case TFalse :
				result = Boolean.FALSE;
				break;
			case TInt :
				result = readInteger();
				break;
			case TShort :
			    // generic number is Integer, use readNextShortValue() if a Short instance is needed			
				//result = new Integer(this.reader.readShort());
				result = this.readShort();
				break;
			case TLong :
				result = readLong();
				break;
			case TFloat :
				result = readFloat();
				break;
			case TByte :
				// generic number is Integer, use readNextByteValue() if a Byte instance is needed
				//result = new Integer(this.reader.readByte());
				result = this.readByte();
				break;
			case TByteString :
				result = readByteString();
				break;
			case TObject :
				result = readObject();
				break;
			case TSymbol :
				result = readByteString();
				break;
			case TArray :
				result = readArray();
				break;
			case TSet :
				result = readSet();
				break;
			case TUTF8String :
				result = readUTF8();
				break;
			case TBehaviour :
				result = readBehaviour();
				break;
			case TTime :
				result = readTime();
				break;
			case TDate :
				result = readDate();
				break;
			case TOrderedCollection :
				result = readOrderedCollection();
				break;
			case TKeyedCollection :
				result = readKeyedCollection();
				break;
			case TFraction :
				result = readFraction();
				break;
			case TByteArray :
				result = readByteArray();
				break;
			case TSortedCollection :
				result = readSortedCollection();
				break;
			case TColor :
				result = readColor();
				break;
			case TZero :
				result = ZERO;
				break;
			default :
				result = this.dispatchUnknown(tag);
				break;
		}
		return result;
	}
	protected Object dispatchUnknown(byte tag)
		throws BinaryObjectReadException, IOException {
		StringBuffer remainder = new StringBuffer();
		while (reader.available()>0) remainder.append(reader.readChar());
		Logger.getLogger(this.getClass()).error("Binary data incorrect. Remainder:"+remainder);
		throw new BinaryObjectReadException("Unknown tag:" + tag);
	}
	protected Object[] readArray()
		throws IOException, BinaryObjectReadException {
		int arraySize = this.readNextIntegerValue();
		Object arrayValue[] = new Object[arraySize];
		for (int count = 0; count < arraySize; count++) {
			arrayValue[count] = this.readNext();
		}
		return (arrayValue);
	}
	protected Object readBehaviour()
		throws IOException, BinaryObjectReadException {
		String className = "";
		try {
			className = this.readNextString();
			return (Class.forName(className));
		} catch (ClassNotFoundException cnfe) {
			throw new BinaryObjectReadException(
				cnfe,
				"Cannot find class " + className);
		}
	}
	protected Number readByte() throws IOException, BinaryObjectReadException {
		return (new Integer(this.reader.readUnsignedByte()));
	}
	protected Object readByteArray()
		throws IOException, BinaryObjectReadException {
		int arraySize = this.readNextIntegerValue();
		byte arrayByte[] = new byte[arraySize];
		for (int count = 0; count < arraySize; count++) {
			arrayByte[count] = this.reader.readByte();
		}
		return (arrayByte);
	}
	protected String readByteString()
		throws IOException, BinaryObjectReadException {
		int byteStringLength = this.readNextIntegerValue();
		char byteStringValue[] = new char[byteStringLength];
		for (int count = 0; count < byteStringLength; count++) {
			byteStringValue[count] = (char) this.reader.readByte();
		}
		return (new String(byteStringValue));
	}
	protected String readTwoByteString()
		throws IOException, BinaryObjectReadException {
		int length = this.readNextIntegerValue();
		byte[] buffer = new byte[length*2];
		this.reader.read(buffer);
		return new String(buffer);
	}
	protected Character readCharacter()
		throws IOException, BinaryObjectReadException {
		char charValue;
		charValue = (char) ((Number) this.readNext()).shortValue();
		return (new Character(charValue));
	}
	protected Date readDate() throws IOException, BinaryObjectReadException {
		Calendar calendarValue = new GregorianCalendar();
		calendarValue.set(Calendar.YEAR, this.readInteger().intValue());
		calendarValue.set(Calendar.MONTH, this.reader.readByte());
		calendarValue.set(Calendar.DAY_OF_MONTH, this.reader.readByte());
		return (calendarValue.getTime());
	}
	protected Number readFloat()
		throws IOException, BinaryObjectReadException {
		int length = this.reader.readByte();
		Number value;
		switch (length) {
			case 4 :
				value = new Float(this.reader.readFloat());
				break;
			case 8 :
				value = new Double(this.reader.readDouble());
				break;
			default :
				throw new BinaryObjectReadException(
					"Unknown float/double length: " + length);
		}
		return (value);
	}
	protected Double readFraction()
		throws IOException, BinaryObjectReadException {
		int numerator = this.readNextIntegerValue();
		int denominator = this.readNextIntegerValue();
		return (new Double(numerator / denominator));
	}
	private void readHeader() throws IOException, BinaryObjectReadException {
		
		if (this.reader.readByte() == 'B')
			if (this.reader.readByte() == 'O')
				if (this.reader.readByte() == 'A') {
					int boaVersion = this.reader.readByte();
					if (boaVersion != this.getVersion()) {
						throw new RuntimeException(
							"Incompatible BOA version, read:"
								+ boaVersion
								+ " expected:"
								+ this.getVersion());
					}
				} else {
					throw new RuntimeException("Data is non-valid BOA format");
				}
	}
	/*
	 * These methods read the object from the stream and, if nessecary,
	 * box them.
	 * 
	 * Return:	instance of (Object or a subclass of Object)
	 */
	protected Integer readInteger()
		throws IOException, BinaryObjectReadException {
		int value = this.reader.readInt();
		return (new Integer(value));
	}
	protected HashMap readKeyedCollection()
		throws IOException, BinaryObjectReadException {
		String className = this.readByteString();
		int mapSize = this.readNextIntegerValue();
		HashMap mapValue = new HashMap();
		for (int count = 0; count < mapSize; count++) {
			mapValue.put(this.readNext(), this.readNext());
		}
		return (mapValue);
	}
	protected Long readLong() throws IOException, BinaryObjectReadException {
		long value = this.reader.readLong();
		return (new Long(value));
	}
	protected Color readColor()
		throws IOException, BinaryObjectReadException {
		int red, green, blue;
		red = reader.read();
		green = reader.read();
		blue = reader.read();
		Color clr = new Color(red, green, blue);
		return (clr);
	}
	/*
	 * Reader methods.
	 */
	public Object readNext() throws BinaryObjectReadException {
		try {
			return (this.dispatch(this.readTag()));
		} catch (IOException catchedException) {
			throw new BinaryObjectReadException(catchedException);
		}
	}
	public Object[] readNextArray() throws BinaryObjectReadException {
		Object value = this.readNext();
		try {
			return (Object[]) value;
		} catch (ClassCastException cce) {
			throw new BinaryObjectReadException(
				cce,
				value + " is not of type Object[]");
		}
	}
	public boolean readNextBooleanValue() throws BinaryObjectReadException {
		Object value = this.readNext();
		try {
			return ((Boolean) value).booleanValue();
		} catch (ClassCastException cce) {
			throw new BinaryObjectReadException(
				cce,
				value + " is not a boolean");
		}
	}
	public byte[] readNextByteArray() throws BinaryObjectReadException {
		Object value = this.readNext();
		try {
			return (byte[]) value;
		} catch (ClassCastException cce) {
			throw new BinaryObjectReadException(
				cce,
				value + " is not a byte[]");
		}
	}
	public byte readNextByteValue() throws BinaryObjectReadException {
		Object value = this.readNext();
		try {
			return ((Number) value).byteValue();
		} catch (ClassCastException cce) {
			throw new BinaryObjectReadException(
				cce,
				value + " is not a byte");
		}
	}
	public char readNextCharValue() throws BinaryObjectReadException {
		Object value = this.readNext();
		try {
			return ((Character) value).charValue();
		} catch (ClassCastException cce) {
			throw new BinaryObjectReadException(cce, value + "is not a char");
		}
	}
	public Date readNextDate() throws BinaryObjectReadException {
		Object value = this.readNext();
		try {
			return (Date) value;
		} catch (ClassCastException cce) {
			throw new BinaryObjectReadException(cce, value + "is not a Date");
		}
	}
	public double readNextDoubleValue() throws BinaryObjectReadException {
		Object value = this.readNext();
		try {
			return ((Double) value).doubleValue();
		} catch (ClassCastException cce) {
			throw new BinaryObjectReadException(
				cce,
				value + " is not a double");
		}
	}
	public float readNextFloatValue() throws BinaryObjectReadException {
		Object value = this.readNext();
		try {
			return ((Float) value).floatValue();
		} catch (ClassCastException cce) {
			throw new BinaryObjectReadException(
				cce,
				value + "is not a float");
		}
	}
	public double readNextFraction() throws BinaryObjectReadException {
		return (this.readNextDoubleValue());
	}
	/*
	 * These methods are public and cast the object read from the BO
	 * to the primitive requested. 
	 * 
	 */
	public int readNextIntegerValue() throws BinaryObjectReadException {
		Object value = this.readNext();
		try {
			return ((Number) value).intValue();
		} catch (ClassCastException cce) {
			throw new BinaryObjectReadException(
				cce,
				value + " is not an int");
		}
	}
	public List readNextList() throws BinaryObjectReadException {
		Object value = this.readNext();
		try {
			return (List) value;
		} catch (ClassCastException cce) {
			throw new BinaryObjectReadException(
				cce,
				value + " is not a List");
		}
	}
	public long readNextLongValue() throws BinaryObjectReadException {
		Object value = this.readNext();
		try {
			return ((Number) value).longValue();
		} catch (ClassCastException cce) {
			throw new BinaryObjectReadException(
				cce,
				value + " is not a long");
		}
	}
	public Object readNextNullValue() throws BinaryObjectReadException {
		Object value = this.readNext();
		if (value == null)
			return null;
		throw new BinaryObjectReadException(null, "value not null");
	}
	public short readNextShortValue() throws BinaryObjectReadException {
		Object value = this.readNext();
		try {
			return ((Number) value).shortValue();
		} catch (ClassCastException cce) {
			throw new BinaryObjectReadException(
				cce,
				value + " is not a short");
		}
	}
	public String[] readNextStringArray() throws BinaryObjectReadException {
		Object[] src = this.readNextArray();
		String[] dest = new String[src.length];
		for (int count = 0; count < src.length; count++) {
			dest[count] = (String) src[count];
		}
		return (dest);
	}
	public String readNextString() throws BinaryObjectReadException {
		Object value = this.readNext();
		try {
			return (String) value;
		} catch (ClassCastException cce) {
			throw new BinaryObjectReadException(
				cce,
				value + " is not a String");
		}
	}
	public Date readNextTime() throws BinaryObjectReadException {
		Object value = this.readNext();
		try {
			return (Date) value;
		} catch (ClassCastException cce) {
			throw new BinaryObjectReadException(
				cce,
				value + " is not a Date");
		}
	}
	protected Object readNull() {
		return null;
	}
	protected Object readObject()
		throws IOException, BinaryObjectReadException {
		throw new BinaryObjectReadException(null,"[BOA] Unknown object");
	}
	protected ArrayList readOrderedCollection()
		throws IOException, BinaryObjectReadException {
		int listSize = this.readNextIntegerValue();
		ArrayList listValue = new ArrayList(listSize);
		for (int count = 0; count < listSize; count++) {
			listValue.add(this.readNext());
		}
		return (listValue);
	}
	protected HashSet readSet()
		throws IOException, BinaryObjectReadException {
		Object[] arrayValue = this.readArray();
		int arrayLength = arrayValue.length;
		HashSet hashValue = new HashSet(arrayLength + 5);
		for (int count = 0; count < arrayLength; count++) {
			hashValue.add(arrayValue[count]);
		}
		return (new HashSet());
	}
	protected Number readShort()
		throws IOException, BinaryObjectReadException {
		int value = this.reader.readUnsignedShort();
		return (new Integer(value));
	}
	protected SortedSet readSortedCollection()
		throws IOException, BinaryObjectReadException {
		SortedSet setValue = new TreeSet();
		int setSize = this.reader.readShort();
		for (int count = 0; count < setSize; count++) {
			setValue.add(this.readNext());
		}
		return (setValue);
	}
	protected byte readTag() throws BinaryObjectReadException, IOException {
		return this.reader.readByte();
	}

	protected Date readTime() throws IOException, BinaryObjectReadException {
/*		Calendar calenderValue = new GregorianCalendar();
		calenderValue.setTimeInMillis(
			this.readNextLongValue()
			- calenderValue.get(Calendar.ZONE_OFFSET)
			- calenderValue.get(Calendar.DST_OFFSET));
		return (calenderValue.getTime());
*/		return null;
	}
	protected String readUTF8()
		throws IOException, BinaryObjectReadException {
		return (reader.readUTF());
	}
	private void setInputStream(ByteArrayInputStream inputStream)
		throws BinaryObjectReadException {
		reader = new DataInputStream(inputStream);
		try {
			this.readHeader();
		} catch (IOException ioe) {
			throw new BinaryObjectReadException(
				ioe,
				"IO Error when reading BOA header");
		}
	}
	private void setOutputStream(ByteArrayOutputStream outputStream)
		throws BinaryObjectReadException {
		out = outputStream;
		writer = new DataOutputStream(out);
		try {
			this.writeHeader();
		} catch (IOException ioe) {
			throw new BinaryObjectReadException(
				ioe,
				"IO Error when writing BOA header");
		}
	}
	private void store(String stringValue) throws IOException {
		int stringLength = stringValue.length();
		this.write(stringLength);
		writer.writeBytes(stringValue);
	}
	private void store(Set setValue) throws IOException {
		Iterator walkThrough = setValue.iterator();
		this.write(setValue.size());
		while (walkThrough.hasNext()) {
			this.write(walkThrough.next());
		}
	}
	public byte[] toByteArray() {
		return out.toByteArray();
	}
	public void write(byte[] arrayValue) throws IOException {
		this.writeTypeIdentifier(TByteArray);
		this.write(arrayValue.length);
		this.writer.write(arrayValue);
	}
	public void write(Object[] arrayValue) throws IOException {
		if (arrayValue == null) {
			this.writeNull();
			return;
		}
		int arrayLength = arrayValue.length;
		this.writeTypeIdentifier(TArray);
		this.write(arrayLength);
		for (int count = 0; count < arrayLength; count++) {
			this.write(arrayValue[count]);
		}
	}
	public void writeNumber(long value) throws IOException {
		if (value <= Byte.MAX_VALUE) {
		    if (value >= 0) {
		        this.writeTypeIdentifier(TByte);
		        writer.writeByte((byte) value);
		        return;
		    }
		}
		if (value <= Short.MAX_VALUE) {
		    if (value >= 0) {
			this.writeTypeIdentifier(TShort);
			writer.writeShort((short) value);
			return;
		    }
		} 
		if (value <= Integer.MAX_VALUE) {
		    if (value >= Integer.MIN_VALUE) {
			this.writeTypeIdentifier(TInt);
			writer.writeInt((int) value);
			return;
		    }
		}
		this.writeTypeIdentifier(TLong);
		writer.writeLong(value);
	}
	public void write(byte byteValue) throws IOException {
		// Byte is 'smallest' number, so call writeByte immediately instead of calling writeNumber
		this.writeTypeIdentifier(TByte);
		writer.writeByte(byteValue);
	}
	public void write(char charValue) throws IOException {
		this.writeTypeIdentifier(TChar);
		this.write((short) charValue);
	}
	public void write(double doubleValue) throws IOException {
		this.writeTypeIdentifier(TFloat);
		this.writer.writeByte(8);
		writer.writeDouble(doubleValue);
	}
	public void write(float floatValue) throws IOException {
		this.writeTypeIdentifier(TFloat);
		writer.writeByte(4);
		writer.writeFloat(floatValue);
	}
	public void write(int intValue) throws IOException {
		this.writeNumber(intValue);
	}
	public void write(long longValue) throws IOException {
		this.writeNumber(longValue);
	}
	public void write(short shortValue) throws IOException {
		this.writeNumber(shortValue);
	}
	public void write(Object value) throws IOException {
		if (value == null) {
			this.writeNull();
			return;
		}
		/* 
		 * Do NOT forget to cast to 'real' type. When write(Object) is called once again,
		 * we're creating an endless recursion. It is important to do things in the right order. 
		 * Bottom-up, first subclasses, followed by superclasses.
		 */
		if (value instanceof String) {
			this.write((String) value);
			return;
		}
		if (value instanceof Integer) {
			this.write(((Integer) value).intValue());
			return;
		}
		if (value instanceof Byte) {
			this.write(((Byte) value).byteValue());
			return;
		}		
		if (value instanceof Boolean) {
			this.write(((Boolean) value).booleanValue());
			return;
		}
		if (value instanceof Character) {
			this.write(((Character) value).charValue());
			return;
		}
		if (value instanceof int[]) {
			System.err.println(
				"Warning: int[] not supported! This will probably result in an TObject");
		}
		if (value instanceof List) {
			this.write((List) value);
			return;
		}
		if (value instanceof Map) {
			this.write((Map) value);
			return;
		}		
		this.writeUnknown(value);
	}
	protected void writeUnknown(Object value) throws IOException {
		throw new IOException("[BOA] cannot binary store:" + value);
	}
	public void write(String stringValue) throws IOException {
		this.writeTypeIdentifier(TUTF8String);
		writer.writeUTF(stringValue);
	}
	public void write(java.util.List listValue) throws IOException {
		int listSize = listValue.size();
		this.writeTypeIdentifier(TOrderedCollection);
		this.write(listSize);
		for (int count = 0; count < listSize; count++) {
			this.write(listValue.get(count));
		}
	}
	public void write(Map mapValue) throws IOException {
		int mapSize = mapValue.size();
		Iterator mapKeys = mapValue.keySet().iterator();
		this.writeTypeIdentifier(TKeyedCollection);
		this.store("Dictionary"); // Smalltalk class for Maps
		this.write(mapSize);
		while (mapKeys.hasNext()) {
			Object key = mapKeys.next();
			this.write(key);
			this.write(mapValue.get(key));
		}
	}
	public void write(Set setValue) throws IOException {
		this.writeTypeIdentifier(TSet);
		this.store(setValue);
	}
	public void write(SortedSet setValue) throws IOException {
		this.writeTypeIdentifier(TSortedCollection);
		this.store(setValue);
	}
	public void write(boolean booleanValue) throws IOException {
		this.writeTypeIdentifier(booleanValue ? TTrue : TFalse);
	}
	public void write(Color clr) throws IOException {
		if (clr == null) {
			this.writeNull();
			return;
		}
		this.writeTypeIdentifier(TColor);
		writer.write((int) clr.getRed());
		writer.write((int) clr.getGreen());
		writer.write((int) clr.getBlue());
	}
	public void writeAsUTF(String stringValue) throws IOException {
		writer.writeUTF(stringValue);
	}
	/* Special write methods */
	public void writeBehaviour(Object objectValue) throws IOException {
		this.writeTypeIdentifier(TBehaviour);
		this.write(objectValue.getClass().getName());
	}
	public void writeDate(Calendar dateValue) throws IOException {
		this.writeTypeIdentifier(TDate);
		this.writer.writeInt(dateValue.get(Calendar.YEAR));
		this.writer.writeByte(dateValue.get(Calendar.MONTH));
		this.writer.writeByte(dateValue.get(Calendar.DAY_OF_MONTH));
	}
	public void writeDate(Date dateValue) throws IOException {
		/*		Calendar c;
				c = new GregorianCalendar();
				c.setTimeInMillis(dateValue.getTime());
				this.writeDate(c);
		*/
	}
	protected void writeHeader() throws IOException {
		this.writer.write('B');
		this.writer.write('O');
		this.writer.write('A');
		this.writer.write(this.getVersion());
	}
	public void writeNull() throws IOException {
		this.writeTypeIdentifier(TNull);
	}
	public void writeSymbol(String symbolValue) throws IOException {
		this.writeTypeIdentifier(TSymbol);
		this.store(symbolValue);
	}
	protected void writeTypeIdentifier(short typeIdentifier)
		throws IOException {
		this.writer.writeByte(typeIdentifier);
	}
}
