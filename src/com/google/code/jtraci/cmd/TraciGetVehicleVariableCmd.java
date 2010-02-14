package com.google.code.jtraci.cmd;

import com.google.code.jtraci.TraciCommand;
import com.google.code.jtraci.io.TraciDataOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * TraCI vehicle value retrieval command.
 * @see http://sourceforge.net/apps/mediawiki/sumo/index.php?title=TraCI/Vehicle_Value_Retrieval
 * 
 * @author DL
 */
public class TraciGetVehicleVariableCmd implements TraciCommand {

    public static final byte IdList = 0x00;
    public static final byte Speed = 0x40;
    public static final byte Position = 0x42;
    public static final byte Angle = 0x43;
    public static final byte RoadId = 0x50;
    public static final byte LaneId = 0x51;
    public static final byte LaneIndex = 0x52;
    public static final byte TypeId = 0x4F;
    public static final byte RouteId = 0x53;
    public static final byte Edges = 0x54;
    public static final byte Color = 0x45;
    public static final byte LanePosition = 0x56;
    public static final byte CO2Emissions = 0x60;
    public static final byte COEmissions = 0x61;
    public static final byte HCEmissions = 0x62;
    public static final byte PMxEmissions = 0x63;
    public static final byte NOxEmissions = 0x64;
    public static final byte FuelConsumption = 0x65;
    public static final byte NoiseEmission = 0x66;

    /**
     * Constructs a TraCI GetVehicleVariable command.
     * @param vehicleVariable   Vehicle variable (see above).
     * @param vehicleId         Vehicle id (ignored for IdList, can be null).
     */
    public TraciGetVehicleVariableCmd(byte vehicleVariable, String vehicleId) {
        this.vehicleVariable = vehicleVariable;
        this.vehicleId = (vehicleId != null ? vehicleId : "");
    }

    /** @see TraciCommand#getId() */
    public byte getId() {
        return ID;
    }

    /** @see TraciCommand#serializeContent() */
    public byte[] serializeContent() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            TraciDataOutputStream tdos = new TraciDataOutputStream(baos);

            tdos.write(vehicleVariable);
            tdos.writeString(vehicleId);
            tdos.close();
            
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("IOException for memory stream", e);
        }
    }

    public static final byte ID = (byte)0xA4;
    public static final byte RESP_ID = (byte)0xB4;
    
    private final byte vehicleVariable;
    private final String vehicleId;
}
