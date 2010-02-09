package com.google.code.jtraci;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Sends TraCI commands down a stream.
 * 
 * Commands are queued for send, and upon request "flushed" down the stream
 * wrapped in TraCI messages.
 * 
 * @author DL
 */
public class TraciCommandSender {
    /**
     * Constructor.
     * @param os   The output stream to write TraCI commands to.
     */
    public TraciCommandSender(OutputStream os) {
        this.dos = new DataOutputStream(os);
    }

    /**
     * Queues a given TraCI command to be sent later.
     * @param cmd   The TraCI command to queue.
     */
    public void queueCommand(TraciCommand cmd) {
        msg.add(cmd);
    }

    /**
     * Sends all previously queued commands down the output stream.
     * @throws IOException on any error.
     */
    public void sendQueuedCommands() throws IOException {
        msg.send(dos);
        dos.flush();
        msg.clear();
    }

    /**
     * Syntactic sugar to send a command immediately.
     * @param cmd   The TraCI command to send.
     */
    public void sendCommand(TraciCommand cmd) throws IOException {
        queueCommand(cmd);
        sendQueuedCommands();
    }

    private final DataOutputStream dos;
    private final TraciMessage msg = new TraciMessage();
}
