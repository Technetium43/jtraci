package com.google.code.jtraci;

import com.google.code.jtraci.cmd.TraciGetVehicleVariableCmd;
import com.google.code.jtraci.cmd.TraciStepCmd;
import com.google.code.jtraci.io.TraciDataInputStream;
import com.google.code.jtraci.resp.TraciStatusResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * A TraCI client that assumes each server response is a direct response to the
 * last client's command.
 * 
 * This client does not (and with current implementation, cannot) support
 * value retrieval subscriptions.
 * @see http://sourceforge.net/apps/mediawiki/sumo/index.php?title=TraCI/Value_Retrieval_Subscription
 * 
 * @author DL
 */
public class TraciClient {
    /**
     * Constructs the TraCI client.
     * @param in    The stream to read TraCI messages from.
     * @param out   The stream to write TraCI messages to.
     */
    public TraciClient(InputStream in, OutputStream out) {
        sender = new TraciCommandSender(out);
        receiver = new TraciRawResponseReceiver(in);
    }

    public void step(double targetTime) throws IOException {
        TraciCommand cmd = new TraciStepCmd(targetTime);
        sender.sendCommand(cmd);

        TraciRawResponse response = waitForResponse(cmd.getId());
        verifyResponse(response);
    }

    public List<String> getVehicleIds() throws IOException {
        TraciCommand cmd = new TraciGetVehicleVariableCmd(
                TraciGetVehicleVariableCmd.IdList, null);
        sender.sendCommand(cmd);

        TraciRawResponse response = waitForResponse(TraciGetVehicleVariableCmd.ID);
        verifyResponse(response);

        response = waitForResponse(TraciGetVehicleVariableCmd.RESP_ID);

        ByteArrayInputStream bais = new ByteArrayInputStream(response.getContent());
        TraciDataInputStream tdis = new TraciDataInputStream(bais);
        tdis.readByte(); // variable
        tdis.readTraciString(); // vehicle id
        tdis.readByte(); // return type
        return tdis.readTraciStringList();
    }

    public float getVehicleSpeed(String vehicleId) throws IOException {
        TraciCommand cmd = new TraciGetVehicleVariableCmd(
                TraciGetVehicleVariableCmd.Speed, vehicleId);
        sender.sendCommand(cmd);

        TraciRawResponse response = waitForResponse(TraciGetVehicleVariableCmd.ID);
        verifyResponse(response);

        response = waitForResponse(TraciGetVehicleVariableCmd.RESP_ID);

        ByteArrayInputStream bais = new ByteArrayInputStream(response.getContent());
        TraciDataInputStream tdis = new TraciDataInputStream(bais);
        tdis.readByte(); // variable
        tdis.readTraciString(); // vehicle id
        tdis.readByte(); // return type
        return tdis.readFloat();
    }

    public String getVehicleRoadId(String vehicleId) throws IOException {
        TraciCommand cmd = new TraciGetVehicleVariableCmd(
                TraciGetVehicleVariableCmd.RoadId, vehicleId);
        sender.sendCommand(cmd);

        TraciRawResponse response = waitForResponse(TraciGetVehicleVariableCmd.ID);
        verifyResponse(response);

        response = waitForResponse(TraciGetVehicleVariableCmd.RESP_ID);

        ByteArrayInputStream bais = new ByteArrayInputStream(response.getContent());
        TraciDataInputStream tdis = new TraciDataInputStream(bais);
        tdis.readByte(); // variable
        tdis.readTraciString(); // vehicle id
        tdis.readByte(); // return type
        return tdis.readTraciString();
    }

    /**
     * Verifies a given TraCI raw response indicates success.
     * @param response   The TraCI raw response.
     * @throws IOException if the TraCI response indicates failure.
     */
    private void verifyResponse(TraciRawResponse rawResponse) throws IOException {
        TraciStatusResponse response = new TraciStatusResponse(rawResponse);
        byte result = response.getResult();
        switch (result) {
            case TraciStatusResponse.TraciStatusSuccess:
                return;
            case TraciStatusResponse.TraciStatusFailure:
                throw new IOException(response.getDescription());
            case TraciStatusResponse.TraciStatusUnimplemented:
                throw new UnsupportedOperationException(response.getDescription());
            default:
                throw new IOException("Unknown result: 0x" + Integer.toHexString(result & 0xFF));
        }
    }

    /**
     * Waits for the next TraCI response with a given id to arrive.
     * TraCI responses with different ids are silently discarded.
     * 
     * @param id   The requested response id.
     * @return   The TraCI raw response with the given id.
     * @throws IOException on any error.
     */
    private TraciRawResponse waitForResponse(byte id) throws IOException {
        TraciRawResponse response;
        do {
            response = receiver.receiveRawResponse();
            //System.out.println("got response: " + Integer.toHexString(0xFF & response.getId()));
        } while (response.getId() != id);
        return response;
    }

    private final TraciCommandSender sender;
    private final TraciRawResponseReceiver receiver;
}
