package com.google.code.jtraci.cmd;

import com.google.code.jtraci.TraciCommand;
import com.google.code.jtraci.io.TraciDataOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * TraCI simulation step command.
 * @see http://sourceforge.net/apps/mediawiki/sumo/index.php?title=TraCI/Control-related_commands
 * 
 * @author DL
 */
public class TraciStepCmd implements TraciCommand {

    public TraciStepCmd(double targetTime) {
        this.targetTime = targetTime;
    }

    public byte getId() {
        return ID;
    }

    public byte[] serializeContent() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            TraciDataOutputStream tdos = new TraciDataOutputStream(baos);

            tdos.writeDouble(targetTime);
            tdos.write(0); // PositionType=0
            tdos.close();

            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("IOException for memory stream", e);
        }
    }

    public static final byte ID = (byte)0x01;
    public final double targetTime;
}
