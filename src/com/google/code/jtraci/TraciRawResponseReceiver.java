package com.google.code.jtraci;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Receives TraCI raw responses from a stream.
 *
 * @author DL
 */
public class TraciRawResponseReceiver {
    /**
     * Constructor.
     * @param is   The input stream to read TraCI raw responses from.
     */
    public TraciRawResponseReceiver(InputStream is) {
        this.dis = new DataInputStream(is);
    }

    public TraciRawResponse receiveRawResponse() throws IOException {
        while (rawResponsesQueue.isEmpty()) {
            receiveNextMessage();
        }
        
        return rawResponsesQueue.remove();
    }

    private void receiveNextMessage() throws IOException {
        int msgSize = dis.readInt();
        if (msgSize < 4) {
            throw new IOException("Invalid TraCI message size: " + msgSize);
        }

        msgSize -= 4;
        while (msgSize > 0) {
            int responseSize = dis.read();
            if (responseSize == 0) {
                responseSize = dis.readInt();
                responseSize -= 5;
                msgSize -= 5;
            } else {
                --responseSize;
                --msgSize;
            }

            if (responseSize < 1) { // 1 for id
                throw new IOException("TraCI content too small: " + responseSize);
            }
            if (responseSize > msgSize) {
                throw new IOException("TraCI response extends beyond its wrapping message");
            }

            byte id = dis.readByte();
            byte[] content = new byte[responseSize-1];
            dis.readFully(content);
            msgSize -= responseSize;

            rawResponsesQueue.add(new TraciRawResponse(id, content));
        }

        if (msgSize != 0) {
            throw new IOException("TraCI message data extends beyond message size: " + msgSize);
        }
    }

    private final DataInputStream dis;
    private final Queue<TraciRawResponse> rawResponsesQueue =
            new LinkedList<TraciRawResponse>();
}
