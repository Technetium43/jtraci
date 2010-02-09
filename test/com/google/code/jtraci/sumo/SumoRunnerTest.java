package com.google.code.jtraci.sumo;

import com.google.code.jtraci.TraciCommand;
import com.google.code.jtraci.TraciCommandSender;
import com.google.code.jtraci.TraciRawResponse;
import com.google.code.jtraci.TraciRawResponseReceiver;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JUnit tests for org.google.code.jtraci.sumo.SumoRunner
 * @author DL
 */
public class SumoRunnerTest {

    public SumoRunnerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        if (!new File(sumoPath).canExecute()) {
            fail("Invalid SUMO path: " + sumoPath);
        }

        if (!new File(networkPath).canRead()) {
            fail("Invalid network file path: " + networkPath);
        }
    }

    @Before
    public void setUp() {
        port = 8888;
        sumo = new SumoRunner(sumoPath, networkPath, port);
    }

    @After
    public void tearDown() {
        sumo.stop();
        sumo = null;
    }

    @Test
    public void testStartStopSanity() throws Exception {
        sumo.start();
        sumo.stop();
    }

    @Test
    public void testPortFailSanity() throws Exception {
        try {
            Socket socket = new Socket(InetAddress.getLocalHost(), port);
            socket.close();
            fail("connect should fail.");
        } catch(ConnectException e) {}
    }

    @Test
    public void testPort() throws Exception {
        sumo.start();

        Socket socket = new Socket(InetAddress.getLocalHost(), port);
        socket.close();

        sumo.stop();
    }

    /** Simple command to test the protocol. */
    private class TraciUnimplementedCommand implements TraciCommand {
        public byte getId() {
            return ID;
        }

        public byte[] serializeContent() {
            return new byte[0];
        }

        public static final byte ID = (byte)0xFF;
    }

    @Test
    public void testTraciProtocolSanity() throws Exception {
        sumo.start();

        Socket socket = new Socket(InetAddress.getLocalHost(), port);
        try {
            TraciCommandSender sender = new TraciCommandSender(socket.getOutputStream());
            TraciRawResponseReceiver receiver = new TraciRawResponseReceiver(socket.getInputStream());

            sender.sendCommand(new TraciUnimplementedCommand());
            
            TraciRawResponse resp = receiver.receiveRawResponse();
            assertEquals(TraciUnimplementedCommand.ID, resp.getId());
            assertEquals(1, resp.getResult());
            
            DataInputStream dis = new DataInputStream(
                    new ByteArrayInputStream(resp.getContent()));
            int descLen = dis.readInt();
            byte[] rawDesc = new byte[descLen];
            dis.readFully(rawDesc);
            String desc = new String(rawDesc);
            assertEquals("Command not implemented in sumo", desc);
        } finally {
            socket.close();
        }

        sumo.stop();
    }

    @Test
    public void testStopNotStarted() {
        sumo.stop();
    }

    @Test
    public void testDoubleStop() throws Exception {
        sumo.start();
        sumo.stop();
        sumo.stop();
    }

    private static final String sumoPath = "C:\\sumo\\sumo-winbin-0.11.1\\sumo.exe";
    private static final String networkPath = "C:\\sumo\\maps\\adlershof.osm.net.xml";
    private int port;
    private SumoRunner sumo;
}