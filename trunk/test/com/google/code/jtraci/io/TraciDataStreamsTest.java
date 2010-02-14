package com.google.code.jtraci.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JUnit tests for org.google.code.jtraci.impl.TraciDataInputStream and
 * org.google.code.jtraci.impl.TraciDataOutputStream
 * @author DL
 */
public class TraciDataStreamsTest {

    public TraciDataStreamsTest() {}

    @Test
    public void testTraciString() throws Exception {
        final String expectedStr = "The quick brown fox jumped over the lazy dog.";

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TraciDataOutputStream tdos = new TraciDataOutputStream(baos);
        tdos.writeString(expectedStr);

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        TraciDataInputStream tdis = new TraciDataInputStream(bais);
        assertEquals(expectedStr, tdis.readString());
    }

    @Test
    public void testTraciStringList() throws Exception {
        final List<String> expectedStrList = new ArrayList<String>();
        expectedStrList.add("The quick brown fox jumped over the lazy dog.");
        expectedStrList.add("The lazy dog jumped over the quick brown fox.");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TraciDataOutputStream tdos = new TraciDataOutputStream(baos);
        tdos.writeStringList(expectedStrList);

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        TraciDataInputStream tdis = new TraciDataInputStream(bais);
        assertEquals(expectedStrList, tdis.readStringList());
    }
}