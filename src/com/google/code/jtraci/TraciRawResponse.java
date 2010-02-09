package com.google.code.jtraci;

/**
 * A serialized TraCI response.
 * @author DL
 */
public class TraciRawResponse {
    public TraciRawResponse(byte id, byte result, byte[] content) {
        this.id = id;
        this.result = result;
        this.content = content;
    }

    public byte getId() {
        return id;
    }

    public byte getResult() {
        return result;
    }

    public byte[] getContent() {
        return content;
    }

    private final byte id;
    private final byte result;
    private final byte[] content;
}
