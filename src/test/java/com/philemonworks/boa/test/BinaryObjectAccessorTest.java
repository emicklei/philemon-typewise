/*
 * Created on 9-dec-2003, changed on 18-03-2004 for zero test
 *
 * (c)copyright 2003, PhilemonWorks. All rights reserved
 * */
package com.philemonworks.boa.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import junit.framework.TestCase;
import com.philemonworks.boa.BinaryObjectAccessor;
import com.philemonworks.boa.BinaryObjectReadException;
import com.philemonworks.util.Color;

/**
 * @author emicklei
 * @author Maarten van Hulsentop
 * 
 * Purpose: VERSION HISTORY 2004-03-23: emm: removed rubbish tests, need to
 * complete this
 *  
 */
public class BinaryObjectAccessorTest extends TestCase {
    public void testWriteReadInteger() throws IOException, Exception {
        int writeInt = 12345678;
        BinaryObjectAccessor binaryObjectWriter, binaryObjectReader;
        ByteArrayOutputStream outStream;
        outStream = new ByteArrayOutputStream();
        binaryObjectWriter = new BinaryObjectAccessor(outStream);
        binaryObjectWriter.write(writeInt);
        binaryObjectWriter.close();
        binaryObjectReader = new BinaryObjectAccessor(new ByteArrayInputStream(outStream.toByteArray()));
        int readInt = binaryObjectReader.readNextIntegerValue();
        assertTrue(writeInt == readInt);
    }

    public void testWriteReadMinus1() throws IOException, Exception {
        int writeInt = -1;
        BinaryObjectAccessor binaryObjectWriter, binaryObjectReader;
        ByteArrayOutputStream outStream;
        outStream = new ByteArrayOutputStream();
        binaryObjectWriter = new BinaryObjectAccessor(outStream);
        binaryObjectWriter.write(writeInt);
        binaryObjectWriter.close();
        binaryObjectReader = new BinaryObjectAccessor(new ByteArrayInputStream(outStream.toByteArray()));
        int readInt = binaryObjectReader.readNextIntegerValue();
        assertEquals(writeInt,readInt);
    }

    public void testWriteReadLong() throws IOException, Exception {
        long writeLong = Long.MAX_VALUE;
        BinaryObjectAccessor binaryObjectWriter, binaryObjectReader;
        ByteArrayOutputStream outStream;
        outStream = new ByteArrayOutputStream();
        binaryObjectWriter = new BinaryObjectAccessor(outStream);
        binaryObjectWriter.write(writeLong);
        binaryObjectWriter.close();
        binaryObjectReader = new BinaryObjectAccessor(new ByteArrayInputStream(outStream.toByteArray()));
        long readLong = binaryObjectReader.readNextLongValue();
        assertTrue(writeLong == readLong);
    }

    public void testWriteReadColor() throws IOException, Exception {
        Color writeColor = new Color(128, 128, 128);
        BinaryObjectAccessor binaryObjectWriter, binaryObjectReader;
        ByteArrayOutputStream outStream;
        outStream = new ByteArrayOutputStream();
        binaryObjectWriter = new BinaryObjectAccessor(outStream);
        binaryObjectWriter.write(writeColor);
        binaryObjectWriter.close();
        binaryObjectReader = new BinaryObjectAccessor(new ByteArrayInputStream(outStream.toByteArray()));
        Color readColor = (Color) binaryObjectReader.readNext();
        assertEquals(writeColor.getRed(), readColor.getRed());
        assertEquals(writeColor, readColor);
    }

    public void testWriteReadShort() throws IOException, Exception {
        short writeShort = 42;
        BinaryObjectAccessor binaryObjectWriter, binaryObjectReader;
        ByteArrayOutputStream outStream;
        outStream = new ByteArrayOutputStream();
        binaryObjectWriter = new BinaryObjectAccessor(outStream);
        binaryObjectWriter.write(writeShort);
        binaryObjectWriter.close();
        binaryObjectReader = new BinaryObjectAccessor(new ByteArrayInputStream(outStream.toByteArray()));
        short readShort = binaryObjectReader.readNextShortValue();
        assertTrue(writeShort == readShort);
    }

    public void testWriteReadFloat() throws IOException, Exception {
        float writeFloat = (float) 123.4567;
        BinaryObjectAccessor binaryObjectWriter, binaryObjectReader;
        ByteArrayOutputStream outStream;
        outStream = new ByteArrayOutputStream();
        binaryObjectWriter = new BinaryObjectAccessor(outStream);
        binaryObjectWriter.write(writeFloat);
        binaryObjectWriter.close();
        binaryObjectReader = new BinaryObjectAccessor(new ByteArrayInputStream(outStream.toByteArray()));
        float readFloat = binaryObjectReader.readNextFloatValue();
        assertEquals(writeFloat, readFloat, (float) 0.01);
    }

    public void testWriteIntegerReadLong() throws IOException, Exception {
        int writeInt = 12345678;
        BinaryObjectAccessor binaryObjectWriter, binaryObjectReader;
        ByteArrayOutputStream outStream;
        outStream = new ByteArrayOutputStream();
        binaryObjectWriter = new BinaryObjectAccessor(outStream);
        binaryObjectWriter.write(writeInt);
        binaryObjectWriter.close();
        binaryObjectReader = new BinaryObjectAccessor(new ByteArrayInputStream(outStream.toByteArray()));
        long readLong = binaryObjectReader.readNextLongValue();
        assertTrue(writeInt == (new Long(readLong)).intValue());
    }

    public void testWriteShortReadInteger() throws IOException, Exception {
        short writeShort = 43;
        BinaryObjectAccessor binaryObjectWriter, binaryObjectReader;
        ByteArrayOutputStream outStream;
        outStream = new ByteArrayOutputStream();
        binaryObjectWriter = new BinaryObjectAccessor(outStream);
        binaryObjectWriter.write(writeShort);
        binaryObjectWriter.close();
        binaryObjectReader = new BinaryObjectAccessor(new ByteArrayInputStream(outStream.toByteArray()));
        int readInt = binaryObjectReader.readNextIntegerValue();
        assertTrue(writeShort == (new Long(readInt)).shortValue());
    }

    public void testReadInt12345678() throws IOException, Exception {}

    public void testReadDate() throws IOException, Exception {
    /*
     * byte[] input = new byte[]{66, 79, 65,1,19,0,0,7,(byte)212,1,19}; Calendar
     * readDate; BinaryObjectAccessor accessorTst = new BinaryObjectAccessor(new
     * ByteArrayInputStream(input)); readDate = new GregorianCalendar();
     * readDate.setTimeInMillis((accessorTst.readNextDate().getTime()));
     * assertEquals(readDate.get(Calendar.YEAR), 2004);
     * assertEquals(readDate.get(Calendar.DAY_OF_MONTH), 19);
     */
    }

    public void testReadTime() throws IOException, Exception {
    /*
     * // 9:07:48 byte[] input = new
     * byte[]{66,79,65,1,18,6,1,(byte)245,(byte)135,44}; Calendar readTime;
     * BinaryObjectAccessor accessorTst = new BinaryObjectAccessor(new
     * ByteArrayInputStream(input)); readTime = new GregorianCalendar();
     * readTime.setTimeInMillis(accessorTst.readNextTime().getTime());
     * assertEquals(readTime.get(Calendar.HOUR_OF_DAY), 9);
     * assertEquals(readTime.get(Calendar.MINUTE), 7);
     */
    }

    public void testReadCharA() throws IOException, Exception {}

    public void testWriteReadChar() throws IOException, Exception {
        char writeChar = 'A';
        BinaryObjectAccessor binaryObjectWriter, binaryObjectReader;
        ByteArrayOutputStream outStream;
        outStream = new ByteArrayOutputStream();
        binaryObjectWriter = new BinaryObjectAccessor(outStream);
        binaryObjectWriter.write(writeChar);
        binaryObjectWriter.close();
        binaryObjectReader = new BinaryObjectAccessor(new ByteArrayInputStream(outStream.toByteArray()));
        char readChar = binaryObjectReader.readNextCharValue();
        assertEquals(writeChar, readChar);
        outStream.reset();
    }

    public void testWriteReadString() throws IOException, Exception {
        String writeString = "Hello World";
        BinaryObjectAccessor binaryObjectWriter, binaryObjectReader;
        ByteArrayOutputStream outStream;
        outStream = new ByteArrayOutputStream();
        binaryObjectWriter = new BinaryObjectAccessor(outStream);
        binaryObjectWriter.write(writeString);
        binaryObjectWriter.close();
        binaryObjectReader = new BinaryObjectAccessor(new ByteArrayInputStream(outStream.toByteArray()));
        String readString = binaryObjectReader.readNextString();
        assertEquals(writeString, readString);
        outStream.reset();
    }

    public void testWriteReadTime() throws IOException, Exception {
    /*
     * Calendar writeCalendar = Calendar.getInstance(); BinaryObjectAccessor
     * binaryObjectWriter, binaryObjectReader; ByteArrayOutputStream outStream;
     * outStream = new ByteArrayOutputStream(); binaryObjectWriter = new
     * BinaryObjectAccessor(outStream);
     * binaryObjectWriter.writeTime(writeCalendar); binaryObjectWriter.close();
     * binaryObjectReader = new BinaryObjectAccessor( new
     * ByteArrayInputStream(outStream.toByteArray())); Calendar readCalendar =
     * binaryObjectReader.readNextTime(); assertEquals(
     * writeCalendar.get(Calendar.HOUR_OF_DAY),
     * readCalendar.get(Calendar.HOUR_OF_DAY)); assertEquals(
     * writeCalendar.get(Calendar.MINUTE), readCalendar.get(Calendar.MINUTE));
     * assertEquals( writeCalendar.get(Calendar.SECOND),
     * readCalendar.get(Calendar.SECOND)); outStream.reset();
     */
    }

    public void testWriteReadDate() throws IOException, Exception {
    /*
     * Calendar writeCalendar = GregorianCalendar.getInstance();
     * BinaryObjectAccessor binaryObjectWriter, binaryObjectReader;
     * ByteArrayOutputStream outStream; outStream = new ByteArrayOutputStream();
     * binaryObjectWriter = new BinaryObjectAccessor(outStream);
     * binaryObjectWriter.writeDate(writeCalendar.getTime());
     * binaryObjectWriter.close(); binaryObjectReader = new
     * BinaryObjectAccessor( new ByteArrayInputStream(outStream.toByteArray()));
     * Calendar readCalendar = new GregorianCalendar();
     * readCalendar.setTimeInMillis(binaryObjectReader.readNextDate().getTime());
     * assertEquals( writeCalendar.get(Calendar.YEAR),
     * readCalendar.get(Calendar.YEAR)); assertEquals(
     * writeCalendar.get(Calendar.DAY_OF_MONTH),
     * readCalendar.get(Calendar.DAY_OF_MONTH)); assertEquals(
     * writeCalendar.get(Calendar.MONTH), readCalendar.get(Calendar.MONTH));
     * outStream.reset();
     */
    }

    public void testWriteReadStringArray() throws IOException, Exception {
        String writeStringArray[] = new String[5];
        writeStringArray[0] = "Hallo";
        writeStringArray[1] = "Hoi";
        writeStringArray[2] = "Hallo";
        BinaryObjectAccessor binaryObjectWriter, binaryObjectReader;
        ByteArrayOutputStream outStream;
        outStream = new ByteArrayOutputStream();
        binaryObjectWriter = new BinaryObjectAccessor(outStream);
        binaryObjectWriter.write(writeStringArray);
        binaryObjectWriter.close();
        binaryObjectReader = new BinaryObjectAccessor(new ByteArrayInputStream(outStream.toByteArray()));
        byte outputBytes[] = outStream.toByteArray();
        String readStringArray[] = binaryObjectReader.readNextStringArray();
        assertEquals(writeStringArray.length, readStringArray.length);
        assertEquals(writeStringArray[0], readStringArray[0]);
        assertEquals(writeStringArray[1], readStringArray[1]);
        assertEquals(writeStringArray[2], readStringArray[2]);
        outStream.reset();
    }

    public void testWriteReadBigStringArray() throws IOException, Exception {}

    public void testHelloString() throws IOException, Exception {}

    public void testReadIntNeg1234567() throws IOException, Exception {}

    public void testReadDoublePi() throws IOException, Exception {}

    public void testArray() throws IOException, Exception {}

    public void testOrderedCollection() throws IOException, Exception {}

    public void testByteArray() throws IOException, Exception {}

    public void testReadWriteByte() throws IOException, Exception {
        byte writeByte = 42;
        BinaryObjectAccessor binaryObjectWriter, binaryObjectReader;
        OutputStreamWriter outStreamWriter;
        ByteArrayOutputStream outStream;
        outStream = new ByteArrayOutputStream();
        binaryObjectWriter = new BinaryObjectAccessor(outStream);
        binaryObjectWriter.write(writeByte);
        binaryObjectWriter.close();
        binaryObjectReader = new BinaryObjectAccessor(new ByteArrayInputStream(outStream.toByteArray()));
        short readByte = binaryObjectReader.readNextShortValue();
        assertTrue(writeByte == readByte);
    }

    public void testReadWriteMapInList() throws IOException, Exception {
        HashMap map = new HashMap();
		map.put("key","value");
		List list = new ArrayList();
		list.add(map);
        BinaryObjectAccessor binaryObjectWriter, binaryObjectReader;
        OutputStreamWriter outStreamWriter;
        ByteArrayOutputStream outStream;
        outStream = new ByteArrayOutputStream();
        binaryObjectWriter = new BinaryObjectAccessor(outStream);
        binaryObjectWriter.write(list);
        binaryObjectWriter.close();
        binaryObjectReader = new BinaryObjectAccessor(new ByteArrayInputStream(outStream.toByteArray()));
		List readList = (List)binaryObjectReader.readNext();
        Map readMap = (Map)readList.get(0); 
        assertTrue(readMap.get("key").equals("value"));
    }	
	
    public void testReadWriteNull() throws IOException, Exception {
        BinaryObjectAccessor writer = new BinaryObjectAccessor();
        writer.write((Object) null);
        BinaryObjectAccessor reader = new BinaryObjectAccessor(writer.toByteArray());
        assertNull(reader.readNext());
    }

    public void testReadWriteZero() throws IOException, Exception {
        BinaryObjectAccessor writer = new BinaryObjectAccessor();
        writer.write(0);
        BinaryObjectAccessor reader = new BinaryObjectAccessor(writer.toByteArray());
        assertEquals(0, reader.readNextIntegerValue());
    }

    public void testReadIntWriteString() throws IOException, Exception {
        String writeString = "Hello World";
        boolean handledException = false;
        BinaryObjectAccessor binaryObjectWriter, binaryObjectReader;
        OutputStreamWriter outStreamWriter;
        ByteArrayOutputStream outStream;
        outStream = new ByteArrayOutputStream();
        binaryObjectWriter = new BinaryObjectAccessor(outStream);
        binaryObjectWriter.write(writeString);
        binaryObjectWriter.close();
        binaryObjectReader = new BinaryObjectAccessor(new ByteArrayInputStream(outStream.toByteArray()));
        try {
            int readInt = binaryObjectReader.readNextIntegerValue();
        } catch (BinaryObjectReadException e) {
            handledException = true;
        }
        assertTrue(handledException);
    }

    public void testReadWriteByteArray() throws IOException, Exception {
        byte writeArray[] = new byte[3];
        writeArray[1] = 3;
        BinaryObjectAccessor binaryObjectWriter, binaryObjectReader;
        OutputStreamWriter outStreamWriter;
        ByteArrayOutputStream outStream;
        outStream = new ByteArrayOutputStream();
        binaryObjectWriter = new BinaryObjectAccessor(outStream);
        binaryObjectWriter.write(writeArray);
        binaryObjectWriter.close();
        binaryObjectReader = new BinaryObjectAccessor(new ByteArrayInputStream(outStream.toByteArray()));
        byte readArray[] = binaryObjectReader.readNextByteArray();
        assertEquals(readArray.length, writeArray.length);
        assertEquals(readArray[1], writeArray[1]);
    }

    public BinaryObjectAccessorTest(String selector) {
        super(selector);
    }

    public void testReadWriteString() throws BinaryObjectReadException, IOException {
        BinaryObjectAccessor writer = new BinaryObjectAccessor();
        writer.write("Hello World");
        BinaryObjectAccessor reader = new BinaryObjectAccessor(writer.toByteArray());
        assertEquals("Hello World", reader.readNextString());
    }
}