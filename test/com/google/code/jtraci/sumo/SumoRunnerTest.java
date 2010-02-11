package com.google.code.jtraci.sumo;

import com.google.code.jtraci.TraciClient;
import com.google.code.jtraci.TraciCommand;
import com.google.code.jtraci.TraciCommandSender;
import com.google.code.jtraci.TraciRawResponse;
import com.google.code.jtraci.TraciRawResponseReceiver;
import com.google.code.jtraci.resp.TraciStatusResponse;
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

        if (!new File(configPath).canRead()) {
            fail("Invalid config file path: " + configPath);
        }
    }

    @Before
    public void setUp() {
        port = 8888;
        sumo = new SumoRunner(sumoPath, configPath, port);
    }

    @After
    public void tearDown() {
        sumo.stop();
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
            
            TraciRawResponse rawResp = receiver.receiveRawResponse();
            assertEquals(TraciUnimplementedCommand.ID, rawResp.getId());

            TraciStatusResponse resp = new TraciStatusResponse(rawResp);
            assertEquals(TraciStatusResponse.TraciStatusUnimplemented, resp.getResult());
            assertEquals("Command not implemented in sumo", resp.getDescription());
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

    @Test
    public void testTraciClientStep() throws Exception {
        sumo.start();
        TraciClient client = sumo.getClient();
        assertEquals(0, client.getVehicleIds().size());
        client.step(0);
        assertEquals(1, client.getVehicleIds().size());
        assertEquals("Rand0", client.getVehicleIds().get(0));
        sumo.stop();
    }

    private static final String sumoPath = "C:\\sumo\\sumo-winbin-0.11.1\\sumo.exe";
    private static final String configPath = "C:\\sumo\\maps\\potsdam.sumo.cfg";
    
    private int port;
    private SumoRunner sumo;
}