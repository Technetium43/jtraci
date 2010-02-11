package com.google.code.jtraci.resp;

import com.google.code.jtraci.TraciRawResponse;
import com.google.code.jtraci.io.TraciDataInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * TraCI command status response.
 * @see http://sourceforge.net/apps/mediawiki/sumo/index.php?title=TraCI/Protocol
 * 
 * @author DL
 */
public class TraciStatusResponse {

    public static final byte TraciStatusSuccess = (byte)0x00;
    public static final byte TraciStatusFailure = (byte)0xFF;
    public static final byte TraciStatusUnimplemented = (byte)0x01;

    public TraciStatusResponse(TraciRawResponse rawResponse) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(rawResponse.getContent());
        TraciDataInputStream tdis = new TraciDataInputStream(bais);

        result = tdis.readByte();
        description = tdis.readTraciString();
    }

    public byte getResult() {
        return result;
    }

    public String getDescription() {
        return description;
    }

    private final byte result;
    private final String description;
}
