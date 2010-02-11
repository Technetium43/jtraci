package com.google.code.jtraci.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
        tdos.writeTraciString(expectedStr);

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        TraciDataInputStream tdis = new TraciDataInputStream(bais);
        assertEquals(expectedStr, tdis.readTraciString());
    }
}