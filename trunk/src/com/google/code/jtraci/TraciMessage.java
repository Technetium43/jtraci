package com.google.code.jtraci;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A TraCI message (a container for a list of commands), as defined by the TraCI
 * protocol.
 *
 * @see http://sourceforge.net/apps/mediawiki/sumo/index.php?title=TraCI/Protocol
 * 
 * @author DL
 */
public class TraciMessage {

    /** Constructs a new, empty, TraCI message. */
    public TraciMessage() {}

    /** Removes all previously added commands, so the object can be reused. */
    public void clear() {
        commandsList.clear();
    }

    /**
     * Adds a TraCI command to the message.
     * @param cmd   The TraCI command to add.
     */
    public void add(TraciCommand cmd) {
        commandsList.add(cmd);
    }

    /**
     * Serializes and sends the TraCI message down a given stream.
     * @param out   The stream to write the TraCI message to.
     * @throws IOException if the stream could not be written to.
     */
    public void send(DataOutput out) throws IOException {
        // Serialize commands
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        
        for (TraciCommand cmd : commandsList) {
            byte[] serializedCmdContent = cmd.serializeContent();
            int cmdLength = serializedCmdContent.length + 2; // +2 for length & id

            if (cmdLength <= 255) {
                dos.writeByte(cmdLength);
            } else {
                cmdLength += 4; // +4 for extended length
                dos.writeByte(0);
                dos.writeInt(cmdLength);
            }
            dos.writeByte(cmd.getId());
            dos.write(serializedCmdContent);
        }
        dos.close();

        // Serialize message
        byte[] serializedMessageContent = bos.toByteArray();
        int msgLength = serializedMessageContent.length + 4; // +4 for length

        out.writeInt(msgLength);
        out.write(serializedMessageContent);
    }

    private List<TraciCommand> commandsList = new ArrayList<TraciCommand>();
}
