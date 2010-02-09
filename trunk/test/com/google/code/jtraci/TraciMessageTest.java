package com.google.code.jtraci;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JUnit tests for org.google.code.jtraci.TraciMessage
 * @author DL
 */
public class TraciMessageTest {

    /** Mock TraCI command for testing. */
    private class MockTraciCommand implements TraciCommand {
        public MockTraciCommand(byte id, int contentSize) {
            this.id = id;
            this.contentSize = contentSize;
        }

        public byte getId() {
            return id;
        }

        public byte[] serializeContent() {
            return new byte[contentSize];
        }

        private final byte id;
        private final int contentSize;
    }

    public TraciMessageTest() {}

    @Before
    public void setUp() {
        message = new TraciMessage();
        bos.reset();
    }

    @Test
    public void testNewMessageSanity() throws Exception {
        message.send(dos);
        
        byte[] serializedMsg = bos.toByteArray();
        assertEquals(4, serializedMsg.length);

        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(serializedMsg));
        assertEquals(4, dis.readInt());
    }

    @Test
    public void testClear() throws Exception {
        message.add(new MockTraciCommand((byte)17, 55));
        message.clear();
        message.send(dos);

        byte[] serializedMsg = bos.toByteArray();
        assertEquals(4, serializedMsg.length);

        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(serializedMsg));
        assertEquals(4, dis.readInt());
    }

    @Test
    public void testAddSanity() throws Exception {
        message.add(new MockTraciCommand((byte)17, 55));
        message.send(dos);

        byte[] serializedMsg = bos.toByteArray();
        assertTrue(serializedMsg.length > 4);

        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(serializedMsg));
        assertEquals(serializedMsg.length, dis.readInt());
    }

    @Test
    public void testSendShortCommand() throws Exception {
        // 253 is the maximum content size for a short command (+2 for length
        // and id is exactly 255)
        final int shortCmdLength = 253;
        final byte cmdId = 17;
        message.add(new MockTraciCommand(cmdId, shortCmdLength));
        message.send(dos);

        byte[] serializedMsg = bos.toByteArray();
        assertEquals(4 + shortCmdLength + 2, serializedMsg.length);

        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(serializedMsg));
        assertEquals(serializedMsg.length, dis.readInt());
        assertEquals((byte)shortCmdLength + 2, dis.readByte());
        assertEquals(cmdId, dis.readByte());
        byte[] actualCmdContent = new byte[shortCmdLength];
        assertEquals(shortCmdLength, dis.read(actualCmdContent));
        assertArrayEquals(new byte[shortCmdLength], actualCmdContent);
    }

    @Test
    public void testSendLongCommand() throws Exception {
        // 254 is the minimum content size for a long command (+2 for length
        // and id is exactly 256)
        final int longCmdLength = 254;
        final byte cmdId = 17;
        message.add(new MockTraciCommand(cmdId, longCmdLength));
        message.send(dos);

        byte[] serializedMsg = bos.toByteArray();
        assertEquals(4 + longCmdLength + 2 + 4, serializedMsg.length);

        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(serializedMsg));
        assertEquals(serializedMsg.length, dis.readInt());
        assertEquals(0, dis.readByte());
        assertEquals(longCmdLength + 6, dis.readInt());
        assertEquals(cmdId, dis.readByte());
        byte[] actualCmdContent = new byte[longCmdLength];
        assertEquals(longCmdLength, dis.read(actualCmdContent));
        assertArrayEquals(new byte[longCmdLength], actualCmdContent);
    }

    TraciMessage message;
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(bos);
}