package com.google.code.jtraci.cmd;

import com.google.code.jtraci.TraciCommand;
import com.google.code.jtraci.io.TraciDataOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * TraCI change vehicle state command.
 * @see http://sourceforge.net/apps/mediawiki/sumo/index.php?title=TraCI/Change_Vehicle_State
 *
 * @author DL
 */
public class TraciChangeVehicleStateCmd implements TraciCommand {
    public static final byte SlowDown = 0x14;

    /**
     * Constructs a "slow down" command: reduces vehicle's speed for an amount
     * of time.
     * 
     * @param vehicleId   Vehicle id.
     * @param speed       New speed.
     * @param duration    Amount of time.
     */
    public TraciChangeVehicleStateCmd(String vehicleId, float speed, float duration) {
        this.vehicleId = vehicleId;
        this.speed = speed;
        this.duration = duration;
    }

    public byte getId() {
        return ID;
    }

    public byte[] serializeContent() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            TraciDataOutputStream tdos = new TraciDataOutputStream(baos);

            tdos.write(SlowDown);
            tdos.writeString(vehicleId);
            tdos.write(TraciCompound);
            tdos.writeInt(2); // item number
            tdos.write(TraciFloat);
            tdos.writeFloat(speed);
            tdos.write(TraciFloat);
            tdos.writeFloat(duration);
            tdos.close();

            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("IOException for memory stream", e);
        }
    }

    public static final byte ID = (byte)0xC4;

    private final String vehicleId;
    private final float speed;
    private final float duration;
}
